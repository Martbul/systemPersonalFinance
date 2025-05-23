package main.core;

        import main.customDynamcStructures.LinkedList;
        import java.io.*;


public class DataAccess {
    private static final String DATA_FILE = "transactions.csv";

    public boolean saveTransaction(Transaction transaction) {
        try {
            FileWriter fw = new FileWriter(DATA_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(transaction.toFileString());
            bw.newLine();
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Грешка при запазване на транзакцията " + e.getMessage());
            return false;
        }
    }


    public boolean updateTransaction(Transaction transaction) {
        LinkedList<Transaction> allTransactions = loadAllTransactions();

        boolean found = false;
        for (int i = 0; i < allTransactions.size(); i++) {
            if (allTransactions.get(i).getId() == transaction.getId()) {
                allTransactions.set(i, transaction);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return saveAllTransactions(allTransactions);
    }

    public boolean deleteTransaction(int transactionId) {
        LinkedList<Transaction> allTransactions = loadAllTransactions();

        boolean found = false;
        for (int i = 0; i < allTransactions.size(); i++) {
            if (allTransactions.get(i).getId() == transactionId) {
                allTransactions.remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return saveAllTransactions(allTransactions);
    }

    public LinkedList<Transaction> loadAllTransactions() {
        LinkedList<Transaction> transactions = new LinkedList<>();

        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
                return transactions;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Transaction transaction = Transaction.fromFileString(line);
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.err.println("грешка: " + e.getMessage());
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Грешка: " + e.getMessage());
        }

        return transactions;
    }


    private boolean saveAllTransactions(LinkedList<Transaction> transactions) {
        try {
            FileWriter fw = new FileWriter(DATA_FILE, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < transactions.size(); i++) {
                bw.write(transactions.get(i).toFileString());
                bw.newLine();
            }

            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Грешка: " + e.getMessage());
            return false;
        }
    }
}