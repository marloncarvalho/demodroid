package br.gov.frameworkdemoiselle.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.gov.frameworkdemoiselle.message.MessageContext;
import br.gov.frameworkdemoiselle.message.SeverityType;

import com.google.inject.Inject;

/**
 * Utilitário que lança tarefas assíncronas.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class Worker {

	@Inject
	private MessageContext messageContext;
	private Object caller;
	private String methodName;
	private Object[] callParams;
	private String onSuccessMessage;
	private String onExceptionMessage;
	private Object onExceptionCaller;
	private String onExceptionMethod;
	private Object[] onExceptionParams;
	private Object onSuccessCaller;
	private String onSuccessMethod;
	private Object[] onSuccessParams;

	/**
	 * Default constructor.
	 * 
	 * @param caller
	 *            Object from which the "method" will be called.
	 * @param method
	 *            Method to be called in "Object".
	 * @param callerParams
	 *            Parameters that will be used to call "Method" in "Object".
	 */
	public Worker(Object caller, String method, Object... callerParams) {
		this.caller = caller;
		this.methodName = method;
		this.callParams = callerParams;
		Beans.getInjector().injectMembers(this);
	}

	/**
	 * Execute an async task.
	 * 
	 * @param caller
	 * @param method
	 * @param callerParams
	 * @return
	 */
	public static Worker doAsynchronously(Object caller, String method, Object... callerParams) {
		return new Worker(caller, method, callerParams);
	}

	public Worker onSuccessMessage(String message) {
		this.onSuccessMessage = message;
		return this;
	}

	public Worker onSuccessCall(Object caller, String method, Object... callerParams) {
		this.onSuccessCaller = caller;
		this.onSuccessMethod = method;
		this.onSuccessParams = callerParams;
		return this;
	}

	public Worker onExceptionMessage(String message) {
		this.onExceptionMessage = message;
		return this;
	}

	public Worker onExceptionCall(Object caller, String method, Object... callerParams) {
		this.onExceptionCaller = caller;
		this.onExceptionMethod = method;
		this.onExceptionParams = callerParams;
		return this;
	}

	/**
	 * Fire the Task!
	 */
	public void execute(final Context context, final String dialogTitle, final String dialogBody) {
		new AsyncTask<Void, Void, Void>() {

			private ProgressDialog dialog;
			
			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(context, dialogTitle, dialogBody);
			}

			@Override
			protected void onPostExecute(Void result) {
				dialog.dismiss();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {

					Log.d("Worker", "Calling [" + methodName + "] in object [" + caller
							+ "] with params [" + callParams + "].");

					Reflections.callMethod(caller, methodName, callParams);

					Log.d("Worker", "Call Success.");
					
					// Show Success Message.
					if (onSuccessMessage != null && !"".equals(onSuccessMessage)) {
						Log.d("Worker","Showing Success Message [" + onSuccessMessage + "];");
						messageContext.add(onSuccessMessage);
					}

					// Call Success Method.
					if (onSuccessCaller != null && onSuccessMethod != null && !"".equals(onSuccessMessage)) {
						Log.d("Worker", "Calling Success Method [" + methodName + "] in object [" + caller
								+ "] with params [" + callParams + "].");
						Reflections.callMethod(onSuccessCaller, onSuccessMethod, onSuccessParams);
						Log.d("Worker","Success Method Called with Success :)");
					}

				} catch (Throwable throwable) {

					// Show Exception Message.
					if (onExceptionMessage != null && !"".equals(onExceptionMessage)) {
						Log.d("Worker","Showing Exception Message [" + onExceptionMessage + "];");
						messageContext.add(onExceptionMessage, SeverityType.ERROR);
					}

					// Call Exception Method.
					if (onExceptionCaller != null && onExceptionMethod != null && !"".equals(onExceptionMessage)) {
						Log.d("Worker", "Calling Success Method [" + methodName + "] in object [" + caller
								+ "] with params [" + callParams + "].");
						Reflections.callMethod(onExceptionCaller, onExceptionMethod, onExceptionParams);
						Log.d("Worker","Exception Method Called with Success :)");
					}

				}
				return null;
			}

		}.execute();
	}
}