/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response401DTO;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author CBARRAGAN
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        String requestPath = uriInfo.getRequestUri().toString();
        return Response.serverError().status(exception.getResponse().getStatus()).
                type(MediaType.APPLICATION_JSON).
                entity(new Response401DTO("Resource not found", "No se pudo encontrar el recurso solicitado", "044", requestPath)).build();

    }
    
}

