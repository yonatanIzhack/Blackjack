package com.example.blackjack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private BlackjackGame game;
    private TextView resultTextView, dealerHandTextView, playerHandTextView;
    private Button hitButton, standButton, doubleButton, splitButton, newGameButton;

    private ImageView playerImg1, playerImg2, playerImg3, playerImg4, playerImg5, playerImg6, playerImg7;
    private ImageView dealerImg1, dealerImg2, dealerImg3, dealerImg4, dealerImg5, dealerImg6, dealerImg7;

    private List<ImageView> playerImages;
    private List<ImageView> dealerImages;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        dealerHandTextView = findViewById(R.id.dealerHandTextView);
        playerHandTextView = findViewById(R.id.playerHandTextView);

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);
        newGameButton = findViewById(R.id.newGameButton);
        doubleButton = findViewById(R.id.doubleButton);
        splitButton = findViewById(R.id.splitButton);

        playerImg1 = findViewById(R.id.playerImg1);;
        playerImg2 = findViewById(R.id.playerImg2);;
        playerImg3 = findViewById(R.id.playerImg3);;
        playerImg4 = findViewById(R.id.playerImg4);;
        playerImg5 = findViewById(R.id.playerImg5);;
        playerImg6 = findViewById(R.id.playerImg6);;
        playerImg7 = findViewById(R.id.playerImg7);;

        playerImages = new ArrayList<>();
        playerImages.add(playerImg1);
        playerImages.add(playerImg2);
        playerImages.add(playerImg3);
        playerImages.add(playerImg4);
        playerImages.add(playerImg5);
        playerImages.add(playerImg6);
        playerImages.add(playerImg7);


        dealerImg1 = findViewById(R.id.dealerImg1);;
        dealerImg2 = findViewById(R.id.dealerImg2);;
        dealerImg3 = findViewById(R.id.dealerImg3);;
        dealerImg4 = findViewById(R.id.dealerImg4);;
        dealerImg5 = findViewById(R.id.dealerImg5);;
        dealerImg6 = findViewById(R.id.dealerImg6);;
        dealerImg7 = findViewById(R.id.dealerImg7);;

        dealerImages = new ArrayList<>();
        dealerImages.add(dealerImg1);
        dealerImages.add(dealerImg2);
        dealerImages.add(dealerImg3);
        dealerImages.add(dealerImg4);
        dealerImages.add(dealerImg5);
        dealerImages.add(dealerImg6);
        dealerImages.add(dealerImg7);

        splitButton.setVisibility(View.GONE);

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHit();
            }
        });

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerStand();
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        doubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerDouble();
            }
        });

        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerSplit();
            }
        });



        startNewGame();
    }

    private void startNewGame() {
        doubleButton.setVisibility(View.VISIBLE);
        splitButton.setVisibility(View.GONE);


        for(int i = 0; i < playerImages.size(); i++){
            playerImages.get(i).setImageResource(0);
        }

        for(int i = 0; i < dealerImages.size(); i++){
            dealerImages.get(i).setImageResource(0);
        }


        resultTextView.setText("");
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleButton.setEnabled(true);
        splitButton.setEnabled(true);
        newGameButton.setEnabled(true);

        game = new BlackjackGame();
        game.dealInitialCards();
        initialUpdateUI();
    }

    private void playerHit() {
        doubleButton.setVisibility(View.GONE);

        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));


        if(!optimalPlay.equals("Hit")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }


        game.playerHit();
        initialUpdateUI();

        if (game.isPlayerBust()) {
            endGame("Bust! You lose.");
        }
    }

    private void playerDouble() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));


        if(!optimalPlay.equals("Double")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        // Draw one additional card for the player
        game.playerDouble();

        updateUI();

        // Stand after doubling
        playerStand();
    }

    private void playerStand() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));


        if(!optimalPlay.equals("Stand")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        // Reveal the dealer's hidden card
        dealerTurn();
    }

    private void playerSplit() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));


        if(!optimalPlay.equals("Split")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        Intent intent = new Intent(MainActivity.this, SplitActivity.class);

        Card firstCard = game.getPlayerHand().get(0);
        Card secondCard = game.getPlayerHand().get(1);

        intent.putExtra("Game", game);


        startActivity(intent);
    }

    private void dealerTurn() {
        while (!game.isGameOver() && game.getDealerScore() < 17) {
            game.dealerHit();
        }

        updateUI();
        endGame(getResultMessage());
    }

    private void endGame(String message) {
        resultTextView.setText(message);

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        newGameButton.setEnabled(true);
    }

    private String getResultMessage() {
        if(game.isDealerBust()){
            return "Dealer bust! you win";
        } else if(game.isPlayerBust()){
            return "Busted! you lose";
        } else if (game.gameResult().equals("Lose")) {
            return "You lose.";
        } else if (game.gameResult().equals("Win")) {
            return "You win!";
        }
        return "Push";
    }

    @SuppressLint("SetTextI18n")
    private void initialUpdateUI() {
        dealerImg1.setImageResource(getCardImageView(game.getDealerHand().get(0)));


        updatePlayerHand();

        if(game.getPlayerHand().size() == 2 && game.getPlayerHand().get(0).getRank() == game.getPlayerHand().get(1).getRank()){
            splitButton.setVisibility(View.VISIBLE);
        }

        if(game.getPlayerScore() == 21){
            hitButton.setEnabled(false);
            doubleButton.setEnabled(false);
            splitButton.setEnabled(false);

            if(game.getPlayerHand().size() == 2){
                updateUI();
                if(game.getDealerScore() != 21){
                    endGame("Blackjack! you win");
                } else {
                    endGame("push");
                }

            }
        }

        if(game.getPlayerScore() == 21){
            hitButton.setEnabled(false);
            doubleButton.setEnabled(false);
            splitButton.setEnabled(false);

            if(game.getPlayerHand().size() == 2){
                updateUI();
                if(game.getDealerScore() != 21){
                    endGame("Blackjack! you win");
                } else {
                    endGame("push");
                }
            }
        }

        if(game.getDealerScore() == 21){
            hitButton.setEnabled(false);
            doubleButton.setEnabled(false);
            splitButton.setEnabled(false);

            if(game.getDealerHand().size() == 2){
                updateUI();
                if(game.getPlayerScore() != 21){
                    endGame("Blackjack! you lose");
                } else {
                    endGame("push");
                }

            }
        }


        playerHandTextView.setText("Your Hand: " + String.valueOf(game.getPlayerScore()));
        dealerHandTextView.setText("Dealer's Hand: " + String.valueOf(game.getDealerHand().get(0).getRank()));
    }

    private void updatePlayerHand() {


        Handler handler = new Handler();

        Runnable updateImageRunnable = new Runnable() {
            @Override
            public void run() {
                playerImages.get(0).setImageResource(getCardImageView(game.getPlayerHand().get(0)));

                handler.postDelayed(this, 1000);

                playerImages.get(1).setImageResource(getCardImageView(game.getPlayerHand().get(1)));

            }
        };

//        for(int i = 0; i < game.getPlayerHand().size(); i++){
//            try {
//                // Sleep for 1000 milliseconds (1 second)
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                // Handle interrupted exception (if needed)
//                e.printStackTrace();
//            }
//
//            Log.d("sleepCheck", game.getPlayerHand().get(i).toString());
//
//            playerImages.get(i).setImageResource(getCardImageView(game.getPlayerHand().get(i)));
//
//        }
    }

    @SuppressLint("SetTextI18n")
    private void splitUpdateUI() {
        doubleButton.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {

        for(int i = 0; i < game.getPlayerHand().size(); i++){
            playerImages.get(i).setImageResource(getCardImageView(game.getPlayerHand().get(i)));
        }

        for(int i = 0; i < game.getDealerHand().size(); i++){
            dealerImages.get(i).setImageResource(getCardImageView(game.getDealerHand().get(i)));
        }

        playerHandTextView.setText("Your hand: " + String.valueOf(game.getPlayerScore()));
        dealerHandTextView.setText("Dealer's Hand: " + String.valueOf(game.getDealerScore()));
    }

    private void showDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Do something when OK button is clicked
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int getCardImageView(Card card){
        String StringCard = card.toString();

        if (StringCard.contains("2")) {
            if (StringCard.contains("♣")) {
                return R.drawable.two_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.two_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.two_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.two_of_spades;
            }
        } else if (StringCard.contains("3")) {
            if (StringCard.contains("♣")) {
                return R.drawable.three_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.three_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.three_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.three_of_spades;
            }
        } else if (StringCard.contains("4")) {
            if (StringCard.contains("♣")) {
                return R.drawable.four_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.four_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.four_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.four_of_spades;
            }
        } else if (StringCard.contains("5")) {
            if (StringCard.contains("♣")) {
                return R.drawable.five_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.five_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.five_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.five_of_spades;
            }
        } else if (StringCard.contains("6")) {
            if (StringCard.contains("♣")) {
                return R.drawable.six_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.six_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.six_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.six_of_spades;
            }
        } else if (StringCard.contains("7")) {
            if (StringCard.contains("♣")) {
                return R.drawable.seven_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.seven_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.seven_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.seven_of_spades;
            }
        } else if (StringCard.contains("8")) {
            if (StringCard.contains("♣")) {
                return R.drawable.eight_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.eight_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.eight_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.eight_of_spades;
            }
        } else if (StringCard.contains("9")) {
            if (StringCard.contains("♣")) {
                return R.drawable.nine_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.nine_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.nine_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.nine_of_spades;
            }
        } else if (StringCard.contains("10")) {
            if (StringCard.contains("♣")) {
                return R.drawable.ten_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.ten_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.ten_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.ten_of_spades;
            }
        } else if (StringCard.contains("J")) {
            if (StringCard.contains("♣")) {
                return R.drawable.jack_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.jack_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.jack_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.jack_of_spades;
            }
        } else if (StringCard.contains("Q")) {
            if (StringCard.contains("♣")) {
                return R.drawable.queen_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.queen_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.queen_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.queen_of_spades;
            }
        } else if (StringCard.contains("K")) {
            if (StringCard.contains("♣")) {
                return R.drawable.king_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.king_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.king_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.king_of_spades;
            }
        } else if (StringCard.contains("A")) {
            if (StringCard.contains("♣")) {
                return R.drawable.ace_of_clubs;
            } else if (StringCard.contains("♦")) {
                return R.drawable.ace_of_diamonds;
            } else if (StringCard.contains("♥")) {
                return R.drawable.ace_of_hearts;
            } else if (StringCard.contains("♠")) {
                return R.drawable.ace_of_spades;
            }
        }
        return 0; // Default image resource if no match is found
    }

    // Method to stop the program for a specified number of seconds
    public static void sleep(int seconds) {
        long startTime = System.currentTimeMillis();
        long targetTime = startTime + seconds * 1000;

        while (System.currentTimeMillis() < targetTime) {
            // Loop until the target time is reached
        }
    }
}

