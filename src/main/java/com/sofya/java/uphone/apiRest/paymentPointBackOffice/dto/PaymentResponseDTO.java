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
public class PaymentResponseDTO {

    private MetaResponse meta;
    private List<DataPaymentResponse> data;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(MetaResponse meta, List<DataPaymentResponse> data) {
        this.meta = meta;
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }

    public List<DataPaymentResponse> getData() {
        return data;
    }

    public void setData(List<DataPaymentResponse> data) {
        this.data = data;
    }

}
