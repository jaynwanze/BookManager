package com.example.bookmanager.pojo;

import androidx.annotation.NonNull;


public class Book
{
    private int id;
    private String title;
    private String author;
    private String category;
    private String startDate;
    private String review;
    private String status;
    private int userId;

    public Book (int id, String title, String author, String category, String startDate, String review, String status, int userId)
        {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.startDate = startDate;
        this.review = review;
        this.status = status;
        this.userId = userId;
    }

    public Book ()
    {

    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    @NonNull
    @Override
    public String toString()
    {
        return
                        " Title: " + this.title +
                        " \nAuthor: " + this.author +
                        " \nCategory: " + this.category +
                        " \nStart Date: " + this.startDate +
                        " \nStatus: " + this.status;
    }
}
