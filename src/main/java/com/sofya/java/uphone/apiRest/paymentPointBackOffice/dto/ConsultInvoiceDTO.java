/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto;

import java.util.List;

/**
 *
 * @author CBARRAGAN
 */
public class ConsultInvoiceDTO {
    private String counterpart;
    private List<AditionalDataConsult> aditionalData;

    public ConsultInvoiceDTO() {
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }

    public List<AditionalDataConsult> getAditionalData() {
        return aditionalData;
    }

    public void setAditionalData(List<AditionalDataConsult> aditionalData) {
        this.aditionalData = aditionalData;
    }
    
}
