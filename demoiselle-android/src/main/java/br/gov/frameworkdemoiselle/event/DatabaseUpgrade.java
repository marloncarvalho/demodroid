package br.gov.frameworkdemoiselle.event;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseUpgrade {

	/** SQLite database object that will be created. **/
	private SQLiteDatabase database;

	public DatabaseUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		this.database = database;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

}
