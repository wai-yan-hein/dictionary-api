/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.dao;

import com.dictionary.api.document.EnglishWord;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author Lenovo
 */
public interface WordDao {

    public EnglishWord saveWord(EnglishWord word);

    public void saveWordList(List<EnglishWord> listWords);

    public List<EnglishWord> getAutoCompletList(String word);
}
