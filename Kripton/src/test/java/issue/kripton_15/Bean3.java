/**
 * 
 */
package issue.kripton_15;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.abubusoft.kripton.annotation.BindElement;
import com.abubusoft.kripton.annotation.BindRoot;



/**
 * @author xcesco
 *
 */
@BindRoot
public class Bean3 implements Serializable {

	public Bean3()
	{
		map=new LinkedHashMap<>();
	}


	private static final long serialVersionUID = 3113613163524431347L;

	@BindElement
	Map<Bean0, Bean0> map;

}
