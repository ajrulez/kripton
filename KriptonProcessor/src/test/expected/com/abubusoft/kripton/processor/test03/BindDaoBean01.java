package com.abubusoft.kripton.processor.test03;

import android.database.Cursor;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.DaoBase;
import com.abubusoft.kripton.common.StringUtil;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * DAO implementation for entity <code>Bean01</code>, based on interface <code>DaoBean01</code>
 * </p>
 *  @see com.abubusoft.kripton.processor.test03.Bean01
 *  @see com.abubusoft.kripton.processor.test03.DaoBean01
 *  @see com.abubusoft.kripton.processor.test03.Bean01Table
 */
public class BindDaoBean01 extends DaoBase implements DaoBean01 {
  public BindDaoBean01(BindDummy01DataSource dataSet) {
    super(dataSet);
  }

  /**
   * <p>Select query is:</p>
   * <pre>SELECT id, message_date, message_text, value FROM bean01 WHERE 1=1</pre>
   *
   * <p>Its parameters are:</p>
   *
   * <pre>[]</pre>
   *
   * <p>Projected column are:</p>
   *
   * <pre>[id, message_date, message_text, value]</pre>
   *
   *
   * @return list of bean or empty list.
   */
  @Override
  public List<Bean01> listAll() {
    // build where condition
    String[] args={};

    Logger.info(StringUtil.formatSQL("SELECT id, message_date, message_text, value FROM bean01 WHERE 1=1"),(Object[])args);
    Cursor cursor = database().rawQuery("SELECT id, message_date, message_text, value FROM bean01 WHERE 1=1", args);
    Logger.info("Rows found: %s",cursor.getCount());

    LinkedList<Bean01> resultList=new LinkedList<Bean01>();
    Bean01 resultBean=null;

    if (cursor.moveToFirst()) {

      int index0=cursor.getColumnIndex("id");
      int index1=cursor.getColumnIndex("message_date");
      int index2=cursor.getColumnIndex("message_text");
      int index3=cursor.getColumnIndex("value");

      do
       {
        resultBean=new Bean01();

        if (!cursor.isNull(index0)) { resultBean.setId(cursor.getLong(index0)); }
        if (!cursor.isNull(index1)) { resultBean.setMessageDate(cursor.getLong(index1)); }
        resultBean.setMessageText(cursor.getString(index2));
        if (!cursor.isNull(index3)) { resultBean.setValue(cursor.getLong(index3)); }

        resultList.add(resultBean);
      } while (cursor.moveToNext());
    }
    cursor.close();

    return resultList;
  }
}