package kripton70;

import kripton70.typeconverters.ByteConverter;
import kripton70.typeconverters.CharacterConverter;
import kripton70.typeconverters.DoubleConverter;
import kripton70.typeconverters.FloatConverter;
import kripton70.typeconverters.IntegerConverter;
import kripton70.typeconverters.ShortConverter;

import com.abubusoft.kripton.annotation.BindType;

@BindType
public class Bean {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((valueBean == null) ? 0 : valueBean.hashCode());
		result = prime * result + valueByteType;
		result = prime * result + valueCharType;
		result = prime * result + valueShortType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bean other = (Bean) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (valueBean == null) {
			if (other.valueBean != null)
				return false;
		} else if (!valueBean.equals(other.valueBean))
			return false;
		if (valueByteType != other.valueByteType)
			return false;
		if (valueCharType != other.valueCharType)
			return false;
		if (valueShortType != other.valueShortType)
			return false;
		return true;
	}

	public long id;
	
	public String description;
	
	public byte valueByteType;
	
	public short valueShortType;
	
	public char valueCharType;
	
	public Bean valueBean;
		
}