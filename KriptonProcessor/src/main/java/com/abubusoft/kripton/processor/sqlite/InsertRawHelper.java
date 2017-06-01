/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
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
package com.abubusoft.kripton.processor.sqlite;

import javax.lang.model.util.Elements;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.ConflictAlgorithmType;
import com.abubusoft.kripton.android.sqlite.SqlUtils;
import com.abubusoft.kripton.common.Converter;
import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.processor.core.ModelProperty;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.exceptions.InvalidMethodSignException;
import com.abubusoft.kripton.processor.exceptions.PropertyNotFoundException;
import com.abubusoft.kripton.processor.sqlite.SqlInsertBuilder.InsertCodeGenerator;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.abubusoft.kripton.processor.sqlite.transform.SQLTransformer;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class InsertRawHelper implements InsertCodeGenerator {

	@Override
	public String generate(Elements elementUtils, MethodSpec.Builder methodBuilder, boolean mapFields, SQLiteModelMethod method, TypeName returnType) {
		SQLDaoDefinition daoDefinition = method.getParent();
		SQLEntity entity = daoDefinition.getEntity();

		String sqlInsert;
		boolean nullable;

		// generate javadoc
		sqlInsert = generateJavaDoc(methodBuilder, method, returnType);

		methodBuilder.addCode("$T contentValues=contentValues();\n", ContentValues.class);
		methodBuilder.addCode("contentValues.clear();\n\n");
		for (Pair<String, TypeName> item : method.getParameters()) {
			String propertyName = method.findParameterAliasByName(item.value0);
			ModelProperty property = entity.get(propertyName);
			if (property == null)
				throw (new PropertyNotFoundException(method, propertyName, item.value1));

			// check same type
			TypeUtility.checkTypeCompatibility(method, item, property);
			// check nullabliity
			nullable = TypeUtility.isNullable(method, item, property);

			if (nullable) {
				// it use raw method param's typeName
				methodBuilder.beginControlFlow("if ($L!=null)", item.value0);
			}
			methodBuilder.addCode("contentValues.put($S, ", daoDefinition.getColumnNameConverter().convert(property.getName()));
			// it does not need to be converted in string

			SQLTransformer.java2ContentValues(methodBuilder, daoDefinition, item.value1, item.value0);
			// SQLTransformer.java2ContentValues(methodBuilder, item.value1,
			// item.value0);
			methodBuilder.addCode(");\n");
			if (nullable) {
				methodBuilder.nextControlFlow("else");
				methodBuilder.addCode("contentValues.putNull($S);\n", daoDefinition.getColumnNameConverter().convert(propertyName));
				methodBuilder.endControlFlow();
			}
			methodBuilder.addCode("\n");
		}
		
		methodBuilder.addCode("//$T and $T will be used to format SQL\n", StringUtils.class, SqlUtils.class);

		if (daoDefinition.isLogEnabled()) {
			methodBuilder.addCode("// log\n");
			methodBuilder.addCode("$T.info($T.formatSQL(\"$L\"));\n", Logger.class, SqlUtils.class, sqlInsert);
		}

		ConflictAlgorithmType conflictAlgorithmType = InsertBeanHelper.getConflictAlgorithmType(method);
		String conflictString1 = "";
		String conflictString2 = "";
				
		if (conflictAlgorithmType != ConflictAlgorithmType.NONE) {
			conflictString1 = "WithOnConflict";
			conflictString2 = ", SQLiteDatabase." + conflictAlgorithmType;
			methodBuilder.addCode("// use $T conflicts algorithm\n", SQLiteDatabase.class);
		}

		// define return value
		if (returnType == TypeName.VOID) {
			methodBuilder.addCode("database().insert$L($S, null, contentValues$L);\n", conflictString1, daoDefinition.getEntity().getTableName(), conflictString2);
		} else if (TypeUtility.isTypeIncludedIn(returnType, Boolean.TYPE, Boolean.class)) {
			methodBuilder.addCode("long result = database().insert$L($S, null, contentValues$L);\n", conflictString1, daoDefinition.getEntity().getTableName(), conflictString2);
			methodBuilder.addCode("return result!=-1;\n");
		} else if (TypeUtility.isTypeIncludedIn(returnType, Long.TYPE, Long.class)) {
			methodBuilder.addCode("long result = database().insert$L($S, null, contentValues$L);\n", conflictString1, daoDefinition.getEntity().getTableName(), conflictString2);
			methodBuilder.addCode("return result;\n");
		} else if (TypeUtility.isTypeIncludedIn(returnType, Integer.TYPE, Integer.class)) {
			methodBuilder.addCode("int result = (int)database().insert$L($S, null, contentValues$L);\n", conflictString1, daoDefinition.getEntity().getTableName(), conflictString2);
			methodBuilder.addCode("return result;\n");
		} else {
			// more than one listener found
			throw (new InvalidMethodSignException(method, "invalid return type"));
		}

		return sqlInsert;
	}

	/**
	 * @param methodBuilder
	 * @param method
	 * @param returnType
	 * @return string sql
	 */
	public String generateJavaDoc(MethodSpec.Builder methodBuilder, SQLiteModelMethod method, TypeName returnType) {
		SQLDaoDefinition daoDefinition = method.getParent();
		Converter<String, String> nc = daoDefinition.getColumnNameConverter();

		String sqlInsert;
		{
			StringBuilder bufferName = new StringBuilder();
			StringBuilder bufferValue = new StringBuilder();
			StringBuilder bufferQuestion = new StringBuilder();

			{
				String separator = "";
				for (Pair<String, TypeName> item : method.getParameters()) {
					String resolvedParamName = method.findParameterAliasByName(item.value0);
					bufferName.append(separator + nc.convert(resolvedParamName));
					bufferValue.append(separator + "${" + resolvedParamName + "}");

					// here it needed raw parameter typeName
					bufferQuestion.append(separator + "'\"+StringUtils.checkSize(contentValues.get(\"" + nc.convert(resolvedParamName) + "\"))+\"'");
					separator = ", ";
				}
			}

			ConflictAlgorithmType conflictAlgorithmType = InsertBeanHelper.getConflictAlgorithmType(method);

			methodBuilder.addJavadoc("<p>SQL insert:</p>\n");
			methodBuilder.addJavadoc("<pre>INSERT $LINTO $L ($L) VALUES ($L)</pre>\n", conflictAlgorithmType.getSql(), daoDefinition.getEntity().getTableName(), bufferName.toString(),
					bufferValue.toString());
			methodBuilder.addJavadoc("\n");

			// list of inserted fields
			methodBuilder.addJavadoc("<p><strong>Inserted columns:</strong></p>\n");
			methodBuilder.addJavadoc("<dl>\n");
			for (Pair<String, TypeName> property : method.getParameters()) {
				String resolvedName = method.findParameterAliasByName(property.value0);
				methodBuilder.addJavadoc("\t<dt>$L</dt>", nc.convert(resolvedName));
				methodBuilder.addJavadoc("<dd>is binded to query's parameter <strong>$L</strong> and method's parameter <strong>$L</strong></dd>\n", "${" + resolvedName + "}", property.value0);
			}
			methodBuilder.addJavadoc("</dl>\n\n");

			{
				for (Pair<String, TypeName> param : method.getParameters()) {
					methodBuilder.addJavadoc("@param $L\n", param.value0);
					methodBuilder.addJavadoc("\tis binded to column <strong>$L</strong>\n", nc.convert(method.findParameterAliasByName(param.value0)));
				}
			}

			// generate sql query
			sqlInsert = String.format("INSERT %sINTO %s (%s) VALUES (%s)", conflictAlgorithmType.getSql(), daoDefinition.getEntity().getTableName(), bufferName.toString(), bufferQuestion.toString());

			generateJavaDocReturnType(methodBuilder, returnType);
		}
		return sqlInsert;
	}

	/**
	 * Generate javadoc about return type of method
	 * 
	 * @param methodBuilder
	 * @param returnType
	 */
	public static void generateJavaDocReturnType(MethodSpec.Builder methodBuilder, TypeName returnType) {
		methodBuilder.addJavadoc("\n");
		if (returnType == TypeName.VOID) {
		} else if (TypeUtility.isTypeIncludedIn(returnType, Boolean.TYPE, Boolean.class)) {
			methodBuilder.addJavadoc("@return <code>true</code> if record is inserted, <code>false</code> otherwise");
		} else if (TypeUtility.isTypeIncludedIn(returnType, Long.TYPE, Long.class)) {
			methodBuilder.addJavadoc("@return <strong>id</strong> of inserted record");
		} else if (TypeUtility.isTypeIncludedIn(returnType, Integer.TYPE, Integer.class)) {
			methodBuilder.addJavadoc("@return <strong>id</strong> of inserted record");
		}
		methodBuilder.addJavadoc("\n");
	}

}
