package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private int selectedThemeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    private TextView selectedThemeTextView;
    private TextView selectedLanguageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the saved theme and locale before setting the content view
        ThemeUtils.applySavedTheme(this);
        ThemeUtils.applySavedLanguage(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar); // Make sure your TabActivity has a Toolbar with this ID
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
            getSupportActionBar().setHomeActionContentDescription(R.string.nav_close);
        }

        // Initialize the TextViews
        selectedThemeTextView = findViewById(R.id.selected_theme_text);
        selectedLanguageTextView = findViewById(R.id.selected_language_text);

        // Update the TextViews with saved preferences
        updateSelectedThemeText();
        updateSelectedLanguageText();

        findViewById(R.id.btn_color_scheme).setOnClickListener(v -> showColorSchemeDialog());
        findViewById(R.id.btn_language).setOnClickListener(v -> showLanguageDialog());
        findViewById(R.id.btn_privacy_policy).setOnClickListener(v -> showPrivacyPolicyDialog());
        findViewById(R.id.btn_suggestion).setOnClickListener(v -> showSuggestionMailDialog());
    }

    private void showColorSchemeDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_color_scheme, null);

        // Create the AlertDialog using the default theme
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners); // Keep this if you want custom corners

        // Get references to dialog components
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group_color_scheme);
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        // Apply the saved theme mode to the radio buttons
        applySavedThemeToRadioButtons(radioGroup);

        // Set up the OK button
        buttonOk.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.radio_light) {
                selectedThemeMode = AppCompatDelegate.MODE_NIGHT_NO; // Light theme
            } else if (selectedId == R.id.radio_dark) {
                selectedThemeMode = AppCompatDelegate.MODE_NIGHT_YES; // Dark theme
            } else {
                selectedThemeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM; // System default
            }

            // Save and apply the selected theme
            saveThemePreference(selectedThemeMode);
            AppCompatDelegate.setDefaultNightMode(selectedThemeMode);
            updateSelectedThemeText();
            dialog.dismiss();
        });

        dialog.show();
        // Get the dialog window
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // Set the width to wrap content with a maximum value
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8); // Set width to 80% of screen width
        dialog.getWindow().setAttributes(params);

    }

    private void showLanguageDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_language, null);

        // Create the AlertDialog using the default theme
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners); // Keep this if you want custom corners

        // Get references to dialog components
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group_language);
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        // Apply the saved language to the radio buttons
        applySavedLanguageToRadioButtons(radioGroup);

        // Set up the OK button
        buttonOk.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String languageCode = "en"; // Default language

            if (selectedId == R.id.radio_spanish) {
                languageCode = "es";
            } else if (selectedId == R.id.radio_french) {
                languageCode = "fr";
            } else if (selectedId == R.id.radio_marathi) {
                languageCode = "mr";
            }

            // Save and apply the selected language
            saveLanguagePreference(languageCode);
            updateLocale(languageCode);
            updateSelectedLanguageText();
            dialog.dismiss();
        });

        dialog.show();
        // Get the dialog window
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // Set the width to wrap content with a maximum value
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8); // Set width to 80% of screen width
        dialog.getWindow().setAttributes(params);
    }

    private void showPrivacyPolicyDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_privacy_policy, null);

        // Create the AlertDialog using the default theme
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners); // Keep this if you want custom corners

        // Get reference to the OK button
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        // Set up the OK button
        buttonOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        // Get the dialog window
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // Set the width to wrap content with a maximum value
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8); // Set width to 80% of screen width
        dialog.getWindow().setAttributes(params);
    }

    private void showSuggestionMailDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sendsuggestionmail, null);

        // Create the AlertDialog using the default theme
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners); // Keep this if you want custom corners

        // Get references to dialog components
        EditText editTextName = dialogView.findViewById(R.id.suggestionName);
        EditText editTextMessage = dialogView.findViewById(R.id.suggestionMessage);
        Button buttonSend = dialogView.findViewById(R.id.buttonSend);

        // Set up the Send button
        buttonSend.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String message = editTextMessage.getText().toString().trim();

            if (!name.isEmpty() && !message.isEmpty()) {
                String subject = "Suggestion from " + name;
                new SendSuggestionMail(SettingsActivity.this, subject, message).execute();
                dialog.dismiss();
            } else {
                Toast.makeText(SettingsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        // Get the dialog window
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        // Set the width to wrap content with a maximum value
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8); // Set width to 80% of screen width
        dialog.getWindow().setAttributes(params);
    }

    private void applySavedThemeToRadioButtons(RadioGroup radioGroup) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int savedThemeMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        switch (savedThemeMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                radioGroup.check(R.id.radio_light);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                radioGroup.check(R.id.radio_dark);
                break;
            default:
                radioGroup.check(R.id.radio_system);
                break;
        }
    }

    private void applySavedLanguageToRadioButtons(RadioGroup radioGroup) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLanguage = sharedPreferences.getString("language_code", "en");

        switch (savedLanguage) {
            case "mr":
                radioGroup.check(R.id.radio_marathi);
                break;
            case "es":
                radioGroup.check(R.id.radio_spanish);
                break;
            case "fr":
                radioGroup.check(R.id.radio_french);
                break;
            default:
                radioGroup.check(R.id.radio_english);
                break;
        }
    }

    private void saveThemePreference(int mode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language_code", languageCode);
        editor.apply();
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Recreate the activity to apply the changes smoothly
        recreate();
    }

    private void updateSelectedThemeText() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int savedThemeMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        String themeText;
        switch (savedThemeMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                themeText = getString(R.string.light);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                themeText = getString(R.string.dark);
                break;
            default:
                themeText = getString(R.string.system_default);
                break;
        }

        if (selectedThemeTextView != null) {
            selectedThemeTextView.setText(themeText);
        }
    }

    private void updateSelectedLanguageText() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLanguage = sharedPreferences.getString("language_code", "en");

        String languageText;
        switch (savedLanguage) {
            case "mr":
                languageText = getString(R.string.marathi);
                break;
            case "es":
                languageText = getString(R.string.spanish);
                break;
            case "fr":
                languageText = getString(R.string.french);
                break;
            default:
                languageText = getString(R.string.system_default);
                break;
        }

        if (selectedLanguageTextView != null) {
            selectedLanguageTextView.setText(languageText);
        }
    }
}
