/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author CBARRAGAN
 */
@Provider
public class IPFilter implements ContainerRequestFilter {

    private static final String ALLOWED_IP = "192.168.1.100"; // Cambia esto a la IP permitida

    @Context
    private HttpServletRequest httpRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String clientIp = getClientIp();
        /*System.out.println("Negocio efectivo");
        System.out.println(clientIp);
         if (!ALLOWED_IP.equals(clientIp)) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("Acceso denegado. Tu IP no está autorizada.")
                    .build());
        }*/
    }

    private String getClientIp() {
        // Obtener la IP del encabezado X-Forwarded-For si está presente
        String forwardedFor = httpRequest.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            // La IP original del cliente está en la primera posición
            return forwardedFor.split(",")[0].trim();
        }
        // Si no hay encabezado, usar getRemoteAddr()
        return httpRequest.getRemoteAddr();
    }
}
