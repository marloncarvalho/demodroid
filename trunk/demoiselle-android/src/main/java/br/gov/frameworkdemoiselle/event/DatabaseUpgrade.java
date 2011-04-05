package br.gov.frameworkdemoiselle.event;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseUpgrade {

	/** SQLite database object that will be created. **/
	private SQLiteDatabase database;

	private int oldVersion;

	private int newVersion;

	public DatabaseUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		this.database = database;
		this.oldVersion = oldVersion;
		this.newVersion = newVersion;
	}

	public int getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(int oldVersion) {
		this.oldVersion = oldVersion;
	}

	public int getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

}
