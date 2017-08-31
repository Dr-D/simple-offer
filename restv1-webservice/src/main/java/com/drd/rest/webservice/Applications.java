package com.drd.rest.webservice;

import com.drd.rest.api.OfferResource;
import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: drd 2017-08-25
 */
@ApplicationPath("/rest")
public class Applications extends Application {

    private Set<Class<?>> singletons;

    public Applications() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/restv1/rest");
        beanConfig.setResourcePackage(OfferResource.class.getPackage().getName());
        beanConfig.setTitle("REST API");
        beanConfig.setScan(true);

        singletons = new HashSet<>();
        //internal
        singletons.add(OfferResourceImpl.class);
        //external
        singletons.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        singletons.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return singletons;
    }
}
