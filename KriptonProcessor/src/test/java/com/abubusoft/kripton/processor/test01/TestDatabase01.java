package com.abubusoft.kripton.processor.test01;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.abubusoft.kripton.processor.BaseProcessorTest;

/**
 * @author xcesco
 *
 */
@RunWith(JUnit4.class)
public class TestDatabase01 extends BaseProcessorTest {

	/**
	 * No @BindType is put in bean definition
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test01() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy01DataSource.class, Bean01.class);
	}

	/**
	 * No @BindType is put in bean definition
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test02() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy02Database.class, Bean01.class, Bean02.class);

	}

	/**
	 * No database schema with @BindDatabaseSchema was found
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected=AssertionError.class)
	public void test03() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy03Database.class, Bean01.class, Bean02.class);
	}

	/**
	 * Class com.abubusoft.kripton.processor.test01.Bean04, used in Dummy04DatabaseSchema DatabaseSchemaDefinition, has no property!
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test04() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy04Database.class, Bean04.class);
	}

	/**
	 * No database schema with @BindDatabaseSchema was found
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test05() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy05Database.class, Bean04.class);		
	}

	@Test(expected = AssertionError.class)
	public void test06() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy06Database.class, Bean06.class);	
	}

	/**
	 * No primary key
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test07() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy07Database.class, Bean07.class);					
	}

	/**
	 * No primary key
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test08() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy08Database.class, Bean08.class);		
	}

	/**
	 * Primary key Long
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test09() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy09Database.class, Bean09.class);		
	}

	/**
	 * Too many primary keys
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test10() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy10Database.class, Bean10.class);		
	}
	
	/**
	 * Too many primary keys
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test11() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy11Error.class, Bean10.class);		
	}
	
	/**
	 * Twice database definitino
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(expected = AssertionError.class)
	public void test12() throws IOException, InstantiationException, IllegalAccessException {
		buildDataSourceProcessorTest(Dummy12ADatabase.class, Dummy12BDatabase.class,Bean12.class);		
	}

}