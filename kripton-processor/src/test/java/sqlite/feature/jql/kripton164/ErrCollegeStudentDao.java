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
package sqlite.feature.jql.kripton164;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;

// TODO: Auto-generated Javadoc
/**
 * Created by xcesco on 30/08/2017.
 */
@BindDao(ErrCollegeStudent.class)
public interface ErrCollegeStudentDao {
   	
	/**
	 * Insert bean from select.
	 *
	 * @param bean the bean
	 */
	@BindSqlInsert(jql = "INSERT OR REPLACE INTO ErrCollegeStudent (surname) SELECT surname FROM CollegeStudent WHERE surname=${bean.surname}")
	void insertBeanFromSelect(CollegeStudent bean);
}
