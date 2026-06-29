package com.codealpha.taskmanager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Gère le cycle de vie des tâches : création, complétion, suppression,
 * filtrage et tri par priorité.
 */
public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Task addTask(String title, String description, Task.Priority priority) {
        Task task = new Task(idGenerator.getAndIncrement(), title, description, priority);
        tasks.add(task);
        return task;
    }

    public Task getTask(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void completeTask(int id) {
        getTask(id).markCompleted();
    }

    public boolean removeTask(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .collect(Collectors.toList());
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Task.Priority priority) {
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public Map<Task.Priority, Long> countByPriority() {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }

    public double completionRate() {
        if (tasks.isEmpty()) return 0.0;
        long completed = tasks.stream().filter(Task::isCompleted).count();
        return (completed * 100.0) / tasks.size();
    }

    public int size() {
        return tasks.size();
    }
}
