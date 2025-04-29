package main.ui;

import javax.naming.directory.SearchControls;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Start extends JFrame {
        private TransactionController controller;
        private JTable transactionTable;
        private TransactionTable tTable;
        private AddTransaction addPanel;
        private Serach searchPanel;
        private TransactionDetails detailPanel;

        public Start() {
                setTitle("Transaction Management System");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(800, 600);
                setLocationRelativeTo(null);

                controller = new TransactionController();

                initComponents();

                setupLayout();

                addEventListeners();
        }

        private void initComponents() {
                tTable = new TransactionTable(controller.getAllTransactions());
                transactionTable = new JTable(tTable);
                transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                addPanel = new AddTransaction(controller);
                searchPanel = new Serach(controller);
                detailPanel = new TransactionDetails();
        }

        private void setupLayout() {
                // Main layout
                setLayout(new BorderLayout());

                // Create a split pane for table and details
                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

                // Table panel with scroll pane
                JScrollPane tableScrollPane = new JScrollPane(transactionTable);
                tableScrollPane.setPreferredSize(new Dimension(750, 300));

                // Add components to split pane
                splitPane.setTopComponent(tableScrollPane);
                splitPane.setBottomComponent(detailPanel);
                splitPane.setResizeWeight(0.7);

                // Create tabs for add and search functionality
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Add Transaction", addPanel);
                tabbedPane.addTab("Search", searchPanel);

                // Add components to frame
                add(tabbedPane, BorderLayout.NORTH);
                add(splitPane, BorderLayout.CENTER);

                // Status bar
                JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel statusLabel = new JLabel("Ready");
                statusBar.add(statusLabel);
                add(statusBar, BorderLayout.SOUTH);
        }

        private void addEventListeners() {
                // Add table selection listener
                transactionTable.getSelectionModel().addListSelectionListener(e -> {
                        if (!e.getValueIsAdjusting()) {
                                int selectedRow = transactionTable.getSelectedRow();
                                if (selectedRow >= 0) {
                                        // Get selected transaction and display details
                                        Transaction transaction = tableModel.getTransactionAt(selectedRow);
                                        detailPanel.displayTransaction(transaction);
                                }
                        }
                });

                // Add refresh action
                addPanel.addTransactionListener(transaction -> {
                        tableModel.refreshData(controller.getAllTransactions());
                        tableModel.fireTableDataChanged();
                });

                // Add search listener
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