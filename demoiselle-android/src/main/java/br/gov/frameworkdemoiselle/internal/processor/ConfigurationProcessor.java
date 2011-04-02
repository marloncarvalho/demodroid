package br.gov.frameworkdemoiselle.internal.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import android.content.Context;
import br.gov.frameworkdemoiselle.annotation.Ignore;
import br.gov.frameworkdemoiselle.configuration.Configuration;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Provider;

public class ConfigurationProcessor {
	private Context context;

	public ConfigurationProcessor(Provider<Context> contextProvider) {
		context = contextProvider.get();
	}

	private Properties getProperties(String file) {
		Properties props = new Properties();
		try {
			props.load(context.getAssets().open(file + ".properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return props;
	}

	public void process(Object object) {
		Field[] fields = Reflections.getNonStaticDeclaredFields(object.getClass());
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Ignore.class)) {
				String value = getProperties(object.getClass().getAnnotation(Configuration.class).resource()).getProperty(field.getName());
				Reflections.setFieldValue(field, object, value);
			}
		}
	}

}
