package main.core;

import main.customDynamcStructures.LinkedList;

import java.util.Date;

public class TransactionController {
    private LinkedList<Transaction> transactions;
    private int idCounter;
    private DataAccess dataAccess;

    public TransactionController() {
        transactions = new LinkedList<>();
        idCounter = 1;
        dataAccess = new DataAccess();
    }

    public LinkedList<Transaction> getAllTransactions() {
        return transactions;
    }

    public boolean addTransaction(Transaction transaction) {
        if (!validateTransaction(transaction)) {
            return false;
        }

        transaction.setId(idCounter);
        transactions.add(transaction);
        idCounter++;

        return dataAccess.saveTransaction(transaction);
    }

    public boolean updateTransaction(Transaction transaction) {
        if (!validateTransaction(transaction) || transaction.getId() <= 0) {
            return false;
        }

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == transaction.getId()) {
                transactions.set(i, transaction);

                return dataAccess.updateTransaction(transaction);
            }
        }

        return false;
    }

    public boolean deleteTransaction(int transactionId) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == transactionId) {
                transactions.remove(i);

                return dataAccess.deleteTransaction(transactionId);
            }
        }

        return false;
    }

    public LinkedList<Transaction> searchTransactions(String keyword, Category category,
                                                      Date startDate, Date endDate,
                                                      Double minAmount, Double maxAmount) {
        LinkedList<Transaction> results = new LinkedList<>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            boolean matches = true;

            if (keyword != null && !keyword.isEmpty()) {
                String lowerKeyword = keyword.toLowerCase();
                if (t.getDescription() == null ||
                        !t.getDescription().toLowerCase().contains(lowerKeyword)) {
                    matches = false;
                }
            }

            if (matches && category != null && t.getCategory() != category) {
                matches = false;
            }

            if (matches && startDate != null && t.getDate().before(startDate)) {
                matches = false;
            }

            if (matches && endDate != null && t.getDate().after(endDate)) {
                matches = false;
            }

            if (matches && minAmount != null && t.getAmount() < minAmount) {
                matches = false;
            }

            if (matches && maxAmount != null && t.getAmount() > maxAmount) {
                matches = false;
            }

            if (matches) {
                results.add(t);
            }
        }

        return results;
    }

    public double getTotalIncome() {
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getCategory() == Category.INCOME) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getTotalExpenses() {
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getCategory() != Category.INCOME) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    private boolean validateTransaction(Transaction transaction) {
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

        if (transaction.getCategory() == Category.INCOME && transaction.getAmount() <= 0) {
            return false;
        }

        return true;
    }

    public void refreshData() {
        transactions = dataAccess.loadAllTransactions();

        idCounter = 1;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() >= idCounter) {
                idCounter = transactions.get(i).getId() + 1;
            }
        }
    }
}
