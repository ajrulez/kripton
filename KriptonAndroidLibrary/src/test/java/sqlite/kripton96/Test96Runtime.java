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
package sqlite.kripton96;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import base.BaseAndroidTest;
import sqlite.kripton93.BindBean93DataSource;


/**
 * @author xcesco
 *
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class Test96Runtime extends BaseAndroidTest {

	@Test
	public void testRunSqlite() throws IOException, InstantiationException, IllegalAccessException {
		BindBean96DataSource dataSource = BindBean96DataSource.instance();
		//dataSource.openWritableDatabase();
		
		final Bean96 bean=new Bean96();
		bean.name="all";

		dataSource.execute(new BindBean96DataSource.SimpleTransaction() {
			
			@Override
			public boolean onExecute(BindBean96DaoFactory daoFactory) throws Throwable {
				Bean96DaoImpl dao = daoFactory.getBean96Dao();
				
				dao.insert(bean);
				
				Bean96 result = dao.selectByBean("all");
				assertTrue(result!=null);
				
				return true;
			}
		});
	}

}
