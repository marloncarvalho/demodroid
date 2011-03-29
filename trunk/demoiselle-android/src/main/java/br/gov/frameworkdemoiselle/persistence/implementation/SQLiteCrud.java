package br.gov.frameworkdemoiselle.persistence.implementation;

import java.util.List;

import android.util.Log;
import br.gov.frameworkdemoiselle.annotation.SQLite;
import br.gov.frameworkdemoiselle.internal.persistence.PersistenceInspector;
import br.gov.frameworkdemoiselle.persistence.EntityManager;
import br.gov.frameworkdemoiselle.persistence.Query;
import br.gov.frameworkdemoiselle.template.Crud;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Inject;

public class SQLiteCrud<E> implements Crud<E> {
	private static final long serialVersionUID = 1L;
	private Class<E> clasz;

	@Inject
	@SQLite
	private EntityManager entityManager;

	@Inject
	private PersistenceInspector inspector;
	
	public SQLiteCrud() {
		clasz = Reflections.getGenericTypeArgument(getClass(), 0);
	}

	public void insert(E object) {
		Log.d("Persistence", "Inserting object");
		entityManager.persist(object);
	}

	public void delete(Object object) {
		Log.d("Persistence", "Deleting object");
		entityManager.remove(object);
	}

	public void update(E object) {
		Log.d("Persistence", "Updating object");
		entityManager.merge(object);
	}

	public E find(long id) {
		Log.d("Persistence", "Find Entity");
		return entityManager.find(clasz, id);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		Log.d("Persistence", "Finding all entities");
		Query query = entityManager.createQuery(clasz, "select * from " + inspector.getTableName(clasz));
		return (List<E>) query.getResultList();
	}

}