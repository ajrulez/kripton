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
package com.abubusoft.kripton.processor.sqlite;

import static com.abubusoft.kripton.processor.core.reflect.TypeUtility.className;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.annotation.BindDataSource;
import com.abubusoft.kripton.android.sqlite.AbstractDataSource;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import com.abubusoft.kripton.processor.core.reflect.TypeUtility;
import com.abubusoft.kripton.processor.exceptions.CircularRelationshipException;
import com.abubusoft.kripton.processor.sqlite.core.EntityUtility;
import com.abubusoft.kripton.processor.sqlite.core.JavadocUtility;
import com.abubusoft.kripton.processor.sqlite.model.SQLDaoDefinition;
import com.abubusoft.kripton.processor.sqlite.model.SQLEntity;
import com.abubusoft.kripton.processor.sqlite.model.SQLiteDatabaseSchema;
import com.abubusoft.kripton.processor.utils.AnnotationProcessorUtilis;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Generates content provider class
 * 
 * @author Francesco Benincasa (abubusoft@gmail.com)
 *
 */
public class BindContentProviderBuilder extends AbstractBuilder {

	public static final String PREFIX = "Bind";

	public static final String SUFFIX = "ContentProvider";
	
	public BindContentProviderBuilder(Elements elementUtils, Filer filer, SQLiteDatabaseSchema model) {
		super(elementUtils, filer, model);
	}

	/**
	 * Generate content provider
	 * 
	 * @param elementUtils
	 * @param filer
	 * @param schema
	 * @throws Exception
	 */
	public static void generate(Elements elementUtils, Filer filer, SQLiteDatabaseSchema schema) throws Exception {
		BindContentProviderBuilder visitorDao = new BindContentProviderBuilder(elementUtils, filer, schema);
		visitorDao.buildDataSource(elementUtils, filer, schema);
	}

	public void buildDataSource(Elements elementUtils, Filer filer, SQLiteDatabaseSchema schema) throws Exception {
		String dataSourceName = schema.getName();
		String dataSourceNameClazz = BindDataSourceBuilder.PREFIX + dataSourceName;
		String contentProviderName = PREFIX + dataSourceName.replace(BindDataSourceBuilder.SUFFIX, "")+ SUFFIX;

		ClassName contentProviderClazz = className(contentProviderName);
		Converter<String, String> convert = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

		PackageElement pkg = elementUtils.getPackageOf(schema.getElement());
		String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();
		
		AnnotationProcessorUtilis.infoOnGeneratedClasses(BindDataSource.class, packageName, dataSourceName);
		builder = TypeSpec.classBuilder(contentProviderName).addModifiers(Modifier.PUBLIC).superclass(ContentProvider.class);
		
		generateOnCreate(dataSourceNameClazz);
		
		generateInsert();
		
		generateQuery();
		
		generateGetType();				
		
		generateDelete();
		
		generateUpdate(); 
		
		generateOnShutdown(dataSourceNameClazz);
		
		// define static fields

		// instance
		builder.addField(FieldSpec.builder(className(dataSourceNameClazz), "dataSource", Modifier.PRIVATE, Modifier.STATIC).addJavadoc("<p>datasource singleton</p>\n").build());		
		builder.addField(FieldSpec.builder(String.class, "AUTHORITY", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).addJavadoc("<p>Content provider authority</p>\n").initializer("$S",schema.authority).build());
		builder.addField(FieldSpec.builder(UriMatcher.class, "sURIMatcher", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).addJavadoc("<p>URI matcher</p>\n").initializer("new $T($T.NO_MATCH)",UriMatcher.class, UriMatcher.class).build());
		builder.addStaticBlock(CodeBlock.builder().addStatement("sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS)").build());

/*
		builder.addJavadoc("<p>\n");
		builder.addJavadoc("Rapresents implementation of datasource $L.\n", schema.getName());
		builder.addJavadoc("This class expose database interface through Dao attribute.\n", schema.getName());
		builder.addJavadoc("</p>\n\n");

		JavadocUtility.generateJavadocGeneratedBy(builder);
		builder.addJavadoc("@see $T\n", className(schema.getName()));
		builder.addJavadoc("@see $T\n", daoFactoryClazz);
		for (SQLDaoDefinition dao : schema.getCollection()) {
			TypeName daoImplName = BindDaoBuilder.daoTypeName(dao);
			builder.addJavadoc("@see $T\n", dao.getElement());
			builder.addJavadoc("@see $T\n", daoImplName);
			builder.addJavadoc("@see $T\n", TypeUtility.typeName(dao.getEntity().getElement()));
		}


		for (SQLDaoDefinition dao : schema.getCollection()) {
			// TypeName daoInterfaceName =
			// BindDaoBuilder.daoInterfaceTypeName(dao);
			TypeName daoImplName = BindDaoBuilder.daoTypeName(dao);
			builder.addField(FieldSpec.builder(daoImplName, convert.convert(dao.getName()), Modifier.PROTECTED).addJavadoc("<p>dao instance</p>\n").initializer("new $T(this)", daoImplName).build());

			// dao with connections
			{
				MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("get" + dao.getName()).addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(BindDaoBuilder.daoTypeName(dao));
				methodBuilder.addCode("return $L;\n", convert.convert(dao.getName()));
				builder.addMethod(methodBuilder.build());
			}
		}

		// interface
		generateMethodExecute(daoFactoryName);

		// generate instance
		generateInstance(dataSourceName);

		// generate open
		generateOpen(dataSourceName);

		// generate openReadOnly
		generateOpenReadOnly(dataSourceName);

		{
			// constructor
			MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED);
			methodBuilder.addStatement("super($S, $L)", schema.fileName, schema.version);
			builder.addMethod(methodBuilder.build());
		}

		// before use entities, order them with dependencies respect
		List<SQLEntity> orderedEntities = generateOrderedEntitiesList(schema);

		// onCreate
		boolean useForeignKey = generateOnCreate(schema, orderedEntities);

		// onUpgrade
		generateOnUpgrade(schema, orderedEntities);

		// onConfigure
		generateOnConfigure(useForeignKey);
		*/

		TypeSpec typeSpec = builder.build();
		JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
	}

	private void generateInsert() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("insert").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Uri.class);
		methodBuilder.addParameter(Uri.class, "uri");
		methodBuilder.addParameter(ContentValues.class, "values");
		
		methodBuilder.addCode("return null;\n");
		
		builder.addMethod(methodBuilder.build());
		
	}
	
	private void generateDelete() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("delete").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Integer.TYPE);
		methodBuilder.addParameter(Uri.class, "uri");
		methodBuilder.addParameter(String.class, "selection");
		methodBuilder.addParameter(ParameterSpec.builder(TypeUtility.arrayTypeName(String.class), "selectionArgs").build());
		
		methodBuilder.addCode("return 0;\n");
		
		builder.addMethod(methodBuilder.build());		
	}
	
	private void generateUpdate() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("update").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Integer.TYPE);
		methodBuilder.addParameter(Uri.class, "uri");
		methodBuilder.addParameter(ContentValues.class, "values");
		methodBuilder.addParameter(String.class, "selection");
		methodBuilder.addParameter(ParameterSpec.builder(TypeUtility.arrayTypeName(String.class), "selectionArgs").build());
		
		methodBuilder.addCode("return 0;\n");
		
		builder.addMethod(methodBuilder.build());
		
	}

	private void generateGetType() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getType").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(String.class);
		methodBuilder.addParameter(Uri.class, "uri");
		
		methodBuilder.addCode("return null;\n");
		
		builder.addMethod(methodBuilder.build());
		
	}

	private void generateOnCreate(String dataSourceNameClazz) {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onCreate").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Boolean.TYPE);

		methodBuilder.addJavadoc("<p>Create datasource and open database in read mode.</p>\n");
		methodBuilder.addJavadoc("\n");
		methodBuilder.addJavadoc("@see android.content.ContentProvider#onCreate()\n");
			
		methodBuilder.addStatement("dataSource = $L.instance()", dataSourceNameClazz);
		methodBuilder.addStatement("dataSource.openReadOnlyDatabase()");		
		
		methodBuilder.addCode("return true;\n");
		
		builder.addMethod(methodBuilder.build());
	}
	
	private void generateOnShutdown(String dataSourceNameClazz) {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("shutdown").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Void.TYPE);

		methodBuilder.addJavadoc("<p>Close database.</p>\n");
		methodBuilder.addJavadoc("\n");
		methodBuilder.addJavadoc("@see android.content.ContentProvider#shutdown()\n");
			
		methodBuilder.addStatement("super.shutdown()");
		methodBuilder.addStatement("dataSource.close()");		
						
		builder.addMethod(methodBuilder.build());
	}
	
	private void generateQuery() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("query").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(Cursor.class);
		methodBuilder.addParameter(Uri.class, "uri");
		methodBuilder.addParameter(ParameterSpec.builder(TypeUtility.arrayTypeName(String.class), "projection").build());
		methodBuilder.addParameter(String.class, "selection");
		methodBuilder.addParameter(ParameterSpec.builder(TypeUtility.arrayTypeName(String.class), "selectionArgs").build());
		methodBuilder.addParameter(String.class, "sortOrder");

		methodBuilder.addCode("return null;\n");
		
		builder.addMethod(methodBuilder.build());
	}
	 

	/**
	 * @param schemaName
	 */
	private void generateOpen(String schemaName) {
		// instance
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("open").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(className(schemaName));

		methodBuilder.addJavadoc("Retrieve data source instance and open it.\n");
		methodBuilder.addJavadoc("@return opened dataSource instance.\n");

		methodBuilder.addStatement("instance.openWritableDatabase()");
		methodBuilder.addCode("return instance;\n");

		builder.addMethod(methodBuilder.build());
	}

	/**
	 * @param schemaName
	 */
	private void generateOpenReadOnly(String schemaName) {
		// instance
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("openReadOnly").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(className(schemaName));

		methodBuilder.addJavadoc("Retrieve data source instance and open it in read only mode.\n");
		methodBuilder.addJavadoc("@return opened dataSource instance.\n");

		methodBuilder.addStatement("instance.openReadOnlyDatabase()");
		methodBuilder.addCode("return instance;\n");

		builder.addMethod(methodBuilder.build());
	}

	/**
	 * @param schema
	 * @param orderedEntities
	 */
	private boolean generateOnCreate(SQLiteDatabaseSchema schema, List<SQLEntity> orderedEntities) {
		boolean useForeignKey = false;
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onCreate").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC);
		methodBuilder.addParameter(SQLiteDatabase.class, "database");
		methodBuilder.addJavadoc("onCreate\n");
		methodBuilder.addCode("// generate tables\n");
		for (SQLEntity item : orderedEntities) {
			if (schema.isLogEnabled()) {
				methodBuilder.addCode("$T.info(\"DDL: %s\",$T.CREATE_TABLE_SQL);\n", Logger.class, BindTableGenerator.tableClassName(item));
			}
			methodBuilder.addCode("database.execSQL($T.CREATE_TABLE_SQL);\n", BindTableGenerator.tableClassName(item));

			if (item.referedEntities.size() > 0) {
				useForeignKey = true;
			}
		}

		methodBuilder.beginControlFlow("if (options.databaseLifecycleHandler != null)");
		methodBuilder.addStatement("options.databaseLifecycleHandler.onCreate(database)");
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());

		return useForeignKey;
	}

	/**
	 * @param schema
	 * @param orderedEntities
	 */
	private void generateOnUpgrade(SQLiteDatabaseSchema schema, List<SQLEntity> orderedEntities) {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onUpgrade").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC);
		methodBuilder.addParameter(SQLiteDatabase.class, "database");
		methodBuilder.addParameter(Integer.TYPE, "oldVersion");
		methodBuilder.addParameter(Integer.TYPE, "newVersion");
		methodBuilder.addJavadoc("onUpgrade\n");

		Collections.reverse(orderedEntities);

		methodBuilder.beginControlFlow("if (options.databaseLifecycleHandler != null)");
		methodBuilder.addStatement("options.databaseLifecycleHandler.onUpdate(database, oldVersion, newVersion, true)");
		methodBuilder.nextControlFlow("else");

		methodBuilder.addCode("// drop tables\n");
		for (SQLEntity item : orderedEntities) {
			if (schema.isLogEnabled()) {
				methodBuilder.addCode("$T.info(\"DDL: %s\",$T.DROP_TABLE_SQL);\n", Logger.class, BindTableGenerator.tableClassName(item));
			}
			methodBuilder.addCode("database.execSQL($T.DROP_TABLE_SQL);\n", BindTableGenerator.tableClassName(item));
		}

		// reorder entities
		Collections.reverse(orderedEntities);

		methodBuilder.addCode("\n");
		methodBuilder.addCode("// generate tables\n");

		for (SQLEntity item : orderedEntities) {
			if (schema.isLogEnabled()) {
				methodBuilder.addCode("$T.info(\"DDL: %s\",$T.CREATE_TABLE_SQL);\n", Logger.class, BindTableGenerator.tableClassName(item));
			}
			methodBuilder.addCode("database.execSQL($T.CREATE_TABLE_SQL);\n", BindTableGenerator.tableClassName(item));
		}

		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());
	}

	/**
	 * @param useForeignKey
	 */
	private void generateOnConfigure(boolean useForeignKey) {
		// onConfigure

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onConfigure").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC);
		methodBuilder.addParameter(SQLiteDatabase.class, "database");
		methodBuilder.addJavadoc("onConfigure\n");
		methodBuilder.addCode("// configure database\n");

		if (useForeignKey) {
			methodBuilder.addStatement("database.setForeignKeyConstraintsEnabled(true)");
		}

		methodBuilder.beginControlFlow("if (options.databaseLifecycleHandler != null)");
		methodBuilder.addStatement("options.databaseLifecycleHandler.onConfigure(database)");
		methodBuilder.endControlFlow();

		builder.addMethod(methodBuilder.build());
	}

	private List<SQLEntity> generateOrderedEntitiesList(SQLiteDatabaseSchema schema) {
		List<SQLEntity> entities = schema.getEntitiesAsList();
		Collections.sort(entities, new Comparator<SQLEntity>() {

			@Override
			public int compare(SQLEntity lhs, SQLEntity rhs) {
				return lhs.getTableName().compareTo(rhs.getTableName());
			}
		});

		List<SQLEntity> list = schema.getEntitiesAsList();

		EntityUtility<SQLEntity> sorder = new EntityUtility<SQLEntity>(list) {

			@Override
			public Collection<SQLEntity> getDependencies(SQLEntity item) {
				return item.referedEntities;
			}

			@Override
			public void generateError(SQLEntity item) {
				throw new CircularRelationshipException(item);
			}
		};

		return sorder.order();
	}

	/**
	 * <p>
	 * Generate transaction an execute method
	 * </p>
	 * 
	 * @param dataSource
	 */
	public void generateMethodExecute(String daoFactory) {

		// create interface
		String transationExecutorName = "Transaction";
		//@formatter:off
		ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(className("AbstractTransaction"), className(daoFactory));
		builder.addType(
				TypeSpec.interfaceBuilder(transationExecutorName)
				.addModifiers(Modifier.PUBLIC)
				.addSuperinterface(parameterizedTypeName)
				.addJavadoc("interface to define transactions\n").build());
		//@formatter:on

		// create SimpleTransaction class
		String simpleTransactionClassName = "SimpleTransaction";
		//@formatter:off
		builder.addType(
				TypeSpec.classBuilder(simpleTransactionClassName)
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.STATIC)
				.addSuperinterface(className(transationExecutorName))
				.addJavadoc("Simple class implements interface to define transactions\n")
				.addMethod(MethodSpec.methodBuilder("onError")
						.addAnnotation(Override.class)
						.returns(Void.TYPE)
						.addModifiers(Modifier.PUBLIC)
						.addParameter(Throwable.class, "e")						
						.addStatement("throw(new $T(e))", KriptonRuntimeException.class)
						.build()
						)
				.build());
				
				
		//@formatter:on

		MethodSpec.Builder executeMethod = MethodSpec.methodBuilder("execute").addModifiers(Modifier.PUBLIC).addParameter(className(transationExecutorName), "transaction");

		executeMethod.addCode("$T connection=openWritableDatabase();\n", SQLiteDatabase.class);

		executeMethod.beginControlFlow("try");
		executeMethod.addCode("connection.beginTransaction();\n");

		executeMethod.beginControlFlow("if (transaction!=null && transaction.onExecute(this))");
		executeMethod.addCode("connection.setTransactionSuccessful();\n");
		executeMethod.endControlFlow();

		executeMethod.nextControlFlow("catch($T e)", Throwable.class);

		executeMethod.addStatement("$T.error(e.getMessage())", Logger.class);
		executeMethod.addStatement("e.printStackTrace()");
		executeMethod.addStatement("if (transaction!=null) transaction.onError(e)");

		executeMethod.nextControlFlow("finally");
		executeMethod.beginControlFlow("try");
		executeMethod.addStatement("connection.endTransaction()");
		executeMethod.nextControlFlow("catch ($T e)", Throwable.class);
		executeMethod.addStatement("$T.warn(\"error closing transaction %s\", e.getMessage())", Logger.class);
		executeMethod.endControlFlow();
		executeMethod.addStatement("close()");
		executeMethod.endControlFlow();

		// generate javadoc
		executeMethod.addJavadoc("<p>Executes a transaction. This method <strong>is thread safe</strong> to avoid concurrent problems. The"
				+ "drawback is only one transaction at time can be executed. The database will be open in write mode.</p>\n");
		executeMethod.addJavadoc("\n");
		executeMethod.addJavadoc("@param transaction\n\ttransaction to execute\n");

		builder.addMethod(executeMethod.build());
	}

}
