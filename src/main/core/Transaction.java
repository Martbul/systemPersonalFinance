package main.core;

        import java.text.SimpleDateFormat;
        import java.util.Date;

public class Transaction {
    private double amount;
    private Category category;
    private Date date;
    private int id;
    private String description;

    public Transaction() {
        this.date = new Date();
    }

    public Transaction(double amount, Category category, Date date, String description) {
        setAmount(amount);
        setCategory(category);
        this.date = date != null ? date : new Date();
        this.description = description != null ? description : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0 && (this.category != null && this.category != Category.INCOME)) {
            throw new Error("Сумата трбва да е положителна");
        }
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;

        if (category != Category.INCOME && this.amount < 0) {
            throw new Error("Сумата трбва да е положителна");
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date != null ? date : new Date();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("%s | %s | %.2f | %s",
                dateFormat.format(date),
                category,
                amount,
                description);
    }

    public String toFileString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("%s,%s,%.2f,%s,%d",
                dateFormat.format(date),
                category.name(),
                amount,
                description,
                id);
    }

    public static Transaction fromFileString(String fileString) throws Exception {
        String[] parts = fileString.split(",");

        if (parts.length < 5) {
            throw new Error("Грешен фила формат");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = dateFormat.parse(parts[0]);
        Category category = Category.valueOf(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        String description = parts[3];
        int id = Integer.parseInt(parts[4]);

        Transaction transaction = new Transaction(amount, category, date, description);
        transaction.setId(id);
        return transaction;
    }

    public Transaction copy() {
        Transaction copy = new Transaction(this.amount, this.category, (Date) this.date.clone(), this.description);
        copy.setId(this.id);
        return copy;
    }
}