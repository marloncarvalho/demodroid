package br.gov.frameworkdemoiselle.internal.module;

import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import br.gov.frameworkdemoiselle.internal.bootstrap.ConfigurationBootstrap;
import br.gov.frameworkdemoiselle.internal.bootstrap.StartupBootstrap;
import br.gov.frameworkdemoiselle.internal.configuration.InternalConfig;
import br.gov.frameworkdemoiselle.internal.persistence.PersistenceInspector;
import br.gov.frameworkdemoiselle.internal.persistence.SQLiteHelper;
import br.gov.frameworkdemoiselle.internal.persistence.SQLiteOpener;
import br.gov.frameworkdemoiselle.internal.persistence.SQLitePersistence;
import br.gov.frameworkdemoiselle.internal.processor.ConfigurationProcessor;
import br.gov.frameworkdemoiselle.persistence.Persistence;
import br.gov.frameworkdemoiselle.persistence.annotation.SQLite;

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
	@SQLite
	public Persistence provideSQLitePersistence(Provider<Context> pContext,
			InternalConfig config, SQLiteHelper sqliteHelper,
			PersistenceInspector persistenceInspector, SQLiteOpener opener) {
		SQLitePersistence sqlite = new SQLitePersistence(config, sqliteHelper,
				persistenceInspector, opener);
		return sqlite;
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
