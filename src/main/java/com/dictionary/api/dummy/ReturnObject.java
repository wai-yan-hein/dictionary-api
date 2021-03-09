/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.dummy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author winswe
 */
@Component
@Scope("prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnObject {

    private String status;
    private String message;
    private Object obj;
    private List objList;
    private Object objExt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List getObjList() {
        return objList;
    }

    public void setObjList(List objList) {
        this.objList = objList;
    }

    public Object getObjExt() {
        return objExt;
    }

    public void setObjExt(Object objExt) {
        this.objExt = objExt;
    }
}
