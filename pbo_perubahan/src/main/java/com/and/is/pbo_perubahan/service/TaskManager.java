package com.and.is.pbo_perubahan.service;

import com.and.is.pbo_perubahan.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void updateTask(String title, Task updatedTask) {
        Task existingTask = findTaskByTitle(title);
        if (existingTask != null) {
            tasks.remove(existingTask);
            tasks.add(updatedTask);
        }
    }

    public void deleteTask(String title) {
        Task task = findTaskByTitle(title);
        if (task != null) {
            tasks.remove(task);
        }
    }

    public void markTaskAsCompleted(String title) {
        Task task = findTaskByTitle(title);
        if (task != null) {
            task.setCompleted(true);
        }
    }

    public Task findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                return task;
            }
        }
        return null;
    }
}
