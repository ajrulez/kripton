/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa (info@abubusoft.com).
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
package sqlite.kripton58.array2;

import com.abubusoft.kripton.annotation.BindType;

import sqlite.kripton58.BeanInner;

@BindType
public class BeanBean {

	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BeanInner[] getValue() {
		return value;
	}

	public void setValue(BeanInner[] value) {
		this.value = value;
	}

	public BeanInner[] getValue2() {
		return value2;
	}

	public void setValue2(BeanInner[] value2) {
		this.value2 = value2;
	}

	private BeanInner[] value;
	
	private BeanInner[] value2;
	

}
