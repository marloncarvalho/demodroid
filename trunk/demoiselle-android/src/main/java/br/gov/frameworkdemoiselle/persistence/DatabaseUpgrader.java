package br.gov.frameworkdemoiselle.persistence;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.gov.frameworkdemoiselle.persistence.annotation.Upgrade;
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

		Map<Integer, Class<?>> map = new TreeMap<Integer, Class<?>>();
		for (Class<?> clasz : upgradersClasses) {
			int version = clasz.getAnnotation(Upgrade.class).version();
			map.put(version, clasz);
		}
		for (Integer key : map.keySet()) {
			try {
				UpgradeRequest request = (UpgradeRequest) Reflections.instantiate(map.get(key));
				request.upgrade(database);
			} finally {

			}
		}
	}

}