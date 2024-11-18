package com.and.is.pbo_perubahan.service;

import com.and.is.pbo_perubahan.model.Task;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final Path storageDirectory;
    private static final String FILE_NAME = "tasks.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Default constructor using a default directory
    public FileStorage() throws IOException {
        this("tasks"); // Default directory
    }

    // Constructor to specify the directory
    public FileStorage(String directory) throws IOException {
        this.storageDirectory = Paths.get(directory);
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory); // Ensure the directory exists
        }
    }

    // Save tasks to a file
    public void saveTasksToFile(List<Task> tasks) throws IOException {
        Path filePath = storageDirectory.resolve(FILE_NAME); // Default file name
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Task task : tasks) {
                writer.write(formatTask(task));
                writer.newLine();
            }
        }
    }

    // Load tasks from a file
    public List<Task> loadTasksFromFile() throws IOException {
        Path filePath = storageDirectory.resolve(FILE_NAME);
        List<Task> tasks = new ArrayList<>();

        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        }

        return tasks;
    }

    // Format a task for saving
    private String formatTask(Task task) {
        return String.join("|",
                task.getTitle(),
                task.getDescription(),
                task.getDueDate() != null ? task.getDueDate().format(DATE_FORMATTER) : "",
                task.getCategory(),
                task.getPriority(),
                Boolean.toString(task.isCompleted()));
    }

    // Parse a task from a formatted string
    private Task parseTask(String line) {
        try {
            String[] fields = line.split("\\|");
            if (fields.length < 6) return null;

            String title = fields[0];
            String description = fields[1];
            LocalDateTime dueDate = fields[2].isEmpty() ? null : LocalDateTime.parse(fields[2], DATE_FORMATTER);
            String category = fields[3];
            String priority = fields[4];
            boolean isCompleted = Boolean.parseBoolean(fields[5]);

            return new Task(title, description, dueDate, category, priority, isCompleted);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle invalid lines gracefully
        }
    }
}
