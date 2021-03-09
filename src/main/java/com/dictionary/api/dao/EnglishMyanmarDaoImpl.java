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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public class EnglishMyanmarDaoImpl implements EnglishMyanmarDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(List<EnglishMyanmar> list) {
        for (EnglishMyanmar em : list) {
            EnglishMyanmar findById = mongoTemplate.findById(em.getWord(), EnglishMyanmar.class);
            if (findById == null) {
                mongoTemplate.save(em);
            }
        }
    }

    @Override
    public void saveDictionary(List<Dictionary> listD) {
        for (Dictionary dic : listD) {
            mongoTemplate.save(dic);
        }
    }

    @Override
    public List<Dictionary> searchDic(String word, String state) {
        Query query = new Query();
        query.addCriteria(Criteria.where("word").is(word).andOperator(Criteria.where("state").is(state)));
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findAllDictionary() {
        return mongoTemplate.findAll(Dictionary.class);
    }

    @Override
    public EnglishMyanmar searchDictionray(String word) {
        return mongoTemplate.findById(word, EnglishMyanmar.class);
    }

    @Override
    public List<EnglishWord> findAllEnglishWord() {
        return mongoTemplate.findAll(EnglishWord.class);
    }

    @Override
    public void save(EnglishMyanmar em) {
        mongoTemplate.save(em);
    }

}
