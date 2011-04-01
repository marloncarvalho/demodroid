package br.gov.frameworkdemoiselle.internal.implementation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import br.gov.frameworkdemoiselle.message.Message;
import br.gov.frameworkdemoiselle.message.MessageContext;
import br.gov.frameworkdemoiselle.message.SeverityType;
import br.gov.frameworkdemoiselle.util.Activities;
import br.gov.frameworkdemoiselle.util.Beans;

/**
 * Default ${link MessageContext} implementation. Delegates to Toaster.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class ToasterMessageContextImpl implements MessageContext {

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.message.MessageContext#add(br.gov.frameworkdemoiselle.message.Message, java.lang.Object[])
	 */
	public void add(final Message message, final Object... params) {
		final Context context = Beans.getBean(Context.class);
		Activity activity = Activities.getActual();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = Toast.makeText(context, message.getText(), Toast.LENGTH_SHORT);
				toast.show();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.message.MessageContext#add(java.lang.String, java.lang.Object[])
	 */
	public void add(final String text, final Object... params) {
		final Context context = Beans.getBean(Context.class);
		Activity activity = Activities.getActual();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see br.gov.frameworkdemoiselle.message.MessageContext#add(java.lang.String, br.gov.frameworkdemoiselle.message.SeverityType, java.lang.Object[])
	 */
	public void add(final String text, final SeverityType severity, final Object... params) {
		final Context context = Beans.getBean(Context.class);
		Activity activity = Activities.getActual();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}

		});
	}

	public void add(int resource, Object... params) {

	}

	public void add(int resource, SeverityType severity, Object... params) {

	}

	public List<Message> getMessages() {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

}
