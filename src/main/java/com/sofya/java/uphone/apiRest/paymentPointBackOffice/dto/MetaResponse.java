/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto;

/**
 *
 * @author CBARRAGAN
 */
public class MetaResponse {
    private String sequenceCompany;

    public MetaResponse(String sequenceCompany) {
        this.sequenceCompany = sequenceCompany;
    }

    public String getSequenceCompany() {
        return sequenceCompany;
    }

    public void setSequenceCompany(String sequenceCompany) {
        this.sequenceCompany = sequenceCompany;
    }
    
}
