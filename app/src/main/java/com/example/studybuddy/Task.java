package com.example.studybuddy;

public class Task {
    private int id;
    private String title;
    private String description;
    private String date;
    private String hour;
    private String subject;
    private String type;

    // Constructeur complet
    public Task(String title, String description, String date, String hour, String subject, String type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.subject = subject;
        this.type = type;
    }

    // Constructeur vide
    public Task() {}

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHour() { return hour; }
    public void setHour(String hour) { this.hour = hour; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
