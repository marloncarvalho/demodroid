package br.gov.frameworkdemoiselle.application;

import java.util.List;

import roboguice.application.RoboApplication;
import br.gov.frameworkdemoiselle.internal.module.DemoiselleModule;
import br.gov.frameworkdemoiselle.util.Beans;

import com.google.inject.Module;

/**
 * Start point for Demoiselle Applications.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class DemoiselleApplication extends RoboApplication {

	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new DemoiselleModule());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Beans.setInjector(getInjector());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
