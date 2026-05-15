/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author CBARRAGAN
 */
public class DataConsult {

    private String code;
    private BigDecimal amount;
    private String expirationDate;
    private List<InvoiceItemConsult> invoiceItems;

    public DataConsult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<InvoiceItemConsult> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemConsult> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }
    
}
