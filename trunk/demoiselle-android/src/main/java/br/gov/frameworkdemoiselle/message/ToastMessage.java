package br.gov.frameworkdemoiselle.message;

import br.gov.frameworkdemoiselle.util.Strings;

/**
 * Message interface implementation to Toast Messages.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class ToastMessage implements Message {

	private final String originalText;

	private String parsedText;

	private final SeverityType severity;

	private final Object[] params;

	public static final SeverityType DEFAULT_SEVERITY = SeverityType.INFO;

	public ToastMessage(String text, SeverityType severity, Object... params) {
		this.originalText = text;
		this.severity = (severity == null ? DEFAULT_SEVERITY : severity);
		this.params = params;
	}

	public String getText() {
		initParsedText();
		return parsedText;
	}

	private void initParsedText() {
		if (parsedText == null) {
			if (originalText != null) {
				parsedText = new String(originalText);
			}
			parsedText = Strings.getString(parsedText, params);
		}
	}

	public SeverityType getSeverity() {
		return severity;
	}

	@Override
	public String toString() {
		initParsedText();
		return Strings.toString(this);
	}
}
