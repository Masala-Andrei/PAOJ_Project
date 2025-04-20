package Services;

import Models.Account;
import Models.Card;
import Models.CreditCard;
import Models.DebitCard;

import java.util.ArrayList;
import java.util.List;

public class CardService extends Debug {
    private List<Card> cards;

    public CardService() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getAllCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        if (debug)
            System.out.println("Card added to the system.");
    }

    public void removeCard(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
            System.out.println("Card removed from the system.");
        } else {
            System.out.println("Card not found in the system.");
        }
    }

    public DebitCard issueDebitCard(String cardholderName, Account linkedAccount) {
        if (linkedAccount == null) {
            System.out.println("Cannot issue debit card: No linked account provided.");
            return null;
        }

        DebitCard card = new DebitCard(cardholderName, linkedAccount);
        addCard(card);
        if (debug)
            System.out.println("Debit card issued to " + cardholderName);
        return card;
    }

    public CreditCard issueCreditCard(String cardholderName, Account linkedAccount, double creditLimit, double interestRate) {
        if (linkedAccount == null) {
            System.out.println("Cannot issue credit card: No linked account provided.");
            return null;
        }

        CreditCard card = new CreditCard(cardholderName, linkedAccount, creditLimit, interestRate);
        addCard(card);
        if (debug)
            System.out.println("Credit card issued to " + cardholderName + " with credit limit of " + creditLimit);
        return card;
    }

    public void blockCard(Card card) {
        if (card != null) {
            card.setActive(false);
            System.out.println("Card has been blocked.");
        }
    }

    public void unblockCard(Card card) {
        if (card != null) {
            card.setActive(true);
            System.out.println("Card has been unblocked.");
        }
    }

    public void resetDailyLimits(DebitCard card) {
        if (card != null) {
            card.resetDailyLimits();
            System.out.println("Daily limits have been reset for the card.");
        }
    }

    public Card findCardById(String cardId) {
        for (Card card : cards) {
            if (card.getCardId().equals(cardId)) {
                return card;
            }
        }
        return null;
    }

    public List<Card> getCardsByAccountId(String accountId) {
        List<Card> result = new ArrayList<>();
        for (Card card : cards) {
            Account linkedAccount = card.getLinkedAccount();
            if (linkedAccount != null && Integer.toString(linkedAccount.getIdAccount()).equals(accountId)) {
                result.add(card);
            }
        }
        return result;
    }


    public void processCardPayment(CreditCard card, double amount) {
        try {
            card.makePayment(amount);
            System.out.println("Payment of " + amount + " processed successfully.");
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
        }
    }
}