package br.gov.frameworkdemoiselle.persistence;

import java.util.List;

public interface Query {

	void setMaxResults(int maxResults);

	void setParameter(int position, Object value);

	void setFirstResult(int firstResult);

	List<?> getResultList();

	Object getRawResult();

	int executeUpdate();
}
