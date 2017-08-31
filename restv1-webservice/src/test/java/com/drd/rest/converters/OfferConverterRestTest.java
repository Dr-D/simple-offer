package com.drd.rest.converters;

import com.drd.rest.model.OfferRest;
import com.drd.service.offer.dto.OfferDto;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Author: darren 2017-08-31
 */
public class OfferConverterRestTest {
    @Test
    public void convertsDtoToRestModel() throws Exception {
        //given
        OfferConverterRest converterRest = new OfferConverterRest();
        OfferDto dto = new OfferDto(1L, "Test Name", "Test Description", 1.0F);

        //when
        OfferRest rest = converterRest.convert(dto);

        //then
        offerDtoEqualsRestModel(dto, rest);
    }

    @Test
    public void convertsRestModelToDto() throws Exception {
        //given
        OfferConverterRest offerConverter = new OfferConverterRest();
        OfferRest rest = new OfferRest(1L, "Test Name", "Test Description", 1.0F);

        //when
        OfferDto entity = offerConverter.convert(rest);

        //then
        offerDtoEqualsRestModel(entity, rest);
    }

    private void offerDtoEqualsRestModel(OfferDto dto, OfferRest rest) {
        Assert.assertEquals(rest.getId(), dto.getId(), " Dto/Entity ID should be equal");
        Assert.assertEquals(rest.getName(), dto.getName(), " Dto/Entity name should be equal");
        Assert.assertEquals(rest.getDescription(), dto.getDescription(), " Dto/Entity description should be equal");
        Assert.assertEquals(rest.getPrice(), dto.getPrice(), " Dto/Entity price should be equal");
    }
}