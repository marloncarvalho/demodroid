package br.gov.frameworkdemoiselle.activity;

import br.gov.frameworkdemoiselle.util.Activities;
import roboguice.activity.RoboActivity;

/**
 * Extends from ${link RoboActivity} adding specific behavior from Demoiselle framework.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class DemoiselleActivity extends RoboActivity {

	/**
	 * Overrides adding specific behavior.
	 * Saves the actual Activity being viewd.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Activities.setCurrent(this);
	}

}
