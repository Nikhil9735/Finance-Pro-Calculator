package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout buttonContainer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {
            Log.d("Ads", "SDK initialized");
        });
        // Load banner ad
        AdHelper.loadBannerAd(this);

        // Apply the saved theme mode before setting the content view
        ThemeUtils.applySavedTheme(this);
        ThemeUtils.applySavedLanguage(this);

        // Get the button container
        buttonContainer = findViewById(R.id.buttonContainer);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Set up toolbar
        setSupportActionBar(toolbar);

        // Create ActionBarDrawerToggle for hamburger animation and drawer behavior
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,  // Optional: String resource for accessibility
                R.string.nav_close  // Optional: String resource for accessibility
        ) {
            @Override
            public void onDrawerSlide(@NonNull android.view.View drawerView, float slideOffset) {
                // Move main content to the right while drawer slides
                super.onDrawerSlide(drawerView, slideOffset);
                findViewById(R.id.main_content).setTranslationX(slideOffset * drawerView.getWidth());
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();  // Sync the state of the drawer toggle with the drawer

        // Navigation Item Listener
        if (navigationView != null) {
            LinearLayout settingsLayout = navigationView.findViewById(R.id.settings);
            if (settingsLayout != null) {
                settingsLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }

            findViewById(R.id.share).setOnClickListener(v -> {
                try {
                    String appName = getString(R.string.app_name);
                    String appLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    String shareMessage = String.format("Install %s Smart Solutions for Every Finance Need\n\n%s", appName, appLink);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            });

            LinearLayout rateLayout = navigationView.findViewById(R.id.rate);
            if (rateLayout != null) {
                rateLayout.setOnClickListener(v -> {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(rateIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Unable to find market app", Toast.LENGTH_LONG).show();
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }

            LinearLayout moreLayout = navigationView.findViewById(R.id.More);
            if (moreLayout != null) {
                moreLayout.setOnClickListener(v -> {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent moreIntent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(moreIntent);
                    } catch (ActivityNotFoundException e) {
                        // Fallback to web link if the market app is not found
                        uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                        moreIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(moreIntent);
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }
        }

        // Define categories and buttons for each category
        String[][] categories = {
                {"EMPLOYEE TOOLS", getString(R.string.empSalary), getString(R.string.empSalaryIncrement)},
                {"BANK TOOLS", "Currency Denomination"},
                {"POST OFFICE", "Recurring Deposit (RD)", "Time Deposit (TD)", "Monthly Income Scheme (MIS)", "National Savings Certificate (NSC)"},
                {"BANK", "Simple Loan", "Fixed Deposit - TDR (Interest Payout)", "Fixed Deposit - STDR (Cumulative)", "Bank Recurring Deposit (RD)", "All Bank Interest Rate (%)"},
                {"Bank & Post Office Schemes", "Mahila Samman Savings Certificate (MSSC)"}
        };

        // Define a list of colors to be manually assigned
        String[] buttonColors = {
                "#0b57d0", "#0b57d0", "#6D214F", "#2C3A47", "#40407a", "#1dd1a1",
                "#ee5253", "#feca57", "#10ac84", "#8395a7", "#5f27cd", "#54a0ff",
                //"#00d2d3", "#ff9ff3", "#c8d6e5"
        };

        int colorIndex = 0;

        // Add buttons dynamically for each category
        for (int i = 0; i < categories.length; i++) {
            String[] category = categories[i];
            addCategoryHeader(category[0]); // Add category header (name)
            for (int j = 1; j < category.length; j++) {
                String color = buttonColors[colorIndex++ % buttonColors.length];
                addButton(category[j], color);
            }

            // ✅ Add AdView after BANK category (index 3 in your list)
            if (i == 3) {
                AdHelper.loadMidiumSquareAd(this, buttonContainer);
            }
        }

    }

    private void addCategoryHeader(String categoryName) {
        // Create a TextView for the category name
        TextView header = new TextView(this);
        header.setText(categoryName);
        header.setTextSize(20);

        // Fetch the text color based on the current theme
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true); // Use the primary text color attribute
        int textColor = typedValue.data;
        header.setTextColor(textColor);

        header.setTypeface(ResourcesCompat.getFont(this, R.font.bold)); // Use custom bold font
        header.setPadding(0, 32, 0, 16); // Add padding for better spacing
        header.setGravity(android.view.Gravity.CENTER); // Center the text horizontally

        // Set layout parameters for the header
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        header.setLayoutParams(headerParams);

        // Add the header to the container
        buttonContainer.addView(header);
    }

    private void addButton(String category, String color) {
        // Create a button
        Button button = new Button(this);
        button.setText(category);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(18); // Set larger text size

        // Apply custom font
        Typeface typeface = ResourcesCompat.getFont(this, R.font.regular); // Replace 'bold' with your font file name
        button.setTypeface(typeface);

        // Get a mutable copy of the drawable
        Drawable background = ContextCompat.getDrawable(this, R.drawable.curve_box).mutate();
        background.setTint(Color.parseColor(color));
        button.setBackground(background);

        // Set layout parameters with increased height
        int heightInDp = 50; // Set desired height in dp
        int heightInPx = (int) (heightInDp * getResources().getDisplayMetrics().density); // Convert dp to px

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                heightInPx // Set the calculated height in pixels
        );

        params.setMargins(0, 12, 0, 12); // Add margins for spacing
        button.setLayoutParams(params);

        // Set click listener
        button.setOnClickListener(v -> openCalculationActivity(category));

        // Add button to the container
        buttonContainer.addView(button);
    }

    private void openCalculationActivity(String operation) {
        // Pass the operation to the MainViewModel
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setOperation(operation);

        Intent intent = new Intent(MainActivity.this, TabActivity.class);
        intent.putExtra("operation", operation);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;  // Handle toggle actions (open/close drawer)
        }
        return super.onOptionsItemSelected(item);
    }
}
