package br.gov.frameworkdemoiselle.internal.persistence.sqlite.column;

import android.database.Cursor;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.util.Reflections;

public class FloatColumn extends MappedColumn {

	@Override
	public String getValue(Object object) {
		Object result = getRawValue(object);
		if (result == null) {
			result = "";
		}
		return result.toString();
	}

	@Override
	public void setValue(Object object, Object source) {
		Cursor cursor = (Cursor) source;
		int index = cursor.getColumnIndex(getName());
		double value = cursor.getFloat(index);
		Reflections.setFieldValue(field, object, value);
	}

}