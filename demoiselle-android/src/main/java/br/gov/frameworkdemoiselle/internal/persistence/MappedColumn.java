package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;

import br.gov.frameworkdemoiselle.persistence.annotation.Column;

abstract public class MappedColumn {
	protected Field field;
	private String name;

	public String getName() {
		if (name == null) {
			if (field.isAnnotationPresent(Column.class)) {
				name = field.getAnnotation(Column.class).name();
			} else {
				name = field.getName().toLowerCase();
			}
		}
		return name;
	}

	public Object getRawValue(Object object) {
		Object result;

		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			result = field.get(object);
			field.setAccessible(accessible);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	public abstract String getValue(Object object);

	public abstract void setValue(Object object, Object source);

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
