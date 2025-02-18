package com.myApplication.blackjack;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame implements Serializable {
    private Deck deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private List<Card> firstHand;
    private List<Card> secondHand;
    boolean HitFirstHand = true;
    boolean isSplit = false;

    private double balance;
    private double bet;

    public BlackjackGame() {
        deck = new Deck();
        deck.shuffle();

        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // for the split
        firstHand = new ArrayList<>();
        secondHand = new ArrayList<>();
    }

    public BlackjackGame(double balance, double bet){
        this.balance = balance;
        this.bet = bet;
    }

    public void dealInitialCards() {
        Card firstCard = deck.drawCard();
        Card secondCard = deck.drawCard();

        playerHand.add(firstCard);
        dealerHand.add(deck.drawCard());
        playerHand.add(secondCard);
        dealerHand.add(deck.drawCard());

        firstHand.add(firstCard);
        secondHand.add(secondCard);
    }

    public int getPlayerScore() {
        return calculateScore(playerHand);
    }

    public int getDealerScore() {
        return calculateScore(dealerHand);
    }

    public int getFirstHandScore(){return calculateScore(firstHand); }

    public int getSecondHandScore(){return calculateScore(secondHand); }

    private int calculateScore(List<Card> hand) {
        int score = 0;
        int countAces = 0;

        for (Card card : hand) {
            score += card.getValue();
            if(card.getValue() == 11){
                countAces++;
            }
        }

        if (score > 21 && countAces >= 1) {
            score -= 10;
            if(score > 21 && countAces >= 2){
                score -= 10;
                if(score > 21 && countAces >= 3){
                    score -= 10;
            }
            }
        }

        return score;
    }

    public void playerHit() {
        playerHand.add(deck.drawCard());
    }

    public void firstHandHit() {
        Card card = deck.drawCard();

        while(card.getValue() == firstHand.get(0).getValue()){
            card = deck.drawCard();
        }

        firstHand.add(card);
    }

    public void secondHandHit() {
        Card card = deck.drawCard();

        while(card.getValue() == secondHand.get(0).getValue()){
            card = deck.drawCard();
        }

        secondHand.add(card);
    }

    public void dealerHit() { dealerHand.add(deck.drawCard()); }

    public void playerDouble() { playerHand.add(deck.drawCard()); }

    public void firstHandDouble() { firstHand.add(deck.drawCard()); }

    public void secondHandDouble() { secondHand.add(deck.drawCard()); }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public List<Card> getFirstHand() {
        return firstHand;
    }

    public List<Card> getSecondHand() {
        return secondHand;
    }

    public List<Card> getDealerHand() {
        return dealerHand;
    }

    public boolean isPlayerBust() {
        return getPlayerScore() > 21;
    }

    public boolean isFirstHandBust() {
        return getFirstHandScore() > 21;
    }

    public boolean isSecondHandBust() {
        return getSecondHandScore() > 21;
    }

    public boolean isDealerBust() {
        return getDealerScore() > 21;
    }


    public String gameResult(){
        if (isPlayerBust()) {
            return "lose";
        } else if (isDealerBust()) {
            return "Win";
        } else if(getPlayerScore() > getDealerScore()){
             return "Win";
        } else if(getPlayerScore() < getDealerScore()){
            return "Lose";
        }

        return "Push";
    }

    public boolean isGameOver() {
        return isPlayerBust() || isDealerBust();
    }


    public void split() {
        // Check if the player has two cards of the same rank
        if (playerHand.size() == 2 && playerHand.get(0).getRank() == playerHand.get(1).getRank()) {
            // Create two separate hands with one card each
            List<Card> firstHand = new ArrayList<>();
            firstHand.add(playerHand.get(0));

            List<Card> secondHand = new ArrayList<>();
            secondHand.add(playerHand.get(1));

            // Deal one card to each hand from the deck
            firstHand.add(deck.drawCard());
            secondHand.add(deck.drawCard());
        }
    }

    public String determineAction(List<Card> playerHand, Card dealerFirstCard) {
        int playerTotal = calculateScore(playerHand);
        int dealerValue = dealerFirstCard.getValue();

        int firstCard = playerHand.get(0).getValue();
        int secondCard;

        if(playerHand.size() > 1){
            secondCard = playerHand.get(1).getValue();
        } else{
            secondCard = -1;
        }

        Log.d("allCards", String.valueOf(firstCard));
        Log.d("allCards", String.valueOf(secondCard));

        // Split hands

        if(firstCard == secondCard
            && playerHand.size() == 2){
            // 2,2 or 3,3
            if(firstCard <= 3){
                if (dealerValue >= 4 && dealerValue <= 7) {
                    return "Split";
                } else {
                    return "Hit";
                }
            }

            // 4,4
            if(firstCard == 4){
                return "Hit";
            }

            // 5,5
            if(firstCard == 5){
                if (dealerValue <= 9) {
                    return "Double";
                } else {
                    return "Hit";
                }
            }

            // 6,6
            if(firstCard == 6){
                if (dealerValue >= 3 && dealerValue <= 6) {
                    return "Split";
                } else {
                    return "Hit";
                }
            }

            // 7,7
            if(firstCard == 7){
                if (dealerValue <= 7) {
                    return "Split";
                } else {
                    return "Hit";
                }
            }

            // 8,8
            if(firstCard == 8){
                return "Split";
            }

            // 9,9
            if(firstCard == 9){
                if (dealerValue == 7 || dealerValue >= 10) {
                    return "Stand";
                } else {
                    return "Split";
                }
            }

            // 10,10
            if(firstCard == 10){
                return "Stand";
            }

            // A,A
            return "Split";
        }

        // soft hands - Ace counted as 11:
        if(isSoft(playerHand)){


            Log.d("softCheck", "soft check");

            // A,2 or A,3
            if(playerTotal <= 14){
                if (dealerValue == 5 || dealerValue == 6 && playerHand.size() == 2) {
                    return "Double";
                } else {
                    return "Hit";
                }
            }

            // A,4 or A,5
            if(playerTotal == 15 || playerTotal == 16){
                if (dealerValue >= 4 && dealerValue <= 6  && playerHand.size() == 2) {
                    return "Double";
                } else {
                    return "Hit";
                }
            }

            // A,6
            if(playerTotal == 17){
                if (dealerValue >= 3 && dealerValue <= 6  && playerHand.size() == 2) {
                    return "Double";
                } else {
                    return "Hit";
                }
            }

            // A,7
            if(playerTotal == 18){
                if (dealerValue == 2) {
                    return "Stand";
                } else if (dealerValue >= 3 && dealerValue <= 6 && playerHand.size() == 2){
                    return "Double";
                } else if (dealerValue >= 7 && dealerValue <= 8){
                    return "Stand";
                } else {
                    return "Hit";
                }
            }

            // A,8 or A,9
            return "Stand";

        // Hard hands - hand is not including Ace or Ace counted as 1
        } else {
            if (playerTotal >= 17) {
                return "Stand";
            } else if (playerTotal >= 13) {
                if (dealerValue <= 6) {
                    return "Stand";
                } else {
                    return "Hit";
                }
            } else if (playerTotal == 12) {
                if (dealerValue >= 4 && dealerValue <= 6) {
                    return "Stand";
                } else {
                    return "Hit";
                }
            } else if (playerTotal == 11) {
                if(dealerValue != 11 && playerHand.size() == 2){
                    return "Double";
                } else {
                    return "Hit";
                }

            } else if (playerTotal == 10) {
                if (dealerValue <= 9 && playerHand.size() == 2) {
                    return "Double";
                } else {
                    return "Hit";
                }

            } else if (playerTotal == 9) {
                if (dealerValue >= 3 && dealerValue <= 6 && playerHand.size() == 2) {
                    return "Double";
                } else {
                    return "Hit";
                }
            } else {
                return "Hit";
            }
        }
    }

    private boolean playerHandContainsAce(List<Card> hand) {
        for (Card card : hand) {
            if (card.getValue() == 11) {
                return true;
            }
        }
        return false;
    }

    private boolean isSoft(List<Card> hand){
        int score = 0;

        for (Card card : hand) {
            score += card.getValue();
        }

        if (playerHandContainsAce(hand) && score < 21){

            return true;
        }

        return false;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }
}