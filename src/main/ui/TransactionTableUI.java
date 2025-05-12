package main.ui;

        import main.core.Transaction;
        import main.customDynamcStructures.LinkedList;

        import javax.swing.table.AbstractTableModel;
        import java.text.SimpleDateFormat;

public class TransactionTableUI extends AbstractTableModel {
        private LinkedList<Transaction> transactions;
        private final String[] columnNames = {"ID", "Дата", "Категория", "Сума", "Описание"};
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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


        public Transaction getTransactionAt(int rowIndex) {
                if (rowIndex >= 0 && rowIndex < transactions.size()) {
                        return transactions.get(rowIndex);
                }
                return null;
        }


        public void refreshData(LinkedList<Transaction> newTransactions) {
                this.transactions = newTransactions;
        }
}