package com.drd.data.dao.offer;

import com.drd.data.entities.OfferEntity;
import com.drd.data.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Author: drd 2017-08-25
 */
public class OfferDaoImpl implements OfferDao {

    private static final Logger log = LoggerFactory.getLogger(OfferDaoImpl.class);

    @PersistenceContext(unitName = "offer")
    protected EntityManager entityManager;

    @Override
    public long create(OfferEntity entity) {
        log.trace("Persisting entity [{}]", entity);
        entityManager.persist(entity);
        log.debug("Persisted entity id [{}]", entity.getId());
        return entity.getId();
    }

    @Override
    public List<OfferEntity> list() {
        log.debug("Getting entities");
        List<OfferEntity> results = entityManager.createQuery("SELECT o FROM OfferEntity o").getResultList();
        log.trace("Got entities: [{}]", results);
        return results;
    }

    @Override
    public OfferEntity get(long id) {
        log.debug("Get entity by id [{}]", id);
        return entityManager.find(OfferEntity.class, id);
    }

    @Override
    public OfferEntity get(String name) {
        log.debug("Get entity by name [{}]", name);
        Query query = entityManager.createQuery("SELECT o FROM OfferEntity o WHERE o.name = :name");
        query.setParameter("name", name);

        OfferEntity result;
        try {
            result = (OfferEntity) query.getSingleResult();
        } catch (NoResultException nre) {
            log.error("No entity found with name [{}]", name);
            throw new EntityNotFoundException("Get entity by name returned no result", nre);
        }
        log.trace("Get returning entity: [{}]", result);
        return result;
    }

    @Override
    public OfferEntity update(OfferEntity offerEntity) {
        log.trace("Updating entity [{}]", offerEntity);
        return entityManager.merge(offerEntity);
    }

    @Override
    public void delete(OfferEntity offerEntity) {
        log.trace("Removing entity [{}]", offerEntity);
        entityManager.remove(offerEntity);
    }
}
