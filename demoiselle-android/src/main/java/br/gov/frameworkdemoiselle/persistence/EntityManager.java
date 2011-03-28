package br.gov.frameworkdemoiselle.persistence;

import br.gov.frameworkdemoiselle.internal.exception.SystemException;

public interface EntityManager {

	void close() throws SystemException;

	<T> T find(Class<T> clazz, Object primaryKey) throws SystemException;

	Object getDelegate() throws SystemException;;

	boolean isOpen() throws SystemException;

	void persist(Object object) throws SystemException;

	void remove(Object object) throws SystemException;

	Query createQuery(Class<?> clazz, String query); 

}