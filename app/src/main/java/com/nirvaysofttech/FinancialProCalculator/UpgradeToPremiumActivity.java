package com.nirvaysofttech.FinancialProCalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class UpgradeToPremiumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.nav_close);
            getSupportActionBar().setTitle(R.string.premium_title);
        }

        AdHelper.loadBannerAd(this);

        // Handle system back button press with animation
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });

        // Set benefit texts
        View benefit1 = findViewById(R.id.benefit1);

        TextView benefitText1 = benefit1.findViewById(R.id.benefit_text);

        benefitText1.setText(R.string.benefit_unlimited);

        // In your onCreate() method:
        MaterialButton restoreButton = findViewById(R.id.btn_restore);
        restoreButton.setOnClickListener(v -> {
            // Implement your restore subscription logic here
            // This might involve calling your billing client's queryPurchases()
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishWithAnimation();
        onBackPressed();
        return true;
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}