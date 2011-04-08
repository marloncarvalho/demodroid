package br.gov.frameworkdemoiselle.activity;

import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import br.gov.frameworkdemoiselle.event.AfterStart;
import br.gov.frameworkdemoiselle.util.Activities;

import com.google.inject.Inject;

/**
 * Extends from ${link RoboActivity} adding specific behavior from Demoiselle framework.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class DemoiselleActivity extends RoboActivity {

	@Inject
	private EventManager eventManager;

	/**
	 * Overrides adding specific behavior.
	 * Saves the actual Activity being viewd.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Activities.setCurrent(this);
		eventManager.fire(new AfterStart());
	}

}
