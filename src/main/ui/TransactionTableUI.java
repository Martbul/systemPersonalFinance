package main.ui;

        import main.core.Transaction;
        import main.customDynamcStructures.LinkedList;

        import javax.swing.table.AbstractTableModel;
        import java.text.SimpleDateFormat;
        import java.util.Date;


public class TransactionTableUI extends AbstractTableModel {
        private LinkedList<Transaction> transactions;
        private final String[] columnNames = {"ID", "Date", "Category", "Amount", "Description"};
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public TransactionTableUI(LinkedList<Transaction> transactions) {
                this.transactions = transactions;
        }

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
        public Object getValueAt(int rowIndex, int columnIndex) {
                if (rowIndex >= transactions.size()) {
                        return null;
                }

                Transaction transaction = transactions.get(rowIndex);

                switch (columnIndex) {
                        case 0: return transaction.getId();
                        case 1: return dateFormat.format(transaction.getDate());
                        case 2: return transaction.getCategory();
                        case 3: return String.format("%.2f", transaction.getAmount());
                        case 4: return transaction.getDescription();
                        default: return null;
                }
        }

        /**
         * Get the transaction at the specified row
         */
        public Transaction getTransactionAt(int rowIndex) {
                if (rowIndex >= 0 && rowIndex < transactions.size()) {
                        return transactions.get(rowIndex);
                }
                return null;
        }

        /**
         * Refresh the data in the table model
         */
        public void refreshData(LinkedList<Transaction> newTransactions) {
                this.transactions = newTransactions;
        }
}