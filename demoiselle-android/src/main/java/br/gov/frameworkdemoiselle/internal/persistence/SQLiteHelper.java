package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;
import java.util.List;

import br.gov.frameworkdemoiselle.util.Reflections;

import android.content.ContentValues;

public class SQLiteHelper {

	public ContentValues getContentValues(List<Field> fields, Object object) {
		ContentValues cv = new ContentValues();
		for (Field field : fields) {
			cv.put(field.getName(), Reflections.getFieldValue(field, object)
					.toString());
		}
		return cv;
	}

}
