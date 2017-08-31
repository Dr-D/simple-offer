package com.drd.rest.webservice;

import com.drd.rest.api.OfferResource;
import com.drd.rest.converters.OfferConverterRest;
import com.drd.rest.model.OfferRest;
import com.drd.service.offer.OfferService;
import com.drd.service.offer.dto.OfferDto;
import com.drd.service.offer.exceptions.UnknownNameException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.Hashtable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests REST api using an in memory server
 * Does not add anything to IntegrationTests might be useful for tdd
 *
 * Author: drd 2017-08-29
 */
public class OfferResourceImplTest {

    @Test
    public void testGet() {
        //given
        OfferDto dto = new OfferDto(1L, "TestName", "TestDescription", 1.0F);

        OfferService offerService = mock(OfferService.class);
        when(offerService.list()).thenReturn(new OfferDto[]{dto});

        OfferResourceImpl offerResource = new OfferResourceImpl();
        offerResource.offerService = offerService;
        offerResource.converter = new OfferConverterRest();

        TJWSEmbeddedJaxrsServer server = initServer(offerResource);

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8081/offer");
        Response response = target.request().get();

        OfferRest[] value = response.readEntity(OfferRest[].class);
        response.close();
        Assert.assertEquals(value[0].getName(), dto.getName());

        server.stop();
    }

    private TJWSEmbeddedJaxrsServer initServer(OfferResource offerResource) {
        TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer();
        server.setPort(8081);
        server.setBindAddress("127.0.0.1");
        server.getDeployment().getResources().add(offerResource);
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("content-type", "application/json");
        server.setContextParameters(hashtable);
        server.start();
        return server;
    }

}