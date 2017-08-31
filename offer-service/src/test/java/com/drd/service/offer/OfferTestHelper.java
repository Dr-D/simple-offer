package com.drd.service.offer;

import com.drd.data.entities.OfferEntity;
import com.drd.service.offer.dto.OfferDto;
import org.testng.Assert;

/**
 * Author: drd 2017-08-29
 */
public abstract class OfferTestHelper {

    public void offerEntityEqualsDto(OfferEntity entity, OfferDto dto) {
        Assert.assertEquals(dto.getId(), entity.getId(), " Dto/Entity ID should be equal");
        Assert.assertEquals(dto.getName(), entity.getName(), " Dto/Entity name should be equal");
        Assert.assertEquals(dto.getDescription(), entity.getDescription(), " Dto/Entity description should be equal");
        Assert.assertEquals(dto.getPrice(), entity.getPrice(), " Dto/Entity price should be equal");
    }
}
