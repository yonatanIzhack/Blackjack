package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SplitActivity extends AppCompatActivity {

    private BlackjackGame game;
    private Button hitButton, standButton, doubleButton, newGameButton;
    private TextView resultTextView, dealerHandTextView, playerHandTextView;
    private ImageView firstHandImg1, firstHandImg2, firstHandImg3, firstHandImg4, firstHandImg5, firstHandImg6, firstHandImg7;
    private ImageView secondHandImg1, secondHandImg2, secondHandImg3, secondHandImg4, secondHandImg5, secondHandImg6, secondHandImg7;
    private ImageView dealerImg1, dealerImg2, dealerImg3, dealerImg4, dealerImg5, dealerImg6, dealerImg7;

    private List<ImageView> firstHandImages;
    private List<ImageView> secondHandImages;
    private List<ImageView> dealerImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        resultTextView = findViewById(R.id.resultTextView);
        dealerHandTextView = findViewById(R.id.dealerHandTextView);
        playerHandTextView = findViewById(R.id.firstHandTextView);

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

        startNewGame();

    }

    private void startNewGame() {
        doubleButton.setVisibility(View.VISIBLE);

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleButton.setEnabled(true);
        newGameButton.setEnabled(true);


        game = (BlackjackGame) getIntent().getSerializableExtra("Game");
        initialUpdateUI();
    }

    private void initialUpdateUI() {

        dealerImg1.setImageResource(getCardImageView(game.getDealerHand().get(0)));

        for(int i = 0; i < game.getFirstHand().size(); i++){
            firstHandImages.get(i).setImageResource(getCardImageView(game.getFirstHand().get(i)));
        }

        for(int i = 0; i < game.getSecondHand().size(); i++){
            secondHandImages.get(i).setImageResource(getCardImageView(game.getSecondHand().get(i)));
        }

        playerHandTextView.setText("Your Hand: " + String.valueOf(game.getPlayerScore()));
        dealerHandTextView.setText("Dealer's Hand: " + String.valueOf(game.getDealerHand().get(0).getRank()));
    }

    private void endGame(String message) {
        resultTextView.setText(message);

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);

        newGameButton.setEnabled(true);
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