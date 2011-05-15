package br.gov.frameworkdemoiselle.task;


public class BeforeImpl extends CallableImpl implements Before {
	private String message;
	private int duration;

	public BeforeImpl(Async async) {
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
