package br.gov.frameworkdemoiselle.internal.persistence.column;

import android.database.Cursor;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.util.Reflections;

public class BooleanColumn extends MappedColumn {

	@Override
	public String getValue(Object value) {
		String resValue = null;

		if (value != null && value instanceof Boolean) {
			resValue = ((Boolean) value).booleanValue() ? "1" : "0";
		}

		return resValue;
	}

	@Override
	public void setValue(Object object, Cursor cursor) {
		int index = cursor.getColumnIndex(getName());
		int value = cursor.getInt(index);
		boolean result = false;
		if ( value == 1 ) {
			result = true;
		}
		Reflections.setFieldValue(field, object, result);
	}

}
