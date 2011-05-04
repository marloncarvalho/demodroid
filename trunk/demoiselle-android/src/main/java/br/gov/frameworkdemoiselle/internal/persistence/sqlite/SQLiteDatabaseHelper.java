package br.gov.frameworkdemoiselle.internal.persistence.sqlite;

import java.util.List;

import roboguice.event.EventManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.gov.frameworkdemoiselle.event.DatabaseCreation;
import br.gov.frameworkdemoiselle.event.DatabaseUpgrade;
import br.gov.frameworkdemoiselle.internal.persistence.SQLBuilder;
import br.gov.frameworkdemoiselle.persistence.DatabaseUpgrader;
import br.gov.frameworkdemoiselle.util.Dex;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE = "DATABASE_NAME";
	private static final String VERSION = "DATABASE_VERSION";

	@Inject
	private SQLBuilder sqlBuilder;

	@Inject
	private EventManager eventManager;

	private Provider<Context> contextProvider;

	private SQLiteDatabase cachedDatabase;

	private int count = 0;

	@Inject
	public SQLiteDatabaseHelper(final Provider<Context> contextProvider) {
		super(contextProvider.get(), getDatabaseName(contextProvider.get()), null, getDatabaseVersion(contextProvider.get()));
		this.contextProvider = contextProvider;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (cachedDatabase == null) {
			cachedDatabase = super.getWritableDatabase();
		} else if (!cachedDatabase.isOpen()) {
			cachedDatabase = super.getWritableDatabase();
		}
		count++;
		return cachedDatabase;
	}

	public void close() {
		count--;
		if (count == 0) {
			super.close();
		}
	}

	public void onCreate(SQLiteDatabase db) {
		eventManager.fire(contextProvider.get(), new DatabaseCreation(db));
		List<Class<?>> tables = Dex.getEntities(contextProvider.get());
		for (Class<?> table : tables) {
			db.execSQL(sqlBuilder.buildCreateTable(table));
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		eventManager.fire(contextProvider.get(), new DatabaseUpgrade(db, oldVersion, newVersion));
		DatabaseUpgrader.executeUpgraders(db, oldVersion, newVersion);
	}

	private static String getDatabaseName(Context context) {
		String name = getMetaDataString(context, DATABASE);
		if (name == null) {
			name = "Demoiselle.db";
		}
		return name;
	}

	private static int getDatabaseVersion(Context context) {
		Integer version = getMetaDataInteger(context, VERSION);
		if ((version == null) || (version.intValue() == 0)) {
			version = Integer.valueOf(1);
		}
		return version.intValue();
	}

	private static String getMetaDataString(Context context, String name) {
		String value = null;
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 128);
			value = ai.metaData.getString(name);
		} catch (Exception e) {
			Log.w("Demoiselle", "Couldn't find meta: " + name);
		}
		return value;
	}

	private static Integer getMetaDataInteger(Context context, String name) {
		Integer value = null;

		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 128);
			value = Integer.valueOf(ai.metaData.getInt(name));
		} catch (Exception e) {
			Log.w("Demoiselle", "Couldn't find meta: " + name);
		}

		return value;
	}

}