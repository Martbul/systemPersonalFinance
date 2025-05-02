
package main.ui;

        import main.core.Category;
        import main.core.Transaction;
        import main.core.TransactionController;
        import main.customDynamcStructures.LinkedList;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

public class Search extends JPanel {
    private final TransactionController controller;
    private JTextField keywordField;
    private JComboBox<Category> categoryComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField minAmountField;
    private JTextField maxAmountField;
    private JButton searchButton;
    private JButton resetButton;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // List of listeners to be notified when search is performed
    private final List<SearchListener> listeners = new ArrayList<>();

    public Search(TransactionController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Search Transactions"));

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Keyword field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Keyword:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        keywordField = new JTextField(20);
        add(keywordField, gbc);

        // Category dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        Category[] categories = new Category[Category.values().length + 1];
        categories[0] = null; // Add null option for "Any category"
        System.arraycopy(Category.values(), 0, categories, 1, Category.values().length);
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Any");
                }
                return this;
            }
        });
        add(categoryComboBox, gbc);

        // Date range
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        add(new JLabel("Start Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        startDateField = new JTextField(10);
        add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        add(new JLabel("End Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        endDateField = new JTextField(10);
        add(endDateField, gbc);

        // Amount range
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        add(new JLabel("Min Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        minAmountField = new JTextField(10);
        add(minAmountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        add(new JLabel("Max Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        maxAmountField = new JTextField(10);
        add(maxAmountField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchButton = new JButton("Search");
        resetButton = new JButton("Reset");
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Add button actions
        searchButton.addActionListener(this::searchAction);
        resetButton.addActionListener(this::resetAction);
    }

    private void searchAction(ActionEvent e) {
        try {
            // Parse search criteria
            String keyword = keywordField.getText().trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }

            Category category = (Category) categoryComboBox.getSelectedItem();

            Date startDate = null;
            if (!startDateField.getText().trim().isEmpty()) {
                try {
                    startDate = dateFormat.parse(startDateField.getText().trim());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid start date format. Use yyyy-MM-dd",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Date endDate = null;
            if (!endDateField.getText().trim().isEmpty()) {
                try {
                    endDate = dateFormat.parse(endDateField.getText().trim());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid end date format. Use yyyy-MM-dd",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Double minAmount = null;
            if (!minAmountField.getText().trim().isEmpty()) {
                try {
                    minAmount = Double.parseDouble(minAmountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid minimum amount format",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Double maxAmount = null;
            if (!maxAmountField.getText().trim().isEmpty()) {
                try {
                    maxAmount = Double.parseDouble(maxAmountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid maximum amount format",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Perform search
            LinkedList<Transaction> results = controller.searchTransactions(
                    keyword, category, startDate, endDate, minAmount, maxAmount);

            // Notify listeners
            notifyListeners(results);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetAction(ActionEvent e) {
        // Clear all fields
        keywordField.setText("");
        categoryComboBox.setSelectedIndex(0);
        startDateField.setText("");
        endDateField.setText("");
        minAmountField.setText("");
        maxAmountField.setText("");

        // Reset search results to show all transactions
        notifyListeners(controller.getAllTransactions());
    }

    /**
     * Interface for search listener
     */
    public interface SearchListener {
        void onSearchPerformed(LinkedList<Transaction> searchResults);
    }

    /**
     * Add a listener to be notified when search is performed
     */
    public void addSearchListener(SearchListener listener) {
        listeners.add(listener);
    }

    /**
     * Notify all listeners that search was performed
     */
    private void notifyListeners(LinkedList<Transaction> searchResults) {
        for (SearchListener listener : listeners) {
            listener.onSearchPerformed(searchResults);
        }
    }
}