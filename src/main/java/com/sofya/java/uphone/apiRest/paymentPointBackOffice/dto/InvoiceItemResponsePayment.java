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
public class InvoiceItemResponsePayment {
    private String key;
    private String value;
    private String flag;

    public InvoiceItemResponsePayment() {
    }

    public InvoiceItemResponsePayment(String key, String value, String flag) {
        this.key = key;
        this.value = value;
        this.flag = flag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    
}
