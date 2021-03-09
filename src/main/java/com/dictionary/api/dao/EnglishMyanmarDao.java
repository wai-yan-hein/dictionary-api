/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.dao;

import com.dictionary.api.document.EnglishMyanmar;
import com.dictionary.api.document.EnglishWord;
import com.dictionary.api.dummy.Dictionary;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface EnglishMyanmarDao {

    public void save(List<EnglishMyanmar> list);

    public void save(EnglishMyanmar em);

    public EnglishMyanmar searchDictionray(String word);

    public void saveDictionary(List<Dictionary> listD);

    public List<Dictionary> searchDic(String word, String state);

    public List<Dictionary> findAllDictionary();

    public List<EnglishWord> findAllEnglishWord();

}
