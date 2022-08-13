package com.example.modernizedshapp.doctor.HealthNews;

import java.util.ArrayList;

public class NewsModel {
    private int totalResult;
    private String status;
    private ArrayList<Articles> articles;

    public NewsModel(int totalResult, String status, ArrayList<Articles> articles) {
        this.totalResult = totalResult;
        this.status = status;
        this.articles = articles;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articles> articles) {
        this.articles = articles;
    }
}
