package br.gov.frameworkdemoiselle.internal.persistence;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.gov.frameworkdemoiselle.util.Dex;

import com.google.inject.Inject;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE = "DATABASE";
	private static final String VERSION = "VERSION";
	private Context context;

	@Inject
	private SQLBuilder sqlBuilder;

	@Inject
	private PersistenceInspector inspector;

	@Inject
	public DatabaseHelper(Context context) {
		super(context, getDatabaseName(context), null, getDatabaseVersion(context));
		this.context = context;
	}

	public void onCreate(SQLiteDatabase db) {
		ArrayList<Class<?>> tables = Dex.getEntities(this.context);
		for (Class<?> table : tables) {
			db.execSQL(sqlBuilder.buildCreateTable(table));
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ArrayList<Class<?>> tables = Dex.getEntities(this.context);
		for (Class<?> table : tables) {
			db.execSQL("DROP TABLE IF EXISTS " + inspector.getTableName(table));
		}
		onCreate(db);
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