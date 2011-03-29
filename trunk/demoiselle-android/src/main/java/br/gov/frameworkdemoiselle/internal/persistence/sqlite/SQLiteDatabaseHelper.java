package br.gov.frameworkdemoiselle.internal.persistence.sqlite;

import java.util.ArrayList;

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
import br.gov.frameworkdemoiselle.util.Dex;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE = "DATABASE";
	private static final String VERSION = "VERSION";
	private Context context;

	@Inject
	private SQLBuilder sqlBuilder;

	@Inject
	private EventManager eventManager;

	@Inject
	public SQLiteDatabaseHelper(Context context) {
		super(context, getDatabaseName(context), null, getDatabaseVersion(context));
		this.context = context;
	}

	public void onCreate(SQLiteDatabase db) {
		eventManager.fire(new DatabaseCreation(db));
		ArrayList<Class<?>> tables = Dex.getEntities(this.context);
		for (Class<?> table : tables) {
			db.execSQL(sqlBuilder.buildCreateTable(table));
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		eventManager.fire(new DatabaseUpgrade(db, oldVersion, newVersion));
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