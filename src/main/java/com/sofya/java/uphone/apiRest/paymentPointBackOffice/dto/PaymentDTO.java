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
public class PaymentDTO {
    private Meta meta;
    private DataPayment data;

    public PaymentDTO() {
    }

    public PaymentDTO(Meta meta, DataPayment data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public DataPayment getData() {
        return data;
    }

    public void setData(DataPayment data) {
        this.data = data;
    }
    
    
    
}
