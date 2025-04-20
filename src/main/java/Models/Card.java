package Models;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public abstract class Card {
    protected String cardId;
    protected String cardNumber;
    protected String cardHolderName;
    protected LocalDate expiryDate;
    protected String cvv;
    protected boolean active;
    protected Account linkedAccount;

    public Card(String cardHolderName, Account linkedAccount) {
        this.cardId = UUID.randomUUID().toString();
        this.cardNumber = generateCardNumber();
        this.cardHolderName = cardHolderName;
        this.expiryDate = LocalDate.now().plusYears(3);
        this.cvv = generateCVV();
        this.active = true;
        this.linkedAccount = linkedAccount;
    }

    private String generateCardNumber() {
        return "4" + String.format("%015d", (long) (Math.random() * 1000000000000000L));
    }

    private String generateCVV() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(900) + 100);
    }

    public String getCardId() {
        return cardId;
    }

    public String getMaskedCardNumber() {
        return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public Account getLinkedAccount() {
        return linkedAccount;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLinkedAccount(Account linkedAccount) {
        this.linkedAccount = linkedAccount;
    }

    public abstract boolean authorize(double amount);
    public abstract void processTransaction(double amount, String merchant);
}