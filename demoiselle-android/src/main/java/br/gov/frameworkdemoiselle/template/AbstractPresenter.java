package br.gov.frameworkdemoiselle.template;


abstract public class AbstractPresenter {
	private AbstractView view;

	public AbstractView getView() {
		return view;
	}

	public void setView(AbstractView view) {
		this.view = view;
	}

}
