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
public class Response400DTO {

    private String title;
    private String detail;
    private List<Error> errors;
    private String instance;
    private String type;

    public Response400DTO() {
    }

    public Response400DTO(String title, String detail, List<Error> errors, String instance, String type) {
        this.title = title;
        this.detail = detail;
        this.errors = errors;
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

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
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
