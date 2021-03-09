/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.document;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public class Definitions {

    private String definition;

    private String example;

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExample() {
        return this.example;
    }
}
