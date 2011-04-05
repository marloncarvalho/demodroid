package br.gov.frameworkdemoiselle.persistence;

final public class Batch {

	public Batch backup() {
		return this;
	}

	public Batch addColumn(String table, String name, String type) {
		return this;
	}

	public Batch dropColumns(String table, String... columns) {
		return this;
	}

	public Batch dropTables(String... tables) {
		return this;
	}

	public Batch renameColumn(String table, String name, String newName) {
		return this;
	}

	public Batch renameTable(String table, String newName) {
		return this;
	}

	public void execute() {

	}

}