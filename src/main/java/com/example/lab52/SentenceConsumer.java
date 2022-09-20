package com.example.lab52;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class SentenceConsumer {
    protected Sentence sentences = new Sentence();

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentences() {
        return sentences;
    }

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        this.sentences.badSentences.add(s);
        System.out.println("Bad = " + this.sentences.badSentences);
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        this.sentences.goodSentences.add(s);
        System.out.println("Good = " + this.sentences.goodSentences);
    }
}
