package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SplitActivity extends AppCompatActivity {

    private BlackjackGame game;

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
    }
}