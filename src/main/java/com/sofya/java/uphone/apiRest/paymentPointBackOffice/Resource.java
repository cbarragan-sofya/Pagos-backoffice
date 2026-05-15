/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import com.google.gson.Gson;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception.UphoneExceptionApiRest;
import javax.ws.rs.core.Response;
import sofya.com.java.sesion.SesionControl;
import sofya.com.java.sesion.SesionException;

/**
 *
 * @author CBARRAGAN
 */
public class Resource {
    protected Gson gson;
    public Resource() {
        gson = new Gson();
    }

    protected Response response(Integer status, Object entity) {
        return Response.status(status).entity(gson.toJson(entity)).build();
    }

    protected Response response(Response.Status status, Object entity) {
        return Response.status(status).entity(entity).build();
    }

    protected Response responseGson(Response.Status status, Object entity) {
        return Response.status(status).entity(gson.toJson(entity)).build();
    }
    protected static String sanitizeInput(String input) {
        // Permitir solo letras, números y puntos
        return input.replaceAll("[^a-zA-Z0-9.]", "");
    }
    
    protected static String sanitizeInputOnlyNumber(String input) {
        // Permitir solo letras, números y puntos
        return input.replaceAll("[^0-9]", "");
    }
    protected Boolean checkSessionSIC(String userName, String sessionId) throws UphoneExceptionApiRest {
        Boolean sessionFlag=Boolean.FALSE;
        try {
            new SesionControl().autorizarAcceso(userName, sessionId);
            sessionFlag=Boolean.TRUE;
        } catch (SesionException e) {
            
        }
        return sessionFlag;
    }
}
