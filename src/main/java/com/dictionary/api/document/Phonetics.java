/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.document;

/**
 *
 * @author Lenovo
 */
public class Phonetics {

    private String text;

    private String audio;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }
}
