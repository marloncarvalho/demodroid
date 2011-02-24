package br.gov.frameworkdemoiselle.persistence;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.gov.frameworkdemoiselle.internal.persistence.SQLBuilder;
import br.gov.frameworkdemoiselle.persistence.annotation.Database;
import br.gov.frameworkdemoiselle.persistence.annotation.Id;
import br.gov.frameworkdemoiselle.persistence.annotation.Table;
import br.gov.frameworkdemoiselle.template.Crud;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class Persistence<E> implements Crud<E> {
	private static final long serialVersionUID = 1L;
	private static String table;
	private static String database;
	private static int version;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static String createDDL;
	private Class<E> clasz;

	@Inject
	private SQLBuilder builder;

	@Inject
	private Provider<Context> provider;

	private class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, database, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (createDDL == null || "".equals(createDDL)) {
				createDDL = builder.buildCreateTable(clasz);
			}
			db.execSQL(createDDL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Persistence", "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + table);
			onCreate(db);
		}
	}

	public Persistence() {
		clasz = Reflections.getGenericTypeArgument(this.getClass(), 0);
		getAnnotations();
	}

	private void getAnnotations() {
		if (clasz.isAnnotationPresent(Table.class)) {
			table = clasz.getAnnotation(Table.class).name();
		}
		if (clasz.isAnnotationPresent(Database.class)) {
			database = clasz.getAnnotation(Database.class).name();
			version = clasz.getAnnotation(Database.class).version();
		}
	}

	public Persistence<E> open() {
		if (mDbHelper == null || !mDb.isOpen()) {
			Log.d("Persistence", "Opening Database: " + database);
			mDbHelper = new DatabaseHelper(provider.get());
			mDb = mDbHelper.getWritableDatabase();
		}
		return this;
	}

	private ContentValues getContentValues(E object) {
		Field[] fields = object.getClass().getDeclaredFields();
		ContentValues initialValues = new ContentValues();
		for (Field field : fields) {
			try {
				if (!field.isAnnotationPresent(Id.class)) {
					initialValues.put(field.getName(), field.get(object).toString());
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

	public boolean close() {
		Log.d("Persistence", "Closing database");
		mDb.close();
		return true;
	}

	public void insert(E object) {
		open();
		Log.d("Persistence", "Inserting object");
		long id = mDb.insertOrThrow(table, null, getContentValues(object));
		Reflections.setFieldValue("id", object, id);
		close();
	}

	public void delete(long id) {
		open();
		Log.d("Persistence", "Deleting object");
		mDb.delete(table, "id=" + id, null);
		close();
	}

	public void update(E object) {
		open();
		Log.d("Persistence", "Updating object");
		ContentValues values = getContentValues(object);
		long id = (Long)Reflections.getFieldValue("id", object);
		mDb.update(table, values, "id=?", new String[] { String.valueOf(id) });
		close();
	}

	public E find(long id) {
		open();
		E object = Reflections.instantiate(clasz);
		Cursor cursor = mDb.query(table, null, "id=?", new String[] { String.valueOf(id) }, null, null, null);
		Field[] fields = object.getClass().getDeclaredFields();
		if (cursor.moveToFirst()) {
			for (Field field : fields) {
				Reflections.setFieldValue(field, object, getValue(field, cursor));
			}
		}
		cursor.close();
		close();
		return object;
	}

	public List<E> findAll() {
		open();
		List<E> list = new CursorList(mDb.query(table, null, null, null, null, null, null));
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
		if (field.getType().getSimpleName().toLowerCase().equals("int")) {
			o = cursor.getInt(idx);
		}
		if (field.getType().getSimpleName().toLowerCase().equals("long")) {
			o = cursor.getLong(idx);
		}
		return o;
	}

	public class CursorList extends AbstractList<E> {
		private Cursor cursor;

		public CursorList(Cursor cursor) {
			this.cursor = cursor;
		}

		@Override
		public E get(int index) {
			E object = null;
			if (cursor.moveToPosition(index)) {
				Field[] fields = clasz.getDeclaredFields();
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
			}
			return object;
		}

		@Override
		public int size() {
			return cursor.getCount();
		}

	}

}
