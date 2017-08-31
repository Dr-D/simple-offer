package com.drd.rest.converters;

import com.drd.rest.model.OfferRest;
import com.drd.service.offer.dto.OfferDto;

/**
 * Author: drd 2017-08-26
 */
public class OfferConverterRest {

    public OfferRest convert(OfferDto dto) {
        return new OfferRest(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice());
    }

    public OfferDto convert(OfferRest offerRest) {
        return new OfferDto(offerRest.getId(), offerRest.getName(), offerRest.getDescription(), offerRest.getPrice());
    }
}
