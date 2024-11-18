package com.and.is.pbo_perubahan.ui;

import com.and.is.pbo_perubahan.model.Task;
import com.and.is.pbo_perubahan.service.FileStorage;
import com.and.is.pbo_perubahan.service.TaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAppGUI extends JFrame {
    private TaskManager taskManager;
    private JTable taskTable;
    private JTextField searchBar;
    private JComboBox<String> filterPriorityBox;
    private JComboBox<String> filterStatusBox;
    private JComboBox<String> sortCriteriaBox;
    private JButton sortOrderButton;
    private boolean isAscending = true;

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

        // Top Panel: Search, Filters, and Sorting
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");

        filterPriorityBox = new JComboBox<>(new String[]{"All Priorities", "High", "Medium", "Low"});
        filterStatusBox = new JComboBox<>(new String[]{"All Statuses", "Completed", "Not Completed"});

        sortCriteriaBox = new JComboBox<>(new String[]{"Priority", "Due Date"});
        sortOrderButton = new JButton("Ascending");
        sortOrderButton.addActionListener(e -> toggleSortOrder());

        searchButton.addActionListener(e -> searchTasks());
        filterPriorityBox.addActionListener(e -> refreshTable());
        filterStatusBox.addActionListener(e -> refreshTable());
        sortCriteriaBox.addActionListener(e -> refreshTable());

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchBar);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Filter by Priority:"));
        topPanel.add(filterPriorityBox);
        topPanel.add(new JLabel("Filter by Status:"));
        topPanel.add(filterStatusBox);
        topPanel.add(new JLabel("Sort by:"));
        topPanel.add(sortCriteriaBox);
        topPanel.add(sortOrderButton);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        taskTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Bottom Panel: Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveTasks());

        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void toggleSortOrder() {
        isAscending = !isAscending;
        sortOrderButton.setText(isAscending ? "Ascending" : "Descending");
        refreshTable();
    }

    private void refreshTable() {
        List<Task> filteredTasks = taskManager.getAllTasks().stream()
                .filter(task -> filterPriority(task) && filterStatus(task))
                .collect(Collectors.toList());

        sortTasks(filteredTasks);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Title", "Description", "Due Date", "Category", "Priority", "Status"});

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

    private void sortTasks(List<Task> tasks) {
        String selectedCriteria = (String) sortCriteriaBox.getSelectedItem();

        tasks.sort((task1, task2) -> {
            int comparison = 0;
            if ("Priority".equals(selectedCriteria)) {
                comparison = task1.getPriority().compareTo(task2.getPriority());
            } else if ("Due Date".equals(selectedCriteria)) {
                if (task1.getDueDate() == null) return isAscending ? 1 : -1;
                if (task2.getDueDate() == null) return isAscending ? -1 : 1;
                comparison = task1.getDueDate().compareTo(task2.getDueDate());
            }
            return isAscending ? comparison : -comparison;
        });
    }

    private void searchTasks() {
        String query = searchBar.getText().toLowerCase();
        List<Task> searchedTasks = taskManager.getAllTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Title", "Description", "Due Date", "Category", "Priority", "Status"});

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Task task : searchedTasks) {
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

    private void saveTasks() {
        FileStorage.save(taskManager.getAllTasks());
        JOptionPane.showMessageDialog(this, "Tasks saved successfully!");
    }
}
