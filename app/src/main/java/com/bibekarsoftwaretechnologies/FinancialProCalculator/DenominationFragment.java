package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

import java.text.DecimalFormat;

public class DenominationFragment extends Fragment {

    private final int[] denominations = {2000, 500, 200, 100, 50, 20, 10, 5, 2, 1}; // Currency denominations
    private TextView totalNotes;
    private TextView totalAmount;
    private TextView numToText;
    private Button buttonClear;
    private View resultBox;
    private MainViewModel mainViewModel;

    DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_denomination, container, false);

        // Find TableLayout and Total Amount TextView
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);
        numToText = view.findViewById(R.id.inWard);
        totalNotes = view.findViewById(R.id.totalNotes);
        totalAmount = view.findViewById(R.id.totalAmount);
        buttonClear = view.findViewById(R.id.buttonClear);
        resultBox = view.findViewById(R.id.resultBox);

        LinearLayout currencyDenomination = view.findViewById(R.id.currencyDenomination);
        // Set OnClickListener
        currencyDenomination.setOnClickListener(v -> {
            // Call hideKeyboard from your ViewModel
            mainViewModel.hideKeyboard(requireContext());
        });

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Create table rows dynamically
        for (int denomination : denominations) {
            addDenominationRow(tableLayout, denomination);
        }

        // Set up buttonClear functionality
        buttonClear.setOnClickListener(v -> {
            clearAllFields();
            resultBox.setVisibility(View.GONE); // Hide resultBox on clear
        });

        // Enable copying text from numToText
        numToText.setOnClickListener(v -> copyToClipboard(numToText.getText().toString()));

        return view;
    }

    private void addDenominationRow(TableLayout tableLayout, int denomination) {
        TableRow tableRow = new TableRow(getContext());

        // Set layout parameters for the TableRow
        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        tableRow.setLayoutParams(rowLayoutParams);
        tableRow.setGravity(Gravity.CENTER_VERTICAL); // Ensure vertical centering of the row

        // Currency column
        TextView currencyTextView = new TextView(getContext());
        currencyTextView.setText(String.valueOf(denomination));
        TableRow.LayoutParams currencyParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        currencyTextView.setLayoutParams(currencyParams);
        currencyTextView.setPadding(16, 16, 16, 16);
        currencyTextView.setGravity(Gravity.CENTER); // Center the text horizontally and vertically
        currencyTextView.setTextSize(18);

        // x column
        TextView x = new TextView(getContext());
        x.setText("x");
        TableRow.LayoutParams xx = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f);
        x.setLayoutParams(xx);
        x.setPadding(16, 16, 16, 16);
        x.setGravity(Gravity.CENTER); // Center the text horizontally and vertically
        x.setTextSize(25);

        // Count column
        // Count column (EditText)
        EditText countEditText = new EditText(getContext());
        countEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        TableRow.LayoutParams countParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f);  // Adjust width to be smaller
        countEditText.setLayoutParams(countParams);
        countEditText.setPadding(16, 16, 16, 16);
        countEditText.setGravity(Gravity.CENTER); // Center the input text
        countEditText.setTextSize(18);

        // Make it look like a rectangle
        countEditText.setBackgroundResource(R.drawable.rectangle_background);  // Set custom background
        countEditText.setCursorVisible(true);  // Ensure cursor is visible when editing

        // Set input filter to limit to 7 digits
        countEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(6),  // Limit input to 7 characters
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isDigit(source.charAt(i))) {
                                return "";  // Disallow non-digit characters
                            }
                        }
                        return null;  // Allow digits
                    }
                }
        });

        // Change color on focus (click)
        countEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Change color when EditText is clicked (gains focus)
                countEditText.setBackgroundResource(R.drawable.rectangle_background_focused);  // Different background when focused
            } else {
                // Revert color when EditText loses focus
                countEditText.setBackgroundResource(R.drawable.rectangle_background);
            }
        });

        TextView equal = new TextView(getContext());
        equal.setText("=");
        TableRow.LayoutParams equalsymbol = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
        equal.setLayoutParams(equalsymbol);
        equal.setPadding(16, 16, 16, 16);
        equal.setGravity(Gravity.CENTER); // Center the text horizontally and vertically
        equal.setTextSize(25);

        // Amount column
        TextView amountTextView = new TextView(getContext());
        amountTextView.setText("0");
        TableRow.LayoutParams amountParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f);
        amountTextView.setLayoutParams(amountParams);
        amountTextView.setPadding(16, 16, 16, 16);
        amountTextView.setGravity(Gravity.CENTER); // Center the text horizontally and vertically
        amountTextView.setTextSize(18);

        // Add TextWatcher to update amount and total when count is changed
        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    resultBox.setVisibility(View.VISIBLE); // Show resultBox when input is provided
                }
                calculateAmountAndTotal(s, denomination, amountTextView);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add views to the row
        tableRow.addView(currencyTextView);
        tableRow.addView(x);
        tableRow.addView(countEditText);
        tableRow.addView(equal);
        tableRow.addView(amountTextView);

        // Add row to the table
        tableLayout.addView(tableRow);
    }

    private void calculateAmountAndTotal(CharSequence countText, int denomination, TextView amountTextView) {
        long count = 0;

        if (countText != null && !countText.toString().isEmpty()) {
            try {
                count = Long.parseLong(countText.toString());
            } catch (NumberFormatException e) {
                amountTextView.setText("0");
                recalculateTotal(); // Ensure total is recalculated when input is invalid
                return;
            }
        }

        long amount = denomination * count;
        amountTextView.setText(String.valueOf(amount));

        recalculateTotal(); // Update total on every change
    }

    private void recalculateTotal() {
        long totalNote = 0;
        long totalAmt = 0;

        TableLayout tableLayout = getView().findViewById(R.id.tableLayout);

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView notes = (TextView) row.getChildAt(2);  // Count column
            TextView amount = (TextView) row.getChildAt(4); // Amount column
            if (notes instanceof TextView && amount instanceof TextView) {
                String notesText = notes.getText().toString();
                String amountText = amount.getText().toString();

                if (!amountText.isEmpty()) {
                    try {
                        totalNote += Long.parseLong(notesText);
                        totalAmt += Long.parseLong(amountText);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        numToText.setText(convertNumberToWords(totalAmt) + " Rupees");
        totalNotes.setText("" + decimalFormat.format(totalNote));
        totalAmount.setText("Rs. "+decimalFormat.format(totalAmt));
    }

    // Method to convert a number to words in the Indian numbering system
// Updated method to convert number to words
    public String convertNumberToWords(long number) {
        if (number == 0) return "Zero";

        String[] places = {"", "Thousand", "Lakh", "Crore"}; // Indian numbering system hierarchy
        long[] parts = new long[4]; // Handle up to Crores

        // Break number into Indian numbering parts
        parts[0] = number % 1000; // Units
        number /= 1000;
        parts[1] = number % 100; // Thousands
        number /= 100;
        parts[2] = number % 100; // Lakhs
        number /= 100;
        parts[3] = number; // Crores

        StringBuilder words = new StringBuilder();
        boolean hasPreviousPart = false;

        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i] > 0) {
                if (hasPreviousPart && i == 0) {
                    words.append("and "); // Add "and" before the last part
                }
                words.append(convertChunk((int) parts[i]))
                        .append(" ")
                        .append(places[i])
                        .append(" ");
                hasPreviousPart = true;
            }
        }

        return words.toString().trim();
    }

    // Helper method to process chunks of 3 digits
    private String convertChunk(int number) {
        String[] ones = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen"};
        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

        StringBuilder words = new StringBuilder();

        if (number >= 100) {
            words.append(ones[number / 100]).append(" Hundred");
            number %= 100;
            if (number > 0) {
                words.append(" "); // Add space only if there's a remainder
            }
        }

        if (number >= 20) {
            words.append(tens[number / 10]).append(" ");
            number %= 10;
        }

        if (number > 0) {
            words.append(ones[number]);
        }

        return words.toString().trim();
    }

    // Set up buttonClear functionality
    private void clearAllFields() {
        mainViewModel.hideKeyboard(requireContext());

        TableLayout tableLayout = getView().findViewById(R.id.tableLayout);
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            EditText countEditText = (EditText) row.getChildAt(2);
            TextView amountTextView = (TextView) row.getChildAt(4);

            if (countEditText instanceof EditText && amountTextView instanceof TextView) {
                countEditText.setText("");
                amountTextView.setText("0");
            }
        }
    }

    private void copyToClipboard(String text) {
        if (!text.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}