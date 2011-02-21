package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;

import javax.inject.Inject;

public class SQLBuilder {
	
	@Inject
	private PersistenceInspector entityHelper;

	public String buildCreateTable(Class<?> cls) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE  ");
		sql.append(entityHelper.getTableName(cls));
		sql.append(" (");
		sql.append(entityHelper.getIdField(cls).getName());
		sql.append(" INTEGER PRIMARY KEY, ");
		for (Field field : entityHelper.getPersistentFields(cls)) {
			sql.append(field.getName());
			sql.append(" ");
			sql.append(getType(field.getType()));
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(");");
		return sql.toString();
	}

	private String getType(Class<?> cls) {
		String type = null;
		if (cls.getSimpleName().toLowerCase().equals("string")) {
			type = "TEXT";
		}
		if (cls.getSimpleName().toLowerCase().equals("integer")) {
			type = "INTEGER";
		}
		if (cls.getSimpleName().toLowerCase().equals("double")
				|| cls.getSimpleName().toLowerCase().equals("float")) {
			type = "REAL";
		}
		return type;
	}
	
}