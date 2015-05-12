package edu.washington.wsmay1.quizdroid;


import java.util.ArrayList;

public class Topic {
    private String title;
    private String description;
    ArrayList<Question> questions;

    public Topic(String title, String description, ArrayList<Question> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Question> questions() {
        return this.questions;
    }
}
