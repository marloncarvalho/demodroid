package br.gov.frameworkdemoiselle.transaction;

import br.gov.frameworkdemoiselle.internal.exception.SystemException;

/**
 * This interface defines additional methods to it, allowing an application to
 * explicitly manage transaction boundaries.
 * 
 * @author Marlon
 */
public interface Transaction {

	Transaction call(InTransaction inTransaction);

	/**
	 * Indicates whether the given transaction is still active.
	 * 
	 * @return a boolean
	 * @throws SystemException
	 */
	boolean isActive() throws SystemException;

	/**
	 * Indicates whether the given transaction is already marked to be rolled
	 * back.
	 * 
	 * @return a boolean
	 * @throws SystemException
	 */
	boolean isMarkedRollback() throws SystemException;

	/**
	 * Create a new transaction and associate it with the current thread.
	 * 
	 * @throws SystemException
	 */
	void begin() throws SystemException;

	/**
	 * Complete the transaction associated with the current thread. When this
	 * method completes, the thread is no longer associated with a transaction.
	 * 
	 * @throws SystemException
	 */
	void commit() throws SystemException;

	/**
	 * Roll back the transaction associated with the current thread. When this
	 * method completes, the thread is no longer associated with a transaction.
	 * 
	 * @throws SystemException
	 */
	void rollback() throws SystemException;

}
