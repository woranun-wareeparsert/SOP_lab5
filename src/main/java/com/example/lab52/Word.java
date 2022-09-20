package com.example.lab52;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {
    public ArrayList<String> badWords = new ArrayList<String>();
    public ArrayList<String> goodWords = new ArrayList<String>();

    public Word() {
        this.goodWords.add("happy");
        this.goodWords.add("enjoy");
        this.goodWords.add("life");
        this.badWords.add("fuck");
        this.badWords.add("olo");
    }
}
