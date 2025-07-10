package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String fromAccount;
    private String toAccount;
    private String amount;
    private String description;
    private LocalDateTime timestamp;
    private String status;

    public Transaction( String fromAccountIBAN, String toAccountIBAN,
                       String amount, String description) {
        this.fromAccount = fromAccountIBAN;
        this.toAccount = toAccountIBAN;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = "Pending";
    }

    public Transaction( String fromAccountIBAN, String toAccountIBAN,
                        String amount, String description, String status, LocalDateTime timestamp) {
        this.fromAccount = fromAccountIBAN;
        this.toAccount = toAccountIBAN;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void completeTransaction() {
        this.status = "Completed";
    }

    public void failTransaction(String reason) {
        this.status = "Failed: " + reason;
    }

    public void showDetailedTransaction(int accId){
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        System.out.println("=======================");
        System.out.println("Date:" + timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println("Time: " + timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        if (accId == Integer.parseInt(fromAccount)) {
            System.out.println("Sent money to: " + toAccount);
            System.out.println("Amount: " + ANSI_RED + "-" + amount);
            System.out.println(ANSI_RESET);
        } else {
            System.out.println("Received money from: " + fromAccount);
            System.out.println("Amount: " + ANSI_GREEN + "+" + amount);
            System.out.println(ANSI_RESET);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Transaction{" +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp.format(formatter) +
                ", status='" + status + '\'' +
                '}';
    }
}