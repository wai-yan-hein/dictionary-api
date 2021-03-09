/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.document;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 *
 * @author Lenovo
 */
@Document("eng-word")
public class EnglishWord {
    @Id
    private String id;
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public EnglishWord(String id, Date createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }


    
}
