/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa.
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
/**
 * 
 */
package com.abubusoft.kripton.processor.exceptions;

import com.abubusoft.kripton.processor.BaseProcessor;

/**
 * @author Francesco Benincasa (abubusoft@gmail.com)
 *
 */
public class KriptonProcessorException extends RuntimeException {

	private static final long serialVersionUID = 2217746400887102609L;

	public KriptonProcessorException() {
		super();
	}

	public KriptonProcessorException(String message) {
		super(message);
	}

	public KriptonProcessorException(Throwable cause) {
		super(cause);
	}

	public KriptonProcessorException(Exception cause) {
		super(cause);
	}

	public String getErrorCode() {
		if (BaseProcessor.TEST_MODE)
			return getClass().getName() + ": ";

		return "";
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 *         (which may be {@code null}).
	 */
	@Override
	public String getMessage() {
		return getErrorCode() + super.getMessage();
	}

}
