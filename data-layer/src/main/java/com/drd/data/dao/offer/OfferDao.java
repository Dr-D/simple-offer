package com.drd.data.dao.offer;

import com.drd.data.entities.OfferEntity;

import java.util.List;

/**
 * Author: drd 2017-08-26
 */
public interface OfferDao {
    long create(OfferEntity entity);

    List<OfferEntity> list();

    OfferEntity get(long id);

    OfferEntity get(String name);

    OfferEntity update(OfferEntity offerEntity);

    void delete(OfferEntity entity);
}
