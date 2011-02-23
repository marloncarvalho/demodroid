package br.gov.frameworkdemoiselle.template;

import java.util.List;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.inject.Provider;

public class DelegateCrud<T, C extends Crud<T>> implements Crud<T> {

	private static final long serialVersionUID = 1L;

	private Class<C> delegateClass;

	private C delegate;

	@Inject
	private Provider<C> provider;

	public void delete(final long id) {
		this.getDelegate().delete(id);
	}

	public List<T> findAll() {
		return getDelegate().findAll();
	}

	protected C getDelegate() {
		if (this.delegate == null) {
			this.delegate = provider.get();
		}
		return this.delegate;
	}

	protected Class<C> getDelegateClass() {
		if (this.delegateClass == null) {
			this.delegateClass = Reflections.getGenericTypeArgument(this.getClass(), 2);
		}
		return this.delegateClass;
	}

	public void insert(final T bean) {
		getDelegate().insert(bean);
	}

	public T find(final long id) {
		return getDelegate().find(id);
	}

	public void update(final T bean) {
		getDelegate().update(bean);
	}

}
