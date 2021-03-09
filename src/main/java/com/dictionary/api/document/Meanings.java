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
public class Meanings {

    private String partOfSpeech;

    private List<Definitions> definitions;

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPartOfSpeech() {
        return this.partOfSpeech;
    }

    public void setDefinitions(List<Definitions> definitions) {
        this.definitions = definitions;
    }

    public List<Definitions> getDefinitions() {
        return this.definitions;
    }
}
