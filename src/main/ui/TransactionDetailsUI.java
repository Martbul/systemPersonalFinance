
package main.ui;

        import main.core.Transaction;

        import javax.swing.*;
        import java.awt.*;
        import java.text.SimpleDateFormat;


public class TransactionDetailsUI extends JPanel {
    private JLabel idLabel;
    private JLabel dateLabel;
    private JLabel categoryLabel;
    private JLabel amountLabel;
    private JLabel descriptionLabel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TransactionDetailsUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Transaction Details"));

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create and add field labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Date:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Category:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Amount:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Description:"), gbc);

        // Create and add value labels
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        idLabel = new JLabel("");
        add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dateLabel = new JLabel("");
        add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        categoryLabel = new JLabel("");
        add(categoryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        amountLabel = new JLabel("");
        add(amountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        descriptionLabel = new JLabel("");
        add(descriptionLabel, gbc);
    }

    public void displayTransaction(Transaction transaction) {
        if (transaction == null) {
            clearFields();
            return;
        }

        idLabel.setText(String.valueOf(transaction.getId()));
        dateLabel.setText(dateFormat.format(transaction.getDate()));
        categoryLabel.setText(transaction.getCategory().toString());
        amountLabel.setText(String.format("%.2f", transaction.getAmount()));
        descriptionLabel.setText(transaction.getDescription());
    }

    private void clearFields() {
        idLabel.setText("");
        dateLabel.setText("");
        categoryLabel.setText("");
        amountLabel.setText("");
        descriptionLabel.setText("");
    }
}