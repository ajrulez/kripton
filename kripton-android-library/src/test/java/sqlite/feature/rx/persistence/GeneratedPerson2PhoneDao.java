package sqlite.feature.rx.persistence;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindDaoMany2Many;
import com.abubusoft.kripton.android.annotation.BindGeneratedDao;
import com.abubusoft.kripton.android.annotation.BindSqlDelete;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import java.util.List;
import sqlite.feature.rx.model.Person;
import sqlite.feature.rx.model.PhoneNumber;

@BindDao(PersonPhoneNumber.class)
@BindGeneratedDao(
    dao = Person2PhoneDao.class
)
@BindDaoMany2Many(
    entity1 = Person.class,
    entity2 = PhoneNumber.class
)
public interface GeneratedPerson2PhoneDao extends Person2PhoneDao {
  @BindSqlSelect(
      where = "id=:id"
  )
  PersonPhoneNumber selectById(@BindSqlParam("id") long id);

  @BindSqlSelect(
      where = "personId=:personId"
  )
  List<PersonPhoneNumber> selectByPersonId(@BindSqlParam("personId") long personId);

  @BindSqlSelect(
      where = "phoneNumberId=:phoneNumberId"
  )
  List<PersonPhoneNumber> selectByPhoneNumberId(@BindSqlParam("phoneNumberId") long phoneNumberId);

  @BindSqlDelete(
      where = "id=:id"
  )
  int deleteById(@BindSqlParam("id") long id);

  @BindSqlDelete(
      where = "personId=:personId"
  )
  int deleteByPersonId(@BindSqlParam("personId") long personId);

  @BindSqlDelete(
      where = "phoneNumberId=:phoneNumberId"
  )
  int deleteByPhoneNumberId(@BindSqlParam("phoneNumberId") long phoneNumberId);

  @BindSqlInsert
  int insert(@BindSqlParam("bean") PersonPhoneNumber bean);
}
