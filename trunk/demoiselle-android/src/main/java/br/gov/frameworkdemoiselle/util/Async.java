package br.gov.frameworkdemoiselle.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * Utility class which create a simple DSL to execute async tasks.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
final public class Async {
	private Callable mainCallable;
	private LinkedList<After> afters = new LinkedList<After>();
	private LinkedList<Before> befores = new LinkedList<Before>();
	Activity activity;
	
	/**
	 * Execute the Task.
	 */
	public void letsGo() {
		// Criar AsyncTask
		// No onPreExecute() chamar os before.
		// No doInBackground() chamar o mainCallable.
		// Caso dê erro, chamar os Crashes
		// Caso não dê erro, chamar o Success.
	}
	
	public Callable using(Activity act) {
		activity = act;
		this.mainCallable = new CallableExecutor(this);
		return this.mainCallable;
	}

	public After after() {
		After after = new AfterExecutor(this);
		afters.add(after);
		return after;
	}

	public Before before() {
		return new Before() {

			@Override
			public Mirror call(String method) {
				return null;
			}

			@Override
			public Async show(String title, String body) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	private List<Ocurring> ocurrings = new LinkedList<Ocurring>();

	public Ocurring ocurring() {
		Ocurring o = new Ocurring() {

			@Override
			public Crash crash() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Success success() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		ocurrings.add(o);
		return o;
	}

	private List<During> durings = new LinkedList<During>();

	public During during() {
		During during = new During() {

			@Override
			public Async show(String title, String body) {
				return Async.this;
			}

		};
		durings.add(during);
		return during;
	}

	public interface Task {
		public Object doInBackground(Object... params);
	}

	public interface After extends Callable, Alert {
	}

	public interface Before extends Callable, Alert {
	}

	public interface Crash extends Callable, Alert {
	}

	public interface Success extends Callable, Alert {
	}

	public interface Result {
		public Async withResultAsArg();

		public Async noArgs();

		public Async withArgs(Object... args);
	}

	public interface Mirror {
		public Result on(Object object);
	}

	public interface Exibhitor {
		public Async show(String title, String body);
	}

	public interface Callable {
		public Mirror call(String method);
	}

	public interface Alert {
		public Async alert(String message, int duration);
	}

	public interface During {
		public Async show(String title, String body);
	}

	public interface Ocurring {
		public Crash crash();

		public Success success();
	}
}
