package com.example.blackjack;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class AnimationUtils {

    public static void moveImageView(ImageView imageView, float newX, float newY) {
        // Create ObjectAnimator to animate the translation of the ImageView
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, newX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, newY);

        // Set duration for the animation (in milliseconds)
        animatorX.setDuration(1000); // 1000 milliseconds = 1 second
        animatorY.setDuration(1000); // 1000 milliseconds = 1 second

        // Set interpolator for smooth acceleration and deceleration
        animatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorY.setInterpolator(new AccelerateDecelerateInterpolator());

        // Start the animation
        animatorX.start();
        animatorY.start();
    }


}
