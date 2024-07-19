package com.example.sproje;

public class DailyProgressModel {

    private boolean completed;
    private int progress;


    public DailyProgressModel() {
    }


    public DailyProgressModel(boolean completed, int progress) {
        this.completed = completed;
        this.progress = progress;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
