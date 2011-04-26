package br.gov.frameworkdemoiselle.util;

final public class Number {

	static public long parseToLong(String num) {
		long result = 0;
		try {
			result = Long.valueOf(num);
		} catch (Throwable t) {
		}
		return result;
	}

	static public int parseToInteger(String num) {
		int result = 0;
		try {
			result = Integer.valueOf(num);
		} catch (Throwable t) {
		}
		return result;
	}
	
	static public double parseToDouble(String num) {
		double result = 0;
		try {
			result = Double.valueOf(num);
		} catch (Throwable t) {
		}
		return result;
	}

}
