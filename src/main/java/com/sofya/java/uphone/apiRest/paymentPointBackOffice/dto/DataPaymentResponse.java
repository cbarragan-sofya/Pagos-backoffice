/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto;

import com.sofya.java.uphone.backend.dto.PagoListDTO;
import com.sofya.java.uphone.backend.model.Pago;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author CBARRAGAN
 */
public class DataPaymentResponse {
    private String code;
    private BigDecimal amount;
    private String expirationDate;
    private List<InvoiceItemResponsePayment> invoiceItems;
     
     //private List<Pago> pagoList;

    public DataPaymentResponse() {
    }

    public DataPaymentResponse(String code, BigDecimal amount, String expirationDate, List<InvoiceItemResponsePayment> invoiceItems) {
        this.code = code;
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.invoiceItems = invoiceItems;
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

    public List<InvoiceItemResponsePayment> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemResponsePayment> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

   

}
