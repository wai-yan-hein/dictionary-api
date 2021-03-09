package com.dictionary.api.controller;

import com.dictionary.api.dao.EnglishMyanmarDao;
import com.dictionary.api.dao.WordDao;
import com.dictionary.api.document.Definitions;
import com.dictionary.api.document.EnglishMyanmar;
import com.dictionary.api.document.EnglishWord;
import com.dictionary.api.document.Meanings;
import com.dictionary.api.dummy.Dictionary;
import com.dictionary.api.dummy.ReturnObject;
import com.google.gson.Gson;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);
    private final Gson gson = new Gson();
    @Autowired
    private WordDao wordDao;
    @Autowired
    private EnglishMyanmarDao englishMyanmarDao;

    @GetMapping("/word")
    public EnglishWord getWord(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new EnglishWord("1", new Date());
    }

    @PostMapping("/auto-word-list")
    @ResponseBody
    public ResponseEntity<?> getAutoWordList(@RequestBody EnglishWord word, HttpServletRequest request) {
        ReturnObject ro = new ReturnObject();
        if (word != null) {
            List<EnglishWord> autoCompletList = wordDao.getAutoCompletList(word.getId());
            ro.setObjList(autoCompletList);
            ro.setMessage("OK");
        } else {
            ro.setMessage("ERROR");
        }
        return ResponseEntity.ok(ro);
    }

    @PostMapping(value = "/save-word")
    @ResponseBody
    public ResponseEntity<?> saveWord(@RequestBody EnglishWord word, HttpServletRequest request) {
        ReturnObject ro = new ReturnObject();
        EnglishWord saveWord = wordDao.saveWord(word);
        ro.setStatus("Saved");
        ro.setObj(saveWord);
        log.info("Save Word");
        return ResponseEntity.ok(ro);

    }

    @PostMapping(value = "/search-word")
    @ResponseBody
    public ResponseEntity<?> searchWord(@RequestBody EnglishWord word, HttpServletRequest request) {
        ReturnObject ro = new ReturnObject();
        if (word != null) {
            EnglishMyanmar em = englishMyanmarDao.searchDictionray(word.getId());
            if (em != null) {
                ro.setMessage("OK");
                ro.setObj(em);
            } else {
                ro.setMessage("Not Found .");
                ro.setObj(em);
            }
        }
        return ResponseEntity.ok(ro);

    }

    @PostMapping(value = "/upload-word-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    ResponseEntity<?> uplodeWordFile(@RequestParam("upload-word-file") MultipartFile file,
            HttpServletRequest request) throws IOException {
        ReturnObject ro = new ReturnObject();
        InputStream inputStream = file.getInputStream();
        if (inputStream != null) {
            List<EnglishWord> wordList = getWordList(inputStream);
            if (!wordList.isEmpty()) {
                log.info("Word Size : " + wordList.size());
                wordDao.saveWordList(wordList);
                ro.setMessage("Upload File Sucess.");
            }
        } else {
            ro.setMessage("File Not Found.");
        }
        return ResponseEntity.ok(ro);
    }

    @PostMapping(value = "/upload-eng-myanmar-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    ResponseEntity<?> uploadEngMyanmarFile(
            @RequestParam("upload-eng-myanmar-file") MultipartFile file, HttpServletRequest request)
            throws IOException {
        ReturnObject ro = new ReturnObject();
        InputStream inputStream = file.getInputStream();
        if (inputStream != null) {
            List<Dictionary> wordList = getEngMyaList(inputStream);
            if (!wordList.isEmpty()) {
                log.info("Word Size : " + wordList.size());
                englishMyanmarDao.saveDictionary(wordList);
                ro.setMessage("Upload File Sucess.");
            }
        } else {
            ro.setMessage("File Not Found.");
        }
        return ResponseEntity.ok(ro);
    }

    @GetMapping("/gen-dictionary")
    public ResponseEntity<?> genDictionary(@RequestParam(value = "name", defaultValue = "World") String name) {
        ReturnObject ro = new ReturnObject();
        List<EnglishMyanmar> genDictionary = genDictionary();
        englishMyanmarDao.save(genDictionary);
        log.info("Generating Finished.");
        ro.setMessage("Generate Dictionary");
        return ResponseEntity.ok(ro);
    }

    @GetMapping("/gen-google-dictionary")
    public ResponseEntity<?> getnGoogleDictioanry(@RequestParam(value = "name", defaultValue = "World") String name) {

        ReturnObject ro = new ReturnObject();
        RestTemplate restTemplate = new RestTemplate();
        List<EnglishWord> listEW = englishMyanmarDao.findAllEnglishWord();
        listEW.stream().map(ew -> ew.getId()).forEachOrdered(word -> {
            try {
                EnglishMyanmar[] listEM = restTemplate.getForObject("https://api.dictionaryapi.dev/api/v2/entries/en_US/" + word + "", EnglishMyanmar[].class);
                if (listEM.length > 0) {
                    List<Meanings> meanings = listEM[0].getMeanings();
                    EnglishMyanmar em = englishMyanmarDao.searchDictionray(word);
                    if (em != null) {
                        em.getMeanings().addAll(meanings);
                        englishMyanmarDao.save(em);
                        log.info("Found Word : " + word);
                    }
                }
            } catch (RestClientException restClientException) {

                log.info("Not Found Word : " + word);
            }
        });
        log.info("Google Words Searching Finished.");
        ro.setMessage("Google Words Searching Finished.");
        return ResponseEntity.ok(ro);
    }

    @PostMapping(value = "/import-google-meaning", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    ResponseEntity<?> importGoogleMeaning(
            @RequestParam("import-google-meaning") MultipartFile file, HttpServletRequest request)
            throws IOException {
        int found = 0;
        int notFound = 0;
        int count = 0;
        ReturnObject ro = new ReturnObject();
        InputStream inputStream = file.getInputStream();
        RestTemplate restTemplate = new RestTemplate();
        if (inputStream != null) {
            List<String> words = getGoogleWords(inputStream);
            int wordSize = words.size();
            if (!words.isEmpty()) {
                log.info("To Import Google List Size : " + wordSize);
                for (String word : words) {
                    try {
                        EnglishMyanmar[] listEM = restTemplate.getForObject("https://api.dictionaryapi.dev/api/v2/entries/en_US/" + word + "", EnglishMyanmar[].class);
                        if (listEM.length > 0) {
                            for (EnglishMyanmar em : listEM) {
                                englishMyanmarDao.save(em);
                            }
                            log.info("Found Words : " + word + " === " + count + 1 + " / " + wordSize);
                            found++;
                        }

                    } catch (RestClientException restClientException) {
                        notFound++;
                        log.info("Not Found Word : " + word + " === " + count + 1 + " / " + wordSize);
                    }
                    //waiting 15 seconds
                    try {
                        Thread.sleep(15 * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                log.info("Google Meanings Importing Sucess .");
                log.info("Import Words : " + words.size() + "- Found Words : " + found + "- Not Founds : " + notFound);
                ro.setStatus("Google Meanings Importing Sucess .");
            }
        } else {
            ro.setMessage("File Not Found.");
        }
        return ResponseEntity.ok(ro);
    }

    private List<EnglishWord> getWordList(InputStream inputStream) {
        List<EnglishWord> listEngWords = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (reader.ready()) {
                String word = reader.readLine();
                listEngWords.add(new EnglishWord(word, new Date()));
            }
        } catch (IOException ex) {
            log.error("Word File Reading :" + ex.getMessage());
        }

        return listEngWords;
    }

    private List<Dictionary> getEngMyaList(InputStream inputStream) {
        List<Dictionary> listD = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                String[] words = line.split(",");
                String word = words[0];
                String state = words[1];
                String def = words[2];
                listD.add(new Dictionary(word, state, def));
            }
        } catch (IOException ex) {
            log.error("Upload English Myanmar Dictionary : " + ex.getMessage());
        }
        return listD;
    }

    private List<String> getGoogleWords(InputStream inputStream) {
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                words.add(line);
            }
        } catch (IOException ex) {
            log.error("getGoogleWords: " + ex.getMessage());
        }
        return words;
    }

    private List<EnglishMyanmar> genDictionary() {
        List<Definitions> listDef = new ArrayList<>();
        List<Meanings> listMeaning = new ArrayList<>();
        List<EnglishMyanmar> listEM = new ArrayList<>();
        HashMap<String, String> hmEM = new HashMap<>();
        List<Dictionary> listDictionary = englishMyanmarDao.findAllDictionary();
        log.info("List Dictionary :" + listDictionary.size());
        for (Dictionary dictionary : listDictionary) {
            String word = dictionary.getWord();
            String state = dictionary.getState();
            String key = word + "," + state;
            if (hmEM.get(key) == null) {
                listMeaning = new ArrayList<>();
                listDef = new ArrayList<>();
                List<Dictionary> searchDic = englishMyanmarDao.searchDic(word, state);
                hmEM.put(key, key);
                //
                for (Dictionary dic : searchDic) {
                    Definitions d = new Definitions();
                    d.setDefinition(dic.getDefinition());
                    listDef.add(d);
                }
                //
                Meanings m = new Meanings();
                m.setPartOfSpeech(state);
                m.setDefinitions(listDef);
                listMeaning.add(m);
                EnglishMyanmar englisMyanmar = new EnglishMyanmar();
                englisMyanmar.setWord(word);
                englisMyanmar.setMeanings(listMeaning);
                listEM.add(englisMyanmar);
                log.info("Generating word " + word);
            }
        }
        HashMap<String, List<Meanings>> hmMeaning = new HashMap<>();
        for (EnglishMyanmar em : listEM) {
            log.info("Word Serializing");
            String word = em.getWord();
            if (hmMeaning.get(word) != null) {
                List<Meanings> list = hmMeaning.get(word);
                List<Meanings> meanings = em.getMeanings();
            } else {
                hmMeaning.put(word, listMeaning);
            }
        }
        return listEM;
    }
}
