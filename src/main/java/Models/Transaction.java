package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private String amount;
    private String description;
    private LocalDateTime timestamp;
    private String status;

    public Transaction(String transactionId, String fromAccount, String toAccount,
                       String amount, String description) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = "Pending";
    }

    public String getTransactionId() {
        return transactionId;
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp.format(formatter) +
                ", status='" + status + '\'' +
                '}';
    }
}