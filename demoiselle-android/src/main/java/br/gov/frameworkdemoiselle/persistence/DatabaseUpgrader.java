package br.gov.frameworkdemoiselle.persistence;

/**
 * Helper class which handle database upgrades.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class DatabaseUpgrader {

	public static Batch createBatch() {
		return new Batch();
	}

}