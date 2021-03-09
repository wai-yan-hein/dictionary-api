/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dictionary.api.dao;

import com.dictionary.api.document.EnglishWord;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Aggregates.sample;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.codecs.PatternCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Meta;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public class WordDaoImpl implements WordDao {

    private static final Logger log = LoggerFactory.getLogger(WordDaoImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public EnglishWord saveWord(EnglishWord word) {
        return mongoTemplate.save(word);
    }

    @Override
    public void saveWordList(List<EnglishWord> listWords) {
        listWords.forEach(word -> {
            mongoTemplate.save(word);
        });
    }

    @Override
    public List<EnglishWord> getAutoCompletList(String word) {
        /*BasicDBObject query = new BasicDBObject();
        query.put("_id", word);*/
        PageRequest pageRequest = PageRequest.of(0, 6);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").regex("^" + word + ""));
        query.with(pageRequest);
        return mongoTemplate.find(query, EnglishWord.class);
    }
}
