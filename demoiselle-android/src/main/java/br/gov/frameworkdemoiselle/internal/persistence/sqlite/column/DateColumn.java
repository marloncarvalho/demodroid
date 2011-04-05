package br.gov.frameworkdemoiselle.internal.persistence.sqlite.column;

import java.util.Date;

import android.database.Cursor;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.util.DateUtils;
import br.gov.frameworkdemoiselle.util.Reflections;

public class DateColumn extends MappedColumn {

	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	@Override
	public String getValue(Object object) {
		String resValue = null;
		Object value = getRawValue(object);

		if (value != null && value instanceof Date) {
			resValue = DateUtils.format((Date) value, DATE_FORMAT);
		}

		return resValue;
	}

	@Override
	public void setValue(Object object, Object source) {
		Cursor cursor = (Cursor) source;
		int index = cursor.getColumnIndex(getName());
		String value = cursor.getString(index);
		Date result = DateUtils.format(value, DATE_FORMAT);
		Reflections.setFieldValue(field, object, result);
	}

}
