/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.abubusoft.kripton.processor.core.reflect;

import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.typeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import android.database.Cursor;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.android.sqlite.OnReadBeanListener;
import com.abubusoft.kripton.android.sqlite.OnReadCursorListener;
import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.ModelMethod;
import com.abubusoft.kripton.processor.core.ModelType;
import com.abubusoft.kripton.processor.core.AnnotationAttributeType;
import com.abubusoft.kripton.processor.core.AssertKripton;
import com.abubusoft.kripton.processor.core.reflect.AnnotationUtility.MethodFoundListener;
import com.abubusoft.kripton.processor.exceptions.InvalidMethodSignException;
import com.abubusoft.kripton.processor.sqlite.CodeBuilderUtility;
import com.abubusoft.kripton.processor.sqlite.PropertyList;
import com.abubusoft.kripton.processor.sqlite.SqlAnalyzer;
import com.abubusoft.kripton.processor.sqlite.SqlSelectBuilder;
import com.abubusoft.kripton.processor.sqlite.core.JavadocUtility;
import com.abubusoft.kripton.processor.sqlite.SelectStatementBuilder;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.abubusoft.kripton.processor.sqlite.transform.SQLTransformer;
import com.abubusoft.kripton.processor.utils.LiteralType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

public abstract class MethodUtility {

	private static final Pattern pattern = Pattern.compile("\\((.*)\\)(.*)");

	public static Pair<String, String> extractResultAndArguments(String value) {
		Matcher matcher = pattern.matcher(value);

		Pair<String, String> result = new Pair<String, String>();
		if (matcher.matches()) {
			result.value0 = matcher.group(1);
			result.value1 = matcher.group(2);
		}
		return result;
	}

	/**
	 * Iterate over methods.
	 * 
	 * @param elementUtils
	 * @param typeElement
	 * @param listener
	 */
	public static void forEachMethods(Elements elementUtils, TypeElement typeElement, MethodFoundListener listener) {

		Map<String, TypeMirror> resolvedParameter = new HashMap<String, TypeMirror>();
		List<? extends TypeMirror> listInterface = typeElement.getInterfaces();
		for (TypeMirror item : listInterface) {
			// if (item instanceof Type$ClassType)
			{
				resolvedParameter.put("E", item);
			}
		}

		List<? extends Element> list = elementUtils.getAllMembers(typeElement);

		for (Element item : list) {
			if (item.getKind() == ElementKind.METHOD) {
				listener.onMethod((ExecutableElement) item);
			}
		}
	}

	public static boolean hasParameterOfType(ModelMethod method, String kindOfParameter) {
		for (Pair<String, TypeMirror> item : method.getParameters()) {
			if (typeName(item.value1).toString().equals(kindOfParameter)) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasParameterOfType(ModelMethod method, LiteralType parameter) {
		for (Pair<String, TypeMirror> item : method.getParameters()) {
			if (typeName(item.value1).toString().equals(parameter.getValue())) {
				return true;
			}
		}

		return false;
	}

	public static int countParameterOfType(ModelMethod method, LiteralType parameter) {
		int counter = 0;
		for (Pair<String, TypeMirror> item : method.getParameters()) {
			if (typeName(item.value1).toString().equals(parameter.getValue())) {
				counter++;
			}
		}

		return counter;
	}

	public static String getNameParameterOfType(ModelMethod method, LiteralType parameter) {
		for (Pair<String, TypeMirror> item : method.getParameters()) {
			if (typeName(item.value1).toString().equals(parameter.getValue())) {
				return item.value0;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param elementUtils
	 * @param builder
	 * @param method
	 */
	public static void generateSelect(Elements elementUtils, Builder builder, SQLiteModelMethod method) {
		SQLDaoDefinition daoDefinition = method.getParent();
		SQLEntity entity = daoDefinition.getEntity();

		SqlSelectBuilder.SelectResultType selectResultType = null;

		// if true, field must be associate to ben attributes
		TypeMirror returnType = method.getReturnClass();
		TypeName returnTypeName = typeName(returnType);

		LiteralType readBeanListener = LiteralType.of(OnReadBeanListener.class.getCanonicalName(), entity.getName());
		LiteralType readCursorListener = LiteralType.of(OnReadCursorListener.class);

		if (TypeUtility.isTypeIncludedIn(returnTypeName, Void.class, Void.TYPE)) {
			// return VOID (in the parameters must be a listener)
			if (hasParameterOfType(method, readCursorListener)) {
				selectResultType = SqlSelectBuilder.SelectResultType.LISTENER_CURSOR;
			} else if (hasParameterOfType(method, readBeanListener)) {
				selectResultType = SqlSelectBuilder.SelectResultType.LISTENER_BEAN;
			}
		} else if (TypeUtility.isTypeIncludedIn(returnTypeName, Cursor.class)) {
			// return Cursor (no listener)
			selectResultType = SqlSelectBuilder.SelectResultType.CURSOR;
		} else if (returnTypeName instanceof ParameterizedTypeName) {
			ClassName listClazzName = ((ParameterizedTypeName) returnTypeName).rawType;
			
			try {
				Class<?> listClazz=Class.forName(listClazzName.toString());
				
				if (Collection.class.isAssignableFrom(listClazz))
				{
					// return List (no listener)
					List<TypeName> list = ((ParameterizedTypeName) returnTypeName).typeArguments;

					if (list.size() == 1) {
						TypeName elementName = ((ParameterizedTypeName) returnTypeName).typeArguments.get(0);
						if (TypeUtility.isSameType(list.get(0), entity.getName().toString())) {
							// entity list
							selectResultType = SqlSelectBuilder.SelectResultType.LIST_BEAN;
						} else if (TypeUtility.isByteArray(elementName) || TypeUtility.isTypePrimitive(elementName) || TypeUtility.isTypeWrappedPrimitive(elementName) || TypeUtility.isTypeIncludedIn(elementName, String.class)) {
							// scalar list
							selectResultType = SqlSelectBuilder.SelectResultType.LIST_SCALAR;
						}
					} else {
						// error
					}					
				}
			} catch(Exception e)
			{
				// error
			}
		} else if (TypeUtility.isEquals(returnTypeName, entity)) {
			// return one element (no listener)
			selectResultType = SqlSelectBuilder.SelectResultType.BEAN;
		} else if (TypeUtility.isTypePrimitive(returnTypeName) || TypeUtility.isTypeWrappedPrimitive(returnTypeName) || TypeUtility.isTypeIncludedIn(returnTypeName, String.class) || TypeUtility.isByteArray(returnTypeName)) {
			// return single value string, int, long, short, double, float, String (no listener)
			selectResultType = SqlSelectBuilder.SelectResultType.SCALAR;
		}

		if (selectResultType == null) {
			throw (new InvalidMethodSignException(method, String.format("'%s' as return type is not supported", returnTypeName)));
		}

		// take field list
		PropertyList fieldList = CodeBuilderUtility.generatePropertyList(elementUtils, daoDefinition, method, BindSqlSelect.class, selectResultType.isMapFields(), null);
		String fieldStatement = fieldList.value0;
		// List<SQLProperty> fields = fieldList.value1;
		String tableStatement = daoDefinition.getEntity().getTableName();

		// separate params used for update bean and params used in whereCondition
		// analyze whereCondition
		ModelAnnotation annotation = method.getAnnotation(BindSqlSelect.class);
		boolean distinctClause = Boolean.valueOf(annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_DISTINCT));

		// parameters
		List<String> paramNames = new ArrayList<String>();
		List<String> paramGetters = new ArrayList<String>();
		List<TypeMirror> paramTypeNames = new ArrayList<TypeMirror>();
		
		// used method parameters
		Set<String> usedMethodParameters = new HashSet<String>();

		SqlAnalyzer analyzer = new SqlAnalyzer();

		String whereSQL = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_WHERE);		
		analyzer.execute(elementUtils, method, whereSQL);
		String whereStatement = analyzer.getSQLStatement();
		paramGetters.addAll(analyzer.getParamGetters());
		paramNames.addAll(analyzer.getParamNames());
		paramTypeNames.addAll(analyzer.getParamTypeNames());
		usedMethodParameters.addAll(analyzer.getUsedMethodParameters());

		String havingSQL = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_HAVING);
		analyzer.execute(elementUtils, method, havingSQL);
		String havingStatement = analyzer.getSQLStatement();
		paramGetters.addAll(analyzer.getParamGetters());
		paramNames.addAll(analyzer.getParamNames());
		paramTypeNames.addAll(analyzer.getParamTypeNames());
		usedMethodParameters.addAll(analyzer.getUsedMethodParameters());

		String groupBySQL = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_GROUP_BY);
		analyzer.execute(elementUtils, method, groupBySQL);
		String groupByStatement = analyzer.getSQLStatement();
		paramGetters.addAll(analyzer.getParamGetters());
		paramNames.addAll(analyzer.getParamNames());
		paramTypeNames.addAll(analyzer.getParamTypeNames());
		usedMethodParameters.addAll(analyzer.getUsedMethodParameters());

		String orderBySQL = annotation.getAttribute(AnnotationAttributeType.ATTRIBUTE_ORDER_BY);
		analyzer.execute(elementUtils, method, orderBySQL);
		String orderByStatement = analyzer.getSQLStatement();
		paramGetters.addAll(analyzer.getParamGetters());
		paramNames.addAll(analyzer.getParamNames());
		paramTypeNames.addAll(analyzer.getParamTypeNames());
		usedMethodParameters.addAll(analyzer.getUsedMethodParameters());
		
		// add as used parameter dynamic components too
		if (method.hasDynamicWhereConditions())
		{
			AssertKripton.assertTrueOrInvalidMethodSignException(!usedMethodParameters.contains(method.dynamicWhereParameterName),method," parameter %s is used like SQL parameter and dynamic WHERE condition.", method.dynamicOrderByParameterName);
			usedMethodParameters.add(method.dynamicWhereParameterName);
		}
		
		if (method.hasDynamicOrderByConditions())
		{
			AssertKripton.assertTrueOrInvalidMethodSignException(!usedMethodParameters.contains(method.dynamicOrderByParameterName),method," parameter %s is used like SQL parameter and dynamic ORDER BY condition.", method.dynamicOrderByParameterName);
			usedMethodParameters.add(method.dynamicOrderByParameterName);
		}

		// select statement
		String sqlWithParameters = SelectStatementBuilder.create().distinct(distinctClause).fields(fieldStatement).table(tableStatement).where(whereSQL).having(havingSQL).groupBy(groupBySQL).orderBy(orderBySQL).build(method);
		String sql = SelectStatementBuilder.create().distinct(distinctClause).fields(fieldStatement).table(tableStatement).where(whereStatement).having(havingStatement).groupBy(groupByStatement).orderBy(orderByStatement).build(method);
		String sqlForLog = SelectStatementBuilder.create().distinct(distinctClause).fields(fieldStatement).table(tableStatement).where(whereStatement).having(havingStatement).groupBy(groupByStatement).orderBy(orderByStatement).buildForLog(method);

		// generate method code
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName()).addAnnotation(Override.class).addModifiers(Modifier.PUBLIC);

		// generate javadoc
		JavadocUtility.generateJavaDocForSelect(methodBuilder, sqlWithParameters, paramNames, method, annotation, fieldList, selectResultType);

		ParameterSpec parameterSpec;
		for (Pair<String, TypeMirror> item : method.getParameters()) {
			parameterSpec = ParameterSpec.builder(TypeName.get(item.value1), item.value0).build();
			methodBuilder.addParameter(parameterSpec);
		}
		methodBuilder.returns(returnTypeName);

		// build where condition (common for every type of select)
		StringBuilder logArgsBuffer = new StringBuilder();
		methodBuilder.addCode("// build where condition\n");
		methodBuilder.addCode("String[] args={");
		{
			String separator = "";

			TypeMirror paramTypeName;
			TypeName paramName;
			
			boolean nullable;
			int i = 0;
			for (String item : paramGetters) {
				methodBuilder.addCode(separator);
				logArgsBuffer.append(separator + "%s");

				paramTypeName = paramTypeNames.get(i);

				if (paramTypeName instanceof ModelType) {
					paramName = ((ModelType) paramTypeName).getName();
				} else
				{
					paramName=typeName(paramTypeName);
				}
				
				// code for query arguments
				nullable = TypeUtility.isNullable(paramName);			
				if (nullable) {
					methodBuilder.addCode("($L==null?\"\":", item);
				}				
							
				// check for string conversion
				TypeUtility.beginStringConversion(methodBuilder, paramTypeName);			
				
				SQLTransformer.java2ContentValues(methodBuilder, daoDefinition, TypeUtility.typeName(paramTypeName), item);
				
				// check for string conversion
				TypeUtility.endStringConversion(methodBuilder, paramTypeName);
				
				if (nullable) {
					methodBuilder.addCode(")");
				}

				separator = ", ";
				i++;
			}
			methodBuilder.addCode("};\n");
		}
			

		methodBuilder.addCode("\n");
		methodBuilder.addCode("//$T will be used in case of dynamic parts of SQL\n", StringUtils.class);			
		
		if (daoDefinition.isLogEnabled()) {
			methodBuilder.addStatement("$T.info($T.formatSQL(\"$L\",(Object[])args))", Logger.class, StringUtils.class, formatSqlForLog(method,sqlForLog));
		} 
		methodBuilder.addStatement("$T cursor = database().rawQuery(\"$L\", args)", Cursor.class, formatSql(method,sql));

		if (daoDefinition.isLogEnabled()) {
			methodBuilder.addCode("$T.info(\"Rows found: %s\",cursor.getCount());\n", Logger.class);
		}

		{			
			switch (selectResultType) {			
			case LISTENER_CURSOR: {
				LiteralType readCursorListenerToExclude = LiteralType.of(OnReadCursorListener.class);
				checkUnusedParameters(method, usedMethodParameters, readCursorListenerToExclude);
			}
				break;
			case LISTENER_BEAN: {
				LiteralType readBeanListenerToExclude = LiteralType.of(OnReadBeanListener.class.getCanonicalName(), entity.getName());
				checkUnusedParameters(method, usedMethodParameters, readBeanListenerToExclude);
			}
				break;
			default:
				checkUnusedParameters(method, usedMethodParameters, null);
				break;
			}
		}

		selectResultType.generate(elementUtils, fieldList, methodBuilder, method, returnType);

		MethodSpec methodSpec = methodBuilder.build();
		builder.addMethod(methodSpec);
	}

	/**
	 * Check if there are unused method parameters. In this case an exception was throws.
	 * 
	 * @param method
	 * @param usedMethodParameters
	 */
	public static void checkUnusedParameters(SQLiteModelMethod method, Set<String> usedMethodParameters, LiteralType excludedClasses) {
		int paramsCount = method.getParameters().size();
		int usedCount = usedMethodParameters.size();

		if (paramsCount > usedCount) {
			StringBuilder sb = new StringBuilder();
			String separator = "";
			for (Pair<String, TypeMirror> item : method.getParameters()) {
				if (excludedClasses != null && typeName(item.value1).toString().equals(excludedClasses.getValue())) {
					usedCount++;
				} else {
					if (!usedMethodParameters.contains(item.value0)) {
						sb.append(separator + "'" + item.value0 + "'");
						separator = ", ";
					}
				}
			}

			if (paramsCount > usedCount) {
				throw (new InvalidMethodSignException(method, "unused parameter(s) " + sb.toString()));
			}
		}
	}
	
	public static String formatSqlForLog(SQLiteModelMethod method, String sql)
	{
		sql=sql.replaceAll("\\%[^s]", "\\%\\%").replaceAll("\\?", "\'%s\'");
		
		return formatSqlInternal(method, sql, "appendForLog");
	}
		
	
	public static String formatSql(SQLiteModelMethod method, String sql)
	{	
		return formatSqlInternal(method, sql, "appendForSQL");		
	}
	
	private static String formatSqlInternal(SQLiteModelMethod method, String sql, String appendMethod)
	{	
		if (method.hasDynamicWhereConditions())
		{
			sql=sql.replace("#{"+method.dynamicWhereParameterName+"}", "\"+StringUtils."+appendMethod+"("+method.dynamicWhereParameterName+")+\"");
		}
		
		if (method.hasDynamicOrderByConditions())
		{
			sql=sql.replace("#{"+method.dynamicOrderByParameterName+"}", "\"+StringUtils."+appendMethod+"("+method.dynamicOrderByParameterName+")+\"");
		}
		
		return sql;
	}


}
