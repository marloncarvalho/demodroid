package br.gov.frameworkdemoiselle.task;


public class DuringImpl implements During {
	private String title;
	private String body;
	private Async async;

	public DuringImpl(Async async) {
		this.async = async;
	}

	public Async show(String title, String body) {
		this.setTitle(title);
		this.setBody(body);
		return async;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

}
