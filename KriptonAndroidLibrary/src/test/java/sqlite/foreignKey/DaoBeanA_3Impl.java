package sqlite.foreignKey;

import android.content.ContentValues;
import android.database.Cursor;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDao;
import com.abubusoft.kripton.android.sqlite.SqlUtils;
import com.abubusoft.kripton.common.StringUtils;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * DAO implementation for entity <code>BeanA_3</code>, based on interface <code>DaoBeanA_3</code>
 * </p>
 *
 *  @see BeanA_3
 *  @see DaoBeanA_3
 *  @see BeanA_3Table
 */
public class DaoBeanA_3Impl extends AbstractDao implements DaoBeanA_3 {
  public DaoBeanA_3Impl(BindDummy2DataSource dataSet) {
    super(dataSet);
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, value_string2 FROM bean_a_3</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>value_string2</dt><dd>is associated to bean's property <strong>valueString2</strong></dd>
   * </dl>
   *
   * @return collection of bean or empty collection.
   */
  @Override
  public List<BeanA_3> selectAll() {
    // build where condition
    String[] _args={};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id, value_string2 FROM bean_a_3",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id, value_string2 FROM bean_a_3", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_3> resultList=new LinkedList<BeanA_3>();
      BeanA_3 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("value_string2");

        do
         {
          resultBean=new BeanA_3();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }
          if (!cursor.isNull(index1)) { resultBean.valueString2=cursor.getString(index1); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, value_string2 FROM bean_a_3 WHERE id=${id}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>value_string2</dt><dd>is associated to bean's property <strong>valueString2</strong></dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is binded to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * @param id
   * 	is binded to <code>${id}</code>
   * @return collection of bean or empty collection.
   */
  @Override
  public List<BeanA_3> selectById(long id) {
    // build where condition
    String[] _args={String.valueOf(id)};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id, value_string2 FROM bean_a_3 WHERE id='%s'",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id, value_string2 FROM bean_a_3 WHERE id=?", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_3> resultList=new LinkedList<BeanA_3>();
      BeanA_3 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("value_string2");

        do
         {
          resultBean=new BeanA_3();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }
          if (!cursor.isNull(index1)) { resultBean.valueString2=cursor.getString(index1); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id FROM bean_a_3 WHERE valueString2=${dummy}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${dummy}</dt><dd>is binded to method's parameter <strong>value</strong></dd>
   * </dl>
   *
   * @param value
   * 	is binded to <code>${dummy}</code>
   * @return collection of bean or empty collection.
   */
  @Override
  public List<BeanA_3> selectByString(String value) {
    // build where condition
    String[] _args={(value==null?"":value)};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id FROM bean_a_3 WHERE value_string2='%s'",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id FROM bean_a_3 WHERE value_string2=?", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_3> resultList=new LinkedList<BeanA_3>();
      BeanA_3 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");

        do
         {
          resultBean=new BeanA_3();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <p>SQL insert:</p>
   * <pre>INSERT INTO bean_a_3 (value_string2) VALUES (${bean.valueString2})</pre>
   *
   * <p><code>bean.id</code> is automatically updated because it is the primary key</p>
   *
   * <p><strong>Inserted columns:</strong></p>
   * <dl>
   * 	<dt>value_string2</dt><dd>is mapped to <strong>${bean.valueString2}</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is mapped to parameter <strong>bean</strong>
   *
   * @return <strong>id</strong> of inserted record
   */
  @Override
  public int insert(BeanA_3 bean) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    if (bean.valueString2!=null) {
      contentValues.put("value_string2", bean.valueString2);
    } else {
      contentValues.putNull("value_string2");
    }

    //StringUtils and SqlUtils will be used to format SQL
    // log
    Logger.info(SqlUtils.formatSQL("INSERT INTO bean_a_3 (value_string2) VALUES ('"+StringUtils.checkSize(contentValues.get("value_string2"))+"')"));
    long result = database().insert("bean_a_3", null, contentValues);
    bean.id=result;

    return (int)result;
  }

  /**
   * <h2>SQL update:</h2>
   * <pre>UPDATE bean_a_3 SET value_string2=${bean.valueString2} WHERE valueString2=${bean.valueString2}</pre>
   *
   * <h2>Updated columns:</h2>
   * <dl>
   * 	<dt>value_string2</dt><dd>is mapped to <strong>${bean.valueString2}</strong></dd>
   * </dl>
   *
   * <h2>Parameters used in where conditions:</h2>
   * <dl>
   * 	<dt>${bean.valueString2}</dt><dd>is mapped to method's parameter <strong>bean.valueString2</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is used as ${bean}
   *
   * @return number of updated records
   */
  @Override
  public int update(BeanA_3 bean) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    if (bean.valueString2!=null) {
      contentValues.put("value_string2", bean.valueString2);
    } else {
      contentValues.putNull("value_string2");
    }

    String[] whereConditionsArray={(bean.valueString2==null?"":bean.valueString2)};

    //StringUtils and SqlUtils will be used to format SQL
    Logger.info(SqlUtils.formatSQL("UPDATE bean_a_3 SET value_string2='"+StringUtils.checkSize(contentValues.get("value_string2"))+"' WHERE valueString2='%s'", (Object[]) whereConditionsArray));
    int result = database().update("bean_a_3", contentValues, "UPDATE bean_a_3 SET value_string2='"+StringUtils.checkSize(contentValues.get("value_string2"))+"' WHERE valueString2='%s'", whereConditionsArray);
    return result;
  }
}
