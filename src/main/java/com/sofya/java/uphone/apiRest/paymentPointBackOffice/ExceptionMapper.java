/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response401DTO;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author CBARRAGAN
 */
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    @Context
    private UriInfo uriInfo;
    @Override
    public Response toResponse(Throwable exception) {
        String requestPath = uriInfo.getRequestUri().toString();
        return Response.serverError().status(400).
                type(MediaType.APPLICATION_JSON).
                entity(new Response401DTO("Error", exception.getMessage(), "044", requestPath)).build();
    }
}