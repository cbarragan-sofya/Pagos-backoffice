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
public class Error {
    private String message;
    private String businessMessage;

    public Error() {
    }

    public Error(String message, String businessMessage) {
        this.message = message;
        this.businessMessage = businessMessage;
    }
    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }

    public void setBusinessMessage(String businessMessage) {
        this.businessMessage = businessMessage;
    }
}
