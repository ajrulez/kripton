/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
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
package com.abubusoft.kripton.processor.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class KriptonClassNotFoundException.
 *
 * @author Francesco Benincasa (info@abubusoft.com)
 */
public class KriptonClassNotFoundException extends KriptonProcessorException {

	/**
	 * Instantiates a new kripton class not found exception.
	 *
	 * @param e the e
	 */
	public KriptonClassNotFoundException(ClassNotFoundException e) {
		super(e);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5217387587464012546L;

}
