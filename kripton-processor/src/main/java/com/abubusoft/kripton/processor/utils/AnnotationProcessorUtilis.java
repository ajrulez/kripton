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
package com.abubusoft.kripton.processor.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;

import com.squareup.javapoet.ClassName;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import com.abubusoft.kripton.android.annotation.BindDataSource;
import com.abubusoft.kripton.processor.BaseProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationProcessorUtilis.
 */
public abstract class AnnotationProcessorUtilis {

	/**
	 * KRIPTON_DEBUG info about generated classes.
	 *
	 * @param annotation the annotation
	 * @param className the class name
	 */
	public static void infoOnGeneratedClasses(Class<? extends Annotation> annotation, ClassName className) {
		String msg = String.format("class '%s' in package '%s' is generated by '@%s' annotation processor", className.simpleName(), className.packageName(), annotation.getSimpleName());
		
		printMessage(msg);
	}
	
	/**
	 * KRIPTON_DEBUG info about generated classes.
	 *
	 * @param annotation the annotation
	 * @param packageName the package name
	 * @param className the class name
	 */
	public static void infoOnGeneratedClasses(Class<? extends Annotation> annotation, String packageName, String className) {
		String msg = String.format("class '%s' in package '%s' is generated by '@%s' annotation processor", className, packageName, annotation.getSimpleName());
		
		printMessage(msg);
	}
	
	/**
	 * Prints the message.
	 *
	 * @param msg the msg
	 */
	private static void printMessage(String msg) 
	{
		if (BaseProcessor.JUNIT_TEST_MODE) {
			logger.info(msg);
		}

		if (!BaseProcessor.JUNIT_TEST_MODE || BaseProcessor.DEBUG_MODE)
			messager.printMessage(Diagnostic.Kind.NOTE, msg);
	}

	/** The logger. */
	static Logger logger = Logger.getGlobal();

	/** The messager. */
	private static Messager messager;

	/**
	 * Inits the.
	 *
	 * @param value the value
	 */
	public static void init(Messager value) {
		messager = value;
	}

	/**
	 * Info on generated file.
	 *
	 * @param annotation the annotation
	 * @param schemaCreateFile the schema create file
	 */
	public static void infoOnGeneratedFile(Class<BindDataSource> annotation, File schemaCreateFile) {
		String msg = String.format("file '%s' in directory '%s' is generated by '@%s' annotation processor", schemaCreateFile.getName(), schemaCreateFile.getParentFile(), annotation.getSimpleName());
		
		printMessage(msg);		
	}
}
