package com.and.is.pbo_perubahan.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String category;
    private String priority;
    private boolean completed;

    public Task(String title, String description, LocalDateTime dueDate, String category, String priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("Title: %s\nDescription: %s\nDue Date: %s\nCategory: %s\nPriority: %s\nStatus: %s",
                title,
                description,
                dueDate != null ? dueDate.toString() : "No due date",
                category,
                priority,
                completed ? "Completed" : "Not Completed");
    }
}
