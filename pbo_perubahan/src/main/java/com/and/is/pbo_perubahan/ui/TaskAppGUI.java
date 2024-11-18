package com.and.is.pbo_perubahan.ui;

import com.and.is.pbo_perubahan.model.Task;
import com.and.is.pbo_perubahan.service.FileStorage;
import com.and.is.pbo_perubahan.service.TaskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main Panel with margin
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margins

        // Top Panel: Search, Filters, and Sorting
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel: Task Table
        taskTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margins around table
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Bottom Panel: CRUD Buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Inner spacing

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTasks());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPriorityBox = new JComboBox<>(new String[]{"All Priorities", "High", "Medium", "Low"});
        filterStatusBox = new JComboBox<>(new String[]{"All Statuses", "Completed", "Not Completed"});
        sortCriteriaBox = new JComboBox<>(new String[]{"Priority", "Due Date"});
        sortOrderButton = new JButton("Ascending");
        sortOrderButton.addActionListener(e -> toggleSortOrder());

        filterPriorityBox.addActionListener(e -> refreshTable());
        filterStatusBox.addActionListener(e -> refreshTable());
        sortCriteriaBox.addActionListener(e -> refreshTable());

        filterPanel.add(new JLabel("Filter by Priority:"));
        filterPanel.add(filterPriorityBox);
        filterPanel.add(new JLabel("Filter by Status:"));
        filterPanel.add(filterStatusBox);
        filterPanel.add(new JLabel("Sort by:"));
        filterPanel.add(sortCriteriaBox);
        filterPanel.add(sortOrderButton);

        topPanel.add(searchPanel);
        topPanel.add(filterPanel);

        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margins

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> openTaskForm(null));

        JButton editButton = new JButton("Edit Task");
        editButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) taskTable.getValueAt(selectedRow, 0);
                Task task = taskManager.findTaskByTitle(title);
                openTaskForm(task);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) taskTable.getValueAt(selectedRow, 0);
                taskManager.deleteTask(title);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton saveButton = new JButton("Save Tasks");
        saveButton.addActionListener(e -> {
            try {
                saveTasks();
            } catch (IOException ex) {
                Logger.getLogger(TaskAppGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(saveButton);

        return bottomPanel;
    }

    private void openTaskForm(Task task) {
        TaskForm form = new TaskForm(this, task);
        form.setVisible(true);
        if (form.isSaved()) {
            if (task == null) {
                taskManager.addTask(form.getTask());
            }
            refreshTable();
        }
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

    private void saveTasks() throws IOException {
        FileStorage fileStorage = new FileStorage();
        fileStorage.saveTasksToFile(taskManager.getAllTasks());
    }
}
