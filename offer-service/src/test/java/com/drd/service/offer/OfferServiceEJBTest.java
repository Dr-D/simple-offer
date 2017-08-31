package com.drd.service.offer;

import com.drd.data.entities.OfferEntity;
import com.drd.data.exceptions.EntityNotFoundException;
import com.drd.data.dao.offer.OfferDao;
import com.drd.service.offer.dto.OfferConverter;
import com.drd.service.offer.dto.OfferDto;
import com.drd.service.offer.exceptions.DuplicateNameException;
import com.drd.service.offer.exceptions.UnknownIDException;
import com.drd.service.offer.exceptions.UnknownNameException;
import org.junit.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Author: drd 2017-08-28
 */
public class OfferServiceEJBTest extends OfferTestHelper {
    @Test
    public void shouldCreateOffer() throws Exception {
        //given
        long newID = 1L;
        OfferEntity entity = new OfferEntity(0L, "TestName", "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(entity.getName())).thenThrow(EntityNotFoundException.class);
        when(mockDao.create(entity)).thenReturn(newID);

        OfferConverter mockConverter = mock(OfferConverter.class);
        OfferDto dto = new OfferDto(0L, "TestName", "TestDescription", 1.0F);
        when(mockConverter.convert(dto)).thenReturn(entity);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = mockConverter;


        //when
        long createdID = offerServiceEJB.create(dto);

        //then
        Assert.assertEquals(createdID, newID, "id is '" + createdID + "' should be '" + newID + "'");
    }

    @Test(expected = DuplicateNameException.class)
    public void shouldFailToCreateWithDuplicateName() throws Exception {
        //given
        OfferDto dto = new OfferDto(0L, "TestName", "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(dto.getName())).thenReturn(new OfferEntity());

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;

        //when
        offerServiceEJB.create(dto);
    }


    @Test
    public void shouldListOffers() throws Exception {
        //given
        OfferEntity entity1 = new OfferEntity(1, "TestName", "TestDescription", 1.0F);
        OfferEntity entity2 = new OfferEntity(2, "TestName2", "TestDescription2", 2.0F);
        List<OfferEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.list()).thenReturn(entities);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = new OfferConverter();

        //when
        OfferDto[] list = offerServiceEJB.list();

        //then
        Assert.assertEquals(list.length, entities.size(), "Expected list length: '" + list.length + " to equal entities size: '" + entities.size() + "'");
        offerEntityEqualsDto(entity1, list[0]);
        offerEntityEqualsDto(entity2, list[1]);
    }

    @Test
    public void shouldGetById() throws Exception {
        //given
        long id = 1;
        OfferEntity entity = new OfferEntity(id, "TestName", "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(1)).thenReturn(entity);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = new OfferConverter();

        //when
        OfferDto dto = offerServiceEJB.get(id);

        //then
        offerEntityEqualsDto(entity, dto);
    }

    @Test
    public void shouldGetByName() throws Exception {
        //given
        String name = "TestName";
        OfferEntity entity = new OfferEntity(1, name, "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get("TestName")).thenReturn(entity);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = new OfferConverter();

        //when
        OfferDto dto = offerServiceEJB.get(name);

        //then
        offerEntityEqualsDto(entity, dto);
    }

    @Test
    public void shouldUpdate() throws Exception {
        //given
        String originalName = "TestName";
        OfferEntity originalEntity = new OfferEntity(1L, originalName, "TestDescription", 1.0F);
        OfferEntity updatedEntity = new OfferEntity(1L, "UpdatedName", "UpdatedDescription", 2.0F);


        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(originalName)).thenReturn(originalEntity);
        when(mockDao.update(originalEntity)).thenReturn(updatedEntity);

        OfferConverter mockConverter = mock(OfferConverter.class);
        OfferDto dto = new OfferDto(0L, originalName, "TestDescription", 1.0F);
        when(mockConverter.convert(dto)).thenReturn(originalEntity);
        when(mockConverter.convert(updatedEntity)).thenReturn(new OfferDto(1L, "UpdatedName", "UpdatedDescription", 2.0F));

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = mockConverter;

        //when
        OfferDto updateDto = offerServiceEJB.update(originalName, dto);

        //then
        offerEntityEqualsDto(updatedEntity, updateDto);
    }

    @Test(expected = UnknownNameException.class)
    public void shouldFailToUpdateUnknownName() throws Exception {
        //given
        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get("Unknown")).thenThrow(EntityNotFoundException.class);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;

        //when
        offerServiceEJB.update("Unknown", new OfferDto(0L, null, null, 0F));
    }

    @Test
    public void shouldDeleteByID() throws Exception {
        //given
        OfferEntity entity = new OfferEntity(1L, "TestName", "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(1L)).thenReturn(entity);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = new OfferConverter();

        //when
        offerServiceEJB.delete(1L);

        //then
        verify(mockDao).delete(entity);
    }

    @Test(expected = UnknownIDException.class)
    public void shouldFailToDeleteUnknownID() throws Exception {
        //given
        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(1)).thenReturn(null);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;

        //when
        offerServiceEJB.delete(1);
    }

    @Test
    public void shouldDeleteByName() throws Exception {
        //given
        String name = "TestName";
        OfferEntity entity = new OfferEntity(1L, name, "TestDescription", 1.0F);

        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get(name)).thenReturn(entity);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;
        offerServiceEJB.offerConverter = new OfferConverter();

        //when
        offerServiceEJB.delete(name);

        //then
        verify(mockDao).delete(entity);
    }

    @Test(expected = UnknownNameException.class)
    public void shouldFailToDeleteUnknownName() throws Exception {
        //given
        OfferDao mockDao = mock(OfferDao.class);
        when(mockDao.get("Unknown")).thenThrow(EntityNotFoundException.class);

        OfferServiceEJB offerServiceEJB = new OfferServiceEJB();
        offerServiceEJB.offerDao = mockDao;

        //when
        offerServiceEJB.delete("Unknown");
    }
}