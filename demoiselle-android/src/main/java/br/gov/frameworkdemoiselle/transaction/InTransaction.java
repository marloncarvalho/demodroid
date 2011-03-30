package br.gov.frameworkdemoiselle.transaction;

public interface InTransaction<Result> {

	Result execute() throws Throwable;

}
