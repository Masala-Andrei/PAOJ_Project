package Models;

public class SavingsAccount extends Account {
    private String interestRate;
    private int commitmentPeriod;

    public SavingsAccount(String type, String name, String balance, String interestRate, int commitmentPeriod) {
        super(type, name, balance);
        this.interestRate = interestRate;
        this.commitmentPeriod = commitmentPeriod;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public int getCommitmentPeriod() {
        return commitmentPeriod;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public void setCommitmentPeriod(int commitmentPeriod) {
        this.commitmentPeriod = commitmentPeriod;
    }

    public void calculateInterest() {
        double balanceAmount = Double.parseDouble(this.balance);
        double interestRateValue = Double.parseDouble(this.interestRate);
        double interestAmount = balanceAmount * (interestRateValue / 100);

        double newBalance = balanceAmount + interestAmount;
        this.balance = String.valueOf(newBalance);
    }

    @Override
    public void withdraw(String amount) {
        double currentBalance = Double.parseDouble(this.balance);
        double withdrawAmount = Double.parseDouble(amount);

        if (withdrawAmount <= currentBalance) {
            double newBalance = currentBalance - withdrawAmount;
            this.balance = String.valueOf(newBalance);
            System.out.println("Withdrawal successful. New balance: " + this.balance);
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    @Override
    public void deposit(String amount) {
        double currentBalance = Double.parseDouble(this.balance);
        double depositAmount = Double.parseDouble(amount);
        double newBalance = currentBalance + depositAmount;
        this.balance = String.valueOf(newBalance);
        System.out.println("Deposit successful. New balance: " + this.balance);
    }

    @Override
    public String getAccountInfo() {
        return "Savings Account: " + this.name +
                "\nBalance: " + this.balance +
                "\nInterest Rate: " + this.interestRate + "%" +
                "\nCommitment Period: " + this.commitmentPeriod + " months";
    }
}