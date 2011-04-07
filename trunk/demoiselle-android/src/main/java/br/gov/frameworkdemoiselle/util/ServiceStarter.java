package br.gov.frameworkdemoiselle.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Helper class responsible to start Services.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class ServiceStarter {

	public static void start(final Class<?> clasz) {
		Activity activity = (Activity) Beans.getBean(Context.class);
		Intent intent = new Intent(activity, clasz);
		activity.startService(intent);
	}

}
