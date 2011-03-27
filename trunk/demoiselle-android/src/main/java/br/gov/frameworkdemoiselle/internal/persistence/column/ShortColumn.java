package br.gov.frameworkdemoiselle.internal.persistence.column;

import android.database.Cursor;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.util.Reflections;

public class ShortColumn extends MappedColumn {

	@Override
	public String getValue(Object object) {
		Object result = getRawValue(object);
		if (result == null) {
			result = "";
		}
		return result.toString();
	}

	@Override
	public void setValue(Object object, Cursor cursor) {
		int index = cursor.getColumnIndex(getName());
		double value = cursor.getShort(index);
		Reflections.setFieldValue(field, object, value);
	}
	
}
