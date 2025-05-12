package main.ui;

import main.core.Transaction;
import main.core.TransactionController;
import main.customDynamcStructures.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Start extends JFrame {
        private TransactionController controller;
        private JTable transactionTable;
        private TransactionTableUI tableModel;
        private AddTransaction addPanel;
        private Search searchPanel;
        private TransactionDetailsUI detailPanel;

        public Start() {
                setTitle("Система за управление на финанси");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(800, 600);
                setLocationRelativeTo(null);

                controller = new TransactionController();

                initComponents();
                setupLayout();
                addEventListeners();
        }

        private void initComponents() {
                tableModel = new TransactionTableUI(controller.getAllTransactions());
                transactionTable = new JTable(tableModel);
                transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                addPanel = new AddTransaction(controller);
                searchPanel = new Search(controller);
                detailPanel = new TransactionDetailsUI();
        }

        private void setupLayout() {
                setLayout(new BorderLayout());

                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

                JScrollPane tableScrollPane = new JScrollPane(transactionTable);
                tableScrollPane.setPreferredSize(new Dimension(750, 300));

                splitPane.setTopComponent(tableScrollPane);
                splitPane.setBottomComponent(detailPanel);
                splitPane.setResizeWeight(0.7);

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Добавяне", addPanel);
                tabbedPane.addTab("Търсене", searchPanel);

                add(tabbedPane, BorderLayout.NORTH);
                add(splitPane, BorderLayout.CENTER);
        }

        private void addEventListeners() {
                transactionTable.getSelectionModel().addListSelectionListener(e -> {
                        if (!e.getValueIsAdjusting()) {
                                int selectedRow = transactionTable.getSelectedRow();
                                if (selectedRow >= 0) {
                                        Transaction transaction = tableModel.getTransactionAt(selectedRow);
                                        detailPanel.displayTransaction(transaction);
                                }
                        }
                });

                addPanel.addTransactionListener(transaction -> {
                        tableModel.refreshData(controller.getAllTransactions());
                        tableModel.fireTableDataChanged();
                });

                searchPanel.addSearchListener(searchResults -> {
                        tableModel.refreshData(searchResults);
                        tableModel.fireTableDataChanged();
                });
        }

        public void refreshTable() {
                tableModel.refreshData(controller.getAllTransactions());
                tableModel.fireTableDataChanged();
        }


}