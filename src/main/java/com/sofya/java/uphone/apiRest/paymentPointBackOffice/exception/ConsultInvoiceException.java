/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception;

import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response400DTO;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author CBARRAGAN
 */
public class ConsultInvoiceException extends Exception {
    
    private Response400DTO resp400;
    private Status status;

    public ConsultInvoiceException(Response400DTO resp400, Status status) {
        this.resp400 = resp400;
        this.status = status;
    }

    public Response400DTO getResp400() {
        return resp400;
    }

    public void setResp400(Response400DTO resp400) {
        this.resp400 = resp400;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

   
    
    
}
