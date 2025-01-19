package com.myApplication.blackjack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.myApplication.blackjack.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BlackjackGame game;
    private TextView resultTextView, dealerHandTextView, playerHandTextView, balanceText, betText;
    private Button hitButton, standButton, doubleButton, splitButton, newGameButton, setBetButton;

    private ImageView playerImg1, playerImg2, playerImg3, playerImg4, playerImg5, playerImg6, playerImg7;
    private ImageView dealerImg1, dealerImg2, dealerImg3, dealerImg4, dealerImg5, dealerImg6, dealerImg7;
    private ImageView faceDownCard;

    private FrameLayout dimOverlay;

    private RewardedAd rewardedAd;
    private final String TAG = "MainActivity";

    private List<ImageView> playerImages;
    private List<ImageView> dealerImages;

    private BlackjackGame balance;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();


        loadAd();
        loadVideo();

        game = new BlackjackGame();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        dealerHandTextView = findViewById(R.id.dealerHandTextView);
        playerHandTextView = findViewById(R.id.playerHandTextView);
        betText = findViewById(R.id.BetText);

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);
        newGameButton = findViewById(R.id.newGameButton);
        doubleButton = findViewById(R.id.doubleButton);
        splitButton = findViewById(R.id.splitButton);
        setBetButton = findViewById(R.id.setBetButton);

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

        dimOverlay = findViewById(R.id.dim_overlay);


        balanceText = findViewById(R.id.balanceText);


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
                loadVideo();
                if (balance.getBalance() <= 0){
                    lowBalanceDialog("watch a video and earn 100$", "Your balance is to low");
                }
                if(mInterstitialAd != null){
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    loadAd();
                }

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

        setBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBetDialog("Set your bet amount");
            }
        });

        dimOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimOverlay.setVisibility(View.GONE);
            }
        });

        balance = new BlackjackGame(100, 10);

        startNewGame();
    }

    public void startNewGame() {

        if(balance.getBet() > balance.getBalance()){
            balance.setBet(balance.getBalance());
            betText.setText(String.valueOf(balance.getBet() + "$"));
        }

        if(balance.getBalance() == 0){
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleButton.setEnabled(false);
            splitButton.setEnabled(false);
            newGameButton.setEnabled(true);
        }

        else{
            doubleButton.setVisibility(View.VISIBLE);
            splitButton.setVisibility(View.GONE);
            newGameButton.setVisibility(View.GONE);


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
    }

    private void playerHit() {
        doubleButton.setVisibility(View.GONE);
        splitButton.setVisibility(View.GONE);

        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));

//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(dealerImg1, "translationX", 200);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(dealerImg1, "translationY", 200);
//
//        // Set duration for the animation (in milliseconds)
//        animatorX.setDuration(1000); // 1000 milliseconds = 1 second
//        animatorY.setDuration(1000); // 1000 milliseconds = 1 second
//
//        // Start the animation
//        animatorX.start();
//        animatorY.start();

        if(!optimalPlay.equals("Hit")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }


        game.playerHit();
        initialUpdateUI();

        if (game.isPlayerBust()) {
            endGame(getResultMessage());
        }
    }

    private void playerDouble() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));
        splitButton.setVisibility(View.GONE);

        if(!optimalPlay.equals("Double")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        balance.setBet(balance.getBet() * 2);
        // Draw one additional card for the player
        game.playerDouble();

        updateUI();

        // Stand after doubling
        playerStand();

        balance.setBet(balance.getBet()/2);

    }

    private void playerStand() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));
        splitButton.setVisibility(View.GONE);
        doubleButton.setVisibility(View.GONE);

        if(!optimalPlay.equals("Stand")){
            showDialog("in that case, you should " + optimalPlay, "played wrong");
        }

        // Reveal the dealer's hidden card
        dealerTurn();
    }

    private void playerSplit() {
        String optimalPlay = game.determineAction(game.getPlayerHand(), game.getDealerHand().get(0));

        game.isSplit = true;

        if(!optimalPlay.equals("Split")){
            showDialog2("in that case, you should " + optimalPlay, "played wrong");
        } else {
            Intent intent = new Intent(MainActivity.this, SplitActivity.class);
            intent.putExtra("Game", game);

            startActivity(intent);
        }
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

        newGameButton.setVisibility(View.VISIBLE);
        newGameButton.setEnabled(true);

        updateUI();
    }

    private String getResultMessage() {
        if(game.isDealerBust()){
            balance.setBalance(balance.getBalance() + balance.getBet());
            return "Dealer bust! you win";
        } else if(game.isPlayerBust()){
            balance.setBalance(balance.getBalance() - balance.getBet());
            return "Busted! you lose";
        } else if (game.gameResult().equals("Lose")) {
            balance.setBalance(balance.getBalance() - balance.getBet());
            return "You lose.";
        } else if (game.gameResult().equals("Win")) {
            balance.setBalance(balance.getBalance() + balance.getBet());
            return "You win!";
        }
        return "Push";
    }

    @SuppressLint("SetTextI18n")
    private void initialUpdateUI() {

        balanceText.setText("Balance \n" + String.valueOf(balance.getBalance()) + "$");

        dealerImg1.setImageResource(getCardImageView(game.getDealerHand().get(0)));

        float x = dealerImg1.getX();
        float y = dealerImg1.getY();

        Log.d("debugLocation", "X: " + x + " y: " + y);

        // Move the second ImageView to the location of the first ImageView with animation
        //AnimationUtils.moveImageViewToLocation(faceDownCard, x, y);


        for(int i = 0; i < game.getPlayerHand().size(); i++){
            playerImages.get(i).setImageResource(getCardImageView(game.getPlayerHand().get(i)));
        }

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
                    balance.setBalance(balance.getBalance() + balance.getBet() * 1.5);
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
                    balance.setBalance(balance.getBalance() - balance.getBet());
                    endGame("Blackjack! you lose");
                } else {
                    endGame("push");
                }
            }
        }


        playerHandTextView.setText("(" + String.valueOf(game.getPlayerScore()) + ")");
        dealerHandTextView.setText("");
    }


    @SuppressLint("SetTextI18n")
    private void updateUI() {

        balanceText.setText("Balance \n" + String.valueOf(balance.getBalance()) + "$");

        for(int i = 0; i < game.getPlayerHand().size(); i++){
            playerImages.get(i).setImageResource(getCardImageView(game.getPlayerHand().get(i)));
        }

        for(int i = 0; i < game.getDealerHand().size(); i++){
            dealerImages.get(i).setImageResource(getCardImageView(game.getDealerHand().get(i)));
        }

        playerHandTextView.setText("(" + String.valueOf(game.getPlayerScore()) + ")");
        dealerHandTextView.setText("(" + String.valueOf(game.getDealerScore()) + ")");
    }


    private void showBetDialog(String title) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_with_textbox, null);

        // Initialize the UI elements in the dialog
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        final EditText inputEditText = dialogView.findViewById(R.id.dialog_input);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        // Set the title
        titleTextView.setText(title);
        inputEditText.setText(String.valueOf(balance.getBet()));

        // Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        okButton.setOnClickListener(v -> {
            String userInput = inputEditText.getText().toString();
            if (isValidInput(userInput)) {
                if(Double.parseDouble(userInput) > balance.getBalance()){
                    Toast.makeText(MainActivity.this, "Your balance is to low", Toast.LENGTH_SHORT).show();
                }
                else {
                    balance.setBet(Double.parseDouble(userInput));
                    betText.setText(userInput + "$");
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private boolean isValidInput(String input) {
        try {
            double number = Double.parseDouble(input);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
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

    private void lowBalanceDialog(String message, String title) {
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
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            showVideo();

        });

        // Show the dialog
        dialog.show();
    }


    private void showDialog2(String message, String title) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        // Initialize UI elements
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        // Set title and message
        titleTextView.setText(title);
        messageTextView.setText(message);

        // Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Handle OK button click
        okButton.setOnClickListener(v -> {
            // Start the new activity
            Intent intent = new Intent(MainActivity.this, SplitActivity.class);
            intent.putExtra("Game", game); // Pass your game object or data here
            startActivity(intent);

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    private InterstitialAd mInterstitialAd;
    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getApplicationContext(), "ca-app-pub-4797712738094338/4016848232", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });
            }
        });

    }

    private void loadVideo(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-4797712738094338/4754180992",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    private void showVideo(){
        if (rewardedAd != null) {

            Activity activityContext = MainActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    balance.setBalance(100);
                    balanceText.setText(String.valueOf(balance.getBalance()));
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
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

    public void onBackPressed() {
        // Optionally, you can show a Toast or any feedback to the user

    }
}

