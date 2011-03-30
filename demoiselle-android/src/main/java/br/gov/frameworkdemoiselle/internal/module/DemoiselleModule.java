package br.gov.frameworkdemoiselle.internal.module;

import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import br.gov.frameworkdemoiselle.annotation.SQLite;
import br.gov.frameworkdemoiselle.internal.bootstrap.ConfigurationBootstrap;
import br.gov.frameworkdemoiselle.internal.bootstrap.StartupBootstrap;
import br.gov.frameworkdemoiselle.internal.processor.ConfigurationProcessor;
import br.gov.frameworkdemoiselle.persistence.EntityManager;
import br.gov.frameworkdemoiselle.persistence.implementation.EntityManagerSQLiteImpl;
import br.gov.frameworkdemoiselle.transaction.SQLiteTransaction;
import br.gov.frameworkdemoiselle.transaction.Transaction;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

public class DemoiselleModule extends AbstractAndroidModule {
	private ConfigurationProcessor configurationProcessor;

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new ConfigurationBootstrap());
		bindListener(Matchers.any(), new StartupBootstrap());
		bind(EntityManager.class).annotatedWith(SQLite.class).to(EntityManagerSQLiteImpl.class);
		bind(Transaction.class).to(SQLiteTransaction.class);
	}

	@Provides
	public ConfigurationProcessor provideConfigurationProcessor(Provider<Context> provider) {
		if (this.configurationProcessor == null) {
			this.configurationProcessor = new ConfigurationProcessor(provider);
		}
		return this.configurationProcessor;
	}

}
