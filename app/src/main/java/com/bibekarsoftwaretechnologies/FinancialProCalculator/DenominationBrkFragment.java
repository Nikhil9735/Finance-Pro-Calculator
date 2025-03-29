package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

public class DenominationBrkFragment extends Fragment {

    private EditText inputAmount;
    private Button calculateButton;
    private TextView errorInputAmount;
    private Button buttonClear;
    private View resultBox;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_denominationbrk, container, false);

        // Bind UI elements
        inputAmount = view.findViewById(R.id.inputAmount);
        calculateButton = view.findViewById(R.id.calculateButton);
        errorInputAmount = view.findViewById(R.id.errorInputAmount);
        buttonClear = view.findViewById(R.id.buttonClear);
        resultBox = view.findViewById(R.id.resultBox);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Set up button click listener
        calculateButton.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());

            String amountString = inputAmount.getText().toString().trim();
            errorInputAmount.setText(""); // Clear previous error messages

            if (amountString.isEmpty()) {
                errorInputAmount.setText("Please enter a valid amount.");
                return;
            }

            try {
                int amount = Integer.parseInt(amountString);
                if (amount <= 0) {
                    errorInputAmount.setText("Amount must be greater than zero.");
                } else {
                    resultBox.setVisibility(View.VISIBLE);
                    calculateDenominations(amount);
                }
            } catch (NumberFormatException e) {
                errorInputAmount.setText("Invalid input. Please enter a numeric amount.");
            }
        });

        // Set up buttonClear functionality
        buttonClear.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());

            resultBox.setVisibility(View.GONE);
            inputAmount.setText("");
            errorInputAmount.setText(""); // Clear error messages
        });

        return view;
    }

    private void calculateDenominations(int amount) {
        int[] denominations = {2000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
        TableLayout tableLayout = resultBox.findViewById(R.id.tableLayout);

        // Clear previous rows except the header
        if (tableLayout.getChildCount() > 1) {
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        }

        for (int denomination : denominations) {
            int count = amount / denomination;
            if (count > 0) {
                // Create a new table row
                TableRow row = new TableRow(getContext());
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                // Denomination column
                TextView denominationText = new TextView(getContext());
                denominationText.setText(String.valueOf(denomination));
                TableRow.LayoutParams currencyParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f);
                denominationText.setLayoutParams(currencyParams);
                denominationText.setPadding(16, 16, 16, 16);
                denominationText.setGravity(Gravity.CENTER);
                denominationText.setTextSize(18);
                row.addView(denominationText);

                // x column
                TextView x = new TextView(getContext());
                x.setText("x");
                TableRow.LayoutParams xx = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);
                x.setLayoutParams(xx);
                x.setPadding(16, 16, 16, 16);
                x.setGravity(Gravity.CENTER); // Center the text horizontally and vertically
                x.setTextSize(25);
                row.addView(x);

                TextView countText = new TextView(getContext());
                countText.setText(String.valueOf(count));
                TableRow.LayoutParams countParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6f);
                countText.setLayoutParams(countParams);
                countText.setPadding(16, 16, 16, 16);
                countText.setGravity(Gravity.CENTER);
                countText.setTextSize(18);
                row.addView(countText);

                // Add the row to the table
                tableLayout.addView(row);

                // Update the remaining amount
                amount %= denomination;
            }
        }
    }
}
