package br.gov.frameworkdemoiselle.message;

import java.util.List;

/**
 * Context interface reserved for messaging purposes.
 * <p>
 * In order to use this, just add the line below in the code:
 * <p>
 * <code>@Inject MessageContext messageContext;</code>
 * 
 * @author SERPRO
 * @see Message
 */
public interface MessageContext {

	/**
	 * Saves a message into the context.
	 * 
	 * @param message
	 */
	void add(Message message, Object... params);

	void add(String text, Object... params);

	void add(String text, SeverityType severity, Object... params);

	void add(int resource, Object... params);

	void add(int resource, SeverityType severity, Object... params);

	/**
	 * Returns all messages in the context.
	 */
	List<Message> getMessages();

	/**
	 * Clears the list of messages in the context.
	 */
	void clear();

}
