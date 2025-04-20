package Models;

public class CreditCard extends Card {
    private double creditLimit;
    private double currentBalance;
    private double interestRate;
    private String billingCycle;
    private double minimumPayment;

    public CreditCard(String cardHolderName, Account linkedAccount, double creditLimit, double interestRate) {
        super(cardHolderName, linkedAccount);
        this.creditLimit = creditLimit;
        this.currentBalance = 0.0;
        this.interestRate = interestRate;
        this.billingCycle = "Monthly";
        this.minimumPayment = 0.05;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public double getMinimumPayment() {
        return minimumPayment * currentBalance;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public void setMinimumPayment(double minimumPayment) {
        this.minimumPayment = minimumPayment;
    }

    public void makePayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        currentBalance -= amount;
        if (currentBalance < 0) {
            currentBalance = 0;
        }

        Transaction transaction = new Transaction(
                generateTransactionId(),
                Integer.toString(linkedAccount.getIdAccount()),
                "Credit Card Payment",
                Double.toString(amount),
                "Payment towards credit card ending " + getMaskedCardNumber().substring(17)
        );

        if (linkedAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) linkedAccount).addTransaction(transaction);
        }
    }

    private String generateTransactionId() {
        return "CC" + System.currentTimeMillis();
    }

    @Override
    public boolean authorize(double amount) {
        if (!active) {
            return false;
        }

        if (currentBalance + amount > creditLimit) {
            return false;
        }

        return true;
    }

    @Override
    public void processTransaction(double amount, String merchant) {
        if (!authorize(amount)) {
            throw new IllegalStateException("Transaction not authorized");
        }

        currentBalance += amount;

        Transaction transaction = new Transaction(
                generateTransactionId(),
                "Credit Card",
                merchant,
                String.valueOf(amount),
                "Purchase at " + merchant + " using card ending " + getMaskedCardNumber().substring(17)
        );

        if (linkedAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) linkedAccount).addTransaction(transaction);
        }
    }

    public void calculateInterest() {
        double monthlyRate = interestRate / 12 / 100;
        double interest = currentBalance * monthlyRate;
        currentBalance += interest;
    }

    @Override
    public String toString() {
        return "Credit Card: " + getMaskedCardNumber() +
                "\nHolder: " + cardHolderName +
                "\nExpiry: " + expiryDate.getMonth() + "/" + expiryDate.getYear() +
                "\nAvailable Credit: " + (creditLimit - currentBalance) +
                "\nCurrent Balance: " + currentBalance;
    }
}