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
package sqlite.feature.contentprovider;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sqlite.feature.contentprovider.kripton35.TestContentProviderInsert;
import sqlite.feature.contentprovider.kripton35.TestContentProviderRuntime;
import sqlite.feature.contentprovider.kripton35.TestContentProviderWrong;

/**
 * The Class TestContentProviderRuntimeSuite.
 */
@RunWith(Suite.class)
//@formatter:off
@Suite.SuiteClasses(
		{ 
		TestContentProviderRuntime.class, 
		TestContentProviderInsert.class,
		TestContentProviderWrong.class
		 })
//@formatter:on
public class TestContentProviderRuntimeSuite  {
	
}
