package Models;

public class DebitCard extends Card {
    private double dailyWithdrawalLimit;
    private double dailyPurchaseLimit;
    private double dailyWithdrawalsToday;
    private double dailyPurchasesToday;

    public DebitCard(String cardHolderName, Account linkedAccount) {
        super(cardHolderName, linkedAccount);
        this.dailyWithdrawalLimit = 1000.0;
        this.dailyPurchaseLimit = 2000.0;
        this.dailyWithdrawalsToday = 0.0;
        this.dailyPurchasesToday = 0.0;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public double getDailyPurchaseLimit() {
        return dailyPurchaseLimit;
    }

    public double getDailyWithdrawalsToday() {
        return dailyWithdrawalsToday;
    }

    public double getDailyPurchasesToday() {
        return dailyPurchasesToday;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public void setDailyPurchaseLimit(double dailyPurchaseLimit) {
        this.dailyPurchaseLimit = dailyPurchaseLimit;
    }

    public void resetDailyLimits() {
        this.dailyWithdrawalsToday = 0.0;
        this.dailyPurchasesToday = 0.0;
    }

    @Override
    public boolean authorize(double amount) {
        if (!active) {
            return false;
        }

        if (linkedAccount == null) {
            return false;
        }

        double accountBalance = Double.parseDouble(linkedAccount.getBalance());

        if (amount > accountBalance) {
            return false;
        }

        if (dailyPurchasesToday + amount > dailyPurchaseLimit) {
            return false;
        }

        return true;
    }

    @Override
    public void processTransaction(double amount, String merchant) {
        if (!authorize(amount)) {
            throw new IllegalStateException("Transaction not authorized");
        }

        linkedAccount.withdraw(String.valueOf(amount));
        dailyPurchasesToday += amount;

        Transaction transaction = new Transaction(
                generateTransactionId(),
                linkedAccount.getName(),
                merchant,
                String.valueOf(amount),
                "Purchase at " + merchant + " using card ending " + getMaskedCardNumber().substring(17)
        );

        if (linkedAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) linkedAccount).addTransaction(transaction);
        }
    }

    public void processATMWithdrawal(double amount) {
        if (!active) {
            throw new IllegalStateException("Card inactive");
        }

        if (linkedAccount == null) {
            throw new IllegalStateException("No linked account");
        }

        double accountBalance = Double.parseDouble(linkedAccount.getBalance());
        if (amount > accountBalance) {
            throw new IllegalStateException("Insufficient funds");
        }

        if (dailyWithdrawalsToday + amount > dailyWithdrawalLimit) {
            throw new IllegalStateException("Daily withdrawal limit exceeded");
        }

        linkedAccount.withdraw(String.valueOf(amount));
        dailyWithdrawalsToday += amount;

        Transaction transaction = new Transaction(
                generateTransactionId(),
                linkedAccount.getName(),
                "ATM",
                String.valueOf(amount),
                "ATM withdrawal using card ending " + getMaskedCardNumber().substring(17)
        );

        if (linkedAccount instanceof TransactionsAccount) {
            ((TransactionsAccount) linkedAccount).addTransaction(transaction);
        }
    }

    private String generateTransactionId() {
        return "DC" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Debit Card: " + getMaskedCardNumber() +
                "\nHolder: " + cardHolderName +
                "\nExpiry: " + expiryDate.getMonth() + "/" + expiryDate.getYear() +
                "\nLinked to: " + linkedAccount.getName();
    }
}