package br.gov.frameworkdemoiselle.task;


public class AfterImpl extends CallableImpl implements After {
	private String message;
	private int duration;

	public AfterImpl(Async async) {
		super(async);
	}

	public Async alert(String message, int duration) {
		this.setMessage(message);
		this.setDuration(duration);
		return async;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

}
