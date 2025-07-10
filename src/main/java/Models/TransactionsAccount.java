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


    @Override
    public String toString() {
        return "Transaction Account: " + this.name +
                "\nBalance: " + this.balance +
                "\nMonthly Fee: " + this.monthlyFee;
    }
}