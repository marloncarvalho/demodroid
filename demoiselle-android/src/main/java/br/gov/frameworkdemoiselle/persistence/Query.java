package br.gov.frameworkdemoiselle.persistence;

import java.util.List;

/**
 * Interface used to control query execution.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public interface Query {

	/**
	 * Set the maximum number of results to retrieve.
	 * 
	 * @param maxResults
	 */
	Query setMaxResults(int maxResults);

	Query setParameter(int position, Object value);

	Query setFirstResult(int firstResult);

	List<?> getResultList();

	List<?> getResultList(EntityLoadListener listener);

	Object getRawResult();

	int executeUpdate();
}
