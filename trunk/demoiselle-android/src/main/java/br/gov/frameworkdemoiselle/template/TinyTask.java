package br.gov.frameworkdemoiselle.template;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Task that must be extended to perform a simple task. Displays a dialog screen
 * at the beginning of the operation reporting progress of the operation. After
 * operation, closes the dialog screen automatically.
 * 
 * @author Marlon Silva Carvalho
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
abstract public class TinyTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	/**
	 * Constantes que indicam o sucesso ou não da operação.
	 */
	public static final int CODE_NULL = 0;
	public static final int INVALID_CODE = 1;
	public static final int OK = 2;
	public static final int UNKNOWN_ERROR = 3;

	/** Diálogo de Progresso da Tarefa. **/
	protected ProgressDialog dialog;

	private Context context;

	public TinyTask(Context context) {
		this.context = context;
	}

	@Override
	final protected Result doInBackground(Params... params) {
		return null;
	}

	@Override
	protected void onPreExecute() {
		Log.d("TinyTask", "PreExecute TinyTask.");
		dialog = ProgressDialog.show(context, "Attention", "Executing...");
	}

	@Override
	protected void onPostExecute(Result result) {
		Log.d("TinyTask", "PostExecute TinyTask.");
		dialog.dismiss();
	}

}