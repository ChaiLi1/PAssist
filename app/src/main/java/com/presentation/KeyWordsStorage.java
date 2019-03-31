package com.presentation;

import java.io.Serializable;

public class KeyWordsStorage implements Serializable {
    private String keyWords;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords){
        this.keyWords = keyWords;
    }
}
