package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.internal.configuration.InternalConfig;
import br.gov.frameworkdemoiselle.persistence.Persistence;
import br.gov.frameworkdemoiselle.util.Reflections;

public class SQLitePersistence implements Persistence {

	private SQLiteOpener opener;

	private PersistenceInspector persistenceInspector;

	private SQLiteHelper sqliteHelper;

	public SQLitePersistence(InternalConfig config, SQLiteHelper sqliteHelper,
			PersistenceInspector persistenceInspector, SQLiteOpener opener) {
		this.persistenceInspector = persistenceInspector;
		this.sqliteHelper = sqliteHelper;
		this.opener = opener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.internal.persistence.Session#insert(java.lang
	 * .Object)
	 */
	public void insert(Object object) {
		SQLiteDatabase database = opener.getWritableDatabase();
		long id = database.insertOrThrow(persistenceInspector.getTableName(object.getClass()), null,
				sqliteHelper.getContentValues(persistenceInspector.getPersistentFields(object.getClass()), object));
		Reflections.setFieldValue(persistenceInspector.getIdField(object.getClass()), object, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.internal.persistence.Session#update(java.lang
	 * .Object)
	 */
	public void update(Object object) {
		SQLiteDatabase database = opener.getWritableDatabase();
		Field id = persistenceInspector.getIdField(object.getClass());
		Object value = Reflections.getFieldValue(id, object);
		String where = id.getName().toLowerCase() + "=?";
		database.update(persistenceInspector.getTableName(object.getClass()),
				sqliteHelper.getContentValues(persistenceInspector.getPersistentFields(object.getClass()), object),
				where, new String[] { value.toString() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.internal.persistence.Session#delete(java.lang
	 * .Object)
	 */
	public void delete(Long id, Class<?> cls) {
		SQLiteDatabase db = opener.getWritableDatabase();
		db.beginTransaction();
		db.delete(persistenceInspector.getTableName(cls), "id=?", new String[] { id.toString() });
		db.endTransaction();
		db.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.internal.persistence.Session#find(java.lang
	 * .Object)
	 */
	public Object find(Long id, Class<?> cls) {
		SQLiteDatabase db = opener.getReadableDatabase();
		String where = persistenceInspector.getIdField(cls).getName() + "=?";
		Cursor cursor = db.query(persistenceInspector.getTableName(cls), null, where, new String[] { id.toString() },
				null, null, null);

		Object o = null;
		if (cursor.moveToFirst()) {
			try {
				o = cls.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			List<Field> fields = persistenceInspector.getPersistentFields(cls);
			for (Field field : fields) {
				Reflections.setFieldValue(field, o, getValue(field, cursor));
			}
			Reflections.setFieldValue(persistenceInspector.getIdField(cls), o,
					getValue(persistenceInspector.getIdField(cls), cursor));
			cursor.close();
		}
		return o;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> findAll(Class<?> cls) {
		SQLiteDatabase db = opener.getReadableDatabase();
		Cursor cursor = db.query(persistenceInspector.getTableName(cls), null, null, null, null, null, null);
		cursor.moveToFirst();
		List objects = new ArrayList();
		while (cursor.isAfterLast() == false) {
			List<Field> fields = persistenceInspector.getPersistentFields(cls);
			Object o = null;
			try {
				o = cls.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			for (Field field : fields) {
				Reflections.setFieldValue(field, o, getValue(field, cursor));
			}
			Reflections.setFieldValue(persistenceInspector.getIdField(cls), o,
					getValue(persistenceInspector.getIdField(cls), cursor));
			objects.add(o);
			cursor.moveToNext();
		}
		cursor.close();
		return objects;
	}

	private Object getValue(Field field, Cursor cursor) {
		Object o = null;
		int idx = cursor.getColumnIndex(field.getName());
		if (field.getType().getSimpleName().toLowerCase().equals("string")) {
			o = cursor.getString(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("double")) {
			o = cursor.getDouble(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("float")) {
			o = cursor.getFloat(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("short")) {
			o = cursor.getShort(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("integer")) {
			o = cursor.getInt(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("long")) {
			o = cursor.getLong(idx);
		}
		return o;
	}
}