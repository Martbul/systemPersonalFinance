package main.core;

        import main.core.Category;
        import main.customDynamcStructures.LinkedList;

        import java.util.ArrayList;
        import java.util.Date;
        import java.util.stream.Collectors;


public class TransactionController {
    private LinkedList<Transaction> transactions;
    private TransactionDAO dataAccess; // Data access object for persistence


    public TransactionController() {
        dataAccess = new TransactionDAO();
        transactions = dataAccess.loadAllTransactions();
    }


    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }


    public boolean addTransaction(Transaction transaction) {
        // Validate transaction
        if (!validateTransaction(transaction)) {
            return false;
        }

        // Add to in-memory list
        transactions.add(transaction);

        // Save to persistent storage
        return dataAccess.saveTransaction(transaction);
    }


    public boolean updateTransaction(Transaction transaction) {
        // Validate transaction
        if (!validateTransaction(transaction) || transaction.getId() <= 0) {
            return false;
        }

        // Find and update in-memory list
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == transaction.getId()) {
                transactions.set(i, transaction);

                // Update in persistent storage
                return dataAccess.updateTransaction(transaction);
            }
        }

        return false; // Transaction not found
    }

    public boolean deleteTransaction(int transactionId) {
        // Find and remove from in-memory list
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == transactionId) {
                transactions.remove(i);

                // Remove from persistent storage
                return dataAccess.deleteTransaction(transactionId);
            }
        }

        return false; // Transaction not found
    }

    public List<Transaction> searchTransactions(String keyword, Category category,
                                                Date startDate, Date endDate,
                                                Double minAmount, Double maxAmount) {
        // Start with all transactions
        List<Transaction> results = new ArrayList<>(transactions);

        // Apply filters
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            results = results.stream()
                    .filter(t -> t.getDescription().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }

        if (category != null) {
            results = results.stream()
                    .filter(t -> t.getCategory() == category)
                    .collect(Collectors.toList());
        }

        if (startDate != null) {
            results = results.stream()
                    .filter(t -> !t.getDate().before(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            results = results.stream()
                    .filter(t -> !t.getDate().after(endDate))
                    .collect(Collectors.toList());
        }

        if (minAmount != null) {
            results = results.stream()
                    .filter(t -> t.getAmount() >= minAmount)
                    .collect(Collectors.toList());
        }

        if (maxAmount != null) {
            results = results.stream()
                    .filter(t -> t.getAmount() <= maxAmount)
                    .collect(Collectors.toList());
        }

        return results;
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getCategory() == Category.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getCategory() != Category.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpenses();
    }


    private boolean validateTransaction(Transaction transaction) {
        // Basic validation rules
        if (transaction == null) {
            return false;
        }

        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            return false;
        }

        if (transaction.getDate() == null) {
            return false;
        }

        if (transaction.getCategory() == null) {
            return false;
        }

        // Amount validation (could be more complex based on requirements)
        if (transaction.getCategory() == Category.INCOME && transaction.getAmount() <= 0) {
            return false; // Income should be positive
        }

        return true;
    }


    public void refreshData() {
        transactions = dataAccess.loadAllTransactions();
    }
}