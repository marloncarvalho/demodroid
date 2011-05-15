package br.gov.frameworkdemoiselle.task;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

/**
 * Utility class which create a simple DSL to execute async tasks.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
final public class Async {
	private CallableImpl mainCallable;
	private List<AfterImpl> afters = new LinkedList<AfterImpl>();
	private List<BeforeImpl> befores = new LinkedList<BeforeImpl>();
	private List<CrashImpl> crashes = new LinkedList<CrashImpl>();
	private List<SuccessImpl> successes = new LinkedList<SuccessImpl>();
	private DuringImpl during;
	Activity activity;

	/**
	 * Execute the Task.
	 */
	public void execute() {
		new AsyncTask<Object, Void, Object>() {
			private ProgressDialog progressDialog;
			private boolean success = true;

			protected void onPreExecute() {
				if (during != null) {
					progressDialog = ProgressDialog.show(activity, during.getTitle(), during.getBody());
				}
				for (BeforeImpl before : befores) {
					showToast(before.getMessage(), before.getDuration());
					invoke(before.getObject(), before.getMethod(), before.getArgs());
				}
			}

			protected void onPostExecute(Object result) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if (success) {
					for (SuccessImpl success : successes) {
						showToast(success.getMessage(), success.getDuration());
						invoke(success.getObject(), success.getMethod(), success.getArgs());
					}
				} else {
					for (CrashImpl crash : crashes) {
						showToast(crash.getMessage(), crash.getDuration());
						invoke(crash.getObject(), crash.getMethod(), crash.getArgs());
					}
				}
				for (AfterImpl after : afters) {
					showToast(after.getMessage(), after.getDuration());
					if (after.isResultAsArg()) {
						invoke(after.getObject(), after.getMethod(), result);
					} else {
						invoke(after.getObject(), after.getMethod(), after.getArgs());
					}
				}
			}

			@Override
			protected Object doInBackground(Object... params) {
				Object result = null;
				try {
					result = Reflections.callMethod(mainCallable.getObject(), mainCallable.getMethod(),
							mainCallable.getArgs());
				} catch (Throwable throwable) {
					success = false;
				}
				return result;
			}

		}.execute();
	}

	private Object invoke(Object object, String method, Object... args) {
		Object result = null;
		if (!Strings.isEmpty(method) && object != null) {

			if (args != null && args.length > 0) {
				result = Reflections.callMethod(object, method, args);
			} else {
				result = Reflections.callMethod(object, method);
			}
		}
		return result;
	}

	private void showToast(String message, int duration) {
		if (!Strings.isEmpty(message)) {
			Toast.makeText(activity, message, duration).show();
		}
	}

	private Callable createMainCallable() {
		if (mainCallable == null) {
			this.mainCallable = new CallableImpl(this);
		}
		return this.mainCallable;
	}

	public Callable using(Activity act) {
		activity = act;
		return createMainCallable();
	}

	public After after() {
		AfterImpl after = new AfterImpl(this);
		afters.add(after);
		return after;
	}

	public Before before() {
		BeforeImpl before = new BeforeImpl(this);
		befores.add(before);
		return before;
	}

	public Occurring occurring() {
		Occurring o = new Occurring() {

			public Crash crash() {
				CrashImpl crash = new CrashImpl(Async.this);
				crashes.add(crash);
				return crash;
			}

			public Success success() {
				SuccessImpl success = new SuccessImpl(Async.this);
				successes.add(success);
				return success;
			}
		};
		return o;
	}

	public During during() {
		during = new DuringImpl(this);
		return during;
	}

}
