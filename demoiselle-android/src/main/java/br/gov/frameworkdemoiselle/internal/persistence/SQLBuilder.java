package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;

import br.gov.frameworkdemoiselle.persistence.annotation.Column;
import br.gov.frameworkdemoiselle.persistence.implementation.EntityManagerSQLiteImpl;

public class SQLBuilder {

	public String buildCreateTable(Class<?> cls) {
		MappedEntity mappedEntity = EntityManagerSQLiteImpl.cached.get(cls);
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE  ");
		sql.append(mappedEntity.getTableName());
		sql.append(" (");
		sql.append(mappedEntity.getIdMappedColumn().getName());
		sql.append(" INTEGER PRIMARY KEY, ");
		for (MappedColumn mappedColumn : mappedEntity.getMappedColumns().values()) {
			sql.append(mappedColumn.getName());
			sql.append(" ");
			sql.append(getType(mappedColumn.getField()));
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