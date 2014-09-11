package meridian.lib;

import java.io.Serializable;

import javax.persistence.*;

public class AbstractRepository {
    @PersistenceContext
    protected EntityManager em;
    
    public <T> T attach(T entity) {
    	if (entity == null)
    		return null;
    	return em.merge(entity);
    }
    
    public EntityManager getEntityManager() {
    	return em;
    }
    
    protected <T> T getEntity(Class<T> klass, Serializable id) {
    	T entity = em.find(klass, id);
    	if (entity == null) {
    		throw new NoResultException("No " + klass.getName() + " found with id " + id);
    	}
    	return entity;
    }
}
