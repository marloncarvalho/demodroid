package br.gov.frameworkdemoiselle.persistence.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.internal.persistence.MappedEntity;
import br.gov.frameworkdemoiselle.persistence.EntityLoadListener;
import br.gov.frameworkdemoiselle.persistence.Query;

/**
 * Default ${link Query} interface implementation. <br>
 * Works with SQLite databases.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class QuerySQLiteImpl implements Query {
	private SQLiteDatabase database;
	private int maxResults;
	private int firstResult = 0;
	private String query;
	private Map<Integer, Object> args = new HashMap<Integer, Object>();
	private MappedEntity mappedEntity;
	private boolean closeable = false;

	/**
	 * Default constructor.
	 * 
	 * @param query Query to be executed.
	 * @param mappedEntity Entity informations.
	 * @param database SQLite database object.
	 * @param closeable If the database must be closed after all.
	 */
	public QuerySQLiteImpl(String query, MappedEntity mappedEntity, SQLiteDatabase database, boolean closeable) {
		this.database = database;
		this.query = query;
		this.mappedEntity = mappedEntity;
		this.closeable = closeable;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#setMaxResults(int)
	 */
	public Query setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#setParameter(int, java.lang.Object)
	 */
	public Query setParameter(int position, Object value) {
		args.put(position, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#setFirstResult(int)
	 */
	public Query setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#getResultList()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getResultList() {
		Cursor cursor = (Cursor) getRawResult();
		List resultList = new ArrayList();
		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Object object = mappedEntity.instantiate();
				for (MappedColumn mappedColumn : mappedEntity.getMappedColumns().values()) {
					mappedColumn.setValue(object, cursor);
				}
				resultList.add(object);
				cursor.moveToNext();
			}
		}
		cursor.close();
		if (closeable) {
			database.close();
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#getRawResult()
	 */
	public Object getRawResult() {
		String[] selectionArgs = new String[args.size()];
		for (Integer position : args.keySet()) {
			selectionArgs[position] = args.get(position).toString();
		}
		if (maxResults > 0) {
			query = query + " LIMIT " + firstResult + "," + maxResults;
		}
		Cursor cursor = database.rawQuery(query, selectionArgs);
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#executeUpdate()
	 */
	public int executeUpdate() {
		String[] selectionArgs = new String[args.size()];
		for (Integer position : args.keySet()) {
			selectionArgs[position] = args.get(position).toString();
		}
		database.execSQL(query, selectionArgs);
		if (closeable) {
			database.close();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.persistence.Query#getResultList(br.gov.frameworkdemoiselle.persistence.EntityLoadListener)
	 */
	@SuppressWarnings("all")
	public List<?> getResultList(EntityLoadListener listener) {
		Cursor cursor = (Cursor) getRawResult();
		List resultList = new ArrayList();
		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Object object = mappedEntity.instantiate();
				for (MappedColumn mappedColumn : mappedEntity.getMappedColumns().values()) {
					mappedColumn.setValue(object, cursor);
				}
				listener.entityLoaded(object);
				resultList.add(object);
				cursor.moveToNext();
			}
		}
		cursor.close();
		if (closeable) {
			database.close();
		}
		return resultList;
	}

}
