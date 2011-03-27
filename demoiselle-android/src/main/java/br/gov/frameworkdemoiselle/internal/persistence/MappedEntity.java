package br.gov.frameworkdemoiselle.internal.persistence;

import java.util.HashMap;
import java.util.Map;

import br.gov.frameworkdemoiselle.persistence.annotation.Id;
import br.gov.frameworkdemoiselle.persistence.annotation.Table;
import br.gov.frameworkdemoiselle.util.Reflections;

public class MappedEntity {
	private MappedColumn idMappedColumn;
	private Class<?> mappedClass;
	private Map<String, MappedColumn> mappedColumns = new HashMap<String, MappedColumn>();

	public Object instantiate() {
		return Reflections.instantiate(mappedClass);
	}

	public void addMappedColumn(MappedColumn column) {
		mappedColumns.put(column.getName(), column);
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

	public MappedColumn getIdMappedColumn() {
		if (idMappedColumn == null) {
			for (MappedColumn mappedColumn : mappedColumns.values()) {
				if (mappedColumn.getField().isAnnotationPresent(Id.class)) {
					idMappedColumn = mappedColumn;
					break;
				}
			}
		}
		return idMappedColumn;
	}
}
