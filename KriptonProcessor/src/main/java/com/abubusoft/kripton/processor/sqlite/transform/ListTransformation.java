package com.abubusoft.kripton.processor.sqlite.transform;

import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.getter;
import static com.abubusoft.kripton.processor.core.reflect.PropertyUtility.setter;

import java.util.ArrayList;
import java.util.List;

import com.abubusoft.kripton.common.CaseFormat;
import com.abubusoft.kripton.common.Converter;
import com.abubusoft.kripton.common.ProcessorHelper;
import com.abubusoft.kripton.processor.core.ModelProperty;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class ListTransformation extends AbstractCompileTimeTransform {

	static Converter<String, String> nc = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

	private ParameterizedTypeName listTypeName;

	private TypeName rawTypeName;

	public ListTransformation(ParameterizedTypeName clazz) {
		this.listTypeName = clazz;
		this.rawTypeName = listTypeName.typeArguments.get(0);
	}

	@Override
	public void generateWriteProperty(Builder methodBuilder, TypeName beanClass, String beanName, ModelProperty property) {
		methodBuilder.addCode("$T.asByteArray($L)", ProcessorHelper.class, getter(beanName, beanClass, property));
	}

	@Override
	public void generateWriteProperty(Builder methodBuilder, String objectName) {
		methodBuilder.addCode("$T.asByteArray($L)", ProcessorHelper.class, objectName);
	}

	@Override
	public void generateReadProperty(Builder methodBuilder, TypeName beanClass, String beanName, ModelProperty property, String cursorName, String indexName) {
		String name = nc.convert(rawTypeName.toString().substring(rawTypeName.toString().lastIndexOf(".") + 1));

		Class<?> listClazz = defineListClass(listTypeName);

		methodBuilder.addCode(setter(beanClass, beanName, property, "$T.asCollection(new $T<$L>(), $L.class, $L.getBlob($L))"), ProcessorHelper.class, listClazz, name, name, cursorName, indexName);
	}

	private Class<?> defineListClass(ParameterizedTypeName listTypeName) {
		if (listTypeName.toString().startsWith(List.class.getCanonicalName())) {
			// it's a list
			return ArrayList.class;
		}
		try {
			return Class.forName(listTypeName.rawType.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void generateResetProperty(Builder methodBuilder, TypeName beanClass, String beanName, ModelProperty property, String cursorName, String indexName) {
		methodBuilder.addCode("$L." + setter(beanClass, beanName, property, "null"));
	}

	@Override
	public String generateColumnType(ModelProperty property) {
		return "BLOB";
	}
}
