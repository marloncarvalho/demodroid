package br.gov.frameworkdemoiselle.persistence;

public interface InTransaction {

	void execute() throws Throwable;

}
