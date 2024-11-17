package com.and.is.pbo_perubahan.ui;

import com.and.is.pbo_perubahan.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskForm extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JTextField categoryField;
    private JComboBox<String> priorityBox;
    private JCheckBox completedCheckBox;
    private Task task;
    private boolean saved;

    public TaskForm(JFrame parent, Task task) {
        super(parent, "Task Form", true);
        this.task = task;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleField = new JTextField(task != null ? task.getTitle() : "");
        descriptionArea = new JTextArea(task != null ? task.getDescription() : "");
        dueDateField = new JTextField(task != null && task.getDueDate() != null
                ? task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "");
        categoryField = new JTextField(task != null ? task.getCategory() : "");
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        if (task != null) priorityBox.setSelectedItem(task.getPriority());
        completedCheckBox = new JCheckBox("Completed", task != null && task.isCompleted());

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(descriptionArea));
        formPanel.add(new JLabel("Due Date (yyyy-MM-dd HH:mm):"));
        formPanel.add(dueDateField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityBox);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(completedCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                saveTask();
                saved = true;
                dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        LocalDateTime dueDate = null;
        if (!dueDateField.getText().trim().isEmpty()) {
            dueDate = LocalDateTime.parse(dueDateField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        String category = categoryField.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        boolean completed = completedCheckBox.isSelected();

        if (task == null) {
            task = new Task(title, description, dueDate, category, priority, completed);
        } else {
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setCategory(category);
            task.setPriority(priority);
            task.setCompleted(completed);
        }
    }

    public Task getTask() {
        return task;
    }

    public boolean isSaved() {
        return saved;
    }
}
