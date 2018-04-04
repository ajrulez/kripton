package com.abubusoft.kripton.common;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static BigDecimal read(String value) {
		if (!StringUtils.hasText(value)) return null;
		
		return new BigDecimal(value);
	}


	public static String write(BigDecimal value) {
		if (value==null) return null;
		
		return value.toPlainString();			
	}
}
