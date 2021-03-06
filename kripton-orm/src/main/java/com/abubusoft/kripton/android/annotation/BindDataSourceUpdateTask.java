/**
 * 
 */
package com.abubusoft.kripton.android.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.abubusoft.kripton.android.sqlite.SQLiteUpdateTask;

/**
 * The Interface BindDataSourceUpdateTask.
 * 
 * @author Francesco Benincasa (info@abubusoft.com)
 */
@Retention(CLASS)
@Target(ANNOTATION_TYPE)
public @interface BindDataSourceUpdateTask {

	/**
	 * Version.
	 *
	 * @return the int
	 */
	int version();

	/**
	 * Task.
	 *
	 * @return the class&lt;? extends SQ lite update task&gt;
	 */
	Class<? extends SQLiteUpdateTask> task();
}
