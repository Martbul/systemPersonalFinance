package main.ui;

import main.core.Category;
import main.core.Transaction;
import main.core.TransactionController;
import main.customDynamcStructures.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Search extends JPanel {
    private final TransactionController controller;
    private JTextField keywordField;
    private JComboBox<Category> categoryComboBox;
    private JTextField minAmountField;
    private JTextField maxAmountField;
    private JButton searchButton;
    private JButton resetButton;
    private JTable criteriaTable;
    private DefaultTableModel tableModel;

    private final List<SearchListener> listeners = new ArrayList<>();

    public Search(TransactionController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Търсене"));

        initComponents();
    }

    private void initComponents() {
        String[] columnNames = {"Criteria", "Value"};
        tableModel = new DefaultTableModel(columnNames, 0);
        criteriaTable = new JTable(tableModel);
        criteriaTable.setRowHeight(30);

        keywordField = new JTextField(20);

        Category[] categories = new Category[Category.values().length + 1];
        categories[0] = null;
        System.arraycopy(Category.values(), 0, categories, 1, Category.values().length);
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("");
                }
                return this;
            }
        });

        minAmountField = new JTextField(10);
        maxAmountField = new JTextField(10);

        JPanel tablePanel = new JPanel(new GridLayout(6, 1, 5, 5));
        tablePanel.add(createLabeledPanel("Дума:", keywordField));
        tablePanel.add(createLabeledPanel("Категория:", categoryComboBox));
        tablePanel.add(createLabeledPanel("Мин сума:", minAmountField));
        tablePanel.add(createLabeledPanel("Макс сума:", maxAmountField));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchButton = new JButton("Търсене");
        resetButton = new JButton("Нулиране");
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(this::searchAction);
        resetButton.addActionListener(this::resetAction);
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void searchAction(ActionEvent e) {
        try {
            String keyword = keywordField.getText().trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }

            Category category = (Category) categoryComboBox.getSelectedItem();




            Double minAmount = null;
            if (!minAmountField.getText().trim().isEmpty()) {
                try {
                    minAmount = Double.parseDouble(minAmountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Невалидна минимална сума",
                            "Грешка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Double maxAmount = null;
            if (!maxAmountField.getText().trim().isEmpty()) {
                try {
                    maxAmount = Double.parseDouble(maxAmountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Невалидна максимална сума",
                            "Грешка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            LinkedList<Transaction> results = controller.searchTransactions(
                    keyword, category, minAmount, maxAmount);

            notifyListeners(results);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Грешка: " + ex.getMessage(),
                    "Грешка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetAction(ActionEvent e) {
        keywordField.setText("");
        categoryComboBox.setSelectedIndex(0);
        minAmountField.setText("");
        maxAmountField.setText("");

        notifyListeners(controller.getAllTransactions());
    }

    public interface SearchListener {
        void onSearchPerformed(LinkedList<Transaction> searchResults);
    }

    public void addSearchListener(SearchListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(LinkedList<Transaction> searchResults) {
        for (SearchListener listener : listeners) {
            listener.onSearchPerformed(searchResults);
        }
    }
}
