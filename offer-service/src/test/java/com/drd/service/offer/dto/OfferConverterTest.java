package com.drd.service.offer.dto;

import com.drd.data.entities.OfferEntity;
import com.drd.service.offer.OfferTestHelper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Author: drd 2017-08-29
 */
public class OfferConverterTest extends OfferTestHelper {

    @Test
    public void convertsEntityToDto() throws Exception {
        //given
        OfferConverter offerConverter = new OfferConverter();
        OfferEntity entity = new OfferEntity(1L, "Test Name", "Test Description", 1.0F);

        //when
        OfferDto dto = offerConverter.convert(entity);

        //then
        offerEntityEqualsDto(entity, dto);
    }

    @Test
    public void convertsDtoToEntity() throws Exception {
        //given
        OfferConverter offerConverter = new OfferConverter();
        OfferDto dto = new OfferDto(1L, "Test Name", "Test Description", 1.0F);

        //when
        OfferEntity entity = offerConverter.convert(dto);

        //then
        offerEntityEqualsDto(entity, dto);
    }
}