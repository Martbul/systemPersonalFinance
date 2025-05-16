
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
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<Category> categoryComboBox;
    private JTextField dateField;
    private JButton addButton;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final List<TransactionListener> listeners = new ArrayList<>();

    public AddTransaction(TransactionController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        descriptionField = new JTextField(20);
        amountField = new JTextField(10);
        categoryComboBox = new JComboBox<>(Category.values());
        dateField = new JTextField(10);
        dateField.setText(dateFormat.format(new Date()));


        JPanel formPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inputPanel.add(createLabeledPanel("Описани:", descriptionField));
        inputPanel.add(createLabeledPanel("Сума:", amountField));
        inputPanel.add(createLabeledPanel("Категория:", categoryComboBox));
        inputPanel.add(createLabeledPanel("Дата:", dateField));

        formPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Добави");
        buttonPanel.add(addButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::addTransactionAction);
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(130, 25));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void addTransactionAction(ActionEvent e) {
        try {
            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Няма описание",
                        "Грешка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Невалидна сума",
                        "Грешка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = (Category) categoryComboBox.getSelectedItem();

            Date date;
            try {
                date = dateFormat.parse(dateField.getText().trim());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Датата е невалидна",
                        "Грешка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Transaction transaction = new Transaction(amount, category, date, description);
            boolean success = controller.addTransaction(transaction);

            if (success) {
                descriptionField.setText("");
                amountField.setText("");
                dateField.setText(dateFormat.format(new Date()));

                notifyListeners(transaction);

                JOptionPane.showMessageDialog(this, "Успешно добавена транзакция",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Неуспешно добавяне на транзакция",
                        "Грешка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Грешка: " + ex.getMessage(),
                    "Грешка", JOptionPane.ERROR_MESSAGE);
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
