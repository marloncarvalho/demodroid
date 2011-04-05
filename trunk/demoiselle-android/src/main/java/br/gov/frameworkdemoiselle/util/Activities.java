package br.gov.frameworkdemoiselle.util;

import android.app.Activity;

final public class Activities {

	private static Activity current;

	private Activities() {
	}

	public static Activity getCurrent() {
		return current;
	}

	public static void setCurrent(Activity actitivy) {
		current = actitivy;
	}

	public static void run(Runnable runnable) {
		if (current != null) {
			current.runOnUiThread(runnable);
		}
	}

}
