/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author CBARRAGAN
 */
public class DataPayment {

    private String counterpart;
    private Service service;
    private BigDecimal amount;
    private String paymentDate;
    private Integer bankId;
    private List<AditionaDataPayment> aditionalData;
    

    public DataPayment() {
    }

    public DataPayment(String counterpart, Service service, BigDecimal amount, String paymentDate, List<AditionaDataPayment> aditionalData) {
        this.counterpart = counterpart;
        this.service = service;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.aditionalData = aditionalData;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<AditionaDataPayment> getAditionalData() {
        return aditionalData;
    }

    public void setAditionalData(List<AditionaDataPayment> aditionalData) {
        this.aditionalData = aditionalData;
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }
    
}
