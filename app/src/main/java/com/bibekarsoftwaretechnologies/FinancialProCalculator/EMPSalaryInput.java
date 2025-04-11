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
    private TextView empSalaryCTCError, empSalaryBonusError, empSalaryMonthlyTaxError, empSalaryMonthlyEmployerPFError, empSalaryMonthlyEmployeePFError, resultMonthlyDeduction, resultAnnualDeduction, resultTakeHomeMonthly, resultTakeHomeAnnual, errorAddDiduction;
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
        empSalaryCTCError = view.findViewById(R.id.empSalaryCTCError);
        empSalaryBonusTextBox = view.findViewById(R.id.empSalaryBonusTextBox);
        empSalaryBonusError = view.findViewById(R.id.empSalaryBonusError);
        empSalaryBonusCheckBox = view.findViewById(R.id.empSalaryBonusCheckBox);
        empSalaryMonthlyTaxTextBox = view.findViewById(R.id.empSalaryMonthlyTaxTextBox);
        empSalaryMonthlyTaxError = view.findViewById(R.id.empSalaryMonthlyTaxError);
        empSalaryMonthlyEmployerPFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployerPFTextBox);
        empSalaryMonthlyEmployerPFError = view.findViewById(R.id.empSalaryMonthlyEmployerPFError);
        empSalaryMonthlyEmployeePFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployeePFTextBox);
        empSalaryMonthlyEmployeePFError = view.findViewById(R.id.empSalaryMonthlyEmployeePFError);
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
        empSalaryRadioGroup = view.findViewById(R.id.empSalaryRadioGroup);
        Button addDeductionButton = view.findViewById(R.id.addDeductionButton);
        resultBox = view.findViewById(R.id.resultBox);

        addDeductionButton.setOnClickListener(v -> {
            if (deductionFieldCount < MAX_MPONTHLY_DEDUCTION_FIELDS) {
                addNewMonthlyDeductionField();
            } else {
                errorAddDiduction.setText(getString(R.string.EmpMonthlyAddDiductionError));
                Toast.makeText(getContext(), getString(R.string.EmpMonthlyAddDiductionError), Toast.LENGTH_SHORT).show();
            }
        });

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> empSalaryHeading.setText(operation));

        CommonMethod.addClearErrorTextWatcher(inputCTC, empSalaryCTCError);
        CommonMethod.addClearErrorTextWatcher(empSalaryBonusTextBox, empSalaryBonusError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError);

        calculateButton.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            mainViewModel.setOperation(getString(R.string.empSalary));
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
                empSalaryBonusTextBox.setHint(getString(R.string.Rs));
            } else if (checkedId == R.id.empSalaryPercentageRadioButton) {
                empSalaryBonusTextBox.setText("");
                empSalaryBonusTextBox.setHint(getString(R.string.percentageSymbol));
            }
        });

        // Add CheckBox listener
        empSalaryBonusCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show RadioGroup when CheckBox is checked
                empSalaryRadioGroup.setVisibility(View.VISIBLE);
                empSalaryBonusTextBox.setVisibility(View.VISIBLE);
                empSalaryBonusTextBox.setText("");
            } else {
                // Hide RadioGroup when CheckBox is unchecked
                empSalaryRadioGroup.setVisibility(View.GONE);
                empSalaryBonusTextBox.setVisibility(View.GONE);
                empSalaryBonusError.setVisibility(View.GONE);
                // Optionally reset RadioGroup to default selection
                empSalaryRadioGroup.check(R.id.empSalaryAmtRadioButton);
            }
        });

        return view;
    }

    private boolean validateInputs() {
        // Validate the CTC input field
        String input1Str = inputCTC.getText().toString();

        try {
            // Validate Deposit Amount
            if (input1Str.isEmpty()) {
                CommonMethod.validateInputs(inputCTC, empSalaryCTCError, getString(R.string.EmpCTC_alert));
                inputCTC.requestFocus();
                return false;
            }

            float input1 = Float.parseFloat(input1Str);
            if (input1 <= 0) {
                CommonMethod.validateInputs(inputCTC, empSalaryCTCError, getString(R.string.EmpCTC_zero_alert));
                mainViewModel.setResultBoxVisibility(false);
                return false;
            }
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(inputCTC, empSalaryCTCError, getString(R.string.EmpCTC_invalid_alert));
            inputCTC.requestFocus();
            return false;
        }


        // Validate bonus if the checkbox is checked
        if (empSalaryBonusCheckBox.isChecked()) {
            String bonusInput = empSalaryBonusTextBox.getText().toString();
            try {
                // Validate Interest Rate
                if (bonusInput.isEmpty()) {
                    CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.bonus_req_alert));
                    empSalaryBonusTextBox.requestFocus();
                    return false;
                }

                if (empSalaryRadioGroup.getCheckedRadioButtonId() == R.id.empSalaryPercentageRadioButton) {
                    float input2 = Float.parseFloat(bonusInput);
                    if (input2 > 100) {
                        CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.Emp_Bonus_Percentage_alert));
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }
                }
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.bonus_invalid_alert));
                empSalaryBonusTextBox.requestFocus();
                return false;
            }
        }


        // Validate empSalaryMonthlyTaxTextBox
        if (empSalaryMonthlyTaxTextBox.getText().toString().isEmpty()) {
            CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.monthly_tax_req_alert));
            empSalaryMonthlyTaxTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyTaxTextBox.getText().toString());
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.monthly_tax_invalid_alert));
            empSalaryMonthlyTaxTextBox.requestFocus();
            return false;
        }

        // Validate empSalaryMonthlyEmployerPFTextBox
        if (empSalaryMonthlyEmployerPFTextBox.getText().toString().isEmpty()) {
            CommonMethod.validateInputs(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError, getString(R.string.employer_pf_req_alert));
            empSalaryMonthlyEmployerPFTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyEmployerPFTextBox.getText().toString());
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError, getString(R.string.employer_pf_invalid_alert));
            empSalaryMonthlyEmployerPFTextBox.requestFocus();
            return false;
        }

        // Validate empSalaryMonthlyEmployeePFTextBox
        if (empSalaryMonthlyEmployeePFTextBox.getText().toString().isEmpty()) {
            CommonMethod.validateInputs(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError, getString(R.string.EmpMonthlyEmployeePF_req_alert));
            empSalaryMonthlyEmployeePFTextBox.requestFocus();
            return false;
        }

        try {
            Float.parseFloat(empSalaryMonthlyEmployeePFTextBox.getText().toString());
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError, getString(R.string.EmpMonthlyEmployeePF_invalid_alert));
            empSalaryMonthlyEmployeePFTextBox.requestFocus();
            return false;
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

        inputCTC.setError(null);
        empSalaryBonusTextBox.setError(null);
        empSalaryMonthlyTaxTextBox.setError(null);
        empSalaryMonthlyEmployerPFTextBox.setError(null);
        empSalaryMonthlyEmployeePFTextBox.setError(null);

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
        newHeading.setText(getString(R.string.EmpMonthlyAddiDiduction) +" "+ deductionFieldCount +" "+ getString(R.string.EmpMonthlyAddiDiductionOptional));
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
        newEditText.setHint(getString(R.string.Rs));
        newEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        newEditText.setMaxLines(1);
        newEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        deductionsContainer.addView(newEditText);
    }
}