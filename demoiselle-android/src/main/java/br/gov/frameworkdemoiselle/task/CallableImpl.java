package br.gov.frameworkdemoiselle.task;


public class CallableImpl implements Callable {
	private String method;
	private Object object;
	private Object[] args;
	private boolean resultAsArg = false;
	protected Async async;

	public CallableImpl(Async async) {
		this.async = async;
	}

	public Reflectable call(String method) {
		this.method = method;
		Reflectable mirror = new Reflectable() {
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public boolean isResultAsArg() {
		return resultAsArg;
	}

	public void setResultAsArg(boolean resultAsArg) {
		this.resultAsArg = resultAsArg;
	}

	public Async getAsync() {
		return async;
	}

	public void setAsync(Async async) {
		this.async = async;
	}

}
