package br.gov.frameworkdemoiselle.persistence;

import android.database.sqlite.SQLiteDatabase;

public interface UpgradeRequest {

	public void upgrade(SQLiteDatabase database);

}
