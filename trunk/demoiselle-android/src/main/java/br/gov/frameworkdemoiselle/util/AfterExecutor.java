package br.gov.frameworkdemoiselle.util;

import android.widget.Toast;
import br.gov.frameworkdemoiselle.util.Async.After;

public class AfterExecutor extends CallableExecutor implements After {

	public AfterExecutor(Async async) {
		super(async);
	}

	@Override
	public Async alert(String message, int duration) {
		if (message != null && !"".equals(message)) {
			Toast.makeText(async.activity, message, duration).show();
		}
		return async;
	}

}
