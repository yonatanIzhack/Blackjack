package com.myApplication.blackjack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myApplication.blackjack.R;

import java.util.ArrayList;
import java.util.List;

public class SplitActivity extends AppCompatActivity {

    private BlackjackGame game;
    private Button hitButton, standButton, doubleButton, newGameButton;
    private TextView firstHandResultTextView, secondHandResultTextView, dealerHandTextView, firstHandTextView, secondHandTextView;
    private ImageView firstHandImg1, firstHandImg2, firstHandImg3, firstHandImg4, firstHandImg5, firstHandImg6, firstHandImg7;
    private ImageView secondHandImg1, secondHandImg2, secondHandImg3, secondHandImg4, secondHandImg5, secondHandImg6, secondHandImg7;
    private ImageView dealerImg1, dealerImg2, dealerImg3, dealerImg4, dealerImg5, dealerImg6, dealerImg7;

    private ImageView pointLeft, pointRight;
    private List<ImageView> firstHandImages;
    private List<ImageView> secondHandImages;
    private List<ImageView> dealerImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_split);


        firstHandResultTextView = findViewById(R.id.firstHandResultTextView);
        secondHandResultTextView = findViewById(R.id.secondHandResultTextView);

        dealerHandTextView = findViewById(R.id.dealerHandTextView);
        firstHandTextView = findViewById(R.id.firstHandTextView);
        secondHandTextView = findViewById(R.id.secondHandTextView);

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);
        newGameButton = findViewById(R.id.newGameButton);
        doubleButton = findViewById(R.id.doubleButton);

        // first hand images
        firstHandImg1 = findViewById(R.id.firstHandImg1);;
        firstHandImg2 = findViewById(R.id.firstHandImg2);;
        firstHandImg3 = findViewById(R.id.firstHandImg3);;
        firstHandImg4 = findViewById(R.id.firstHandImg4);;
        firstHandImg5 = findViewById(R.id.firstHandImg5);;
        firstHandImg6 = findViewById(R.id.firstHandImg6);
        firstHandImg7 = findViewById(R.id.firstHandImg7);;

        firstHandImages = new ArrayList<>();

        firstHandImages.add(firstHandImg1);
        firstHandImages.add(firstHandImg2);
        firstHandImages.add(firstHandImg3);
        firstHandImages.add(firstHandImg4);
        firstHandImages.add(firstHandImg5);
        firstHandImages.add(firstHandImg6);
        firstHandImages.add(firstHandImg7);

        // second hand images
        secondHandImg1 = findViewById(R.id.secondHandImg1);;
        secondHandImg2 = findViewById(R.id.secondHandImg2);;
        secondHandImg3 = findViewById(R.id.secondHandImg3);;
        secondHandImg4 = findViewById(R.id.secondHandImg4);;
        secondHandImg5 = findViewById(R.id.secondHandImg5);;
        secondHandImg6 = findViewById(R.id.secondHandImg6);
        secondHandImg7 = findViewById(R.id.secondHandImg7);;

        secondHandImages = new ArrayList<>();

        secondHandImages.add(secondHandImg1);
        secondHandImages.add(secondHandImg2);
        secondHandImages.add(secondHandImg3);
        secondHandImages.add(secondHandImg4);
        secondHandImages.add(secondHandImg5);
        secondHandImages.add(secondHandImg6);
        secondHandImages.add(secondHandImg7);

        // dealer images
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

        pointLeft = findViewById(R.id.pointLeft);
        pointRight = findViewById(R.id.pointRight);

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

        doubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerDouble();
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        startNewGame();

    }

    private void openMainActivity() {
        Intent intent = new Intent(SplitActivity.this, MainActivity.class);

        startActivity(intent);
    }

    private void startNewGame() {
        doubleButton.setVisibility(View.VISIBLE);
        newGameButton.setVisibility(View.GONE);


        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleButton.setEnabled(true);
        newGameButton.setEnabled(true);


        game = (BlackjackGame) getIntent().getSerializableExtra("Game");
        game.firstHandHit();
        game.secondHandHit();
        initialUpdateUI();
    }

    private void playerHit(){
        String optimalPlay;

        doubleButton.setVisibility(View.GONE);

        if(game.HitFirstHand){
            optimalPlay = game.determineAction(game.getFirstHand(), game.getDealerHand().get(0));

            game.firstHandHit();
        }else {
            optimalPlay = game.determineAction(game.getSecondHand(), game.getDealerHand().get(0));

            game.secondHandHit();
        }

        if(!optimalPlay.equals("Hit")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        initialUpdateUI();

        if(game.isFirstHandBust()){
            firstHandResultTextView.setText("Busted!");;
            if(game.HitFirstHand){
                playerStand();
            }
        }

        if(game.isSecondHandBust()){
            secondHandResultTextView.setText("Busted!");
            playerStand();
        }
    }

    public void playerStand(){
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));

        if(game.HitFirstHand){
            game.HitFirstHand = false;
            doubleButton.setVisibility(View.VISIBLE);
            optimalPlay = game.determineAction(game.getFirstHand(), game.getDealerHand().get(0));

        }else {
            optimalPlay = game.determineAction(game.getSecondHand(), game.getDealerHand().get(0));

            if(game.isFirstHandBust() && game.isSecondHandBust()){
                endGame();
            }
            else {
                dealerTurn();
            }
        }

        if(!optimalPlay.equals("Stand")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        initialUpdateUI();
    }


    public void playerDouble(){
        String optimalPlay;


        if(game.HitFirstHand){
            optimalPlay = game.determineAction(game.getFirstHand(), game.getDealerHand().get(0));
            game.firstHandDouble();
        }else {
            optimalPlay = game.determineAction(game.getSecondHand(), game.getDealerHand().get(0));
            game.secondHandDouble();
        }

        if(!optimalPlay.equals("Double")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        initialUpdateUI();

        playerStand();
    }

    private void dealerTurn() {
        while (!game.isGameOver() && game.getDealerScore() < 17) {
            game.dealerHit();
        }


        if(game.isFirstHandBust()){
            firstHandResultTextView.setText("Lose");
        } else if (game.isDealerBust()){
            firstHandResultTextView.setText("Win");
        } else if(game.getFirstHandScore() > game.getDealerScore()){
            firstHandResultTextView.setText("Win");
        } else if (game.getFirstHandScore() < game.getDealerScore()) {
            firstHandResultTextView.setText("Lose");
        } else {
            firstHandResultTextView.setText("Push");
        }

        if(game.isSecondHandBust()){
            secondHandResultTextView.setText("Lose");
        } else if (game.isDealerBust()){
            secondHandResultTextView.setText("Win");
        } else if(game.getSecondHandScore() > game.getDealerScore()){
            secondHandResultTextView.setText("Win");
        } else if (game.getSecondHandScore() < game.getDealerScore()) {
            secondHandResultTextView.setText("Lose");
        } else {
            secondHandResultTextView.setText("Push");
        }

        if(!(game.isFirstHandBust() && game.isSecondHandBust())){
            UpdateUI();
        }

        endGame();
    }

    private void endGame() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);

        pointRight.setVisibility(View.GONE);
        newGameButton.setEnabled(true);
        newGameButton.setVisibility(View.VISIBLE);


        pointLeft.setVisibility(View.GONE);

        dealerHandTextView.setText("(" + String.valueOf(game.getDealerScore()) + ")");
    }

    private String getResultMessage() {
        if(game.isDealerBust()){
            return "Dealer bust! you win";
        } else if(game.isFirstHandBust()){

        } else if (game.gameResult().equals("Lose")) {
            return "You lose.";
        } else if (game.gameResult().equals("Win")) {
            return "You win!";
        }
        return "Push";
    }

    private void initialUpdateUI() {

        hitButton.setEnabled(true);
        doubleButton.setEnabled(true);

        if(game.HitFirstHand){
            pointLeft.setVisibility(View.VISIBLE);
            pointRight.setVisibility(View.GONE);
        } else {
            pointLeft.setVisibility(View.GONE);
            pointRight.setVisibility(View.VISIBLE);
        }
        dealerImg1.setImageResource(getCardImageView(game.getDealerHand().get(0)));

        for(int i = 0; i < game.getFirstHand().size(); i++){
            firstHandImages.get(i).setImageResource(getCardImageView(game.getFirstHand().get(i)));
        }

        for(int i = 0; i < game.getSecondHand().size(); i++){
            secondHandImages.get(i).setImageResource(getCardImageView(game.getSecondHand().get(i)));
        }

        firstHandTextView.setText("(" + String.valueOf(game.getFirstHandScore()) + ")");
        secondHandTextView.setText("(" + String.valueOf(game.getSecondHandScore()) + ")");

        if(game.getFirstHandScore() == 21 && game.HitFirstHand){
            hitButton.setEnabled(false);
            doubleButton.setEnabled(false);
        }

        if(game.getSecondHandScore() == 21 && !game.HitFirstHand){
            hitButton.setEnabled(false);
            doubleButton.setEnabled(false);
        }
    }

    public void UpdateUI(){
        for(int i = 0; i < game.getDealerHand().size(); i++){
            dealerImages.get(i).setImageResource(getCardImageView(game.getDealerHand().get(i)));
        }

        dealerHandTextView.setText("(" + String.valueOf(game.getDealerScore()) + ")");

    }

    private void showDialog(String message, String title) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        // Initialize the UI elements in the dialog
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        // Set the title and message
        titleTextView.setText(title);
        messageTextView.setText(message);

        // Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Handle the OK button click
        okButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
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
}