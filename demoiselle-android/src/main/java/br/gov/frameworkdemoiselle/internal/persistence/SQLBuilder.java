package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.persistence.annotation.Column;

public class SQLBuilder {

	@Inject
	private PersistenceInspector persistenceInspector;

	public String buildCreateTable(Class<?> cls) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE  ");
		sql.append(persistenceInspector.getTableName(cls));
		sql.append(" (");
		sql.append(persistenceInspector.getIdField(cls).getName());
		sql.append(" INTEGER PRIMARY KEY, ");
		for (Field field : persistenceInspector.getPersistentFields(cls)) {
			sql.append(field.getName());
			sql.append(" ");
			sql.append(getType(field));
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(");");
		return sql.toString();
	}

	private String getType(Field field) {
		String type = null;
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			if (column.type() != null && !"".equals(column.type())) {
				type = column.type();
			}
		}

		if (type == null) {
			Class<?> cls = field.getType();
			if (cls.getSimpleName().toLowerCase().equals("date")) {
				type = "DATE";
			}
			if (cls.getSimpleName().toLowerCase().equals("string")) {
				type = "TEXT";
			}
			if (cls.getSimpleName().toLowerCase().equals("integer") || cls.getSimpleName().toLowerCase().equals("boolean")) {
				type = "INTEGER";
			}
			if (cls.getSimpleName().toLowerCase().equals("double") || cls.getSimpleName().toLowerCase().equals("float")) {
				type = "REAL";
			}
		}

		return type;
	}

}