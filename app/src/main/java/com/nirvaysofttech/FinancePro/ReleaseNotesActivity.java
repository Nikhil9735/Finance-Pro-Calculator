package com.nirvaysofttech.FinancePro;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReleaseNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_notes);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.release_notes_title);
        }

        // Set version and date
        TextView txtVersion = findViewById(R.id.txt_version);
        TextView txtDate = findViewById(R.id.txt_release_date);

        AdHelper.loadBannerAd(this);

        try {
            // Get version name
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            txtVersion.setText(pInfo.versionName);

            // OPTION 1: Using string resource from build.gradle.kts
            String buildDate = getString(R.string.build_date);
            txtDate.setText(buildDate);

            // OPTION 2: Using BuildConfig (uncomment if using this approach)
            // txtDate.setText("Released: " + BuildConfig.BUILD_DATE);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Fallback to current date if something goes wrong
            txtDate.setText(new SimpleDateFormat("MMMM d, yyyy").format(new Date()));
        }

        // Handle system back button press with animation
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });

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