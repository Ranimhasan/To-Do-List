package com.example.sproje;


public class GoalItem {
    private String goalName;
    private boolean isCompleted;

    public GoalItem(String goalName, String kategori, boolean isCompleted) {
        this.goalName = goalName;
        this.isCompleted = isCompleted;
    }

    public String getGoalName() {
        return goalName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
