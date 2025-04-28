package main;

import javax.swing.*;

public class main {

            public static void main(String[] args) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SwingUtilities.invokeLater(() -> {
                    MainWindow mainWindow = new MainWindow();
                    mainWindow.setVisible(true);
                });
            }

    }
