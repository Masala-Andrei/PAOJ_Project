package Models;

import java.util.ArrayList;

public class TransactionsAccount extends Account {
    private ArrayList<Transaction> transactions;
    private double monthlyFee;


    public TransactionsAccount(String type, String name, String balance, double monthlyFee, String IBAN) {
        super(type, name, balance, IBAN);
        this.transactions = new ArrayList<>();
        this.monthlyFee = monthlyFee;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }


    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void applyMonthlyFee() {
        double currentBalance = Double.parseDouble(this.balance);
        double newBalance = currentBalance - monthlyFee;
        this.balance = String.valueOf(newBalance);
        System.out.println("Monthly fee of " + monthlyFee + " applied. New balance: " + this.balance);
    }

//    @Override
//    public void withdraw(String amount) {
//        if (withdrawalsThisMonth >= withdrawalLimit) {
//            System.out.println("Withdrawal limit reached for this month!");
//            return;
//        }
//
//        double currentBalance = Double.parseDouble(this.balance);
//        double withdrawAmount = Double.parseDouble(amount);
//
//        if (withdrawAmount <= currentBalance) {
//            double newBalance = currentBalance - withdrawAmount;
//            this.balance = String.valueOf(newBalance);
//            withdrawalsThisMonth++;
//
//            Transaction transaction = new Transaction(
//                    "W" + System.currentTimeMillis(),
//                    this.name,
//                    "Cash",
//                    amount,
//                    "Withdrawal"
//            );
//            transaction.completeTransaction();
//            addTransaction(transaction);
//
//            System.out.println("Withdrawal successful. New balance: " + this.balance);
//        } else {
//            System.out.println("Insufficient funds!");
//        }
//    }

//    @Override
//    public void deposit(String amount) {
//        double currentBalance = Double.parseDouble(this.balance);
//        double depositAmount = Double.parseDouble(amount);
//        double newBalance = currentBalance + depositAmount;
//        this.balance = String.valueOf(newBalance);
//
//        Transaction transaction = new Transaction(
//                "D" + System.currentTimeMillis(),
//                "Cash",
//                this.name,
//                amount,
//                "Deposit"
//        );
//        transaction.completeTransaction();
//        addTransaction(transaction);
//
//        System.out.println("Deposit successful. New balance: " + this.balance);
//    }

    public void transfer(TransactionsAccount recipient, String amount) {
        double transferAmount = Double.parseDouble(amount);
        double currentBalance = Double.parseDouble(this.balance);

        if (transferAmount <= currentBalance) {
            Transaction transaction = new Transaction(
                    "T" + System.currentTimeMillis(),
                    this.name,
                    recipient.getName(),
                    amount,
                    "Transfer"
            );

            double newSenderBalance = currentBalance - transferAmount;
            this.balance = String.valueOf(newSenderBalance);

            double recipientBalance = Double.parseDouble(recipient.getBalance());
            double newRecipientBalance = recipientBalance + transferAmount;
            recipient.setBalance(String.valueOf(newRecipientBalance));

            transaction.completeTransaction();
            addTransaction(transaction);
            recipient.addTransaction(transaction);

            System.out.println("Transfer successful. New balance: " + this.balance);
        } else {
            System.out.println("Insufficient funds for transfer!");
        }
    }

    @Override
    public String getAccountInfo() {
        return "Transaction Account: " + this.name +
                "\nBalance: " + this.balance +
                "\nMonthly Fee: " + this.monthlyFee;
    }
}