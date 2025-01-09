package com.myApplication.blackjack;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AnimationUtils {

    final int FINAL_X = 0;
    final int FINAL_Y = 0;

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

    public static void setImageViewLocation(ImageView imageView, float newX, float newY) {
        // Create ObjectAnimator to animate the translation of the ImageView
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, newX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, newY);

        // Set interpolator for smooth acceleration and deceleration
        animatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorY.setInterpolator(new AccelerateDecelerateInterpolator());

        // Start the animation
        animatorX.start();
        animatorY.start();
    }

    public static void getCardFromDeck(ImageView imageView) {
        // Get the layout parameters of the ImageView
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();

        // Get the original X and Y position from layout parameters
        int originalX = layoutParams.leftMargin;
        int originalY = layoutParams.topMargin;

        // Create ObjectAnimator to animate the translation of the ImageView
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(R.drawable.face_down_card, String.valueOf(View.TRANSLATION_X), originalX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(R.drawable.face_down_card, String.valueOf(View.TRANSLATION_Y), originalY);

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


    public static void moveImageViewToLocation(ImageView imageView, float x, float y) {

        // Calculate the translation distances
        float deltaX = x - imageView.getX();
        float deltaY = y - imageView.getY();

        // Create ObjectAnimator to animate the translation of the ImageView
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, deltaX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, deltaY);

        // Set duration for the animation (in milliseconds)
        animatorX.setDuration(1000); // 1000 milliseconds = 1 second
        animatorY.setDuration(1000); // 1000 milliseconds = 1 second

        // Start the animation
        animatorX.start();
        animatorY.start();
    }


}
