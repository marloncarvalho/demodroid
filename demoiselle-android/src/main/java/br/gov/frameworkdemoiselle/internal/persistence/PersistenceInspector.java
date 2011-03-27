package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.frameworkdemoiselle.annotation.Transient;
import br.gov.frameworkdemoiselle.internal.configuration.InternalConfig;
import br.gov.frameworkdemoiselle.persistence.annotation.Id;
import br.gov.frameworkdemoiselle.persistence.annotation.Table;

/**
 * Helper to get entity informations.
 * 
 * @author Marlon
 * @Since 2.0.0
 */
final public class PersistenceInspector {
	private List<Class<?>> list = new ArrayList<Class<?>>();

	/**
	 * Get all persistent field from a specific class.
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	public <T> List<Field> getPersistentFields(Class<T> cls) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : cls.getDeclaredFields()) {
			if (!field.isAnnotationPresent(Transient.class) && !field.isAnnotationPresent(Id.class)) {
				if (field.getType().isPrimitive() || field.getType().equals(String.class)
						|| field.getType().equals(Date.class)) {
					if (!Modifier.isStatic(field.getModifiers())) {
						list.add(field);
					}
				}
			}
		}
		return list;
	}

	public <T> List<Field> getAllPersistentFields(Class<T> cls) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : cls.getDeclaredFields()) {
			if (!field.isAnnotationPresent(Transient.class)) {
				if (field.getType().isPrimitive() || field.getType().equals(String.class)
						|| field.getType().equals(Date.class)) {
					if (!Modifier.isStatic(field.getModifiers())) {
						list.add(field);
					}
				}
			}
		}
		return list;
	}

	/**
	 * Get the field which specifies the entity id.
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	public <T> Field getIdField(Class<T> cls) {
		try {
			List<Field> fields = getAllPersistentFields(cls);
			for (Field field : fields) {
				if (field.isAnnotationPresent(Id.class)) {
					return field;
				}
			}
			return cls.getDeclaredField("id");
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the table name for a specific entity.
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	public <T> String getTableName(Class<T> cls) {
		String tableName = "";
		if (cls.isAnnotationPresent(Table.class)) {
			tableName = cls.getAnnotation(Table.class).name();
		} else {
			tableName = cls.getSimpleName().toLowerCase();
		}
		return tableName;
	}

	/**
	 * Get all entities informed by the user.
	 * 
	 * @param classLoader
	 * @param config
	 * @return
	 */
	public List<Class<?>> getEntities(ClassLoader classLoader, InternalConfig config) {
		if (this.list.isEmpty()) {
			String entities = config.getDomainClasses();
			for (String str : entities.split(",")) {
				try {
					list.add(classLoader.loadClass(config.getDomainPackage() + "." + str));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return list;
	}
}