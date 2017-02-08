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
 * DAO implementation for entity <code>BeanA_1</code>, based on interface <code>DaoBeanA_1</code>
 * </p>
 *
 *  @see BeanA_1
 *  @see DaoBeanA_1
 *  @see BeanA_1Table
 */
public class DaoBeanA_1Impl extends AbstractDao implements DaoBeanA_1 {
  public DaoBeanA_1Impl(BindDummyDataSource dataSet) {
    super(dataSet);
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, bean_a2_id, value_string FROM bean_a_1</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>bean_a2_id</dt><dd>is associated to bean's property <strong>beanA2Id</strong></dd>
   * 	<dt>value_string</dt><dd>is associated to bean's property <strong>valueString</strong></dd>
   * </dl>
   *
   * @return collection of bean or empty collection.
   */
  @Override
  public List<BeanA_1> selectAll() {
    // build where condition
    String[] _args={};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id, bean_a2_id, value_string FROM bean_a_1",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id, bean_a2_id, value_string FROM bean_a_1", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_1> resultList=new LinkedList<BeanA_1>();
      BeanA_1 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("bean_a2_id");
        int index2=cursor.getColumnIndex("value_string");

        do
         {
          resultBean=new BeanA_1();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }
          if (!cursor.isNull(index1)) { resultBean.beanA2Id=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.valueString=cursor.getString(index2); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, bean_a2_id, value_string FROM bean_a_1 WHERE id=${id}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>bean_a2_id</dt><dd>is associated to bean's property <strong>beanA2Id</strong></dd>
   * 	<dt>value_string</dt><dd>is associated to bean's property <strong>valueString</strong></dd>
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
  public List<BeanA_1> selectById(long id) {
    // build where condition
    String[] _args={String.valueOf(id)};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id, bean_a2_id, value_string FROM bean_a_1 WHERE id='%s'",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id, bean_a2_id, value_string FROM bean_a_1 WHERE id=?", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_1> resultList=new LinkedList<BeanA_1>();
      BeanA_1 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("bean_a2_id");
        int index2=cursor.getColumnIndex("value_string");

        do
         {
          resultBean=new BeanA_1();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }
          if (!cursor.isNull(index1)) { resultBean.beanA2Id=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.valueString=cursor.getString(index2); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id FROM bean_a_1 WHERE valueString=${dummy}</pre>
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
  public List<BeanA_1> selectByString(String value) {
    // build where condition
    String[] _args={(value==null?"":value)};

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    Logger.info(SqlUtils.formatSQL("SELECT id FROM bean_a_1 WHERE value_string='%s'",(Object[])_args));
    try (Cursor cursor = database().rawQuery("SELECT id FROM bean_a_1 WHERE value_string=?", _args)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<BeanA_1> resultList=new LinkedList<BeanA_1>();
      BeanA_1 resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");

        do
         {
          resultBean=new BeanA_1();

          if (!cursor.isNull(index0)) { resultBean.id=cursor.getLong(index0); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <p>SQL insert:</p>
   * <pre>INSERT INTO bean_a_1 (bean_a2_id, value_string) VALUES (${bean.beanA2Id}, ${bean.valueString})</pre>
   *
   * <p><code>bean.id</code> is automatically updated because it is the primary key</p>
   *
   * <p><strong>Inserted columns:</strong></p>
   * <dl>
   * 	<dt>bean_a2_id</dt><dd>is mapped to <strong>${bean.beanA2Id}</strong></dd>
   * 	<dt>value_string</dt><dd>is mapped to <strong>${bean.valueString}</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is mapped to parameter <strong>bean</strong>
   *
   * @return <strong>id</strong> of inserted record
   */
  @Override
  public int insert(BeanA_1 bean) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    contentValues.put("bean_a2_id", bean.beanA2Id);

    if (bean.valueString!=null) {
      contentValues.put("value_string", bean.valueString);
    } else {
      contentValues.putNull("value_string");
    }

    //StringUtils and SqlUtils will be used to format SQL
    // log
    Logger.info(SqlUtils.formatSQL("INSERT INTO bean_a_1 (bean_a2_id, value_string) VALUES ('"+StringUtils.checkSize(contentValues.get("bean_a2_id"))+"', '"+StringUtils.checkSize(contentValues.get("value_string"))+"')"));
    long result = database().insert("bean_a_1", null, contentValues);
    bean.id=result;

    return (int)result;
  }

  /**
   * <h2>SQL update:</h2>
   * <pre>UPDATE bean_a_1 SET bean_a2_id=${bean.beanA2Id}, value_string=${bean.valueString} WHERE valueString=${bean.valueString}</pre>
   *
   * <h2>Updated columns:</h2>
   * <dl>
   * 	<dt>bean_a2_id</dt><dd>is mapped to <strong>${bean.beanA2Id}</strong></dd>
   * 	<dt>value_string</dt><dd>is mapped to <strong>${bean.valueString}</strong></dd>
   * </dl>
   *
   * <h2>Parameters used in where conditions:</h2>
   * <dl>
   * 	<dt>${bean.valueString}</dt><dd>is mapped to method's parameter <strong>bean.valueString</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is used as ${bean}
   *
   * @return number of updated records
   */
  @Override
  public int update(BeanA_1 bean) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    contentValues.put("bean_a2_id", bean.beanA2Id);

    if (bean.valueString!=null) {
      contentValues.put("value_string", bean.valueString);
    } else {
      contentValues.putNull("value_string");
    }

    String[] whereConditionsArray={(bean.valueString==null?"":bean.valueString)};

    //StringUtils and SqlUtils will be used to format SQL
    Logger.info(SqlUtils.formatSQL("UPDATE bean_a_1 SET bean_a2_id='"+StringUtils.checkSize(contentValues.get("bean_a2_id"))+"', value_string='"+StringUtils.checkSize(contentValues.get("value_string"))+"' WHERE valueString='%s'", (Object[]) whereConditionsArray));
    int result = database().update("bean_a_1", contentValues, "UPDATE bean_a_1 SET bean_a2_id='"+StringUtils.checkSize(contentValues.get("bean_a2_id"))+"', value_string='"+StringUtils.checkSize(contentValues.get("value_string"))+"' WHERE valueString='%s'", whereConditionsArray);
    return result;
  }
}
