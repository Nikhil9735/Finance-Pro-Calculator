package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

public class AllBankInterestRateFragment extends Fragment {

    private Button calculateButton;
    private Button buttonClear;
    private Spinner ageGroupDropdown;
    private Spinner selectBankDropdown;
    private TableLayout tableLayout;
    private TextView bankName;
    private TextView ageGroup;
    private View resultBox;

    // Fixed Deposit Rates Data
    private String[][] fdRates = {
            // Bank 1 - AU Small Finance Bank (For Adults)
            {"7 days to 1 month 15 days", "3.75%"},
            {"1 month 16 days to 3 months", "5.50%"},
            {"More than 3 months to 6 months", "6.00%"},
            {"More than 6 months to 1 year", "7.25%"},
            {"More than 1 year to 15 months", "7.85%"},
            {"More than 15 months but less than 18 months", "7.50%"},
            {"18 months", "8.00%"},
            {"More than 18 months to 2 years", "7.75%"},
            {"More than 2 years to 3 years", "7.50%"},
            {"More than 3 years to 45 months", "7.50%"},
            {"More than 45 months but less than 5 years", "7.25%"},
            {"5 years to 10 years", "7.25%"},

            // Bank 1 - AU Small Finance Bank (For Senior Citizens)
            {"7 days to 1 month 15 days", "4.25%"},
            {"1 month 16 days to 3 months", "6.00%"},
            {"More than 3 months to 6 months", "6.50%"},
            {"More than 6 months to 1 year", "7.75%"},
            {"More than 1 year to 15 months", "8.35%"},
            {"More than 15 months but less than 18 months", "8.00%"},
            {"18 months", "8.50%"},
            {"More than 18 months to 2 years", "8.25%"},
            {"More than 2 years to 3 years", "8.00%"},
            {"More than 3 years to 45 months", "8.00%"},
            {"More than 45 months but less than 5 years", "7.75%"},
            {"5 years to 10 years", "7.75%"},

            // Bank 2 - Axis Bank (For Adults)
            {"7 days to 29 days", "3.00%"},
            {"30 days to 45 days", "3.50%"},
            {"46 days to 60 days", "4.25%"},
            {"61 days to less than 3 months", "4.50%"},
            {"3 months to under 6 months", "4.75%"},
            {"6 months to under 9 months", "5.75%"},
            {"9 months to under 1 year", "6.00%"},
            {"1 year to less than 15 months", "6.70%"},
            {"15 months to under 2 years", "7.25%"},
            {"2 years to under 5 years", "7.10%"},
            {"5 years to 10 years", "7.00%"},

            // Bank 2 - Axis Bank (For Senior Citizens)
            {"7 days to 29 days", "3.50%"},
            {"30 days to 45 days", "4.00%"},
            {"46 days to 60 days", "4.75%"},
            {"61 days to less than 3 months", "5.00%"},
            {"3 months to under 6 months", "5.25%"},
            {"6 months to under 9 months", "6.25%"},
            {"9 months to under 1 year", "6.50%"},
            {"1 year to less than 15 months", "7.20%"},
            {"15 months to under 2 years", "7.75%"},
            {"2 years to under 5 years", "7.60%"},
            {"5 years to 10 years", "7.75%"},
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_bank_interest_rate, container, false);

        // Bind UI elements
        calculateButton = view.findViewById(R.id.calculateButton);
        buttonClear = view.findViewById(R.id.buttonClear);
        ageGroupDropdown = view.findViewById(R.id.ageGroupDropdown);
        selectBankDropdown = view.findViewById(R.id.selectBankDropdown);
        tableLayout = view.findViewById(R.id.tableLayout);
        bankName = view.findViewById(R.id.bankName);
        ageGroup = view.findViewById(R.id.selectedAgeGroup);
        resultBox = view.findViewById(R.id.resultBox);

        // Set up button click listener for 'Calculate'
        calculateButton.setOnClickListener(v -> {
            resultBox.setVisibility(View.VISIBLE);
            updateFDTable();
        });

        // Set up buttonClear functionality
        buttonClear.setOnClickListener(v -> resetDropdownsAndTable());


        return view;
    }

    private void updateFDTable() {
        // Clear existing rows
        tableLayout.removeAllViews();

        // Add the heading row
        TableRow headingRow = new TableRow(getActivity());
        headingRow.setGravity(Gravity.CENTER_VERTICAL);

        // Create the Days TextView
        TextView daysHeadingTextView = new TextView(getActivity());
        daysHeadingTextView.setText("Days");
        daysHeadingTextView.setPadding(16, 16, 16, 16);
        daysHeadingTextView.setTextSize(18);
        daysHeadingTextView.setGravity(Gravity.CENTER); // Center text
        daysHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        daysHeadingTextView.setTypeface(null, Typeface.BOLD);
        daysHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        daysHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        // Create the Annual Interest TextView
        TextView annualRateHeadingTextView = new TextView(getActivity());
        annualRateHeadingTextView.setText("Annual Interest (%)");
        annualRateHeadingTextView.setPadding(16, 16, 16, 16);
        annualRateHeadingTextView.setTextSize(18);
        annualRateHeadingTextView.setGravity(Gravity.CENTER); // Center text
        annualRateHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        annualRateHeadingTextView.setTypeface(null, Typeface.BOLD);
        annualRateHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        annualRateHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        // Add the heading TextViews to the row
        headingRow.addView(daysHeadingTextView);
        headingRow.addView(annualRateHeadingTextView);

        // Add the heading row to the TableLayout
        tableLayout.addView(headingRow);

        // Get selected bank and age group
        String selectedBank = selectBankDropdown.getSelectedItem().toString();
        String selectedAgeGroup = ageGroupDropdown.getSelectedItem().toString();

        bankName.setText(selectedBank);
        ageGroup.setText(selectedAgeGroup);

        // Determine the index for the bank and age group
        int bankIndex = selectedBank.equals("AU Small Finance Bank") ? 0 : 12;
        int ageGroupIndex = selectedAgeGroup.equals("Adult (Below 60 Years)") ? 0 : 12;

        // Add FD Rates dynamically to the table
        for (int i = bankIndex + ageGroupIndex; i < bankIndex + ageGroupIndex + 12; i++) {
            TableRow row = new TableRow(getActivity());
            row.setGravity(Gravity.CENTER_VERTICAL);

            // Create the Days TextView
            TextView daysTextView = new TextView(getActivity());
            daysTextView.setText(fdRates[i][0]);
            daysTextView.setPadding(16, 16, 16, 16);
            daysTextView.setTextSize(16);
            daysTextView.setGravity(Gravity.CENTER); // Center text
            daysTextView.setBackgroundResource(R.drawable.cell_border);
            daysTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            // Enable multiline wrapping for Days TextView
            daysTextView.setSingleLine(false);
            daysTextView.setEllipsize(null);

            // Create the Annual Rate TextView
            TextView annualRateTextView = new TextView(getActivity());
            annualRateTextView.setText(fdRates[i][1]);
            annualRateTextView.setPadding(16, 16, 16, 16);
            annualRateTextView.setTextSize(16);
            annualRateTextView.setGravity(Gravity.CENTER); // Center text
            annualRateTextView.setBackgroundResource(R.drawable.cell_border);
            annualRateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            // Add both TextViews to the row
            row.addView(daysTextView);
            row.addView(annualRateTextView);

            // Add the row to the TableLayout
            tableLayout.addView(row);
        }
    }

    // Function to reset dropdowns and clear the table
    private void resetDropdownsAndTable() {
        // Reset the dropdowns to their default selections
        selectBankDropdown.setSelection(0); // Reset to the first item in the dropdown
        ageGroupDropdown.setSelection(0);  // Reset to the first item in the dropdown

        resultBox.setVisibility(View.GONE);
    }

}
