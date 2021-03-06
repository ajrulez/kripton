package sqlite.feature.in;

import com.abubusoft.kripton.android.sqlite.BindDaoFactory;

/**
 * <p>
 * Represents dao factory interface for AppDataSource.
 * This class expose database interface through Dao attribute.
 * </p>
 *
 * @see AppDataSource
 * @see DaoCity
 * @see DaoCityImpl
 * @see City
 * @see DaoPerson
 * @see DaoPersonImpl
 * @see Person
 */
public interface BindAppDaoFactory extends BindDaoFactory {
  /**
   *
   * retrieve dao DaoCity
   */
  DaoCityImpl getDaoCity();

  /**
   *
   * retrieve dao DaoPerson
   */
  DaoPersonImpl getDaoPerson();
}
