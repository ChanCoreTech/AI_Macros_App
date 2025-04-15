package org.aimacrosapp;

import java.util.List;

public class UserGoalData {
        private UserGoals userGoals;
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