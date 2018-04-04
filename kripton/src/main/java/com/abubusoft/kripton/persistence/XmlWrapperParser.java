package com.abubusoft.kripton.persistence;

import java.io.InputStream;
import java.io.Reader;

import com.abubusoft.kripton.BinderContext;
import com.abubusoft.kripton.BinderType;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import com.abubusoft.kripton.xml.XMLParser;
import com.fasterxml.jackson.core.JsonEncoding;

public class XmlWrapperParser implements ParserWrapper {
	
	public XMLParser xmlParser;

	public XmlWrapperParser(BinderContext context, InputStream inputStream, BinderType supportedFormat) {
		//KXmlParser xmlParser=new KXmlParser();
		this.xmlParser = new XMLParser();
		this.xmlParser.setInput(inputStream, JsonEncoding.UTF8.getJavaName());
	}

	public XmlWrapperParser(BinderContext context, Reader reader, BinderType supportedFormat) {
		this.xmlParser = new XMLParser();
		this.xmlParser.setInput(reader);
	}

	@Override
	public void close() {
		try {
			xmlParser.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw (new KriptonRuntimeException(e));
		}

	}

}
