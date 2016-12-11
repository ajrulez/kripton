package bind.kripton76;

import java.util.List;

import com.abubusoft.kripton.annotation.BindType;
import com.abubusoft.kripton.annotation.BindXml;
import com.abubusoft.kripton.binder2.xml.XmlType;

@BindType(value="root", allFields=true)
public class BeanAttribute76List {
	
	@BindXml(xmlType=XmlType.VALUE_CDATA)
	public List<Byte> valueByteList;
	
}