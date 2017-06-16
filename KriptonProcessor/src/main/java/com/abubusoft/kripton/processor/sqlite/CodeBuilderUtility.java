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

import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.getter;
import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.typeName;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.util.Elements;

import com.abubusoft.kripton.processor.core.AnnotationAttributeType;
import com.abubusoft.kripton.processor.core.ModelAnnotation;
import com.abubusoft.kripton.processor.core.ModelMethod;
import com.abubusoft.kripton.processor.core.reflect.AnnotationUtility;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.exceptions.IncompatibleAttributesInAnnotationException;
import com.abubusoft.kripton.processor.exceptions.PropertyInAnnotationNotFoundException;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLProperty;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteModelMethod;
import com.abubusoft.kripton.processor.sqlite.transform.SQLTransformer;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;

import android.content.ContentValues;

public abstract class CodeBuilderUtility {

	/**
	 * Generate code necessary to put bean properties in content values map.
	 * Returns the primary key
	 * 
	 * @param elementUtils
	 * @param daoDefinition
	 * @param method
	 *            used to code generation
	 * @param checkProperty
	 *            if true, check if property used in query is present as
	 *            attribute in bean
	 * @param alreadyUsedBeanPropertiesNames
	 *            optional
	 * @return primary key.
	 */
	public static PropertyList generatePropertyList(Elements elementUtils, SQLDaoDefinition daoDefinition, SQLiteModelMethod method, Class<? extends Annotation> annotationClazz, boolean checkProperty,
			Set<String> alreadyUsedBeanPropertiesNames) {
		PropertyList result = new PropertyList();

		SQLEntity entity = daoDefinition.getEntity();

		ModelAnnotation annotation = method.getAnnotation(annotationClazz);

		// check included and excluded fields
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.FIELDS);
		if (alreadyUsedBeanPropertiesNames != null) {
			includedFields.removeAll(alreadyUsedBeanPropertiesNames);
		}
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.EXCLUDED_FIELDS));

		if (includedFields.size() > 0 && excludedFields.size() > 0) {
			throw (new IncompatibleAttributesInAnnotationException(daoDefinition, method, annotation, AnnotationAttributeType.VALUE, AnnotationAttributeType.EXCLUDED_FIELDS));
		}

		StringBuilder buffer = new StringBuilder();
		String separator = "";

		if (checkProperty) {
			// check included
			for (String item : includedFields) {
				if (!entity.contains(item)) {
					throw (new PropertyInAnnotationNotFoundException(method, item));
				}
			}
			if (includedFields.size() > 0) {
				result.explicitDefinition = true;
			} else {
				result.explicitDefinition = false;
			}

			// check excluded
			for (String item : excludedFields) {
				if (!entity.contains(item)) {
					throw (new PropertyInAnnotationNotFoundException(method, item));
				}
			}

			// for each property in entity except primaryKey and excluded
			// properties
			for (SQLProperty item : entity.getCollection()) {
				if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
					continue;
				if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
					continue;

				buffer.append(separator + item.columnName);
				result.value1.add(item);
				separator = ", ";
			}
		} else {
			// get fields from property
			if (includedFields.size() == 0) {
				result.explicitDefinition = false;
				for (SQLProperty item : entity.getCollection()) {
					includedFields.add(item.getName());
				}
			} else {
				result.explicitDefinition = true;
			}

			for (String item : includedFields) {
				if (entity.contains(item))
				{
					buffer.append(separator + daoDefinition.getEntity().findByName(item).columnName);
				} else {
					buffer.append(separator + item);	
				}
				
				result.value1.add(null);
				separator = ", ";
			}
		}

		result.value0 = buffer.toString();

		return result;

	}

	/**
	 * Generate code necessary to put bean properties in content values map.
	 * Return primary key
	 * 
	 * @param elementUtils
	 * @param daoDefinition
	 * @param method
	 * @param methodBuilder
	 *            used to code generation
	 * @param alreadyUsedBeanPropertiesNames
	 *            optional
	 * @return primary key.
	 */
	public static List<SQLProperty> populateContentValuesFromEntity(Elements elementUtils, SQLDaoDefinition daoDefinition, SQLiteModelMethod method, Class<? extends Annotation> annotationClazz,
			Builder methodBuilder, List<String> alreadyUsedBeanPropertiesNames) {
		List<SQLProperty> listPropertyInContentValue = new ArrayList<SQLProperty>();

		SQLEntity entity = daoDefinition.getEntity();
		// check included and excluded fields
		ModelAnnotation annotation = method.getAnnotation(annotationClazz);
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.FIELDS);
		if (includedFields == null)
			includedFields = new ArrayList<String>();
		// CESCOZ
		/*
		 * if (alreadyUsedBeanPropertiesNames != null) {
		 * includedFields.removeAll(alreadyUsedBeanPropertiesNames); }
		 */

		List<String> temp = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.EXCLUDED_FIELDS);
		if (temp == null)
			temp = new ArrayList<String>();
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(temp);

		if (includedFields.size() > 0 && excludedFields.size() > 0) {
			throw (new IncompatibleAttributesInAnnotationException(daoDefinition, method, annotation, AnnotationAttributeType.VALUE, AnnotationAttributeType.EXCLUDED_FIELDS));
		}
		// check included
		for (String item : includedFields) {
			if (!entity.contains(item)) {
				throw (new PropertyInAnnotationNotFoundException(method, item));
			}
		}
		// check excluded
		for (String item : excludedFields) {
			if (!entity.contains(item)) {
				throw (new PropertyInAnnotationNotFoundException(method, item));
			}
		}

		// initialize contentValues
		SQLProperty primaryKey = entity.getPrimaryKey();

		boolean includePrimaryKey = annotation.getAttributeAsBoolean(AnnotationAttributeType.INCLUDE_PRIMARY_KEY);

		// for each property in entity except primaryKey and excluded properties
		for (SQLProperty item : entity.getCollection()) {
			if (!includePrimaryKey && item.equals(primaryKey) || excludedFields.contains(item.getName()))
				continue;
			if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
				continue;
			if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
				continue;

			// add property to list of used properties
			listPropertyInContentValue.add(item);
		}

		return listPropertyInContentValue;

	}

	public static void generateContentValuesFromEntity(Elements elementUtils, SQLDaoDefinition daoDefinition, ModelMethod method, Class<? extends Annotation> annotationClazz, Builder methodBuilder,
			List<String> alreadyUsedBeanPropertiesNames) {
		// all check is already done

		SQLEntity entity = daoDefinition.getEntity();

		String entityName = method.getParameters().get(0).value0;
		TypeName entityClassName = typeName(entity.getElement());

		// check included and excluded fields
		ModelAnnotation annotation = method.getAnnotation(annotationClazz);
		List<String> includedFields = AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.FIELDS);
		// CESCOZ
		/*
		 * if (alreadyUsedBeanPropertiesNames != null) {
		 * includedFields.removeAll(alreadyUsedBeanPropertiesNames); }
		 */
		Set<String> excludedFields = new HashSet<String>();
		excludedFields.addAll(AnnotationUtility.extractAsStringArray(elementUtils, method, annotation, AnnotationAttributeType.EXCLUDED_FIELDS));

		// initialize contentValues
		SQLProperty primaryKey = entity.getPrimaryKey();
		methodBuilder.addCode("$T contentValues=contentValues();\n", ContentValues.class);
		methodBuilder.addCode("contentValues.clear();\n\n");
		// for each property in entity except primaryKey and excluded properties

		boolean includePrimaryKey = annotation.getAttributeAsBoolean(AnnotationAttributeType.INCLUDE_PRIMARY_KEY);

		for (SQLProperty item : entity.getCollection()) {
			if (!includePrimaryKey && item.equals(primaryKey) || excludedFields.contains(item.getName()))
				continue;
			if (includedFields.size() > 0 && !includedFields.contains(item.getName()))
				continue;
			if (excludedFields.size() > 0 && excludedFields.contains(item.getName()))
				continue;

			if (TypeUtility.isNullable(item)) {
				methodBuilder.beginControlFlow("if ($L!=null)", getter(entityName, entityClassName, item));
			}

			// add property to list of used properties
			methodBuilder.addCode("contentValues.put($S, ", item.columnName);
			SQLTransformer.java2ContentValues(methodBuilder, entityClassName, entityName, item);
			methodBuilder.addCode(");\n");

			if (TypeUtility.isNullable(item)) {
				methodBuilder.nextControlFlow("else");
				methodBuilder.addCode("contentValues.putNull($S);\n", item.columnName);
				methodBuilder.endControlFlow();
			}
			methodBuilder.addCode("\n");

		}

	}

}
