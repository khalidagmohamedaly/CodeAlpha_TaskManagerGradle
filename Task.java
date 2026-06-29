package com.codealpha.taskmanager;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente une tâche dans le gestionnaire de tâches.
 */
public class Task {

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    private final int id;
    private String title;
    private String description;
    private Priority priority;
    private boolean completed;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Task(int id, String title, String description, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide.");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = Objects.requireNonNullElse(priority, Priority.MEDIUM);
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide.");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = Objects.requireNonNullElse(priority, Priority.MEDIUM);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] #%d %s (%s)%s",
                completed ? "x" : " ", id, title, priority,
                description != null && !description.isBlank() ? " - " + description : "");
    }
}
