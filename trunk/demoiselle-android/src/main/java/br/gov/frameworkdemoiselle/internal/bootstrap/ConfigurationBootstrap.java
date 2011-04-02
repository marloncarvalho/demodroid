package br.gov.frameworkdemoiselle.internal.bootstrap;

import br.gov.frameworkdemoiselle.configuration.Configuration;
import br.gov.frameworkdemoiselle.internal.processor.ConfigurationProcessor;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

@SuppressWarnings("all")
public class ConfigurationBootstrap implements TypeListener {
	private Provider<ConfigurationProcessor> configurationProcessor;

	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
		if (typeLiteral.getRawType().isAnnotationPresent(Configuration.class)) {
			if (configurationProcessor == null) {
				configurationProcessor = typeEncounter.getProvider(ConfigurationProcessor.class);
			}
			typeEncounter.register(new ConfigurationListener());
		}
	}

	class ConfigurationListener implements InjectionListener {

		public void afterInjection(Object o) {
			configurationProcessor.get().process(o);
		}

	}

}