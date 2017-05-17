/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.abubusoft.kripton.processor.sqlite.core;

import java.util.Date;
import java.util.List;

import com.abubusoft.kripton.android.sqlite.OnReadBeanListener;
import com.abubusoft.kripton.android.sqlite.OnReadCursorListener;
import com.abubusoft.kripton.common.Pair;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.processor.BindDataSourceSubProcessor;
import com.abubusoft.kripton.processor.Version;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.sqlite.AbstractSelectCodeGenerator.JavadocPart;
import com.abubusoft.kripton.processor.sqlite.AbstractSelectCodeGenerator.JavadocPartType;
import com.abubusoft.kripton.processor.sqlite.PropertyList;
import com.abubusoft.kripton.processor.sqlite.SqlSelectBuilder.SelectType;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

public abstract class JavadocUtility {
	/**
	 * 
	 * @param builder
	 * 
	 */
	public static void generateJavadocGeneratedBy(Builder builder) {
		if (!BindDataSourceSubProcessor.TEST_MODE) {
			builder.addJavadoc("<p><strong>This class is generated by Kripton Annotation Processor ($L)</strong></p>\n\n", Version.getVersion());
			builder.addJavadoc("@since $L\n", (new Date()).toString());
		}
	}

	public static void generateJavaDocForSelect(MethodSpec.Builder methodBuilder, String sql, List<String> sqlParams, SQLiteModelMethod method, ModelAnnotation annotation, PropertyList fieldList,
			SelectType selectResultType, JavadocPart... javadocParts) {
		SQLDaoDefinition daoDefinition = method.getParent();
		TypeName beanTypeName = TypeName.get(daoDefinition.getEntity().getElement().asType());

		methodBuilder.addJavadoc("<h2>Select SQL:</h2>\n\n", annotation.getSimpleName());
		methodBuilder.addJavadoc("<pre>$L</pre>", sql);
		methodBuilder.addJavadoc("\n\n");

		// there will be alway some projected column
		{
			methodBuilder.addJavadoc("<h2>Projected columns:</h2>\n");
			methodBuilder.addJavadoc("<dl>\n");
			int i = 0;
			String[] columnList = fieldList.value0.split(",");
			for (String column : columnList) {
				// KRIPTON_DEBUG field info only it exists
				if (fieldList.value1.get(i) != null) {
					methodBuilder.addJavadoc("\t<dt>$L</dt>", column.trim());
					SQLProperty attribute = fieldList.value1.get(i);
					methodBuilder.addJavadoc("<dd>is associated to bean's property <strong>$L</strong></dd>", attribute.getName());
				} else {
					methodBuilder.addJavadoc("\t<dt>$L</dt>", column.trim());
					methodBuilder.addJavadoc("<dd>no bean's property is associated</dd>");
				}
				methodBuilder.addJavadoc("\n");
				i++;
			}
			methodBuilder.addJavadoc("</dl>");
			methodBuilder.addJavadoc("\n\n");
		}

		// dynamic parts
		if (method.info.hasDynamicOrderByConditions() || method.info.hasDynamicWhereConditions() || method.info.hasDynamicPageSizeConditions()) {
			methodBuilder.addJavadoc("<h2>Dynamic parts:</h2>\n");
			methodBuilder.addJavadoc("<dl>\n");
			if (method.info.hasDynamicWhereConditions()) {
				methodBuilder.addJavadoc("\t<dt>#{$L}</dt><dd>is part of where conditions resolved at runtime.</dd>\n", method.info.dynamicWhereParameterName);
			}
			if (method.info.hasDynamicOrderByConditions()) {
				methodBuilder.addJavadoc("\t<dt>#{$L}</dt><dd>is part of order statement resolved at runtime.</dd>\n", method.info.dynamicOrderByParameterName);
			}
			if (method.info.hasDynamicPageSizeConditions()) {
				methodBuilder.addJavadoc("\t<dt>#{$L}</dt><dd>is part of limit statement resolved at runtime.</dd>\n", method.info.dynamicPageSizeName);
			}

			methodBuilder.addJavadoc("</dl>");
			methodBuilder.addJavadoc("\n\n");
		}

		// query parameters
		if (sqlParams.size() > 0) {
			methodBuilder.addJavadoc("<h2>Query's parameters:</h2>\n");
			methodBuilder.addJavadoc("<dl>\n");
			for (String param : sqlParams) {
				methodBuilder.addJavadoc("\t<dt>$L</dt><dd>is binded to method's parameter <strong>$L</strong></dd>\n", "${" + param + "}", method.findParameterNameByAlias(param));
			}
			methodBuilder.addJavadoc("</dl>");
			methodBuilder.addJavadoc("\n\n");
		}

		// method params
		ParameterSpec parameterSpec;
		for (Pair<String, TypeName> item : method.getParameters()) {
			parameterSpec = ParameterSpec.builder(item.value1, item.value0).build();

			methodBuilder.addJavadoc("@param $L\n", parameterSpec.name);
			if (beanTypeName.equals(item.value1)) {
				methodBuilder.addJavadoc("\tis used as $L\n", "${" + method.findParameterAliasByName(item.value0) + "}");
			} else if (TypeUtility.isTypeEquals(item.value1, ParameterizedTypeName.get(TypeUtility.className(OnReadBeanListener.class), beanTypeName))) {
				methodBuilder.addJavadoc("\tis the $T listener\n", beanTypeName);
			} else if (TypeUtility.isTypeEquals(item.value1, TypeUtility.className(OnReadCursorListener.class))) {
				methodBuilder.addJavadoc("\tis the cursor listener\n", beanTypeName);
			} else if (item.value0.equals(method.info.dynamicWhereParameterName)) {
				methodBuilder.addJavadoc("\tis used as <strong>dynamic WHERE statement</strong> and it is formatted by ({@link $T#format})\n", StringUtils.class);
			} else if (item.value0.equals(method.info.dynamicOrderByParameterName)) {
				methodBuilder.addJavadoc("\tis used as <strong>dynamic ORDER BY statement</strong> and it is formatted by ({@link $T#format})\n", StringUtils.class);
			} else if (item.value0.equals(method.info.dynamicPageSizeName)) {
				methodBuilder.addJavadoc("\tis used as <strong>dynamic LIMIT statement</strong> and it is formatted by ({@link $T#format})\n", StringUtils.class);
			} else {
				methodBuilder.addJavadoc("\tis binded to <code>$L</code>\n", "${" + method.findParameterAliasByName(item.value0) + "}");
			}
		}
		for (JavadocPart item : javadocParts) {
			if (item.javadocPartType != JavadocPartType.ADD_PARAMETER)
				continue;
			methodBuilder.addJavadoc("@param $L\n", item.name);
			methodBuilder.addJavadoc("\t$L\n", item.description);
		}

		for (JavadocPart item : javadocParts) {
			if (item.javadocPartType != JavadocPartType.RETURN)
				continue;
			methodBuilder.addJavadoc("@return $L\n", item.description);
			// override return
			return;
		}
		// return type
		switch (selectResultType) {
		case BEAN:
			methodBuilder.addJavadoc("@return selected bean or <code>null</code>.\n");
			break;
		case CURSOR:
			methodBuilder.addJavadoc("@return cursor. Closing the cursor is delegated to the calling code.\n");
			break;
		case LIST_BEAN:
			methodBuilder.addJavadoc("@return collection of bean or empty collection.\n");
			break;
		case LIST_SCALAR:
			methodBuilder.addJavadoc("@return collection of single value extracted by query.\n");
			break;
		case SCALAR:
			methodBuilder.addJavadoc("@return single value extracted by query.\n");
			break;
		case PAGED_RESULT:
			methodBuilder.addJavadoc("@return paginated result.\n");
			break;
		default:
			// case LISTENER_BEAN:
			// case LISTENER_CURSOR:
			break;
		}
	}
}
