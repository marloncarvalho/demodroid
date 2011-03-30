package br.gov.frameworkdemoiselle.template;

import roboguice.event.EventManager;
import android.os.Bundle;
import br.gov.frameworkdemoiselle.activity.DemoiselleActivity;
import br.gov.frameworkdemoiselle.event.AfterCreation;
import br.gov.frameworkdemoiselle.event.BeforeCreation;
import br.gov.frameworkdemoiselle.stereotype.UserView;

import com.google.inject.Inject;

abstract public class AbstractView extends DemoiselleActivity {

	private int contentView;
	
	@Inject
	private EventManager eventManager;

	public AbstractView() {
		if (getClass().isAnnotationPresent(UserView.class)) {
			contentView = getClass().getAnnotation(UserView.class).value();
		}
	}

	public void registerPresenter(AbstractPresenter presenter) {
		presenter.setView(this);
	}

	@Override
	final protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(contentView);
		eventManager.fire(this, new BeforeCreation());
		processCreation(savedInstanceState);
		eventManager.fire(this, new AfterCreation());
	}

	protected abstract void processCreation(Bundle savedInstanceState);

}
