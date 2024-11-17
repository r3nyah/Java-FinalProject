package com.and.is.pbo_perubahan;

import com.and.is.pbo_perubahan.service.FileStorage;
import com.and.is.pbo_perubahan.service.TaskManager;
import com.and.is.pbo_perubahan.ui.TaskAppGUI;

import javax.swing.*;

public class Pbo_perubahan {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        if (FileStorage.load() != null) {
            manager.getAllTasks().addAll(FileStorage.load());
        }

        SwingUtilities.invokeLater(() -> {
            TaskAppGUI app = new TaskAppGUI(manager);
            app.setVisible(true);
        });
    }
}
