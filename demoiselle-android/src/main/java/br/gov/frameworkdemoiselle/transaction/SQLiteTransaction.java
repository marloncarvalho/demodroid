package br.gov.frameworkdemoiselle.transaction;

import br.gov.frameworkdemoiselle.internal.exception.SystemException;
import br.gov.frameworkdemoiselle.internal.persistence.sqlite.SQLiteDatabaseHelper;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Responsible to manage transactions using SQLite.
 * 
 * @author marlonsilvacarvalho
 * @since 1.0.0
 */
@Singleton
public class SQLiteTransaction implements Transaction {

	@Inject
	private SQLiteDatabaseHelper databaseHelper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.transaction.Transaction#isActive()
	 */
	public boolean isActive() throws SystemException {
		return databaseHelper.getWritableDatabase().inTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.frameworkdemoiselle.transaction.Transaction#isMarkedRollback()
	 */
	public boolean isMarkedRollback() throws SystemException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.transaction.Transaction#begin()
	 */
	public void begin() throws SystemException {
		databaseHelper.getWritableDatabase().beginTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.transaction.Transaction#commit()
	 */
	public void commit() throws SystemException {
		databaseHelper.getWritableDatabase().setTransactionSuccessful();
		databaseHelper.getWritableDatabase().endTransaction();

		if (!databaseHelper.getWritableDatabase().inTransaction()) {
			databaseHelper.getWritableDatabase().close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.transaction.Transaction#rollback()
	 */
	public void rollback() throws SystemException {
		databaseHelper.getWritableDatabase().endTransaction();
		databaseHelper.getWritableDatabase().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.frameworkdemoiselle.transaction.Transaction#call(br.gov.
	 * frameworkdemoiselle.persistence.InTransaction)
	 */
	public Transaction call(InTransaction inTransaction) throws RuntimeException {
		begin();
		try {
			inTransaction.execute();
			commit();
		} catch (Throwable throwable) {
			rollback();
			throw new RuntimeException(throwable);
		}
		return this;
	}

}