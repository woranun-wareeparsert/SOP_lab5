package com.example.lab52;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Route(value = "index2")
public class MyView2 extends FormLayout {
    private TextField addWord, addSentence;
    private TextArea good, bad;
    private Button btnGood, btnBad, btnAddSentence, btnShowSentence;
    private ComboBox listgood, listbad;
    protected Word word = new Word();

    public MyView2(){
        addWord = new TextField("Add Word");
        addSentence = new TextField("Add Sentence");
        good = new TextArea("Good Sentences");
        bad = new TextArea("Bad Sentences");
        btnGood = new Button("Add Good Word");
        btnBad = new Button("Add Bad Word");
        btnAddSentence = new Button("Add Sentence");
        btnShowSentence = new Button("Show Sentence");
        listgood = new ComboBox("Good Words");
        listbad = new ComboBox("Bad Words");

        HorizontalLayout hl = new HorizontalLayout();
        VerticalLayout vl1 = new VerticalLayout();
        VerticalLayout vl2 = new VerticalLayout();

        listgood.setItems(word.goodWords);
        listbad.setItems(word.badWords);

        vl1.add(addWord, btnGood, btnBad, listgood, listbad);
        vl2.add(addSentence, btnAddSentence, good, bad, btnShowSentence);
        hl.add(vl1, vl2);
        this.add(hl);

        btnGood.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("words", addWord.getValue());

            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGoodWord")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            listgood.setItems(out);
        });

        btnBad.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("words", addWord.getValue());

            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBadWord")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            listbad.setItems(out);
        });

        btnAddSentence.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("sentence", addSentence.getValue());

            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proofSentence")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println(out);
            Notification notification = Notification.show(out);
            this.add(notification);
        });

        btnShowSentence.addClickListener(event -> {

            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();

            good.setValue("[" + String.join(", ", out.goodSentences) + "]");
            bad.setValue("[" + String.join(", ", out.badSentences) + "]");

        });
    }
}
