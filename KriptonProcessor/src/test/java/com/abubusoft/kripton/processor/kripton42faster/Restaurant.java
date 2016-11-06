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
package com.abubusoft.kripton.processor.kripton42faster;

import com.abubusoft.kripton.annotation.BindType;
import com.abubusoft.kripton.annotation.BindXml;
import com.abubusoft.kripton.binder.xml.XmlType;

@BindType
public class Restaurant {

	@BindXml(XmlType.ATTRIBUTE)
	public long id;
			
	@BindXml(XmlType.ATTRIBUTE)
	public String name;
	
	@BindXml(XmlType.VALUE_CDATA)
	public String address;
		
	@BindXml(XmlType.ATTRIBUTE)
	public Double longitude;
	
	@BindXml(XmlType.ATTRIBUTE)
	public Double latitude;
}