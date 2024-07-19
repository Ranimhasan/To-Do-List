package com.example.sproje;


import java.io.Serializable;

public class SubGoal implements Serializable {
        private String subGoalName;
        private String subGoalDescription;
        private String subGoalStartDate;
        private String subGoalEndDate;


        public SubGoal() {
        }


        public SubGoal(String subGoalName, String subGoalDescription, String subGoalStartDate, String subGoalEndDate) {
            this.subGoalName = subGoalName;
            this.subGoalDescription = subGoalDescription;
            this.subGoalStartDate = subGoalStartDate;
            this.subGoalEndDate = subGoalEndDate;
        }


        public String getSubGoalName() {
            return subGoalName;
        }

        public void setSubGoalName(String subGoalName) {
            this.subGoalName = subGoalName;
        }

        public String getSubGoalDescription() {
            return subGoalDescription;
        }

        public void setSubGoalDescription(String subGoalDescription) {
            this.subGoalDescription = subGoalDescription;
        }

        public String getSubGoalStartDate() {
            return subGoalStartDate;
        }

        public void setSubGoalStartDate(String subGoalStartDate) {
            this.subGoalStartDate = subGoalStartDate;
        }

        public String getSubGoalEndDate() {
            return subGoalEndDate;
        }

        public void setSubGoalEndDate(String subGoalEndDate) {
            this.subGoalEndDate = subGoalEndDate;
        }
    }

