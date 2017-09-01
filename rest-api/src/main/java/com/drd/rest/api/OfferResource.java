package com.drd.rest.api;

import com.drd.rest.model.OfferRest;
import com.drd.rest.model.StructuredError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Author: drd 2017-08-25
 */
@Path("/offer")
@Api(value = "offer", description = "offer resource")
public interface OfferResource {
    @ApiOperation("Creates an offer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful creation of offer", response = OfferRest.class),
            @ApiResponse(code = 460, message = "Duplicate Name", response = StructuredError.class)})
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    Response create(OfferRest offer);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful list offer resources", response = OfferRest.class)})
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    Response list();

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful get offer resource", response = OfferRest.class),
            @ApiResponse(code = 461, message = "Unknown name", response = StructuredError.class)})
    @GET
    @Path("/id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    Response get(@PathParam("id") long id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful get offer resource", response = OfferRest.class),
            @ApiResponse(code = 462, message = "Unknown ID", response = StructuredError.class)})
    @GET
    @Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    Response get(@PathParam("name") String name);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful update offer resource", response = OfferRest.class),
            @ApiResponse(code = 461, message = "Unknown name", response = StructuredError.class)})
    @PUT
    @Path("/{name}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    Response update(@PathParam("name") String name, OfferRest offer);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful delete offer resource", response = OfferRest.class),
            @ApiResponse(code = 462, message = "Unknown ID", response = StructuredError.class)})
    @DELETE
    @Path("/id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    Response delete(@PathParam("id") long id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful delete offer resource", response = OfferRest.class),
            @ApiResponse(code = 461, message = "Unknown name", response = StructuredError.class)})
    @DELETE
    @Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    Response delete(@PathParam("name") String name);
}
