package org.woehlke.twitterwall.oodm.repository.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woehlke.twitterwall.oodm.entities.common.DomainObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tw on 28.06.17.
 */
public class DomainRepositoryImpl<T extends DomainObject> implements DomainRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(DomainRepositoryImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public T persist(T domainObject) {
        log.debug("try to persist: "+domainObject.toString());
        entityManager.persist(domainObject);
        entityManager.flush();
        log.debug("persisted: "+domainObject.toString());
        return domainObject;
    }

    @Override
    public T update(T domainObject) {
        log.debug("try to merge: "+domainObject.toString());
        domainObject = entityManager.merge(domainObject);
        entityManager.flush();
        log.debug("merged: "+domainObject.toString());
        return domainObject;
    }

    @Override
    public List<T> getAll(Class<T> myClass) {
        String name = myClass.getSimpleName() + ".getAll";
        log.debug(name);
        TypedQuery<T> query = entityManager.createNamedQuery(name, myClass);
        List<T> result = query.getResultList();
        log.debug(name+"  "+result.size());
        return result;
    }

    @Override
    public long count(Class<T> myClass) {
        String name = myClass.getSimpleName() + ".count";
        log.debug(name);
        TypedQuery<Long> query = entityManager.createNamedQuery(name, Long.class);
        long result = query.getSingleResult();
        log.debug(name+"  "+result);
        return result;
    }
}
