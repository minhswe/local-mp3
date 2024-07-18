package vlu.android.numberseven.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vlu.android.numberseven.R;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private int[] colors = {Color.parseColor("#7c3660"), Color.parseColor("#0f8a75")}; // Example colors (replace with your desired colors)
    private int currentColorIndex = 0;
    private static final int ANIMATION_INTERVAL = 3000; // Interval in milliseconds between color changes
    private static final int ANIMATION_DURATION = 2000; // Duration of color transition animation in milliseconds

    private Runnable gradientChangeRunnable = new Runnable() {
        @Override
        public void run() {
            animateBackgroundGradient();
            handler.postDelayed(this, ANIMATION_INTERVAL + ANIMATION_DURATION); // Wait for animation to complete before starting next
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        handler.post(gradientChangeRunnable);

        // Set click listeners for buttons
        findViewById(R.id.btn_login).setOnClickListener(v -> openLoginActivity());
        findViewById(R.id.btn_register).setOnClickListener(v -> openRegisterActivity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gradientChangeRunnable);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void animateBackgroundGradient() {
        int startColor = colors[currentColorIndex];
        currentColorIndex = (currentColorIndex + 1) % colors.length;
        int endColor = colors[currentColorIndex];

        LinearLayout mainLayout = findViewById(R.id.main);

        // Create ValueAnimator for smooth color transition
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimation.setDuration(ANIMATION_DURATION);
        colorAnimation.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            LinearGradient gradient = new LinearGradient(0, 0, 0, mainLayout.getHeight(),
                    startColor, animatedColor, Shader.TileMode.CLAMP);

            // Create a new GradientDrawable and set the gradient
            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable();
            gradientDrawable.setGradientType(android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setColors(new int[]{startColor, animatedColor});
            gradientDrawable.setOrientation(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM);

            mainLayout.setBackground(gradientDrawable);
        });
        colorAnimation.start();
    }
}