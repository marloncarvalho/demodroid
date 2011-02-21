package br.gov.frameworkdemoiselle.template;

import java.util.List;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.persistence.Persistence;
import br.gov.frameworkdemoiselle.persistence.annotation.SQLite;
import br.gov.frameworkdemoiselle.util.Reflections;

@SuppressWarnings("serial")
public class SQLiteCrud<T> implements Crud<T, Long> {

	@Inject @SQLite
	private Persistence persistence;

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Class<?> cls = Reflections.getGenericTypeArgument(this.getClass(), 0);
		return (List<T>) persistence.findAll(cls);
	}

	public void insert(T object) {
		persistence.insert(object);
	}
	
	public void update(T object) {
		persistence.update(object);
	}

	public void delete(Long id) {
		Class<?> cls = Reflections.getGenericTypeArgument(this.getClass(), 0);
		persistence.delete(id, cls);
	}

	@SuppressWarnings("unchecked")
	public T load(Long id) {
		Class<?> cls = Reflections.getGenericTypeArgument(this.getClass(), 0);
		return (T) persistence.find(id, cls);
	}

}
