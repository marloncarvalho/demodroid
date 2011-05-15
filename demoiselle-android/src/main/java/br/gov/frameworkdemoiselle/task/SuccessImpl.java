package br.gov.frameworkdemoiselle.task;


public class SuccessImpl extends CallableImpl implements Success {
	private String message;
	private int duration;

	public SuccessImpl(Async async) {
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
