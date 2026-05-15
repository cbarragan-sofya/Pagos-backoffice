/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception;

import javax.ws.rs.core.Response;

/**
 *
 * @author CBARRAGAN
 */
public class UphoneExceptionApiRest extends Exception {

    private String code;
    private String message;
    private Response.Status status;

    public UphoneExceptionApiRest(String code, String message, Response.Status status) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public UphoneExceptionApiRest(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

}
