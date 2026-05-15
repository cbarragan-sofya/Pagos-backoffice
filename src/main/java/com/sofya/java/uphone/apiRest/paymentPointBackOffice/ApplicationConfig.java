/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author CBARRAGAN
 */
@javax.ws.rs.ApplicationPath("/uphoneAPI")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.CorsFilter.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.ExceptionMapper.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.GenericExceptionMapper.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.IPFilter.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.ObjectMapperProvider.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.RateLimitingFilter.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.controller.AuthController.class);
        resources.add(com.sofya.java.uphone.apiRest.paymentPointBackOffice.controller.InvoiceController.class);
    }

}
