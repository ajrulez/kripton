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
package sqlite.feature.pkstring.many2many1;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindSqlDelete;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.android.annotation.BindSqlUpdate;

// TODO: Auto-generated Javadoc
/**
 * The Interface BaseDao.
 *
 * @param <E>
 *            the element type
 */
public interface BaseStringDao<E> {

	/**
	 * Select all.
	 *
	 * @return the list
	 */
	@BindSqlSelect
	List<E> selectAll();

	/**
	 * Insert.
	 *
	 * @param bean
	 *            the bean
	 * @return the long
	 */
	@BindSqlInsert(includePrimaryKey=true)
	long insert(E bean);

	/**
	 * Select by id.
	 *
	 * @param id
	 *            the id
	 * @return the e
	 */
	@BindSqlSelect(where = "id=${id}")
	E selectById(String id);

	/**
	 * Delete by id.
	 *
	 * @param id
	 *            the id
	 * @return the int
	 */
	@BindSqlDelete(where = "id=${id}")
	int deleteById(String id);

	/**
	 * Delete by id.
	 *
	 * @param id
	 *            the id
	 * @return the int
	 */
	@BindSqlUpdate(where = "id=${id}")
	int updateById(String name, String id);

}
