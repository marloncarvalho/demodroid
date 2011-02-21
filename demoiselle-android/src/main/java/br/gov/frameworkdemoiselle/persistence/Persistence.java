package br.gov.frameworkdemoiselle.persistence;

import java.util.List;

public interface Persistence {

	public abstract void insert(Object object);

	public abstract void update(Object object);

	public abstract void delete(Long id, Class<?> cls);

	public abstract Object find(Long id, Class<?> cls);
	
	public List<?> findAll(Class<?> cls);

}