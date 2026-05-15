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
public class InvoiceDTO {
    private Meta meta;
    private List<DataConsult> data;

    public InvoiceDTO() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<DataConsult> getData() {
        return data;
    }

    public void setData(List<DataConsult> data) {
        this.data = data;
    }

}
