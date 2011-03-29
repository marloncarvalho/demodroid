package br.gov.frameworkdemoiselle.persistence.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.internal.persistence.MappedColumn;
import br.gov.frameworkdemoiselle.internal.persistence.MappedEntity;
import br.gov.frameworkdemoiselle.persistence.Query;

public class QuerySQLiteImpl implements Query {
	private SQLiteDatabase database;
	private int maxResults;
	private int firstResult = 0;
	private String query;
	private Map<Integer, Object> args = new HashMap<Integer, Object>();
	private MappedEntity mappedEntity;

	public QuerySQLiteImpl(String query, MappedEntity mappedEntity, SQLiteDatabase database) {
		this.database = database;
		this.query = query;
		this.mappedEntity = mappedEntity;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public void setParameter(int position, Object value) {
		args.put(position, value);
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

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
		return resultList;
	}

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

	@Override
	public int executeUpdate() {
		String[] selectionArgs = new String[args.size()];
		for (Integer position : args.keySet()) {
			selectionArgs[position] = args.get(position).toString();
		}
		database.execSQL(query, selectionArgs);
		return 0;
	}

}
