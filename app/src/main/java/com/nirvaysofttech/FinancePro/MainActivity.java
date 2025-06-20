package com.nirvaysofttech.FinancePro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout buttonContainer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private SearchView searchView;
    private MenuItem searchItem;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_NOTICE_DISMISSED = "notice_dismissed";
    private static final String KEY_APP_VERSION = "app_version";

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

        // Check notice visibility before setting up other views
        checkNoticeVisibility();

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
                R.string.nav_open,
                R.string.nav_close
        ) {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                findViewById(R.id.main_content).setTranslationX(slideOffset * drawerView.getWidth());
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupNavigation();

        // Define categories and buttons for each category
        setupCalculatorButtons();
    }

    private void checkNoticeVisibility() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersion = prefs.getInt(KEY_APP_VERSION, 0);
        int currentVersion = getAppVersionCode();

        // Show notice if:
        // 1. It was never dismissed, OR
        // 2. The app has been updated
        boolean shouldShowNotice = !prefs.getBoolean(KEY_NOTICE_DISMISSED, false)
                || (savedVersion != currentVersion);

        View noticeBoard = findViewById(R.id.notice_board);
        noticeBoard.setVisibility(shouldShowNotice ? View.VISIBLE : View.GONE);

        // Update saved version if needed
        if (savedVersion != currentVersion) {
            prefs.edit()
                    .putInt(KEY_APP_VERSION, currentVersion)
                    .putBoolean(KEY_NOTICE_DISMISSED, false) // Reset dismissal on update
                    .apply();
        }
    }

    private int getAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public void onDismissClick(View view) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_NOTICE_DISMISSED, true)
                .apply();

        View noticeBoard = findViewById(R.id.notice_board);
        noticeBoard.setVisibility(View.GONE);
    }

    private void setupNavigation() {
        if (navigationView != null) {
            LinearLayout settingsLayout = navigationView.findViewById(R.id.settings);
            if (settingsLayout != null) {
                settingsLayout.setOnClickListener(v -> {
                    startActivity(new Intent(this, SettingsActivity.class));
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
                        Toast.makeText(this, "Unable to find market app", Toast.LENGTH_LONG).show();
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
                        uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                        moreIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(moreIntent);
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }

            findViewById(R.id.upgradeToPremium).setOnClickListener(v -> {
                startActivity(new Intent(this, UpgradeToPremiumActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawer(GravityCompat.START);
            });

            Button contactBtn = findViewById(R.id.btn_contact_form);
            contactBtn.setOnClickListener(v -> {
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            Button premiumBtn = findViewById(R.id.btn_premium);
            premiumBtn.setOnClickListener(v -> {
                startActivity(new Intent(this, UpgradeToPremiumActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }

    private void setupCalculatorButtons() {
        String[][] categories = {
                {"BANK TOOLS", "Currency Denomination"},
                {"EMPLOYEE TOOLS", getString(R.string.empSalary), getString(R.string.empSalaryIncrement)},
                {"BANK", "Basic Loan", "Fixed Deposit - TDR (Interest Payout)", "Fixed Deposit - STDR (Cumulative)", "Bank Recurring Deposit (RD)", "All Bank Interest Rate (%)"},
                {"POST OFFICE", "Recurring Deposit (RD)", "Time Deposit (TD)", "Monthly Income Scheme (MIS)", "National Savings Certificate (NSC)"},
                {"Bank & Post Office Schemes", "Mahila Samman Savings Certificate (MSSC)"}
        };

        String[] buttonColors = {
                "#0b57d0", "#0b57d0", "#6D214F", "#2C3A47", "#40407a", "#1dd1a1",
                "#ee5253", "#feca57", "#10ac84", "#8395a7", "#5f27cd", "#54a0ff",
        };

        int colorIndex = 0;

        for (int i = 0; i < categories.length; i++) {
            String[] category = categories[i];
            addCategoryHeader(category[0]);
            for (int j = 1; j < category.length; j++) {
                String color = buttonColors[colorIndex++ % buttonColors.length];
                addButton(category[j], color);
            }

            if (i == 3) {
                AdHelper.loadMidiumSquareAd(this, buttonContainer);
            }
        }
    }

    private void addCategoryHeader(String categoryName) {
        TextView header = new TextView(this);
        header.setText(categoryName);
        header.setTextSize(20);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        int textColor = typedValue.data;
        header.setTextColor(textColor);

        header.setTypeface(ResourcesCompat.getFont(this, R.font.bold));
        header.setPadding(0, 32, 0, 16);
        header.setGravity(android.view.Gravity.CENTER);

        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        header.setLayoutParams(headerParams);
        buttonContainer.addView(header);
    }

    private void addButton(String category, String color) {
        Button button = new Button(this);
        button.setText(category);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(18);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.regular);
        button.setTypeface(typeface);

        Drawable background = ContextCompat.getDrawable(this, R.drawable.curve_box).mutate();
        background.setTint(Color.parseColor(color));
        button.setBackground(background);

        int heightInPx = (int) (50 * getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, heightInPx
        );
        params.setMargins(0, 12, 0, 12);
        button.setLayoutParams(params);

        button.setOnClickListener(v -> openCalculationActivity(category));
        buttonContainer.addView(button);
    }

    private void openCalculationActivity(String operation) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setOperation(operation);

        Intent intent = new Intent(MainActivity.this, TabActivity.class);
        intent.putExtra("operation", operation);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search...");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterButtons(newText);
                return true;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                restoreAllButtons();
                return true;
            }
        });

        return true;
    }

    private void filterButtons(String query) {
        query = query.toLowerCase();
        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            View child = buttonContainer.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                String text = button.getText().toString().toLowerCase();
                button.setVisibility(text.contains(query) ? View.VISIBLE : View.GONE);
            } else if (child instanceof TextView) {
                child.setVisibility(View.GONE);
            }
        }
    }

    private void restoreAllButtons() {
        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            buttonContainer.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}