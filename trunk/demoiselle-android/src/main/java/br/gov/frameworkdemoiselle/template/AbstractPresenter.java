package br.gov.frameworkdemoiselle.template;

@SuppressWarnings("all")
abstract public class AbstractPresenter<V extends AbstractView> {
	private V view;

	public V getView() {
		return view;
	}

	void setView(V view) {
		this.view = view;
	}

}
