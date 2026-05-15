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
public class Response401DTO {

    private String title;
    private String detail;
    private String instance;
    private String type;

    public Response401DTO() {
    }

    public Response401DTO(String title, String detail, String instance, String type) {
        this.title = title;
        this.detail = detail;
        this.instance = instance;
        this.type = type;
    }
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
   
}
