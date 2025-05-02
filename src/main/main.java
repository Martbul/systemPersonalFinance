package main;

import main.ui.Start;

import javax.swing.*;

public class main {
            public static void main(String[] args) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> {
                    Start mainWindow = new Start();
                    mainWindow.setVisible(true);
                });}

    }
