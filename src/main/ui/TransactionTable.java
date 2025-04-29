package main.ui;

import main.core.Transaction;
import main.customDynamcStructures.LinkedList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionTable extends JPanel {
        private LinkedList<Transaction> transactions;
        private JTable table;
        private TransactionTableModel tableModel;
        private JScrollPane scrollPane;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        public TransactionTable() {
                this.transactions = new ArrayList<>();
                initComponents();
        }

        private void initComponents() {
                setLayout(new BorderLayout());

                tableModel = new TransactionTableModel();

                table = new JTable(tableModel);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                table.setRowHeight(25);
                table.setAutoCreateRowSorter(true);

                scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(600, 300));

                add(scrollPane, BorderLayout.CENTER);

                JLabel titleLabel = new JLabel("Transactions", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                add(titleLabel, BorderLayout.NORTH);
        }


        public void updateTransactions(List<Transaction> transactions) {
                this.transactions = transactions;
                tableModel.fireTableDataChanged();
        }


        public void addTransaction(Transaction transaction) {
                transactions.add(transaction);
                tableModel.fireTableDataChanged();
        }


        public void removeTransaction(int index) {
                if (index >= 0 && index < transactions.size()) {
                        transactions.remove(index);
                        tableModel.fireTableDataChanged();
                }
        }


        public Transaction getSelectedTransaction() {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        return transactions.get(modelRow);
                }
                return null;
        }

        public int getSelectedIndex() {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                        return table.convertRowIndexToModel(selectedRow);
                }
                return -1;
        }


        private class TransactionTableModel extends AbstractTableModel {
                private String[] columnNames = {"Date", "Category", "Amount"};

                @Override
                public int getRowCount() {
                        return transactions.size();
                }

                @Override
                public int getColumnCount() {
                        return columnNames.length;
                }

                @Override
                public String getColumnName(int column) {
                        return columnNames[column];
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                        switch (columnIndex) {
                                case 0: return String.class;
                                case 1: return String.class;
                                case 2: return Double.class;
                                default: return Object.class;
                        }
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                        Transaction transaction = transactions.get(rowIndex);

                        switch (columnIndex) {
                                case 0:
                                        return dateFormat.format(transaction.getDate());
                                case 1:
                                        return transaction.getCategory().getDisplayName();
                                case 2:
                                        return transaction.getAmount();
                                default:
                                        return null;
                        }
                }
        }
}