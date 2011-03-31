package br.gov.frameworkdemoiselle.message;

/**
 * Represents the configuration of a single message.
 * 
 * @author SERPRO
 */
public interface Message {

	/**
	 * Represents the text of the message.
	 */
	String getText();

	/**
	 * Represents the kind of message. It could be useful for presentation
	 * layer.
	 */
	SeverityType getSeverity();

}
