package com.example.modernizedshapp.doctor.Diagnosis.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class Chat {

    private final Date date;
    private int id;
    private int userId;
    private String lastRequest;
    @Nullable
    private String lastDoctorQuestion;
    @Nullable
    private String lastDoctorQuestionId;
    @Nullable
    private String conditionsArray;

    private Chat(Date date, int id, int userId, String lastRequest, @Nullable String lastDoctorQuestion, @Nullable String lastDoctorQuestionId, @Nullable String conditionsArray) {
        this.date = date;
        this.id = id;
        this.userId = userId;
        this.lastRequest = lastRequest;
        this.lastDoctorQuestion = lastDoctorQuestion;
        this.lastDoctorQuestionId = lastDoctorQuestionId;
        this.conditionsArray = conditionsArray;
    }

    public static Builder builder(int id, int userId) {
        return new Builder(id, userId);
    }

    @Nullable
    public String getLastDoctorQuestion() {
        return lastDoctorQuestion;
    }

    public void setLastDoctorQuestion(@Nullable String lastDoctorQuestion) {
        this.lastDoctorQuestion = lastDoctorQuestion;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "date=" + date +
                ", id=" + id +
                ", userId=" + userId +
                ", lastRequest='" + lastRequest + '\'' +
                ", lastDoctorQuestion='" + lastDoctorQuestion + '\'' +
                ", lastDoctorQuestionId='" + lastDoctorQuestionId + '\'' +
                ", conditionArray=" + conditionsArray +
                '}';
    }

    @Nullable
    public String getLastDoctorQuestionId() {
        return lastDoctorQuestionId;
    }

    public void setLastDoctorQuestionId(@Nullable String lastDoctorQuestionId) {
        this.lastDoctorQuestionId = lastDoctorQuestionId;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getConditionsArray() {
        return conditionsArray;
    }

    public void setConditionsArray(@Nullable String conditionsArray) {
        this.conditionsArray = conditionsArray;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(String lastRequest) {
        this.lastRequest = lastRequest;
    }

    public static final class Builder {
        private final int chatId;
        private final int userId;
        private Date date = null;
        private String lastRequest = null;
        private String lastDoctorQuestion = null;
        private String lastDoctorQuestionId = null;
        private String conditionsArray = null;

        private Builder(int id, int userId) {
            this.chatId = id;
            this.userId = userId;
        }


        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder lastRequest(String lastRequest) {
            this.lastRequest = lastRequest;
            return this;
        }

        public Builder lastDoctorQuestionAndId(@NonNull String lastDoctorQuestion, @NonNull String lastDoctorQuestionId) {
            this.lastDoctorQuestion = lastDoctorQuestion;
            this.lastDoctorQuestionId = lastDoctorQuestionId;
            return this;
        }


        public Builder conditionArray(String conditionArray) {
            this.conditionsArray = conditionArray;
            return this;
        }


        public Chat build() {
            if (date == null) {
                this.date = new Date();
            }
            if (lastRequest == null) {
                this.lastRequest = "";
            }
            if (lastDoctorQuestion == null || lastDoctorQuestionId == null) {
                this.lastDoctorQuestion = null;
                this.lastDoctorQuestionId = null;
            }
            return new Chat(this.date, this.chatId, this.userId, this.lastRequest, this.lastDoctorQuestion, this.lastDoctorQuestionId, this.conditionsArray);
        }
    }
}
