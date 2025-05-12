
package main.ui;

import main.core.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TransactionDetailsUI extends JPanel {
    private JTable detailsTable;
    private DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public TransactionDetailsUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Детайли"));
        initComponents();

        Dimension fixedSize = new Dimension(350, 150);
        setPreferredSize(fixedSize);
        setMaximumSize(fixedSize);
        setMinimumSize(fixedSize);
    }

    private void initComponents() {
        String[] columnNames = {"", "Стойност"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        detailsTable = new JTable(tableModel);
        detailsTable.setRowHeight(25);
        detailsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        detailsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        initializeTableData();

        add(detailsTable, BorderLayout.CENTER);
    }

    private void initializeTableData() {
        tableModel.setRowCount(0);

        tableModel.addRow(new Object[]{"ID", ""});
        tableModel.addRow(new Object[]{"Дата", ""});
        tableModel.addRow(new Object[]{"Категория", ""});
        tableModel.addRow(new Object[]{"Сума", ""});
        tableModel.addRow(new Object[]{"Описание", ""});
    }

    public void displayTransaction(Transaction transaction) {
        if (transaction == null) {
            clearFields();
            return;
        }

        tableModel.setValueAt(String.valueOf(transaction.getId()), 0, 1);
        tableModel.setValueAt(dateFormat.format(transaction.getDate()), 1, 1);
        tableModel.setValueAt(transaction.getCategory().toString(), 2, 1);
        tableModel.setValueAt(String.format("%.2f", transaction.getAmount()), 3, 1);
        tableModel.setValueAt(transaction.getDescription(), 4, 1);
    }

    private void clearFields() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 1);
        }
    }
}