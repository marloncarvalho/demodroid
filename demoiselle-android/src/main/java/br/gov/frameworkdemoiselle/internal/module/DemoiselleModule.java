package br.gov.frameworkdemoiselle.internal.module;

import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import br.gov.frameworkdemoiselle.internal.bootstrap.ConfigurationBootstrap;
import br.gov.frameworkdemoiselle.internal.bootstrap.StartupBootstrap;
import br.gov.frameworkdemoiselle.internal.processor.ConfigurationProcessor;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

public class DemoiselleModule extends AbstractAndroidModule {
	private ConfigurationProcessor configurationProcessor;

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new ConfigurationBootstrap());
		bindListener(Matchers.any(), new StartupBootstrap());
	}

	@Provides
	public ConfigurationProcessor provideConfigurationProcessor(
			Provider<Context> provider) {
		if (this.configurationProcessor == null) {
			this.configurationProcessor = new ConfigurationProcessor(provider);
		}
		return this.configurationProcessor;
	}

}
