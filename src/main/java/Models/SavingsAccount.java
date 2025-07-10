package Models;

public class SavingsAccount extends Account {
    private String interestRate;
    private int commitmentPeriod;

    public SavingsAccount(String type, String name, String balance, String interestRate, int commitmentPeriod, String IBAN) {
        super(type, name, balance, IBAN);
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
    public String toString() {
        return "Savings Account: " + this.name +
                "\nBalance: " + this.balance +
                "\nInterest Rate: " + this.interestRate + "%" +
                "\nCommitment Period: " + this.commitmentPeriod + " months";
    }
}