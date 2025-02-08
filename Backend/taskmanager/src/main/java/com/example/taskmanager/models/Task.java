package com.example.taskmanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status;
    private Date dueDate;
    private String priority;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Date getDueDate() { return dueDate; }
    public String getPriority() { return priority; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
}
