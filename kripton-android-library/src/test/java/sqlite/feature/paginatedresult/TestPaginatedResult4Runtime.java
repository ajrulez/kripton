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
package sqlite.feature.paginatedresult;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.PaginatedResult;

import base.BaseAndroidTest;
import sqlite.feature.paginatedresult.model.Person;

// TODO: Auto-generated Javadoc
/**
 * The Class TestPaginatedResult4Runtime.
 *
 * @author xcesco
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TestPaginatedResult4Runtime extends BaseAndroidTest {

	/**
	 * Test run.
	 */
	@Test
	public void testRun() {				
		try (BindPerson4DataSource dataSource = BindPerson4DataSource.open(); Dao4PersonImpl dao = dataSource.getDao4Person()) {
			dao.deleteAll();
			
			for (int i = 0; i < 100; i++) {
				dao.insertOne(String.format("name%03d", i), String.format("surname%03d", i), String.format("birthCity%03d", i), new Date());
			}

			PaginatedResult<Person> result = dao.select(0, 10);

			int i = 0;

			while (result.nextPage()) {
				Logger.info("---------------");
				Logger.info("\tPage " + i);
				Logger.info("---------------");
				for (Person item : result.list()) {
					Logger.info(item.toString());
				}

				assertTrue(result.list().get(0).name.equals(String.format("name%03d", i * 10)));

				i++;
			}
		}

	}

	/**
	 * Test goto page.
	 */
	@Test
	public void testGotoPage() {
		try (BindPerson4DataSource dataSource = BindPerson4DataSource.open(); Dao4PersonImpl dao = dataSource.getDao4Person()) {
			dao.deleteAll();
			
			for (int i = 0; i < 100; i++) {
				dao.insertOne(String.format("name%03d", i), String.format("surname%03d", i), String.format("birthCity%03d", i), new Date());
			}

			PaginatedResult<Person> result = dao.select(0, 10);

			{
				int i = 5;
				result.gotoPage(i);
				Logger.info("---------------");
				Logger.info("\tPage " + i);
				Logger.info("---------------");
				for (Person item : result.list()) {
					Logger.info(item.toString());
				}
				assertTrue(result.list().get(0).name.equals(String.format("name%03d", i * 10)));
			}

			{
				int i = 11;
				result.gotoPage(i);
				Logger.info("---------------");
				Logger.info("\tPage " + i);
				Logger.info("---------------");
				for (Person item : result.list()) {
					Logger.info(item.toString());
				}
				assertTrue(result.list().size()==0);
				assertTrue(!result.hasNext());
				//assertTrue(result.list().get(0).name.equals(String.format("name%03d", i * 10)));
			}
			
			{
				int i = -111;
				result.gotoPage(i);
				Logger.info("---------------");
				Logger.info("\tPage " + i);
				Logger.info("---------------");
				for (Person item : result.list()) {
					Logger.info(item.toString());
				}
				assertTrue(result.list().size()==0);
				assertTrue(!result.hasNext());
				//assertTrue(result.list().get(0).name.equals(String.format("name%03d", i * 10)));
			}

		}
	}
}
