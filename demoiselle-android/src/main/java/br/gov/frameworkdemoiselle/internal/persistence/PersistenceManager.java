package br.gov.frameworkdemoiselle.internal.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PersistenceManager {
	private SQLiteDatabase database;

	@Inject
	private DatabaseHelper databaseHelper;

	@Inject
	public PersistenceManager(Provider<Context> context) {
		this.databaseHelper = new DatabaseHelper(context.get());
	}

	public SQLiteDatabase getDatabase() {
		open();
		return this.database;
	}

	public SQLiteDatabase open() {
		this.database = this.databaseHelper.getWritableDatabase();
		return this.database;
	}

	public void close() {
		if (this.database != null) {
			this.database.close();
			this.database = null;
		}
	}
}
