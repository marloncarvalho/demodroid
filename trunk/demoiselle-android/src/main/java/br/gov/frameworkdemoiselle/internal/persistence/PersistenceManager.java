package br.gov.frameworkdemoiselle.internal.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.SQLiteDatabaseHelper;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PersistenceManager {
	private SQLiteDatabase database;

	@Inject
	private SQLiteDatabaseHelper databaseHelper;

	@Inject
	public PersistenceManager(Provider<Context> context) {
		this.databaseHelper = new SQLiteDatabaseHelper(context.get());
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
