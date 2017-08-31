package com.drd.service.offer.dto;

import com.drd.data.entities.OfferEntity;

/**
 * Convert Dto to Entity and vice versa
 *
 * Author: drd 2017-08-25
 */
public class OfferConverter {

    public OfferDto convert(OfferEntity entity) {
        return new OfferDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice());
    }

    public OfferEntity convert(OfferDto dto) {
        return new OfferEntity(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice());
    }
}
