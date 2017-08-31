package com.drd.rest.webservice;

import com.drd.rest.api.OfferResource;
import com.drd.rest.api.RestAPIConstants;
import com.drd.rest.converters.OfferConverterRest;
import com.drd.rest.model.OfferRest;
import com.drd.rest.model.StructuredError;
import com.drd.service.offer.OfferService;
import com.drd.service.offer.dto.OfferDto;
import com.drd.service.offer.exceptions.DuplicateNameException;
import com.drd.service.offer.exceptions.UnknownIDException;
import com.drd.service.offer.exceptions.UnknownNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Author: drd 2017-08-25
 */
public class OfferResourceImpl implements OfferResource, RestAPIConstants {
    private static final Logger log = LoggerFactory.getLogger(OfferResourceImpl.class);

    @EJB
    OfferService offerService;

    @Inject
    OfferConverterRest converter;

    @Override
    public Response create(OfferRest offer) {
        log.info("Creating offer");
        log.trace("Creating offer[{}]", offer);

        if (offer.getId() != 0) {
            StructuredError structuredError = new StructuredError(Response.Status.BAD_REQUEST.getStatusCode(), "Error Creating offer, id should be zero");
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }

        try {
            long id = offerService.create(converter.convert(offer));
            return Response.created(new URI("/offer/name/" + offer.getName())).status(Response.Status.CREATED).entity(id).build();
        } catch (DuplicateNameException dne) {
            StructuredError structuredError = new StructuredError(DUPLICATE_NAME, dne.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        } catch (URISyntaxException use) {
            use.printStackTrace();
            StructuredError structuredError = new StructuredError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error with URI syntax");
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }
    }

    @Override
    public Response list() {
        log.info("Listing offers");
        OfferRest[] offerRests = Arrays.stream(offerService.list()).map(dto -> converter.convert(dto)).toArray(OfferRest[]::new);
        return Response.ok().entity(offerRests).build();
    }

    @Override
    public Response get(long id) {
        log.info("Getting offer by id [{}]", id);

        OfferDto offerDto;
        try {
            offerDto = offerService.get(id);
        } catch (UnknownIDException uide) {
            StructuredError structuredError = new StructuredError(UNKNOWN_ID, uide.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }

        OfferRest offer = converter.convert(offerDto);
        return Response.ok().entity(offer).build();
    }

    @Override
    public Response get(String name) {
        log.info("Getting offer by name [{}]", name);
        OfferDto offerDto;
        try {
            offerDto = offerService.get(name);
        } catch (UnknownNameException une) {
            StructuredError structuredError = new StructuredError(UNKNOWN_NAME, une.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }

        OfferRest offer = converter.convert(offerDto);
        return Response.ok().entity(offer).build();
    }

    /**
     * Update an offer requires the name of the offer being update
     *
     * @param name  the offer that is being updated
     * @param offer the updated offer
     * @return OK Response contains the location of the entity
     */
    @Override
    public Response update(String name, OfferRest offer) {
        log.info("Updating offer with name: [{}]", name);
        log.trace("Updating offer : [{}]", offer);
        OfferDto updatedOffer;
        try {
            updatedOffer = offerService.update(name, converter.convert(offer));
            return Response.created(new URI("/offer/name/" + offer.getName())).status(Response.Status.OK).entity(converter.convert(updatedOffer)).build();
        } catch (UnknownNameException une) {
            StructuredError structuredError = new StructuredError(UNKNOWN_NAME, une.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        } catch (DuplicateNameException dne) {
            StructuredError structuredError = new StructuredError(DUPLICATE_NAME, dne.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        } catch (URISyntaxException use) {
            use.printStackTrace();
            StructuredError structuredError = new StructuredError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error with URI syntax");
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }
    }

    @Override
    public Response delete(long id) {
        log.info("Deleting offer by id [{}]", id);
        try {
            offerService.delete(id);
        } catch (UnknownIDException une) {
            StructuredError structuredError = new StructuredError(UNKNOWN_ID, une.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }
        return Response.ok().build();
    }

    @Override
    public Response delete(String name) {
        log.info("Deleting offer by name [{}]", name);
        try {
            offerService.delete(name);
        } catch (UnknownNameException une) {
            StructuredError structuredError = new StructuredError(UNKNOWN_NAME, une.getMessage());
            return Response.status(structuredError.getStatus()).entity(structuredError).build();
        }
        return Response.ok().build();
    }
}