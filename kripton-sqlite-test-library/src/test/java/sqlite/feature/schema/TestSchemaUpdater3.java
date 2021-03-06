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
package sqlite.feature.schema;

import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.DataSourceOptions;
import com.abubusoft.kripton.android.sqlite.SQLiteTestUtils;

import base.BaseAndroidTest;
import sqlite.feature.schema.version2.BindSchoolDataSource;

/**
 * The Class TestSchemaUpdater3.
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TestSchemaUpdater3 extends BaseAndroidTest {

	/**
	 * Destroy and recreate everything.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCustomUpdateSingleStep() throws Exception {
		SQLiteTestUtils.resetDataSourceInstance(BindSchoolDataSource.class);
		BindSchoolDataSource.build(DataSourceOptions.builder().addUpdateTask(3, new FileInputStream("schemas/school_update_2_3.sql")).build());

		SQLiteTestUtils.forceDataSourceSchemaUpdate(BindSchoolDataSource.getInstance(), 3);
		SQLiteTestUtils.verifySchema(BindSchoolDataSource.getInstance(), new FileInputStream("schemas/school_schema_2.sql"));

		Logger.info("finish");
	}

}
