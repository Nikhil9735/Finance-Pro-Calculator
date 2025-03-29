package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;

public class EMPSalaryInput extends Fragment {

    private EditText inputCTC, empSalaryBonusTextBox, empSalaryMonthlyTaxTextBox, empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployeePFTextBox, empSalaryAddiDeduction1TextBox, empSalaryAddiDeduction2TextBox;
    private Button calculateButton, buttonClear;
    private TextView errorInputAmount, resultMonthlyDeduction, resultAnnualDeduction, resultTakeHomeMonthly, resultTakeHomeAnnual, errorAddDiduction;
    private CheckBox empSalaryBonusCheckBox;
    private RadioGroup empSalaryRadioGroup;
    private LinearLayout deductionsContainer, resultBox;
    private int deductionFieldCount = 2;
    private static final int MAX_MPONTHLY_DEDUCTION_FIELDS = 5;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empsalaryinput, container, false);

        // Initialize the input fields and result TextViews
        inputCTC = view.findViewById(R.id.empSalaryCTCTextBox);
        errorInputAmount = view.findViewById(R.id.errorInputAmount);
        empSalaryMonthlyTaxTextBox = view.findViewById(R.id.empSalaryMonthlyTaxTextBox);
        empSalaryBonusCheckBox = view.findViewById(R.id.empSalaryBonusCheckBox);
        empSalaryMonthlyEmployerPFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployerPFTextBox);
        empSalaryMonthlyEmployeePFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployeePFTextBox);
        empSalaryAddiDeduction1TextBox = view.findViewById(R.id.empSalaryAddiDiduction1TextBox);
        empSalaryAddiDeduction2TextBox = view.findViewById(R.id.empSalaryAddiDiduction2TextBox);
        errorAddDiduction = view.findViewById(R.id.errorAddDiduction);
        calculateButton = view.findViewById(R.id.calculateButton);
        buttonClear = view.findViewById(R.id.buttonClear);
        resultMonthlyDeduction = view.findViewById(R.id.SalarySummeryTotalMonthlyDeductionResult);
        resultAnnualDeduction = view.findViewById(R.id.SalarySummeryTotalAnnualDeductionsResult);
        resultTakeHomeMonthly = view.findViewById(R.id.SalarySummeryTakeHomeMonthlySalaryResult);
        resultTakeHomeAnnual = view.findViewById(R.id.SalarySummeryTakeHomeAnnualSalaryResult);

        deductionsContainer = view.findViewById(R.id.deductionsContainer);
        TextView empSalaryHeading = view.findViewById(R.id.empSalaryHeading);
        empSalaryBonusTextBox = view.findViewById(R.id.empSalaryBonusTextBox);
        empSalaryRadioGroup = view.findViewById(R.id.empSalaryRadioGroup);
        Button addDeductionButton = view.findViewById(R.id.addDeductionButton);
        resultBox = view.findViewById(R.id.resultBox);

        addDeductionButton.setOnClickListener(v -> {
            if (deductionFieldCount < MAX_MPONTHLY_DEDUCTION_FIELDS) {
                addNewMonthlyDeductionField();
            } else {
                errorAddDiduction.setText("Maximum 5 deductions allowed");
                Toast.makeText(getContext(), "Maximum 5 deductions allowed", Toast.LENGTH_SHORT).show();
            }
        });

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> empSalaryHeading.setText(operation));

        CommonMethod.addClearErrorTextWatcher(inputCTC, errorInputAmount);

        calculateButton.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            mainViewModel.setOperation("Employee Salary");
            // Call the validation method
            if (validateInputs()) {
                calculateSalary();
            }
        });

        // Observe visibility for resultBox
        mainViewModel.getResultBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            resultBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        buttonClear.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            clearAllFields();
        });

        empSalaryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.empSalaryAmtRadioButton) {
                empSalaryBonusTextBox.setText("");
                empSalaryBonusTextBox.setHint("Rs.");
            } else if (checkedId == R.id.empSalaryPercentageRadioButton) {
                empSalaryBonusTextBox.setText("");
                empSalaryBonusTextBox.setHint("%");
            }
        });

        // Add CheckBox listener
        empSalaryBonusCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show RadioGroup when CheckBox is checked
                empSalaryRadioGroup.setVisibility(View.VISIBLE);
                empSalaryBonusTextBox.setVisibility(View.VISIBLE);
            } else {
                // Hide RadioGroup when CheckBox is unchecked
                empSalaryRadioGroup.setVisibility(View.GONE);
                empSalaryBonusTextBox.setVisibility(View.GONE);
                // Optionally reset RadioGroup to default selection
                empSalaryRadioGroup.check(R.id.empSalaryAmtRadioButton);
            }
        });

        return view;
    }

    private boolean validateInputs() {
        // Validate the CTC input field
        String input1Str = inputCTC.getText().toString();

        if (input1Str.isEmpty()) {
            CommonMethod.validateInputs(inputCTC, errorInputAmount, "Please enter your CTC.");
            inputCTC.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(input1Str);
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(inputCTC, errorInputAmount, "Please enter valid numbers.");
            inputCTC.requestFocus();
            return false;
        }

        // Validate empSalaryMonthlyTaxTextBox
        if (empSalaryMonthlyTaxTextBox.getText().toString().isEmpty()) {
            empSalaryMonthlyTaxTextBox.setError("Monthly Tax is mandatory.");
            empSalaryMonthlyTaxTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyTaxTextBox.getText().toString());
        } catch (NumberFormatException e) {
            empSalaryMonthlyTaxTextBox.setError("Please enter a valid Monthly Tax value.");
            empSalaryMonthlyTaxTextBox.requestFocus();
            return false;
        }

        // Validate empSalaryMonthlyEmployerPFTextBox
        if (empSalaryMonthlyEmployerPFTextBox.getText().toString().isEmpty()) {
            empSalaryMonthlyEmployerPFTextBox.setError("Monthly Employer PF is mandatory.");
            empSalaryMonthlyEmployerPFTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyEmployerPFTextBox.getText().toString());
        } catch (NumberFormatException e) {
            empSalaryMonthlyEmployerPFTextBox.setError("Please enter a valid Monthly Employer PF value.");
            empSalaryMonthlyEmployerPFTextBox.requestFocus();
            return false;
        }

        // Validate empSalaryMonthlyEmployeePFTextBox
        if (empSalaryMonthlyEmployeePFTextBox.getText().toString().isEmpty()) {
            empSalaryMonthlyEmployeePFTextBox.setError("Monthly Employee PF is mandatory.");
            empSalaryMonthlyEmployeePFTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyEmployeePFTextBox.getText().toString());
        } catch (NumberFormatException e) {
            empSalaryMonthlyEmployeePFTextBox.setError("Please enter a valid Monthly Employee PF value.");
            empSalaryMonthlyEmployeePFTextBox.requestFocus();
            return false;
        }

        // Validate bonus if the checkbox is checked
        if (empSalaryBonusCheckBox.isChecked()) {
            String bonusInput = empSalaryBonusTextBox.getText().toString();
            if (bonusInput.isEmpty()) {
                empSalaryBonusTextBox.setError("Bonus field is mandatory when checked.");
                empSalaryBonusTextBox.requestFocus();
                return false;
            }

            try {
                Float.parseFloat(bonusInput);
            } catch (NumberFormatException e) {
                empSalaryBonusTextBox.setError("Please enter a valid bonus value.");
                empSalaryBonusTextBox.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void calculateSalary() {
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");

        // Retrieve and parse input values with default values for empty fields
        float empSalaryCTC = parseFloatOrDefault(inputCTC.getText().toString(), 0);
        float empSalaryMonthlyTax = parseFloatOrDefault(empSalaryMonthlyTaxTextBox.getText().toString(), 0);
        float empSalaryMonthlyEmployerPF = parseFloatOrDefault(empSalaryMonthlyEmployerPFTextBox.getText().toString(), 0);
        float empSalaryMonthlyEmployeePF = parseFloatOrDefault(empSalaryMonthlyEmployeePFTextBox.getText().toString(), 0);
        float empSalaryAddiDeduction1 = parseFloatOrDefault(empSalaryAddiDeduction1TextBox.getText().toString(), 0);
        float empSalaryAddiDeduction2 = parseFloatOrDefault(empSalaryAddiDeduction2TextBox.getText().toString(), 0);

        // Initialize total monthly deductions
        float totalMonthlyDeduction = empSalaryMonthlyTax + empSalaryMonthlyEmployerPF + empSalaryMonthlyEmployeePF + empSalaryAddiDeduction1 + empSalaryAddiDeduction2;

        // Retrieve bonus value and adjust deductions based on the selected mode
        String bonusText = empSalaryBonusTextBox.getText().toString();
        if (!bonusText.isEmpty()) {
            float bonusValue = Float.parseFloat(bonusText);
            if (empSalaryRadioGroup.getCheckedRadioButtonId() == R.id.empSalaryAmtRadioButton) {
                totalMonthlyDeduction += bonusValue / 12; // Divide by 12 to get monthly deduction
            } else if (empSalaryRadioGroup.getCheckedRadioButtonId() == R.id.empSalaryPercentageRadioButton) {
                totalMonthlyDeduction += (empSalaryCTC * (bonusValue / 100)) / 12; // Percentage of CTC distributed over 12 months
            }
        }

        // Loop through all dynamically added fields and add their values to the total deduction
        for (int i = 0; i < deductionsContainer.getChildCount(); i++) {
            View child = deductionsContainer.getChildAt(i);
            if (child instanceof EditText) {
                EditText deductionField = (EditText) child;
                String deductionText = deductionField.getText().toString();
                if (!deductionText.isEmpty()) {
                    totalMonthlyDeduction += Float.parseFloat(deductionText);
                }
            }
        }

        // Calculate Total Annual Deductions
        float totalAnnualDeductions = totalMonthlyDeduction * 12;

        // Calculate Take-Home Monthly and Annual Salary
        float monthlyCTC = empSalaryCTC / 12;
        float takeHomeMonthlySalary = monthlyCTC - totalMonthlyDeduction;
        float takeHomeAnnualSalary = takeHomeMonthlySalary * 12;

        float maturityAmt = 0;
        float number1Result = 0;

        // Update the ViewModel with calculated values
        mainViewModel.setUserInputs(maturityAmt, number1Result, totalMonthlyDeduction, totalAnnualDeductions, takeHomeMonthlySalary, takeHomeAnnualSalary);

        // Format and display the results
        resultMonthlyDeduction.setText("Rs. " + decimalFormat.format(totalMonthlyDeduction));
        resultAnnualDeduction.setText("Rs. " + decimalFormat.format(totalAnnualDeductions));
        resultTakeHomeMonthly.setText("Rs. " + decimalFormat.format(takeHomeMonthlySalary));
        resultTakeHomeAnnual.setText("Rs. " + decimalFormat.format(takeHomeAnnualSalary));

        // Show result and chart box
        mainViewModel.setResultBoxVisibility(true);
        mainViewModel.setChartBoxVisibility(true);
    }

    // Helper method to parse float or return a default value
    private float parseFloatOrDefault(String value, float defaultValue) {
        if (value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void clearAllFields() {
        // Clear input fields
        inputCTC.setText("");
        empSalaryBonusTextBox.setText("");
        empSalaryMonthlyTaxTextBox.setText("");
        empSalaryMonthlyEmployerPFTextBox.setText("");
        empSalaryMonthlyEmployeePFTextBox.setText("");
        empSalaryAddiDeduction1TextBox.setText("");
        empSalaryAddiDeduction2TextBox.setText("");
        resultBox.setVisibility(View.GONE);
        errorAddDiduction.setText("");
        empSalaryBonusCheckBox.setChecked(false);

        // Clear results
        resultMonthlyDeduction.setText("");
        resultAnnualDeduction.setText("");
        resultTakeHomeMonthly.setText("");
        resultTakeHomeAnnual.setText("");

        empSalaryRadioGroup.check(R.id.empSalaryAmtRadioButton);

        // Clear dynamically added fields in deductionsContainer
        deductionsContainer.removeAllViews();

        // Reset deduction field count
        deductionFieldCount = 2;

        mainViewModel.setChartBoxVisibility(false);
        mainViewModel.setResultBoxVisibility(false);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton); // Reset Growth radio button

        // Display a message indicating the fields have been cleared
        Toast.makeText(getContext(), "All fields have been cleared", Toast.LENGTH_SHORT).show();
    }

    // Add new monthly deduction fields dynamically
    private void addNewMonthlyDeductionField() {
        deductionFieldCount++;

        // Create a new TextView for heading
        TextView newHeading = new TextView(getContext());
        newHeading.setText("Monthly Additional Deduction " + deductionFieldCount + " (Optional) ");
        newHeading.setTextColor(getResources().getColor(R.color.selectedText));
        newHeading.setTextSize(15.6f);
        newHeading.setPadding(4, 25, 0, 0);

        // Apply custom font programmatically
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.regular);
        if (typeface != null) {
            newHeading.setTypeface(typeface);
        }
        deductionsContainer.addView(newHeading);

        // Create a new EditText for input
        EditText newEditText = new EditText(getContext());
        newEditText.setHint("Rs.");
        newEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        newEditText.setMaxLines(1);
        newEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        deductionsContainer.addView(newEditText);
    }
}