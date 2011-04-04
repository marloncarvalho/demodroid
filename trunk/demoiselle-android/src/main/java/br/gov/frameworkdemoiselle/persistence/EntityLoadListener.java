package br.gov.frameworkdemoiselle.persistence;

/**
 * Called when an entity is loaded from underlying storage system.
 * 
 * @author Marlon Silva Carvalho
 *
 * @since 1.0.0
 * @param <E>
 */
public interface EntityLoadListener {

	/**
	 * Called immediately after entity is loaded from storage.
	 * 
	 * @param entity Loaded entity.
	 */
	void entityLoaded(Object entity);
	
}
