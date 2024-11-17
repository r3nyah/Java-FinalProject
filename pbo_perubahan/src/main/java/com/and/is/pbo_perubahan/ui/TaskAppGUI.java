package com.and.is.pbo_perubahan.ui;

import com.and.is.pbo_perubahan.model.Task;
import com.and.is.pbo_perubahan.service.FileStorage;
import com.and.is.pbo_perubahan.service.TaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAppGUI extends JFrame {
    private TaskManager taskManager;
    private JTable taskTable;
    private JTextField searchBar;
    private JComboBox<String> filterPriorityBox;
    private JComboBox<String> filterStatusBox;

    public TaskAppGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("To-Do List Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Layout setup
        setLayout(new BorderLayout());

        // Top Panel: Search and Filters
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");

        filterPriorityBox = new JComboBox<>(new String[]{"All Priorities", "High", "Medium", "Low"});
        filterStatusBox = new JComboBox<>(new String[]{"All Statuses", "Completed", "Not Completed"});

        searchButton.addActionListener(e -> searchTasks());
        filterPriorityBox.addActionListener(e -> refreshTable());
        filterStatusBox.addActionListener(e -> refreshTable());

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchBar);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Filter by Priority:"));
        topPanel.add(filterPriorityBox);
        topPanel.add(new JLabel("Filter by Status:"));
        topPanel.add(filterStatusBox);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        taskTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Bottom Panel: Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton markCompleteButton = new JButton("Mark as Completed");
        JButton saveButton = new JButton("Save");

        addButton.addActionListener(e -> openTaskForm(null));
        editButton.addActionListener(e -> editSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        markCompleteButton.addActionListener(e -> markTaskAsCompleted());
        saveButton.addActionListener(e -> saveTasks());

        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(markCompleteButton);
        bottomPanel.add(saveButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        // Get filtered tasks
        List<Task> filteredTasks = taskManager.getAllTasks().stream()
                .filter(task -> filterPriority(task) && filterStatus(task))
                .collect(Collectors.toList());

        // Table Model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Title", "Description", "Due Date", "Category", "Priority", "Status"});

        // Populate table rows
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Task task : filteredTasks) {
            tableModel.addRow(new Object[]{
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate() != null ? task.getDueDate().format(formatter) : "No Due Date",
                    task.getCategory(),
                    task.getPriority(),
                    task.isCompleted() ? "Completed" : "Not Completed"
            });
        }

        taskTable.setModel(tableModel);
    }

    private boolean filterPriority(Task task) {
        String selectedPriority = (String) filterPriorityBox.getSelectedItem();
        return selectedPriority.equals("All Priorities") || selectedPriority.equals(task.getPriority());
    }

    private boolean filterStatus(Task task) {
        String selectedStatus = (String) filterStatusBox.getSelectedItem();
        return selectedStatus.equals("All Statuses")
                || (selectedStatus.equals("Completed") && task.isCompleted())
                || (selectedStatus.equals("Not Completed") && !task.isCompleted());
    }

    private void searchTasks() {
        String query = searchBar.getText().toLowerCase();
        List<Task> searchResults = taskManager.getAllTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query)
                        || task.getDescription().toLowerCase().contains(query))
                .collect(Collectors.toList());

        // Table Model for search results
        DefaultTableModel searchTableModel = new DefaultTableModel();
        searchTableModel.setColumnIdentifiers(new String[]{"Title", "Description", "Due Date", "Category", "Priority", "Status"});

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Task task : searchResults) {
            searchTableModel.addRow(new Object[]{
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate() != null ? task.getDueDate().format(formatter) : "No Due Date",
                    task.getCategory(),
                    task.getPriority(),
                    task.isCompleted() ? "Completed" : "Not Completed"
            });
        }

        taskTable.setModel(searchTableModel);
    }

    private void openTaskForm(Task task) {
        TaskForm taskForm = new TaskForm(this, task);
        taskForm.setVisible(true);

        if (taskForm.isSaved()) {
            if (task == null) {
                taskManager.addTask(taskForm.getTask());
            } else {
                taskManager.updateTask(task.getTitle(), taskForm.getTask());
            }
            refreshTable();
        }
    }

    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            Task task = taskManager.findTaskByTitle(title);
            if (task != null) {
                openTaskForm(task);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            taskManager.deleteTask(title);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskAsCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            taskManager.markTaskAsCompleted(title);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTasks() {
        FileStorage.save(taskManager.getAllTasks());
        JOptionPane.showMessageDialog(this, "Tasks saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
