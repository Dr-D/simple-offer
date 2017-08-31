package com.drd.service.offer;

import com.drd.data.entities.OfferEntity;
import com.drd.data.exceptions.EntityNotFoundException;
import com.drd.data.dao.offer.OfferDao;
import com.drd.service.offer.dto.OfferConverter;
import com.drd.service.offer.dto.OfferDto;
import com.drd.service.offer.exceptions.DuplicateNameException;
import com.drd.service.offer.exceptions.UnknownIDException;
import com.drd.service.offer.exceptions.UnknownNameException;
import com.drd.service.offer.exceptions.UnknownServerException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

/**
 * CRUD Service for Offer
 * <p>
 * Author: drd 2017-08-25
 */
@Stateless
public class OfferServiceEJB implements OfferService {

    private static final Logger log = LoggerFactory.getLogger(OfferServiceEJB.class);

    @Inject
    OfferDao offerDao;

    @Inject
    OfferConverter offerConverter;

    public long create(OfferDto offerDto) {
        log.trace("Creating entity from offerDto: {}", offerDto);

        //Consider catching PersistenceException from entityManager.persist() instead
        //Or adding offerDao.countByName(...)
        try {
            offerDao.get(offerDto.getName());
        } catch (EntityNotFoundException enfe) {
            OfferEntity entity = offerConverter.convert(offerDto);
            long id = offerDao.create(entity);
            log.debug("Created entity with id [{}]", id);
            return id;
        }

        log.error("Constraint violation creating offer ");
        throw new DuplicateNameException("Create failed, check offer name/id is unique");
    }

    @Override
    public OfferDto[] list() {
        log.debug("Getting entities");
        return offerDao.list().stream().map(e -> offerConverter.convert(e)).toArray(OfferDto[]::new);
    }

    @Override
    public OfferDto get(long id) {
        log.debug("Getting offer by id: {}", id);
        OfferEntity offerEntity = offerDao.get(id);
        if (offerEntity == null) {
            log.error("Error getting offer by id");
            throw new UnknownIDException(String.format("Get could not find offer with id: '%d'", id));
        }

        return offerConverter.convert(offerEntity);
    }

    @Override
    public OfferDto get(String name) {
        log.debug("Getting offer by name: {}", name);

        OfferEntity offerEntity;
        try {
            offerEntity = offerDao.get(name);

        } catch (EntityNotFoundException enfe) {
            log.error("Error getting offer by name");
            throw new UnknownNameException(String.format("Get could not find offer with name: '%s'", name));
        }

        return offerConverter.convert(offerEntity);
    }

    @Override
    public OfferDto update(String name, OfferDto offerDto) {
        log.debug("Updating offer with name: {}", name);

        OfferEntity offerEntityOrig;
        try {
            offerEntityOrig = offerDao.get(name);
        } catch (EntityNotFoundException enfe) {
            throw new UnknownNameException(String.format("Update could not find offer with name: '%s'", name));
        }

        offerDto.setId(offerEntityOrig.getId());

        OfferEntity offerEntity;
        try {
            offerEntity = offerDao.update(offerConverter.convert(offerDto));
        } catch (PersistenceException ex) {
            Throwable cause = ex.getCause();
            if (cause.getClass() == ConstraintViolationException.class) {
                log.debug("Update offer failed due to constraint violation");
                throw new DuplicateNameException("Update failed, check offer name is unique");
            } else {
                throw new UnknownServerException("Update failed due to unknown server error");
            }
        }

        log.debug("Merging OfferEntity : {}", offerEntity);
        return offerConverter.convert(offerEntity);
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting offer by id: {}", id);

        OfferEntity offerEntity = offerDao.get(id);
        if (offerEntity == null) {
            log.error("Delete could not find offer by id");
            throw new UnknownIDException(String.format("Delete could not find offer with id: '%d'", id));
        }

        offerDao.delete(offerEntity);
    }

    @Override
    public void delete(String name) {
        log.debug("Deleting offer by name: {}", name);

        OfferEntity offerEntity;
        try {
            offerEntity = offerDao.get(name);
        } catch (EntityNotFoundException enfe) {
            log.error("Delete could not find offer by name");
            throw new UnknownNameException(String.format("Delete could not find offer with name: '%s'", name));
        }

        offerDao.delete(offerEntity);
    }
}
