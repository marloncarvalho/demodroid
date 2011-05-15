package br.gov.frameworkdemoiselle.task;

public interface Result {
	public Async withResultAsArg();

	public Async noArgs();

	public Async withArgs(Object... args);
}
