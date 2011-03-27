package br.gov.frameworkdemoiselle.event;

import android.database.sqlite.SQLiteDatabase;

/**
 * Database creation event.
 * Fired by SQLite before database creation. 
 * 
 * @author marlonsilvacarvalho
 * @since 1.0.0
 */
public class DatabaseCreation {

	/** SQLite database object that will be created. **/
	private SQLiteDatabase database;

	public DatabaseCreation(SQLiteDatabase database) {
		this.database = database;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

}
