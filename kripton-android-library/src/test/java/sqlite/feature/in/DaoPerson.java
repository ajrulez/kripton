/*******************************************************************************
 * Copyright 2018 Francesco Benincasa (info@abubusoft.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package sqlite.feature.in;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;

/**
 * The Interface DaoPerson.
 */
@BindDao(Person.class)
public interface DaoPerson {

	/**
	 * Select all.
	 *
	 * @return the list
	 */
	//@BindSqlSelect(jql="select * from person where birthCityId in (${inCity})")
	//List<Person> selectAll(@BindSqlParam(collection=true) List<Long> inCity);
	
	@BindSqlSelect(jql="select * from person where birthCityId=${birthCityId}")
	List<Person> select1All(@BindSqlParam long birthCityId);
	
	/**
	 * Insert.
	 *
	 * @param bean the bean
	 */
	@BindSqlInsert
	void insert(Person bean);
}
