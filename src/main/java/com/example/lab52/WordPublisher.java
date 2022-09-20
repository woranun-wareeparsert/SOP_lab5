package com.example.lab52;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    protected Word words = new Word();
    protected Sentence sentence = new Sentence();

    @RequestMapping(value = "/addBadWord", method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@RequestBody MultiValueMap<String, String> s){
        Map<String, String> d = s.toSingleValueMap();
        this.words.badWords.add(d.get("words"));
        System.out.println(this.words.badWords);
        return this.words.badWords;
    }

//    @RequestMapping(value = "/delBad/{s}", method = RequestMethod.GET)
//    public ArrayList<String> deleteBadWord(@PathVariable("s") String s){
//        this.words.badWords.remove(s);
//        return this.words.badWords;
//    }

    @RequestMapping(value = "/addGoodWord", method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@RequestBody MultiValueMap<String, String> s){
        Map<String, String> d = s.toSingleValueMap();
        this.words.goodWords.add(d.get("words"));
        System.out.println(this.words.goodWords);
        return this.words.goodWords;
    }

//    @RequestMapping(value = "/delGood/{s}", method = RequestMethod.GET)
//    public ArrayList<String> deleteGoodWord(@PathVariable("s") String s){
//        this.words.goodWords.remove(s);
//        return this.words.goodWords;
//    }

    @RequestMapping(value = "/proofSentence", method = RequestMethod.POST)
    public String proofSentence(@RequestBody MultiValueMap<String, String> s){
        Map<String, String> d = s.toSingleValueMap();
        boolean bad = false;
        boolean good = false;

        for (String badword: this.words.badWords){
            if (d.get("sentence").contains(badword)){
               bad = true;
            }
        }
        for (String goodword: this.words.goodWords){
            if (d.get("sentence").contains(goodword)){
                good = true;
            }
        }

        if (bad == true && good == true) {
            rabbitTemplate.convertAndSend("Fanout", "", d.get("sentence"));
            System.out.println("Found Bad & Good Word");
            return ("Found Bad & Good Word");
        } else if (bad == true) {
            rabbitTemplate.convertAndSend("Direct", "bad", d.get("sentence"));
            System.out.println("Found Bad Word");
            return ("Found Bad Word");
        } else if (good == true) {
            rabbitTemplate.convertAndSend("Direct", "good", d.get("sentence"));
            System.out.println("Found Good Word");
            return ("Found Good Word");
        } else {
            System.out.println("Not Found");
            return ("Not Found");
        }
    }

    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        Object list = rabbitTemplate.convertSendAndReceive("Direct", "get", "");
        return (Sentence) list;
    }
}
