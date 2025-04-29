package main.core;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Transaction {
    private double amount;
    private Category category;
    private Date date;


    public Transaction() {
        this.date = new Date();
    }

    public Transaction(double amount, Category category, Date date) {
        setAmount(amount);
        setCategory(category);
        this.date = date != null ? date : new Date();
    }

    public double getAmount() {
        return amount;
    }


    public void setAmount(double amount) {
        if (amount < 0 && (this.category != null && this.category != Category.INCOME)) {
            throw new Error("Amount cannot be negative for expenses. Use positive values.");
        }
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;

        if (category != Category.INCOME && this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative for expenses. Use positive values.");
        }
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date != null ? date : new Date();
    }


    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s | %s | %.2f | %s",
                dateFormat.format(date),
                category,
                amount);
    }


    public String toFileString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s,%s,%.2f,%s",
                dateFormat.format(date),
                category.name(),
                amount);
    }


    public static Transaction fromFileString(String fileString) throws Exception {
        String[] parts = fileString.split(",");

        if (parts.length < 4) {
            throw new Exception("Invalid transaction file format");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(parts[0]);
        Category category = Category.valueOf(parts[1]);
        double amount = Double.parseDouble(parts[2]);

        return new Transaction(amount, category, date);
    }

    public Transaction copy() {
        return new Transaction(this.amount, this.category, (Date) this.date.clone());
    }
}