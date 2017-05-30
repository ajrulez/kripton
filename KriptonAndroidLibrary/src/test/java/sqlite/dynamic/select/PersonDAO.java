package sqlite.dynamic.select;

import java.util.Date;
import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlDynamicOrderBy;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.android.annotation.BindSqlDynamicWhere;
import com.abubusoft.kripton.android.sqlite.OnReadBeanListener;

import sqlite.dynamic.Person;

@BindDao(Person.class)
public interface PersonDAO {

	@BindSqlInsert
	void insertOne(String name, String surname, String birthCity, Date birthDay);

	@BindSqlSelect(orderBy = "name")
	List<Person> selectAll();

	@BindSqlSelect(where = "name like ${nameTemp} || '%'")
	List<Person> selectOne(@BindSqlParam("nameTemp") String nameValue, @BindSqlDynamicWhere String where, @BindSqlDynamicOrderBy String orderBy);

	@BindSqlSelect(orderBy = "name")
	void selectBeanListener(OnReadBeanListener<Person> beanListener, @BindSqlDynamicOrderBy String orderBy);

	// @BindSqlSelect(orderBy="typeName")
	// void selectCursorListener(OnReadCursorListener cursorListener);
}