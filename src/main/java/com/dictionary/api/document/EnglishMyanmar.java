/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Lenovo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document("english_myanmar")
public class EnglishMyanmar implements Serializable {

    @Id
    private String word;

    private List<Phonetics> phonetics;

    private List<Meanings> meanings;

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

    public void setPhonetics(List<Phonetics> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Phonetics> getPhonetics() {
        return this.phonetics;
    }

    public void setMeanings(List<Meanings> meanings) {
        this.meanings = meanings;
    }

    public List<Meanings> getMeanings() {
        return this.meanings;
    }
}
