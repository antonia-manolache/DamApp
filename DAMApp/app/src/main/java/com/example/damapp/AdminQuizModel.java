package com.example.damapp;

public class AdminQuizModel {
    private String quizId;
    private String quizName;
    private long duration;

    public AdminQuizModel(){

    }

    public AdminQuizModel(String quizId, String quizName, long duration){
        this.quizId = quizId;
        this.quizName = quizName;
        this.duration = duration;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
