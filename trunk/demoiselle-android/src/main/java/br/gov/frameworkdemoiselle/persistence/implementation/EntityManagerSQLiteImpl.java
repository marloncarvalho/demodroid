package br.gov.frameworkdemoiselle.persistence.implementation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.ContentValues;
import android.database.Cursor;
import br.gov.frameworkdemoiselle.internal.exception.SystemException;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.internal.persistence.MappedEntity;
import br.gov.frameworkdemoiselle.internal.persistence.PersistenceInspector;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.SQLiteDatabaseHelper;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.BooleanColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.DateColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.DoubleColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.FloatColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.IntegerColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.ShortColumn;
import br.gov.frameworkdemoiselle.persistence.EntityManager;
import br.gov.frameworkdemoiselle.persistence.Query;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * EntityManager for SQLite databases.
 * 
 * @author marlonsilvacarvalho
 * @since 1.0.0
 */
@Singleton
public class EntityManagerSQLiteImpl implements EntityManager {

	@Inject
	private SQLiteDatabaseHelper databaseHelper;

	@Inject
	private PersistenceInspector persistenceInspector;

	private Map<Class<?>, MappedEntity> cached = new WeakHashMap<Class<?>, MappedEntity>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.persistence.EntityManager#close()
	 */
	public void close() throws SystemException {
		databaseHelper.getWritableDatabase().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#merge(java.lang.
	 * Object)
	 */
	public void merge(Object object) throws SystemException {
		MappedEntity mappedEntity = getMappedEntity(object);
		MappedColumn idMappedColumn = mappedEntity.getIdMappedColumn();
		Object idValue = idMappedColumn.getValue(object);
		String id = "";
		if (idValue != null) {
			id = idValue.toString();
		} else {
			id = "0";
		}
		databaseHelper.getWritableDatabase().update(mappedEntity.getTableName(),
				createContentValues(mappedEntity, object), idMappedColumn.getName() + "=? ", new String[] { id });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#find(java.lang.Class
	 * , java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> clazz, Object primaryKey) throws SystemException {
		MappedEntity mappedEntity = getMappedEntity(clazz);
		Cursor cursor = databaseHelper.getWritableDatabase().query(mappedEntity.getTableName(), null, "id=?",
				new String[] {}, null, null, null);

		Object object = mappedEntity.instantiate();

		if (cursor.moveToFirst()) {
			for (MappedColumn mappedColumn : mappedEntity.getMappedColumns().values()) {
				mappedColumn.setValue(object, cursor);
			}
		}

		cursor.close();
		return (T) object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.persistence.EntityManager#getDelegate()
	 */
	public Object getDelegate() throws SystemException {
		return databaseHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.persistence.EntityManager#isOpen()
	 */
	public boolean isOpen() throws SystemException {
		return databaseHelper.getWritableDatabase().isOpen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#persist(java.lang
	 * .Object)
	 */
	public void persist(Object object) throws SystemException {
		MappedEntity mappedEntity = getMappedEntity(object);
		databaseHelper.getWritableDatabase().insertOrThrow(mappedEntity.getTableName(), null,
				createContentValues(mappedEntity, object));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#remove(java.lang
	 * .Object)
	 */
	public void remove(Object object) throws SystemException {
		MappedEntity mappedEntity = getMappedEntity(object);
		MappedColumn idMappedColumn = mappedEntity.getIdMappedColumn();
		Object idValue = idMappedColumn.getValue(object);
		String id = "";
		if (idValue != null) {
			id = idValue.toString();
		} else {
			id = "0";
		}
		databaseHelper.getWritableDatabase().delete(mappedEntity.getTableName(), idMappedColumn.getName() + "=? ",
				new String[] { id });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#createQuery(java
	 * .lang.String)
	 */
	public Query createQuery(Class<?> clazz, String query) {
		return new QuerySQLiteImpl(query, getMappedEntity(clazz), databaseHelper.getReadableDatabase());
	}

	private MappedEntity getMappedEntity(Object object) {
		MappedEntity mappedEntity = null;

		if (cached.containsKey(object.getClass())) {
			mappedEntity = cached.get(object.getClass());
		} else {
			mappedEntity = createMappedEntity(object);
			cached.put(object.getClass(), mappedEntity);
		}

		return mappedEntity;
	}

	private MappedEntity createMappedEntity(Object object) {
		MappedEntity result = new MappedEntity();

		result.setMappedClass(object.getClass());

		List<Field> fields = persistenceInspector.getAllPersistentFields(object.getClass());
		for (Field field : fields) {
			result.addMappedColumn(getMappedColumnByType(field, object));
		}

		return result;
	}

	private MappedColumn getMappedColumnByType(Field field, Object object) {
		MappedColumn mappedColumn = null;
		Object value = Reflections.getFieldValue(field, object);

		if (value instanceof Boolean || value.getClass().equals(boolean.class)) {
			mappedColumn = new BooleanColumn();
		} else if (value instanceof Date) {
			mappedColumn = new DateColumn();
		} else if (value instanceof Integer || value.getClass().equals(int.class)) {
			mappedColumn = new IntegerColumn();
		} else if (value instanceof Float || value.getClass().equals(float.class)) {
			mappedColumn = new FloatColumn();
		} else if (value instanceof Short || value.getClass().equals(short.class)) {
			mappedColumn = new ShortColumn();
		} else if (value instanceof Double || value.getClass().equals(double.class)) {
			mappedColumn = new DoubleColumn();
		}

		mappedColumn.setField(field);
		return mappedColumn;
	}

	private ContentValues createContentValues(MappedEntity mappedEntity, Object object) {
		ContentValues result = new ContentValues();

		Collection<MappedColumn> columns = mappedEntity.getMappedColumns().values();
		for (MappedColumn column : columns) {
			result.put(column.getName(), column.getValue(object));
		}

		return result;
	}

}
