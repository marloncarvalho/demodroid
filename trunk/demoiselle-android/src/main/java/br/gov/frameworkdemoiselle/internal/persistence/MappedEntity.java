package br.gov.frameworkdemoiselle.internal.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.frameworkdemoiselle.persistence.annotation.Column;
import br.gov.frameworkdemoiselle.persistence.annotation.Id;
import br.gov.frameworkdemoiselle.persistence.annotation.Table;

public class MappedEntity {
	private Field idField;
	private Class<?> mappedClass;
	private List<String> columns = new ArrayList<String>();
	private Map<String, Field> fields = new HashMap<String, Field>();
	private Map<String, MappedColumn> mappedColumns = new HashMap<String, MappedColumn>();

	public void addMappedColumn(MappedColumn column) {
		mappedColumns.put(column.getName(), column);
	}

	public String getIdFieldName() {
		String result = null;

		Field field = getIdField();
		if (field.isAnnotationPresent(Column.class)) {
			result = field.getAnnotation(Column.class).name();
		} else {
			result = field.getName().toLowerCase();
		}

		return result;
	}

	public Field getIdField() {
		if (idField == null) {
			try {
				for (Field field : fields.values()) {
					if (field.isAnnotationPresent(Id.class)) {
						idField = field;
					}
				}
				idField = mappedClass.getDeclaredField("id");
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}
		return idField;
	}

	public void addField(String fieldName, Field field) {
		fields.put(fieldName, field);
	}

	public Field getField(String fieldName) {
		return fields.get(fieldName);
	}

	public String getTableName() {
		String table = "";

		if (mappedClass.isAnnotationPresent(Table.class)) {
			table = mappedClass.getAnnotation(Table.class).name();
		} else {
			table = mappedClass.getName().toLowerCase();
		}

		return table;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setMappedColumns(Map<String, MappedColumn> mappedColumns) {
		this.mappedColumns = mappedColumns;
	}

	public Map<String, MappedColumn> getMappedColumns() {
		return mappedColumns;
	}

	public void setMappedClass(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
	}

	public Class<?> getMappedClass() {
		return mappedClass;
	}
}
