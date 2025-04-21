package com.nirvaysofttech.FinancialProCalculator;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.nav_close);
            getSupportActionBar().setTitle(R.string.about_us);
        }

        // Set version info
        TextView versionTextView = findViewById(R.id.version_text);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionTextView.setText(getString(R.string.version, versionName));
        } catch (PackageManager.NameNotFoundException e) {
            versionTextView.setText(R.string.version);
        }

        // Handle system back button press with animation
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });

        findViewById(R.id.email_layout).setOnClickListener(v -> SettingsActivity.showSuggestionMailDialog(AboutUsActivity.this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishWithAnimation();
        return true;
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}