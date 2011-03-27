package br.gov.frameworkdemoiselle.internal.exception;

public class SystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SystemException() {
		super();
	}

	public SystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SystemException(String arg0) {
		super(arg0);
	}

	public SystemException(Throwable arg0) {
		super(arg0);
	}

}
