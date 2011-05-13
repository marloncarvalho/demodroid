package br.gov.frameworkdemoiselle.util;

import br.gov.frameworkdemoiselle.util.Async.Callable;
import br.gov.frameworkdemoiselle.util.Async.Mirror;
import br.gov.frameworkdemoiselle.util.Async.Result;

public class CallableExecutor implements Callable {
	private String method;
	private Object object;
	private Object[] args;
	private boolean resultAsArg = false;
	protected Async async;

	public CallableExecutor(Async async) {
		this.async = async;
	}

	public Mirror call(String method) {
		this.method = method;
		Mirror mirror = new Mirror() {
			public Result on(Object object) {
				Result result = new Result() {

					public Async withResultAsArg() {
						resultAsArg = true;
						return async;
					}

					public Async withArgs(Object... arg) {
						args = arg;
						return async;
					}

					public Async noArgs() {
						return async;
					}
				};
				return result;
			}
		};
		return mirror;
	}

}
