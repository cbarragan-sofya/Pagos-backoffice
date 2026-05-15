/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception;

import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response400DTO;
import javax.ws.rs.core.Response;

/**
 *
 * @author CBARRAGAN
 */
public class PaymentInvoiceException extends Exception {
    private Response400DTO resp400;
    private Response.Status status;

    public PaymentInvoiceException(Response400DTO resp400, Response.Status status) {
        this.resp400 = resp400;
        this.status = status;
    }

    public Response400DTO getResp400() {
        return resp400;
    }

    public void setResp400(Response400DTO resp400) {
        this.resp400 = resp400;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    
}
