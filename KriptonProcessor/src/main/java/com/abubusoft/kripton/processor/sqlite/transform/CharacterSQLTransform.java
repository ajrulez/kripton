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
package com.abubusoft.kripton.processor.sqlite.transform;

import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.getter;
import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.setter;

import com.abubusoft.kripton.processor.core.ModelProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLColumnType;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

/**
 * Transformer between a string and a Java Character object
 * 
 * @author Francesco Benincasa (abubusoft@gmail.com)
 *
 */
class CharacterSQLTransform extends AbstractSQLTransform {

	public CharacterSQLTransform(boolean nullable) {
		defaultValue = "0";
		if (nullable) {
			defaultValue = "null";
		}
	}

	@Override
	public void generateWriteProperty(Builder methodBuilder, TypeName beanClass, String beanName,
			ModelProperty property) {
		methodBuilder.addCode("(int)$L", getter(beanName, beanClass, property));
	}

	@Override
	public void generateWriteParam(Builder methodBuilder, SQLDaoDefinition sqlDaoDefinition, String paramName, TypeName paramTypeName) {
		methodBuilder.addCode("(int)$L", paramName);
	}

	@Override
	public void generateReadProperty(Builder methodBuilder, TypeName beanClass, String beanName, ModelProperty property,
			String cursorName, String indexName) {
		methodBuilder.addCode(setter(beanClass, beanName, property, "(char)$L.getInt($L)"), cursorName, indexName);
	}

	@Override
	public void generateReadParam(Builder methodBuilder, SQLDaoDefinition daoDefinition, TypeName paramTypeName, String cursorName, String indexName) {
		methodBuilder.addCode("(char)$L.getInt($L)", cursorName, indexName);
	}

	@Override
	public void generateDefaultValue(Builder methodBuilder) {
		methodBuilder.addCode(defaultValue);
	}

	protected String defaultValue;

	@Override
	public void generateResetProperty(Builder methodBuilder, TypeName beanClass, String beanName,
			ModelProperty property, String cursorName, String indexName) {

		methodBuilder.addCode(setter(beanClass, beanName, property, "0"));
	}

	@Override
	public SQLColumnType getColumnType() {
		return SQLColumnType.INTEGER;
	}

}
