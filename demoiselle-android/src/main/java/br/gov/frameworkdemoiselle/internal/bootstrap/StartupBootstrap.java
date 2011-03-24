package br.gov.frameworkdemoiselle.internal.bootstrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.gov.frameworkdemoiselle.annotation.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

@SuppressWarnings("all")
public class StartupBootstrap implements TypeListener, InjectionListener {
	public static Set<Object> objects = new HashSet<Object>();

	@SuppressWarnings("unchecked")
	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
		if (typeLiteral.getRawType().isAnnotationPresent(BusinessController.class)) {
			typeEncounter.register(this);
			Method[] methods = typeLiteral.getRawType().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Startup.class)) {
					typeEncounter.register(this);
					break;
				}
			}
		}
	}

	public void afterInjection(Object o) {
		if (objects.contains(o)) {
			return;
		}
		Method[] methods = o.getClass().getMethods();
		for (Method method : methods) {
			try {
				if (method.isAnnotationPresent(Startup.class)) {
					method.invoke(o, null);
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		objects.add(o);
	}

}
