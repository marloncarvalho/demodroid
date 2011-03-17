package br.gov.frameworkdemoiselle.persistence;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.gov.frameworkdemoiselle.annotation.Transient;
import br.gov.frameworkdemoiselle.internal.persistence.PersistenceInspector;
import br.gov.frameworkdemoiselle.internal.persistence.PersistenceManager;
import br.gov.frameworkdemoiselle.persistence.annotation.Id;
import br.gov.frameworkdemoiselle.persistence.annotation.Table;
import br.gov.frameworkdemoiselle.template.Crud;
import br.gov.frameworkdemoiselle.util.DateUtils;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Inject;

public class Persistence<E> implements Crud<E> {
	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private String table;
	private Class<E> clasz;

	@Inject
	private PersistenceManager persistenceManager;

	@Inject
	private PersistenceInspector inspector;

	public Persistence() {
		clasz = Reflections.getGenericTypeArgument(this.getClass(), 0);
		getAnnotations();
	}

	public SQLiteDatabase getDatabase() {
		return getPersistenceManager().getDatabase();
	}

	private void getAnnotations() {
		if (clasz.isAnnotationPresent(Table.class)) {
			table = clasz.getAnnotation(Table.class).name();
		}
	}

	private ContentValues getContentValues(E object) {
		List<Field> fields = inspector.getAllPersistentFields(clasz);
		ContentValues initialValues = new ContentValues();
		for (Field field : fields) {
			try {
				if (!field.isAnnotationPresent(Id.class) && !field.isAnnotationPresent(Transient.class)) {
					Object value = field.get(object);
					String resValue = null;
					if (value != null && value instanceof Date) {
						resValue = DateUtils.format((Date) value, DATE_FORMAT);
					} else if (value != null && value instanceof Boolean) {
						resValue = ((Boolean) value).booleanValue() ? "1" : "0";
					} else if (value != null) {
						resValue = value.toString();
					}
					initialValues.put(field.getName(), resValue);
				}
			} catch (IllegalArgumentException e) {
				Log.e("Persistence", e.getMessage());
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				Log.e("Persistence", e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return initialValues;
	}

	public void insert(E object) {
		Log.d("Persistence", "Inserting object");
		getPersistenceManager().open();
		long id = getPersistenceManager().getDatabase().insertOrThrow(table, null, getContentValues(object));
		getPersistenceManager().close();
		Reflections.setFieldValue("id", object, id);
	}

	public void delete(long id) {
		Log.d("Persistence", "Deleting object");
		getPersistenceManager().open();
		getPersistenceManager().getDatabase().delete(table, "id=" + id, null);
		getPersistenceManager().close();
	}

	public void update(E object) {
		Log.d("Persistence", "Updating object");
		ContentValues values = getContentValues(object);
		long id = (Long) Reflections.getFieldValue("id", object);
		getPersistenceManager().open();
		getPersistenceManager().getDatabase().update(table, values, "id=?", new String[] { String.valueOf(id) });
		getPersistenceManager().close();
	}

	public E find(long id) {
		E object = Reflections.instantiate(clasz);
		Cursor cursor = getPersistenceManager().getDatabase().query(table, null, "id=?",
				new String[] { String.valueOf(id) }, null, null, null);
		List<Field> fields = inspector.getAllPersistentFields(clasz);
		if (cursor.moveToFirst()) {
			for (Field field : fields) {
				Reflections.setFieldValue(field, object, getValue(field, cursor));
			}
		}
		cursor.close();
		return object;
	}

	public List<E> findAll() {
		getPersistenceManager().open();
		List<E> list = new CursorList(getPersistenceManager().getDatabase().query(table, null, null, null, null, null,
				null));
		return list;
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
		if (field.getType().getSimpleName().toLowerCase().equals("date")) {
			o = DateUtils.format(cursor.getString(idx), DATE_FORMAT);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("int")) {
			o = cursor.getInt(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("long")) {
			o = cursor.getLong(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("boolean")) {
			o = cursor.getInt(idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
		}
		return o;
	}

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	/**
	 * Special List.
	 * 
	 * @author Marlon
	 * 
	 */
	public class CursorList extends AbstractList<E> {
		private List<E> objects = new ArrayList<E>();

		public CursorList(Cursor cursor) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					E object = null;
					List<Field> fields = inspector.getAllPersistentFields(clasz);
					try {
						object = clasz.newInstance();
					} catch (InstantiationException e1) {
						Log.e("Persistence", e1.getMessage());
						throw new RuntimeException(e1);
					} catch (IllegalAccessException e1) {
						Log.e("Persistence", e1.getMessage());
						throw new RuntimeException(e1);
					}
					for (Field field : fields) {
						try {
							field.set(object, getValue(field, cursor));
						} catch (IllegalArgumentException e) {
							Log.e("Persistence", e.getMessage());
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							Log.e("Persistence", e.getMessage());
							throw new RuntimeException(e);
						}
					}
					cursor.moveToNext();
					objects.add(object);
				}
			}
			cursor.close();
			persistenceManager.close();
		}

		public E get(int index) {
			return objects.get(index);
		}

		@Override
		public int size() {
			return objects.size();
		}

	}

}
