package br.gov.frameworkdemoiselle.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import br.gov.frameworkdemoiselle.persistence.annotation.Entity;
import dalvik.system.DexFile;

public class Dex {

	public static ArrayList<Class<?>> getEntities(Context context) {
		ArrayList<Class<?>> entityClasses = new ArrayList<Class<?>>();
		try {
			String path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
			DexFile dexfile = new DexFile(path);
			Enumeration<?> entries = dexfile.entries();

			while (entries.hasMoreElements()) {
				String name = (String) entries.nextElement();
				Class<?> discoveredClass = null;
				Class<?> superClass = null;
				try {
					discoveredClass = Class.forName(name, true, context.getClass().getClassLoader());
					superClass = discoveredClass.getSuperclass();
				} catch (ClassNotFoundException e) {
					Log.e("Demoiselle", e.getMessage());
				}

				if ((discoveredClass == null) || (superClass == null)
						|| (!discoveredClass.isAnnotationPresent(Entity.class)))
					continue;
				entityClasses.add(discoveredClass);
			}

		} catch (IOException e) {
			Log.e("Demoiselle", e.getMessage());
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("Demoiselle", e.getMessage());
		}

		return entityClasses;
	}

}
