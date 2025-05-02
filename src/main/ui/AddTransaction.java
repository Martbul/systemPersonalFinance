package main.ui;

import main.core.Category;
import main.core.Transaction;
import main.core.TransactionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTransaction extends JPanel {
    private final TransactionController controller;
    private JTextField amountField;
    private JComboBox<Category> categoryComboBox;
    private JTextField dateField;
    private JTextField descriptionField;
    private JButton addButton;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final List<TransactionListener> listeners = new ArrayList<>();

    public AddTransaction(TransactionController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Add New Transaction"));

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Description field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

        // Amount field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        amountField = new JTextField(10);
        add(amountField, gbc);

        // Category dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        categoryComboBox = new JComboBox<>(Category.values());
        add(categoryComboBox, gbc);

        // Date field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        add(new JLabel("Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        dateField = new JTextField(10);
        dateField.setText(dateFormat.format(new Date())); // Default to today
        add(dateField, gbc);

        // Add button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add Transaction");
        add(addButton, gbc);

        // Add button action
        addButton.addActionListener(this::addTransactionAction);
    }

    private void addTransactionAction(ActionEvent e) {
        try {
            // Parse values
            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description cannot be empty",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = (Category) categoryComboBox.getSelectedItem();

            Date date;
            try {
                date = dateFormat.parse(dateField.getText().trim());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and add transaction
            Transaction transaction = new Transaction(amount, category, date, description);
            boolean success = controller.addTransaction(transaction);

            if (success) {
                // Clear fields
                descriptionField.setText("");
                amountField.setText("");
                dateField.setText(dateFormat.format(new Date()));

                // Notify listeners
                notifyListeners(transaction);

                JOptionPane.showMessageDialog(this, "Transaction added successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add transaction",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public interface TransactionListener {
        void onTransactionAdded(Transaction transaction);
    }


    public void addTransactionListener(TransactionListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(Transaction transaction) {
        for (TransactionListener listener : listeners) {
            listener.onTransactionAdded(transaction);
        }
    }
}