package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;

public class EMPIncrementFragment extends Fragment {

    private Button calculateButton, buttonClear;
    private EditText empIncrementCTCTextBox, empIncrementPercentageTextBox;
    private TextView IncrementSummeryCurrentCTCHeading, IncrementSummeryCurrentCTCResult,
            IncrementSummeryMonthlyIncrementAmountHeading, IncrementSummeryMonthlyIncrementAmountResult,
            IncrementSummeryAnnualIncrementAmountHeading, IncrementSummeryAnnualIncrementAmountResult,
            IncrementSummeryTakeHomeAnnualIncrement, IncrementSummeryNewCTCResult, empIncrementeditTextHeading;
    private RadioGroup empIncrementRadioGroup;
    private LinearLayout resultBox;
    private MainViewModel mainViewModel;
    private boolean isClearButtonClicked = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emphike, container, false);

        // Initialize UI elements
        empIncrementCTCTextBox = view.findViewById(R.id.empIncrementCTCTextBox);
        empIncrementeditTextHeading = view.findViewById(R.id.empIncrementeditTextHeading);
        empIncrementPercentageTextBox = view.findViewById(R.id.empIncrementPercentageTextBox);
        empIncrementRadioGroup = view.findViewById(R.id.empIncrementRadioGroup);
        calculateButton = view.findViewById(R.id.calculateButton);
        buttonClear = view.findViewById(R.id.buttonClear);
        IncrementSummeryCurrentCTCHeading  = view.findViewById(R.id.IncrementSummeryCurrentCTCHeading);
        IncrementSummeryCurrentCTCResult = view.findViewById(R.id.IncrementSummeryCurrentCTCResult);
        IncrementSummeryMonthlyIncrementAmountHeading  = view.findViewById(R.id.IncrementSummeryMonthlyIncrementAmountHeading);
        IncrementSummeryMonthlyIncrementAmountResult  = view.findViewById(R.id.IncrementSummeryMonthlyIncrementAmountResult);
        IncrementSummeryAnnualIncrementAmountHeading  = view.findViewById(R.id.IncrementSummeryAnnualIncrementAmountHeading);
        IncrementSummeryAnnualIncrementAmountResult = view.findViewById(R.id.IncrementSummeryAnnualIncrementAmountResult);
        IncrementSummeryTakeHomeAnnualIncrement  = view.findViewById(R.id.IncrementSummeryTakeHomeAnnualIncrementHeading);
        IncrementSummeryNewCTCResult = view.findViewById(R.id.IncrementSummeryNewCTCResult);
        resultBox = view.findViewById(R.id.resultBox);

        // ViewModel for keyboard hiding (if needed)
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        // Observe visibility for resultBox
        mainViewModel.getResultBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            resultBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        // Set calculate button listener
        calculateButton.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            calculateIncrement();
        });

        buttonClear.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            //isClearButtonClicked = true; // Mark the flag as true
            clearAllFields();
            //isClearButtonClicked = false; // Reset the flag after clearing
        });

        empIncrementRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.empIncrementAmtRadioButton) {
                empIncrementeditTextHeading.setText("Increment Percentage(%)");
                empIncrementPercentageTextBox.setHint("%");
                clearAllFields();
            } else if (checkedId == R.id.empIncrementPercentageRadioButton) {
                empIncrementeditTextHeading.setText("Increment Amount");
                empIncrementPercentageTextBox.setHint("Rs.");
                clearAllFields();
            }
        });

        return view;
    }

    private void calculateIncrement() {
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");
        DecimalFormat percentageDecimalFormat = new DecimalFormat("##,##,##0.00###");


        // Get input values
        String input1 = empIncrementCTCTextBox.getText().toString();
        String input2 = empIncrementPercentageTextBox.getText().toString();

        if (!input1.isEmpty() && !input2.isEmpty()) {
            try {
                float currentCtc = Float.parseFloat(input1);
                float incrementPercentage = Float.parseFloat(input2);

                // Check selected radio button
                int selectedId = empIncrementRadioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.empIncrementAmtRadioButton) {
                    mainViewModel.setOperation(getString(R.string.empIncrementAmount));
                    // Increment amount calculation
                    float annualIncrementAmount = (currentCtc * incrementPercentage) / 100;
                    float monthylIncrementAmount = annualIncrementAmount / 12;
                    float newCtc = currentCtc + annualIncrementAmount;

//                    IncrementSummeryCurrentCTCHeading.setVisibility(View.VISIBLE);
//                    IncrementSummeryCurrentCTCResult.setVisibility(View.VISIBLE);
                    IncrementSummeryMonthlyIncrementAmountHeading.setVisibility(View.VISIBLE);
                    IncrementSummeryMonthlyIncrementAmountResult.setVisibility(View.VISIBLE);
                    IncrementSummeryAnnualIncrementAmountHeading.setVisibility(View.VISIBLE);
                    IncrementSummeryAnnualIncrementAmountResult.setVisibility(View.VISIBLE);

                    float userInput1 = 0;
                    float userInput2 = 0;

                    // Update the ViewModel with calculated values
                    mainViewModel.setUserInputs(userInput1, userInput2, currentCtc, monthylIncrementAmount, annualIncrementAmount, newCtc);
                    // Update results
                    IncrementSummeryCurrentCTCResult.setText(decimalFormat.format(currentCtc));
                    IncrementSummeryMonthlyIncrementAmountResult.setText(decimalFormat.format(monthylIncrementAmount));
                    IncrementSummeryAnnualIncrementAmountResult.setText(decimalFormat.format(annualIncrementAmount));
                    IncrementSummeryNewCTCResult.setText(decimalFormat.format(newCtc));

                } else if (selectedId == R.id.empIncrementPercentageRadioButton) {

                    mainViewModel.setOperation(getString(R.string.empIncrementPercent));
                    IncrementSummeryTakeHomeAnnualIncrement.setText("Increment Percentage");
//                    IncrementSummeryCurrentCTCHeading.setVisibility(View.GONE);
//                    IncrementSummeryCurrentCTCResult.setVisibility(View.GONE);
                    IncrementSummeryMonthlyIncrementAmountHeading.setVisibility(View.GONE);
                    IncrementSummeryMonthlyIncrementAmountResult.setVisibility(View.GONE);
                    IncrementSummeryAnnualIncrementAmountHeading.setVisibility(View.GONE);
                    IncrementSummeryAnnualIncrementAmountResult.setVisibility(View.GONE);

                    // Percentage calculation
                    float percentage = (incrementPercentage / currentCtc) * 100;

                    IncrementSummeryCurrentCTCHeading.setText("Employee Increment Feedback");
                    if (percentage >= 15) {
                        IncrementSummeryCurrentCTCResult.setText("Excellent! You received a significant increment.");
                    } else if (percentage >= 10) {
                        IncrementSummeryCurrentCTCResult.setText("Good job! Your increment is above average.");
                    } else {
                        IncrementSummeryCurrentCTCResult.setText("Your increment is average. Keep up the hard work!");
                    }

                    float userInput1 = 0;
                    float userInput2 = 0;
                    float annualIncrementAmount = 100 - percentage;
                    float monthylIncrementAmount = 0;
                    float currentCtcc = 0;

                    // Update the ViewModel with calculated values
                    mainViewModel.setUserInputs(userInput1, userInput2, annualIncrementAmount, currentCtcc, monthylIncrementAmount, percentage);

                    // Update results
                    IncrementSummeryNewCTCResult.setText(percentageDecimalFormat.format(percentage) + "%");

                } else {
                    Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid input format", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (input1.isEmpty()) {
                empIncrementCTCTextBox.setError("Please enter current CTC");
            }
            if (input2.isEmpty()) {
                empIncrementPercentageTextBox.setError("Please enter increment percentage");
            }
        }
        // Show result and chart box
        mainViewModel.setResultBoxVisibility(true);
        mainViewModel.setChartBoxVisibility(true);
    }

    private void clearAllFields() {
        // Clear input fields
        empIncrementCTCTextBox.setText("");
        empIncrementPercentageTextBox.setText("");
        IncrementSummeryCurrentCTCResult.setText("");
        IncrementSummeryMonthlyIncrementAmountResult.setText("");
        IncrementSummeryAnnualIncrementAmountResult.setText("");
        IncrementSummeryNewCTCResult.setText("");
        resultBox.setVisibility(View.GONE);

        // Reset radio group only if the clear button was clicked
//        if (isClearButtonClicked) {
//            empIncrementRadioGroup.check(R.id.empIncrementAmtRadioButton);
//        }

        mainViewModel.setChartBoxVisibility(false);
        mainViewModel.setResultBoxVisibility(false);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton); // Reset Growth radio button

        // Display a message indicating the fields have been cleared
        Toast.makeText(getContext(), "All fields have been cleared", Toast.LENGTH_SHORT).show();
    }
}