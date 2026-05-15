/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.controller;

import com.sofya.java.uphone.apiRest.paymentPointBackOffice.ConfigManager;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.Resource;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.AuthDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response401DTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.TokenDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.jwt.JWebToken;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author CBARRAGAN
 */
@Path("partner")
public class AuthController extends Resource {

    public AuthController() {
    }

    private Boolean validateUser(AuthDTO authDTO) {
        Boolean validUser = Boolean.TRUE;
        if (authDTO.getClient_id() == null || authDTO.getClient_id().isEmpty() || authDTO.getClient_secret() == null || authDTO.getClient_secret().isEmpty() || !authDTO.getClient_id().equals(ConfigManager.get("Client_Id")) || !authDTO.getClient_secret().equals(ConfigManager.get("Client_Secret"))) {
            validUser = Boolean.FALSE;
        }
        return validUser;
    }

    @POST
    @Path("v1/oauth/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCuotas(
            @HeaderParam("client_id") String client_id,
            @HeaderParam("client_secret") String client_secret,
            AuthDTO authDTO) {
        authDTO.setClient_id(sanitizeInput(authDTO.getClient_id()));
        authDTO.setClient_secret(sanitizeInput(authDTO.getClient_secret()));
        try {
            if (!validateUser(authDTO)) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Client credentials are invalid", "033", "/v1/oauth/token")).build();
            }
            
            Integer expirationTime = Integer.valueOf(ConfigManager.get("expirationTime"));
            LocalDateTime ldt = LocalDateTime.now().plusSeconds(expirationTime);
            JSONArray audArray = new JSONArray();
            audArray.put(authDTO.getClient_id());

            JSONObject jwtPayload = new JSONObject();
            jwtPayload.put("status", 0);
            jwtPayload.put("sub", authDTO.getClient_id());
            jwtPayload.put("aud", audArray);
            jwtPayload.put("exp", ldt.toEpochSecond(ZoneOffset.UTC));
            String token = new JWebToken(jwtPayload).toString();
            TokenDTO tokenJwt = new TokenDTO(token, expirationTime, "Bearer");
            return response(Response.Status.OK, tokenJwt);

        } catch (Exception e) {
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(e.getMessage()).build();
        }

    }
}
