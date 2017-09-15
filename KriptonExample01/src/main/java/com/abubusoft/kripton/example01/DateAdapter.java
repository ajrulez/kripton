package com.abubusoft.kripton.example01;

import java.util.Date;

import com.abubusoft.kripton.BindTypeAdapter;

public class DateAdapter implements BindTypeAdapter<Date, Long> {

	@Override
	public Date toJava(Long dataValue) {
		return new Date(dataValue);
	}

	@Override
	public Long toData(Date javaValue) {
		return javaValue.getTime();
	}

}
