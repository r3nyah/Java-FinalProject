package com.and.is.pbo_perubahan;

import com.and.is.pbo_perubahan.service.TaskManager;
import com.and.is.pbo_perubahan.ui.TaskAppGUI;

import javax.swing.*;

public class Pbo_perubahan {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManager taskManager = new TaskManager();
            TaskAppGUI gui = new TaskAppGUI(taskManager);
            gui.setVisible(true);
        });
    }
}
