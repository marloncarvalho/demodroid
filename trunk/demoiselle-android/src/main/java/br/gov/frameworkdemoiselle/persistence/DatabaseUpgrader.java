package br.gov.frameworkdemoiselle.persistence;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Dex;
import br.gov.frameworkdemoiselle.util.Reflections;

/**
 * Helper class which handle database upgrades.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class DatabaseUpgrader {

	public static Batch createBatch() {
		return new Batch();
	}

	/**
	 * Execute all upgraders defined by the application.<br>
	 * Search classpath for classes which have @Upgrade annotation and execute method upgrade in each one.
	 * 
	 */
	public static void executeUpgraders(SQLiteDatabase database, int oldVersion, int newVersion) {
		Context context = Beans.getBean(Context.class);
		List<Class<?>> upgradersClasses = Dex.getUpgraders(context);
		for (Class<?> clasz : upgradersClasses) {
			try {
				UpgradeRequest request = (UpgradeRequest) Reflections.instantiate(clasz);
				request.upgrade(database);
			} finally {

			}
		}
	}

}