package com.drd.service.offer;

import com.drd.service.offer.dto.OfferDto;


/**
 * Author: drd 2017-08-26
 */
public interface OfferService {

    long create(OfferDto offerDto);

    OfferDto[] list();

    OfferDto get(long id);

    OfferDto get(String name);

    OfferDto update(String name, OfferDto offerDto);

    void delete(long id);

    void delete(String name);
}