package br.gov.frameworkdemoiselle.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import br.gov.frameworkdemoiselle.persistence.annotation.Entity;
import br.gov.frameworkdemoiselle.persistence.annotation.Upgrade;
import dalvik.system.DexFile;

/**
 * Utility class responsible to get special informations about Dex file.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class Dex {

	public static List<Class<?>> getEntities(Context context) {
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

	public static List<Class<?>> getUpgraders(Context context) {
		ArrayList<Class<?>> upgraderClasses = new ArrayList<Class<?>>();
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
						|| (!discoveredClass.isAnnotationPresent(Upgrade.class))) {
					continue;
				}
				upgraderClasses.add(discoveredClass);
			}

		} catch (IOException e) {
			Log.e("Demoiselle", e.getMessage());
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("Demoiselle", e.getMessage());
		}

		return upgraderClasses;
	}

}
