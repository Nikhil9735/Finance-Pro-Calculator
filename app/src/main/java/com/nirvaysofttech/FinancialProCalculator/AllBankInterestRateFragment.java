package com.nirvaysofttech.FinancialProCalculator;

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

import com.nirvaysofttech.FinancialProCalculator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllBankInterestRateFragment extends Fragment {

    private Button calculateButton;
    private Button buttonClear;
    private Spinner ageGroupDropdown;
    private Spinner selectBankDropdown;
    private TableLayout tableLayout;
    private TextView bankName;
    private TextView ageGroup;
    private View resultBox;

    // Fixed Deposit Rates Data - Organized by Bank and Age Group
    private final Map<String, Map<String, List<String[]>>> fdRatesMap = new HashMap<String, Map<String, List<String[]>>>() {{
        put("AU Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 1 month 15 days", "3.75%"},
                    new String[]{"1 month 16 days to 3 months", "5.50%"},
                    new String[]{"More than 3 months to 6 months", "5.75%"},
                    new String[]{"More than 6 months to 1 years", "7.00%"},
                    new String[]{"More than 1 years to 15 months", "7.60%"},
                    new String[]{"More than 15 months to less than 18 months", "7.40%"},
                    new String[]{"18 months", "7.75%"},
                    new String[]{"More than 18 months to 2 years", "7.50%"},
                    new String[]{"More than 2 years to 3 years", "7.50%"},
                    new String[]{"More than 3 years to 45 months", "7.50%"},
                    new String[]{"More than 45 months to 5 years", "7.25%"},
                    new String[]{"More than 5 years to 10 years", "7.25%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 1 month 15 days", "4.25%"},
                    new String[]{"1 month 16 days to 3 months", "6.00%"},
                    new String[]{"More than 3 months to 6 months", "6.25%"},
                    new String[]{"More than 6 months to 1 year", "7.50%"},
                    new String[]{"More than 1 year to 15 months", "8.10%"},
                    new String[]{"More than 15 months to less than 18 months", "7.90%"},
                    new String[]{"18 months", "8.25%"},
                    new String[]{"More than 18 months to 2 years", "8.00%"},
                    new String[]{"More than 2 years to 3 years", "8.00%"},
                    new String[]{"More than 3 years to 45 months", "8.00%"},
                    new String[]{"More than 45 months but less than 5 years", "7.75%"},
                    new String[]{"5 years to 10 years", "7.75%"}
            ));
        }});

        put("Axis Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.00%"},
                    new String[]{"30 days to 45 days", "3.50%"},
                    new String[]{"46 days to 60 days", "4.25%"},
                    new String[]{"61 days to 3 months", "4.50%"},
                    new String[]{"More than 3 months to 6 months", "4.75%"},
                    new String[]{"More than 6 months to 9 months", "5.75%"},
                    new String[]{"More than 9 months to 1 year", "6.00%"},
                    new String[]{"More than 1 year to 15 months", "6.70%"},
                    new String[]{"More than 15 months to 2 years", "7.25%"},
                    new String[]{"More than 2 years to 3 years", "7.10%"},
                    new String[]{"More than 3 years to 10 years", "7.00%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.50%"},
                    new String[]{"30 days to 45 days", "4.00%"},
                    new String[]{"46 days to 60 days", "4.75%"},
                    new String[]{"61 days to 3 months", "5.00%"},
                    new String[]{"More than 3 months to 6 months", "5.25%"},
                    new String[]{"More than 6 months to 9 months", "6.25%"},
                    new String[]{"More than 9 months to 1 year", "6.50%"},
                    new String[]{"More than 1 year to 15 months", "7.20%"},
                    new String[]{"More than 15 months to 2 years", "7.75%"},
                    new String[]{"More than 2 years to 3 years", "7.60%"},
                    new String[]{"More than 3 years to 10 years", "7.75%"}
            ));
        }});

        put("Bandhan Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to less than 2 months", "3.50%"},
                    new String[]{"2 months to less than 3 months", "4.50%"},
                    new String[]{"3 months to less than 6 months", "4.50%"},
                    new String[]{"6 months to less than 1 year", "4.50%"},
                    new String[]{"1 year", "8.05%"},
                    new String[]{"More than 1 year to 1 year 9 months", "8.00%"},
                    new String[]{"More than 1 year 9 months to 3 years", "7.25%"},
                    new String[]{"More than 3 years to 5 years", "7.25%"},
                    new String[]{"More than 5 years to 10 years", "5.85%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.75%"},
                    new String[]{"31 days to less than 2 months", "4.25%"},
                    new String[]{"2 months to less than 3 months", "5.25%"},
                    new String[]{"3 months to less than 6 months", "5.25%"},
                    new String[]{"6 months to less than 1 year", "5.25%"},
                    new String[]{"1 year", "8.55%"},
                    new String[]{"More than 1 year to 1 year 9 months", "8.50%"},
                    new String[]{"More than 1 year 9 months to 3 years", "7.75%"},
                    new String[]{"More than 3 years to 5 years", "7.75%"},
                    new String[]{"More than 5 years to 10 years", "6.60%"}
            ));
        }});

        put("Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 45 days", "3.00%"},
                    new String[]{"46 days to 179 days", "4.50%"},
                    new String[]{"180 days to less than 1 year", "6.00%"},
                    new String[]{"1 year to less than 2 years", "6.80%"},
                    new String[]{"2 years", "6.80%"},
                    new String[]{"More than 2 years to less than 3 years", "6.75%"},
                    new String[]{"3 years to less than 5 years", "6.50%"},
                    new String[]{"5 years to 10 years", "6.00%"},
                    new String[]{"400 days", "7.30%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.50%"},
                    new String[]{"31 days to 45 days", "3.50%"},
                    new String[]{"46 days to 179 days", "5.00%"},
                    new String[]{"180 days to less than 1 year", "6.50%"},
                    new String[]{"1 year to less than 2 years", "7.30%"},
                    new String[]{"2 years", "7.30%"},
                    new String[]{"More than 2 years to less than 3 years", "7.25%"},
                    new String[]{"3 years to less than 5 years", "7.00%"},
                    new String[]{"5 years to 10 years", "6.50%"},
                    new String[]{"400 days", "7.80%"}
            ));
        }});

        put("Bank of Maharashtra", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "2.75%"},
                    new String[]{"31 days to 45 days", "3.00%"},
                    new String[]{"46 days to 90 days", "4.75%"},
                    new String[]{"91 days to 119 days", "4.90%"},
                    new String[]{"120 days to 180 days", "5.10%"},
                    new String[]{"181 days to 270 days", "5.50%"},
                    new String[]{"271 days to 364 days", "5.60%"},
                    new String[]{"365 days (1 year)", "6.50%"},
                    new String[]{"More than 1 year to 2 years", "6.25%"},
                    new String[]{"More than 2 years to 3 years", "6.25%"},
                    new String[]{"More than 3 years to 5 years", "6.00%"},
                    new String[]{"5 years & above", "6.00%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.25%"},
                    new String[]{"31 days to 45 days", "3.50%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 119 days", "5.40%"},
                    new String[]{"120 days to 180 days", "5.60%"},
                    new String[]{"181 days to 270 days", "6.00%"},
                    new String[]{"271 days to 364 days", "6.10%"},
                    new String[]{"365 days (1 year)", "7.00%"},
                    new String[]{"More than 1 year to 2 years", "6.75%"},
                    new String[]{"More than 2 years to 3 years", "6.75%"},
                    new String[]{"More than 3 years to 5 years", "6.50%"},
                    new String[]{"5 years & above", "6.50%"}
            ));
        }});;

        put("Canara Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 45 days", "4.00%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 269 days", "6.15%"},
                    new String[]{"270 days to less than 1 year", "6.25%"},
                    new String[]{"1 year", "6.85%"},
                    new String[]{"More than 1 year to 2 years", "6.85%"},
                    new String[]{"2 years 1 day to 3 years", "7.30%"},
                    new String[]{"More than 3 years to 5 years", "7.40%"},
                    new String[]{"5 years to 10 years", "6.70%"},
                    new String[]{"444 days", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 45 days", "4.00%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 269 days", "6.65%"},
                    new String[]{"270 days to less than 1 year", "6.75%"},
                    new String[]{"1 year", "7.35%"},
                    new String[]{"More than 1 year to 2 years", "7.35%"},
                    new String[]{"2 years 1 day to 3 years", "7.80%"},
                    new String[]{"More than 3 years to 5 years", "7.90%"},
                    new String[]{"5 years to 10 years", "7.20%"},
                    new String[]{"444 days", "7.75%"}
            ));
        }});

        put("Capital Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 89 Days", "4.00%"},
                    new String[]{"90 Days to 179 Days", "4.75%"},
                    new String[]{"180 Days to less than 1 Year", "5.50%"},
                    new String[]{"1 Year to less than 5 Years", "7.15%"},
                    new String[]{"5 Years and upto 10 Years", "7.10%"},
                    new String[]{"12 Months", "7.55%"},
                    new String[]{"400 Days", "7.60%"},
                    new String[]{"600 Days", "7.40%"},
                    new String[]{"900 Days", "7.30%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 89 Days", "4.50%"},
                    new String[]{"90 Days to 179 Days", "5.25%"},
                    new String[]{"180 Days to less than 1 Year", "6.00%"},
                    new String[]{"1 Year to less than 5 Years", "7.65%"},
                    new String[]{"5 Years and upto 10 Years", "7.60%"},
                    new String[]{"12 Months", "8.05%"},
                    new String[]{"400 Days", "8.10%"},
                    new String[]{"600 Days", "7.90%"},
                    new String[]{"900 Days", "7.80%"}
            ));
        }});

        put("Central Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 14 days", "3.50%"},
                    new String[]{"15 days to 30 days", "3.75%"},
                    new String[]{"31 days to 45 days", "3.75%"},
                    new String[]{"46 days to 59 days", "4.50%"},
                    new String[]{"60 days to 90 days", "4.75%"},
                    new String[]{"91 days to 179 days", "5.00%"},
                    new String[]{"180 days to 270 days", "6.00%"},
                    new String[]{"271 days to 364 days", "6.25%"},
                    new String[]{"1 year to less than 2 years", "6.75%"},
                    new String[]{"2 years to less than 3 years", "7.00%"},
                    new String[]{"3 years to less than 5 years", "6.50%"},
                    new String[]{"5 years to 10 years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 14 days", "4.00%"},
                    new String[]{"15 days to 30 days", "4.25%"},
                    new String[]{"31 days to 45 days", "4.25%"},
                    new String[]{"46 days to 59 days", "5.00%"},
                    new String[]{"60 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 270 days", "6.50%"},
                    new String[]{"271 days to 364 days", "6.75%"},
                    new String[]{"1 year to less than 2 years", "7.25%"},
                    new String[]{"2 years to less than 3 years", "7.50%"},
                    new String[]{"3 years to less than 5 years", "7.00%"},
                    new String[]{"5 years to 10 years", "6.75%"}
            ));
        }});

        put("City Union Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 14 days", "5.00%"},
                    new String[]{"15 days to 45 days", "5.50%"},
                    new String[]{"46 days to 90 days", "5.75%"},
                    new String[]{"91 days to 180 days", "6.00%"},
                    new String[]{"181 days to 270 days", "6.25%"},
                    new String[]{"271 days to 332 days", "6.50%"},
                    new String[]{"333 days", "7.50%"},
                    new String[]{"334 days to 400 days", "7.00%"},
                    new String[]{"401 days to 3 years", "6.50%"},
                    new String[]{"Above 3 years upto 10 years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 14 days", "5.00%"},
                    new String[]{"15 days to 45 days", "5.50%"},
                    new String[]{"46 days to 90 days", "5.75%"},
                    new String[]{"91 days to 180 days", "6.00%"},
                    new String[]{"181 days to 270 days", "6.50%"},
                    new String[]{"271 days to 332 days", "6.75%"},
                    new String[]{"333 days", "8.00%"},
                    new String[]{"334 days to 400 days", "7.25%"},
                    new String[]{"401 days to 3 years", "6.75%"},
                    new String[]{"Above 3 years upto 10 years", "6.50%"}
            ));
        }});

        put("CSB Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 60 days", "4.50%"},
                    new String[]{"61 days to 90 days", "5.00%"},
                    new String[]{"91 days to 179 days", "5.55%"},
                    new String[]{"180 days to 190 days", "6.00%"},
                    new String[]{"191 days*", "7.25%"},
                    new String[]{"192 days to less than 1 year", "4.25%"},
                    new String[]{"1 year", "5.00%"},
                    new String[]{"Above 1 year to 500 days", "5.50%"},
                    new String[]{"1501 days*", "8.00%"},
                    new String[]{"Above 501 days to 2 years", "5.50%"},
                    new String[]{"Above 2 years to less than 750 days", "5.75%"},
                    new String[]{"1750 days*", "7.10%"},
                    new String[]{"Above 750 days to 5 years", "5.75%"},
                    new String[]{"Above 5 years to less than 7 years", "6.00%"},
                    new String[]{"17 years*", "7.00%"},
                    new String[]{"Above 7 years to 10 years", "6.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 60 days", "4.50%"},
                    new String[]{"61 days to 90 days", "5.00%"},
                    new String[]{"91 days to 179 days", "5.55%"},
                    new String[]{"180 days to 190 days", "6.50%"},
                    new String[]{"191 days*", "7.25%"},
                    new String[]{"192 days to less than 1 year", "4.75%"},
                    new String[]{"1 year", "5.50%"},
                    new String[]{"Above 1 year to 500 days", "6.00%"},
                    new String[]{"501 days*", "8.00%"},
                    new String[]{"Above 501 days to 2 years", "6.00%"},
                    new String[]{"Above 2 years to less than 750 days", "6.25%"},
                    new String[]{"750 days*", "7.10%"},
                    new String[]{"Above 750 days to 5 years", "6.25%"},
                    new String[]{"Above 5 years to less than 7 years", "6.50%"},
                    new String[]{"7 years*", "7.00%"},
                    new String[]{"Above 7 years to 10 years", "6.50%"}
            ));
        }});

        put("DCB Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"45 days", "3.75%"},
                    new String[]{"46 days to 90 days", "4.00%"},
                    new String[]{"91 days to less than 6 months", "4.75%"},
                    new String[]{"6 months to less than 10 months", "6.25%"},
                    new String[]{"10 months to less than 12 months", "7.25%"},
                    new String[]{"12 months", "7.15%"},
                    new String[]{"12 months 1 day to 12 months 10 days", "7.85%"},
                    new String[]{"12 months 11 days to 18 months 5 days", "7.15%"},
                    new String[]{"18 months 6 days to less than 700 days", "7.50%"},
                    new String[]{"More than 700 days to less than 25 months", "7.55%"},
                    new String[]{"25 months to 26 months", "8.00%"},
                    new String[]{"More than 26 months to less than 37 months", "7.60%"},
                    new String[]{"37 months to 38 months", "7.90%"},
                    new String[]{"More than 38 months to less than 61 months", "7.40%"},
                    new String[]{"61 months", "7.65%"},
                    new String[]{"More than 61 months to 120 months", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"45 days", "4.25%"},
                    new String[]{"46 days to 90 days", "4.50%"},
                    new String[]{"91 days to less than 6 months", "5.25%"},
                    new String[]{"6 months to less than 10 months", "6.75%"},
                    new String[]{"10 months to less than 12 months", "7.75%"},
                    new String[]{"12 months", "7.65%"},
                    new String[]{"12 months 1 day to 12 months 10 days", "8.35%"},
                    new String[]{"12 months 11 days to 18 months 5 days", "7.65%"},
                    new String[]{"18 months 6 days to less than 700 days", "8.00%"},
                    new String[]{"More than 700 days to less than 25 months", "8.05%"},
                    new String[]{"25 months to 26 months", "8.60%"},
                    new String[]{"More than 26 months to less than 37 months", "8.10%"},
                    new String[]{"37 months to 38 months", "8.50%"},
                    new String[]{"More than 38 months to less than 61 months", "7.90%"},
                    new String[]{"61 months", "8.15%"},
                    new String[]{"More than 61 months to 120 months", "7.75%"}
            ));
        }});

    }};

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
        tableLayout.removeAllViews();

        // Add the heading row
        TableRow headingRow = new TableRow(getActivity());
        headingRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView daysHeadingTextView = new TextView(getActivity());
        daysHeadingTextView.setText("Days");
        daysHeadingTextView.setPadding(16, 16, 16, 16);
        daysHeadingTextView.setTextSize(18);
        daysHeadingTextView.setGravity(Gravity.CENTER);
        daysHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        daysHeadingTextView.setTypeface(null, Typeface.BOLD);
        daysHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        daysHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        TextView annualRateHeadingTextView = new TextView(getActivity());
        annualRateHeadingTextView.setText("Annual Interest (%)");
        annualRateHeadingTextView.setPadding(16, 16, 16, 16);
        annualRateHeadingTextView.setTextSize(18);
        annualRateHeadingTextView.setGravity(Gravity.CENTER);
        annualRateHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        annualRateHeadingTextView.setTypeface(null, Typeface.BOLD);
        annualRateHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        annualRateHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        headingRow.addView(daysHeadingTextView);
        headingRow.addView(annualRateHeadingTextView);
        tableLayout.addView(headingRow);

        // Get selected bank and age group
        String selectedBank = selectBankDropdown.getSelectedItem().toString();
        String selectedAgeGroup = ageGroupDropdown.getSelectedItem().toString();

        bankName.setText(selectedBank);
        ageGroup.setText(selectedAgeGroup);

        // Get FD rates based on bank and age group
        List<String[]> selectedRates = fdRatesMap.getOrDefault(selectedBank, new HashMap<>())
                .getOrDefault(selectedAgeGroup, new ArrayList<>());

        // Add FD Rates dynamically to the table
        for (String[] rate : selectedRates) {
            TableRow row = new TableRow(getActivity());
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView daysTextView = new TextView(getActivity());
            daysTextView.setText(rate[0]);
            daysTextView.setPadding(16, 16, 16, 16);
            daysTextView.setTextSize(16);
            daysTextView.setGravity(Gravity.CENTER);
            daysTextView.setBackgroundResource(R.drawable.cell_border);
            daysTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            TextView annualRateTextView = new TextView(getActivity());
            annualRateTextView.setText(rate[1]);
            annualRateTextView.setPadding(16, 16, 16, 16);
            annualRateTextView.setTextSize(16);
            annualRateTextView.setGravity(Gravity.CENTER);
            annualRateTextView.setBackgroundResource(R.drawable.cell_border);
            annualRateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            row.addView(daysTextView);
            row.addView(annualRateTextView);
            tableLayout.addView(row);
        }
    }

    private void resetDropdownsAndTable() {
        selectBankDropdown.setSelection(0);
        ageGroupDropdown.setSelection(0);
        resultBox.setVisibility(View.GONE);
        tableLayout.removeAllViews();
    }
}
