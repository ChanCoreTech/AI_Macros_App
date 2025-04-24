package org.aimacrosapp;

import java.util.List;
//class to aid in filling in goal history table
public class UserGoalData {
        private UserGoals userGoals;
        //list of goal history data
        private List<UserGoalHistory> goalHistoryList;

        public UserGoalData(UserGoals userGoals, List<UserGoalHistory> goalHistoryList) {
            this.userGoals = userGoals;
            this.goalHistoryList = goalHistoryList;
        }

        public UserGoals getUserGoals() {
            return userGoals;
        }

        public List<UserGoalHistory> getHistory() {
            return goalHistoryList;
        }
    }