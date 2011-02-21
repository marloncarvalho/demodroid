package br.gov.frameworkdemoiselle.internal.persistence;

import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.gov.frameworkdemoiselle.internal.configuration.InternalConfig;

import com.google.inject.Provider;

public class SQLiteOpener extends SQLiteOpenHelper {
	private Context context;
	private InternalConfig config;
	
	@Inject
	private PersistenceInspector persistenceInspector;
	
	@Inject
	private SQLBuilder builder;
	
	@Inject
	public SQLiteOpener(Provider<Context> provider, InternalConfig config) {
		super(provider.get(), config.getDatabaseName(), null, Integer.valueOf(config.getDatabaseVersion()));
		this.context = provider.get();
		this.config = config;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(SQLiteDatabase database) {
		List<Class<?>> classes = persistenceInspector.getEntities(context.getClassLoader(), config);
		for (Class cls : classes) {
			database.execSQL(builder.buildCreateTable(cls));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
