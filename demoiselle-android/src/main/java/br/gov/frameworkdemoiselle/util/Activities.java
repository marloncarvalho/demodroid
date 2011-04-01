package br.gov.frameworkdemoiselle.util;

import android.app.Activity;

final public class Activities {

	private static Activity actual;

	private Activities() {
	}

	public static Activity getActual() {
		return actual;
	}

	public static void setActual(Activity actitivy) {
		actual = actitivy;
	}

	public static void run(Runnable runnable) {
		if (actual != null) {
			actual.runOnUiThread(runnable);
		}
	}

}
