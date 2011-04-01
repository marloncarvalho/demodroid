package br.gov.frameworkdemoiselle.activity;

import br.gov.frameworkdemoiselle.util.Activities;
import roboguice.activity.RoboActivity;

public class DemoiselleActivity extends RoboActivity {

	@Override
	protected void onStart() {
		super.onStart();
		Activities.setActual(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Activities.setActual(null);
	}

}
