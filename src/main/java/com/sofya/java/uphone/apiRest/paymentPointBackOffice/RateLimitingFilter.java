/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

/**
 *
 * @author CBARRAGAN
 */
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response401DTO;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.function.Function;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Provider
public class RateLimitingFilter implements ContainerRequestFilter {
    // REQUEST_LIMIT número de peticiones en TIME_WINDOW(segundos)
    private static final long REQUEST_LIMIT = 1000;
    private static final long TIME_WINDOW = 60;
    private static final ConcurrentHashMap<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null || authHeader.isEmpty()) {
                authHeader = "auth";
            }

            RequestCounter counter = requestCounts.computeIfAbsent(authHeader, new Function<String, RequestCounter>() {
                @Override
                public RequestCounter apply(String k) {
                    return new RequestCounter();
                }
            });

            if (counter.incrementAndCheckLimit()) {
                String requestPath = uriInfo.getRequestUri().toString();
                requestContext.abortWith(Response.serverError().status(429).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Too many requests”", "Too many requests in a short time, try again in 1 minute.", "010", requestPath)).build());
            }
        } catch (Exception e) {
            requestContext.abortWith(Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/oauth/token")).build());
        }

    }

    private static class RequestCounter {

        private long count;
        private long lastRequestTime;

        public synchronized boolean incrementAndCheckLimit() {
            long now = System.currentTimeMillis() / 1000;

            if (now - lastRequestTime > TIME_WINDOW) {
                count = 1;
                lastRequestTime = now;
                return false;
            }

            count++;
            return count > REQUEST_LIMIT;
        }
    }

    public static class ErrorResponse {

        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }
}
