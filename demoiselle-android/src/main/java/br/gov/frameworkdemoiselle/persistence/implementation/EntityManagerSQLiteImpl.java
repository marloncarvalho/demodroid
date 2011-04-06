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
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.LongColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.ShortColumn;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.column.StringColumn;
import br.gov.frameworkdemoiselle.persistence.EntityManager;
import br.gov.frameworkdemoiselle.persistence.Query;
import br.gov.frameworkdemoiselle.transaction.Transaction;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * EntityManager for SQLite databases.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
@Singleton
public class EntityManagerSQLiteImpl implements EntityManager {

	@Inject
	private SQLiteDatabaseHelper databaseHelper;

	@Inject
	private PersistenceInspector persistenceInspector;

	@Inject
	private Transaction transaction;

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
		MappedEntity mappedEntity = getMappedEntity(object.getClass());
		MappedColumn idMappedColumn = mappedEntity.getIdMappedColumn();
		Object idValue = idMappedColumn.getValue(object);
		String id = "";
		if (idValue != null) {
			id = idValue.toString();
		} else {
			id = "0";
		}
		databaseHelper.getWritableDatabase()
				.update(mappedEntity.getTableName(), createContentValues(mappedEntity, object, false),
						idMappedColumn.getName() + "=? ", new String[] { id });
		if (!transaction.isActive()) {
			close();
		}
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
				new String[] {primaryKey.toString()}, null, null, null);

		Object object = mappedEntity.instantiate();

		if (cursor.moveToFirst()) {
			for (MappedColumn mappedColumn : mappedEntity.getMappedColumns().values()) {
				mappedColumn.setValue(object, cursor);
			}
		}

		cursor.close();
		if (!transaction.isActive()) {
			close();
		}

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
		MappedEntity mappedEntity = getMappedEntity(object.getClass());
		long id = databaseHelper.getWritableDatabase().insertOrThrow(mappedEntity.getTableName(), null,
				createContentValues(mappedEntity, object, false));
		mappedEntity.getIdMappedColumn().setValue(object, id);
		if (!transaction.isActive()) {
			close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#remove(java.lang
	 * .Object)
	 */
	public void remove(Object object) throws SystemException {
		MappedEntity mappedEntity = getMappedEntity(object.getClass());
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
		if (!transaction.isActive()) {
			close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.persistence.EntityManager#createQuery(java
	 * .lang.String)
	 */
	public Query createQuery(Class<?> clazz, String query) {
		return new QuerySQLiteImpl(query, getMappedEntity(clazz), databaseHelper.getWritableDatabase(),
				!transaction.isActive());
	}

	private MappedEntity getMappedEntity(Class<?> clasz) {
		MappedEntity mappedEntity = null;

		if (cached.containsKey(clasz)) {
			mappedEntity = cached.get(clasz);
		} else {
			mappedEntity = createMappedEntity(clasz);
			cached.put(clasz, mappedEntity);
		}

		return mappedEntity;
	}

	private MappedEntity createMappedEntity(Class<?> clasz) {
		MappedEntity result = new MappedEntity();

		result.setMappedClass(clasz);

		List<Field> fields = persistenceInspector.getAllPersistentFields(clasz);
		for (Field field : fields) {
			result.addMappedColumn(getMappedColumnByType(field));
		}

		return result;
	}

	private MappedColumn getMappedColumnByType(Field field) {
		MappedColumn mappedColumn = null;

		if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
			mappedColumn = new BooleanColumn();
		} else if (field.getType().equals(Date.class)) {
			mappedColumn = new DateColumn();
		} else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
			mappedColumn = new IntegerColumn();
		} else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
			mappedColumn = new LongColumn();
		} else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
			mappedColumn = new FloatColumn();
		} else if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
			mappedColumn = new ShortColumn();
		} else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
			mappedColumn = new DoubleColumn();
		} else if (field.getType().equals(String.class)) {
			mappedColumn = new StringColumn();
		}

		mappedColumn.setField(field);
		return mappedColumn;
	}

	private ContentValues createContentValues(MappedEntity mappedEntity, Object object, boolean includeId) {
		ContentValues result = new ContentValues();

		Collection<MappedColumn> columns = mappedEntity.getMappedColumns().values();
		for (MappedColumn column : columns) {
			if (column.equals(mappedEntity.getIdMappedColumn())) {
				if (includeId) {
					result.put(column.getName(), column.getValue(object));
				}
			} else {
				result.put(column.getName(), column.getValue(object));
			}
		}

		return result;
	}

}
