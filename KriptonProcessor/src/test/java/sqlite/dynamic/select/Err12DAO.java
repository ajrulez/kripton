package sqlite.dynamic.select;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlDynamicOrderBy;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;

import sqlite.dynamic.Person;

@BindDao(Person.class)
public interface Err12DAO {
	
	@BindSqlInsert
	void insertOne(String name, String surname, String birthCity);
	
	@BindSqlSelect(where="typeName like ${nameTemp} || '%' ")
	List<Person> selectOne(@BindSqlDynamicOrderBy String name, @BindSqlDynamicOrderBy String name2, @BindSqlParam("nameTemp") String nameValue);
	
}