package com.nirvaysofttech.FinancialProCalculator;

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
import android.widget.RadioButton;
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

    private EditText empSalaryCTCTextBox, empSalaryBonusTextBox, empSalaryMonthlyTaxTextBox, empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployeePFTextBox, empSalaryAddiDeduction1TextBox, empSalaryAddiDeduction2TextBox;
    private Button calculateButton, buttonClear;
    private TextView empSalaryCTCError, empSalaryBonusError, empSalaryMonthlyTaxeditTextHeading, empSalaryMonthlyTaxError, empSalaryMonthlyEmployerPFHeding, empSalaryMonthlyEmployerPFError,
            empSalaryMonthlyEmployeePFHeding, empSalaryMonthlyEmployeePFError, empSalaryMonthlyAddDiduction1Heding, empSalaryMonthlyAddDiduction2Heding, errorAddDiduction,
            SalarySummeryTotalMonthlyDeductionHeading, SalarySummeryTotalMonthlyDeductionResult, SalarySummeryTotalAnnualDeductionsHeading, SalarySummeryTotalAnnualDeductionsResult,
            SalarySummeryTakeHomeMonthlySalaryHeading, SalarySummeryTakeHomeMonthlySalaryResult, SalarySummeryTakeHomeAnnualSalary, SalarySummeryTakeHomeAnnualSalaryResult;
    private CheckBox empSalaryBonusCheckBox;
    private RadioGroup empSalaryRadioGroup, empIncrementRadioGroup;
    private RadioButton empIncrementAmtRadioButton;
    private LinearLayout bonusIncludeOption, deductionsContainer, resultBox;
    private int deductionFieldCount = 2;
    private static final int MAX_MPONTHLY_DEDUCTION_FIELDS = 5;
    private MainViewModel mainViewModel;
    String operation;
    int checkedId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empsalaryinput, container, false);

        // Initialize the input fields and result TextViews
        empIncrementRadioGroup = view.findViewById(R.id.empIncrementRadioGroup);
        empIncrementAmtRadioButton = view.findViewById(R.id.empIncrementAmtRadioButton);
        empSalaryCTCTextBox = view.findViewById(R.id.empSalaryCTCTextBox);
        empSalaryCTCError = view.findViewById(R.id.empSalaryCTCError);
        bonusIncludeOption = view.findViewById(R.id.bonusIncludeOption);
        empSalaryBonusTextBox = view.findViewById(R.id.empSalaryBonusTextBox);
        empSalaryBonusError = view.findViewById(R.id.empSalaryBonusError);
        empSalaryBonusCheckBox = view.findViewById(R.id.empSalaryBonusCheckBox);
        empSalaryMonthlyTaxeditTextHeading = view.findViewById(R.id.empSalaryMonthlyTaxeditTextHeading);
        empSalaryMonthlyTaxTextBox = view.findViewById(R.id.empSalaryMonthlyTaxTextBox);
        empSalaryMonthlyTaxError = view.findViewById(R.id.empSalaryMonthlyTaxError);
        empSalaryMonthlyEmployerPFHeding = view.findViewById(R.id.empSalaryMonthlyEmployerPFHeding);
        empSalaryMonthlyEmployerPFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployerPFTextBox);
        empSalaryMonthlyEmployerPFError = view.findViewById(R.id.empSalaryMonthlyEmployerPFError);
        empSalaryMonthlyEmployeePFHeding = view.findViewById(R.id.empSalaryMonthlyEmployeePFHeding);
        empSalaryMonthlyEmployeePFTextBox = view.findViewById(R.id.empSalaryMonthlyEmployeePFTextBox);
        empSalaryMonthlyEmployeePFError = view.findViewById(R.id.empSalaryMonthlyEmployeePFError);
        empSalaryMonthlyAddDiduction1Heding = view.findViewById(R.id.empSalaryMonthlyAddDiduction1Heding);
        empSalaryAddiDeduction1TextBox = view.findViewById(R.id.empSalaryAddiDiduction1TextBox);
        empSalaryMonthlyAddDiduction2Heding = view.findViewById(R.id.empSalaryMonthlyAddDiduction2Heding);
        empSalaryAddiDeduction2TextBox = view.findViewById(R.id.empSalaryAddiDiduction2TextBox);
        errorAddDiduction = view.findViewById(R.id.errorAddDiduction);
        calculateButton = view.findViewById(R.id.calculateButton);
        buttonClear = view.findViewById(R.id.buttonClear);
        SalarySummeryTotalMonthlyDeductionHeading = view.findViewById(R.id.SalarySummeryTotalMonthlyDeductionHeading);
        SalarySummeryTotalMonthlyDeductionResult = view.findViewById(R.id.SalarySummeryTotalMonthlyDeductionResult);
        SalarySummeryTotalAnnualDeductionsHeading = view.findViewById(R.id.SalarySummeryTotalAnnualDeductionsHeading);
        SalarySummeryTotalAnnualDeductionsResult = view.findViewById(R.id.SalarySummeryTotalAnnualDeductionsResult);
        SalarySummeryTakeHomeMonthlySalaryHeading = view.findViewById(R.id.SalarySummeryTakeHomeMonthlySalaryHeading);
        SalarySummeryTakeHomeMonthlySalaryResult = view.findViewById(R.id.SalarySummeryTakeHomeMonthlySalaryResult);
        SalarySummeryTakeHomeAnnualSalary = view.findViewById(R.id.SalarySummeryTakeHomeAnnualSalary);
        SalarySummeryTakeHomeAnnualSalaryResult = view.findViewById(R.id.SalarySummeryTakeHomeAnnualSalaryResult);

        deductionsContainer = view.findViewById(R.id.deductionsContainer);
        TextView empSalaryHeading = view.findViewById(R.id.empSalaryHeading);
        empSalaryRadioGroup = view.findViewById(R.id.empSalaryRadioGroup);
        Button addDeductionButton = view.findViewById(R.id.addDeductionButton);
        resultBox = view.findViewById(R.id.resultBox);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> empSalaryHeading.setText(operation));
        operation = mainViewModel.getOperation().getValue();

        CommonMethod.addClearErrorTextWatcher(empSalaryCTCTextBox, empSalaryCTCError);
        CommonMethod.addClearErrorTextWatcher(empSalaryBonusTextBox, empSalaryBonusError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError);
        CommonMethod.addClearErrorTextWatcher(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError);

        // Observe visibility for resultBox
        mainViewModel.getResultBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            resultBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        calculateButton.setOnClickListener(v -> {
            calculateSalaryAndIncrement();
        });

        buttonClear.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());
            clearAllFields();

            if (operation.equals(getString(R.string.empSalaryIncrement))) {
                empIncrementAmtRadioButton.setChecked(true); // ✅ Select default radio
            }
        });

        if(operation.equals(getString(R.string.empSalary))){
            empIncrementRadioGroup.setVisibility(View.GONE);
            bonusIncludeOption.setVisibility(View.VISIBLE);
            empSalaryMonthlyTaxeditTextHeading.setText(getString(R.string.EmpMomthlyTax));
            empSalaryMonthlyEmployerPFHeding.setVisibility(View.VISIBLE);
            empSalaryMonthlyEmployerPFTextBox.setVisibility(View.VISIBLE);
            empSalaryMonthlyEmployerPFError.setVisibility(View.VISIBLE);
            empSalaryMonthlyEmployeePFHeding.setVisibility(View.VISIBLE);
            empSalaryMonthlyEmployeePFTextBox.setVisibility(View.VISIBLE);
            empSalaryMonthlyEmployeePFError.setVisibility(View.VISIBLE);
            empSalaryMonthlyAddDiduction1Heding.setVisibility(View.VISIBLE);
            empSalaryAddiDeduction1TextBox.setVisibility(View.VISIBLE);
            empSalaryMonthlyAddDiduction2Heding.setVisibility(View.VISIBLE);
            empSalaryAddiDeduction2TextBox.setVisibility(View.VISIBLE);
            errorAddDiduction.setVisibility(View.VISIBLE);
            addDeductionButton.setVisibility(View.VISIBLE);

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

            empSalaryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.empSalaryAmtRadioButton) {
                    empSalaryBonusTextBox.setText("");
                    empSalaryBonusTextBox.setHint(getString(R.string.Rs));
                } else if (checkedId == R.id.empSalaryPercentageRadioButton) {
                    empSalaryBonusTextBox.setText("");
                    empSalaryBonusTextBox.setHint(getString(R.string.percentageSymbol));
                }
            });

            addDeductionButton.setOnClickListener(v -> {
                if (deductionFieldCount < MAX_MPONTHLY_DEDUCTION_FIELDS) {
                    addNewMonthlyDeductionField();
                } else {
                    errorAddDiduction.setText(getString(R.string.EmpMonthlyAddDiductionError));
                    Toast.makeText(getContext(), getString(R.string.EmpMonthlyAddDiductionError), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (operation.equals(getString(R.string.empSalaryIncrement))) {
            empIncrementRadioGroup.setVisibility(View.VISIBLE);
            bonusIncludeOption.setVisibility(View.GONE);
            empSalaryMonthlyTaxeditTextHeading.setText(getString(R.string.empIncrementPercent));
            empSalaryMonthlyEmployerPFHeding.setVisibility(View.GONE);
            empSalaryMonthlyEmployerPFTextBox.setVisibility(View.GONE);
            empSalaryMonthlyEmployerPFError.setVisibility(View.GONE);
            empSalaryMonthlyEmployeePFHeding.setVisibility(View.GONE);
            empSalaryMonthlyEmployeePFTextBox.setVisibility(View.GONE);
            empSalaryMonthlyEmployeePFError.setVisibility(View.GONE);
            empSalaryMonthlyAddDiduction1Heding.setVisibility(View.GONE);
            empSalaryAddiDeduction1TextBox.setVisibility(View.GONE);
            empSalaryMonthlyAddDiduction2Heding.setVisibility(View.GONE);
            empSalaryAddiDeduction2TextBox.setVisibility(View.GONE);
            errorAddDiduction.setVisibility(View.GONE);
            addDeductionButton.setVisibility(View.GONE);

            empIncrementRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.empIncrementAmtRadioButton) {
                    empSalaryMonthlyTaxeditTextHeading.setText(getString(R.string.empIncrementPercent));
                    empSalaryMonthlyTaxTextBox.setHint("%");
                    empSalaryCTCTextBox.setText("");
                    empSalaryMonthlyTaxTextBox.setText("");
                    clearAllFields();
                } else if (checkedId == R.id.empIncrementPercentageRadioButton) {
                    empSalaryMonthlyTaxeditTextHeading.setText(getString(R.string.empIncrementAmount));
                    empSalaryMonthlyTaxTextBox.setHint("Rs.");
                    empSalaryCTCTextBox.setText("");
                    empSalaryMonthlyTaxTextBox.setText("");
                    clearAllFields();
                }
            });
        }

        return view;
    }


    private void calculateSalaryAndIncrement() {

        // Call the validation method
        if (!validateInputs(operation)) {
            return; // Exit if validation fails
        }

        // Format the results
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");
        mainViewModel.hideKeyboard(requireContext());

        float totalMonthlyDeduction = 0;
        float totalAnnualDeductions = 0;
        float takeHomeMonthlySalary = 0;
        float takeHomeAnnualSalary = 0;
        float maturityAmt = 0;
        float number1Result = 0;

        try {
            if (operation.equals(getString(R.string.empSalary))) {

                // Retrieve and parse input values with default values for empty fields
                float empSalaryCTC = parseFloatOrDefault(empSalaryCTCTextBox.getText().toString(), 0);
                float empSalaryMonthlyTax = parseFloatOrDefault(empSalaryMonthlyTaxTextBox.getText().toString(), 0);
                float empSalaryMonthlyEmployerPF = parseFloatOrDefault(empSalaryMonthlyEmployerPFTextBox.getText().toString(), 0);
                float empSalaryMonthlyEmployeePF = parseFloatOrDefault(empSalaryMonthlyEmployeePFTextBox.getText().toString(), 0);
                float empSalaryAddiDeduction1 = parseFloatOrDefault(empSalaryAddiDeduction1TextBox.getText().toString(), 0);
                float empSalaryAddiDeduction2 = parseFloatOrDefault(empSalaryAddiDeduction2TextBox.getText().toString(), 0);

                // Initialize total monthly deductions
                totalMonthlyDeduction = empSalaryMonthlyTax + empSalaryMonthlyEmployerPF + empSalaryMonthlyEmployeePF + empSalaryAddiDeduction1 + empSalaryAddiDeduction2;

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
                totalAnnualDeductions = totalMonthlyDeduction * 12;

                // Calculate Take-Home Monthly and Annual Salary
                float monthlyCTC = empSalaryCTC / 12;
                takeHomeMonthlySalary = monthlyCTC - totalMonthlyDeduction;
                takeHomeAnnualSalary = takeHomeMonthlySalary * 12;

                maturityAmt = 0;
                number1Result = 0;

            } else if (operation.equals(getString(R.string.empSalaryIncrement))) {

                String input1 = empSalaryCTCTextBox.getText().toString();
                String input2 = empSalaryMonthlyTaxTextBox.getText().toString();

                float currentCtc = Float.parseFloat(input1);
                float incrementPercentage = Float.parseFloat(input2);

                // Check selected radio button
                checkedId = empIncrementRadioGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.empIncrementAmtRadioButton) {
                    mainViewModel.setOperation(getString(R.string.empIncrementAmount));
                    // Increment amount calculation
                    float annualIncrementAmount = (currentCtc * incrementPercentage) / 100;
                    float monthylIncrementAmount = annualIncrementAmount / 12;
                    float newCtc = currentCtc + annualIncrementAmount;

                    //IncrementSummeryCurrentCTCHeading.setVisibility(View.VISIBLE);
                    //SalarySummeryTotalMonthlyDeductionResult.setVisibility(View.VISIBLE);
                    SalarySummeryTotalAnnualDeductionsHeading.setVisibility(View.VISIBLE);
                    SalarySummeryTotalAnnualDeductionsResult.setVisibility(View.VISIBLE);
                    SalarySummeryTakeHomeMonthlySalaryHeading.setVisibility(View.VISIBLE);
                    SalarySummeryTakeHomeMonthlySalaryResult.setVisibility(View.VISIBLE);

                    totalMonthlyDeduction = currentCtc;
                    totalAnnualDeductions = monthylIncrementAmount;
                    takeHomeMonthlySalary = annualIncrementAmount;
                    takeHomeAnnualSalary = newCtc;

                } else if (checkedId == R.id.empIncrementPercentageRadioButton) {

                    mainViewModel.setOperation(getString(R.string.empIncrementPercent));
                    SalarySummeryTakeHomeAnnualSalary.setText(getString(R.string.empIncrementPercent));
                    //IncrementSummeryCurrentCTCHeading.setVisibility(View.GONE);
                    //SalarySummeryTotalMonthlyDeductionResult.setVisibility(View.GONE);
                    SalarySummeryTotalAnnualDeductionsHeading.setVisibility(View.GONE);
                    SalarySummeryTotalAnnualDeductionsResult.setVisibility(View.GONE);
                    SalarySummeryTakeHomeMonthlySalaryHeading.setVisibility(View.GONE);
                    SalarySummeryTakeHomeMonthlySalaryResult.setVisibility(View.GONE);

                    // Percentage calculation
                    float percentage = (incrementPercentage / currentCtc) * 100;

                    SalarySummeryTotalMonthlyDeductionHeading.setText(getString(R.string.empIncrementFeedbackResult));
                    if (percentage >= 15) {
                        SalarySummeryTotalMonthlyDeductionResult.setText(getString(R.string.empIncrementFeedbackOption1));
                    } else if (percentage >= 10) {
                        SalarySummeryTotalMonthlyDeductionResult.setText(getString(R.string.empIncrementFeedbackOption2));
                    } else {
                        SalarySummeryTotalMonthlyDeductionResult.setText(getString(R.string.empIncrementFeedbackOption3));
                    }


                    totalMonthlyDeduction = 100 - percentage;
                    takeHomeAnnualSalary = percentage;

                    // Update results
                    SalarySummeryTakeHomeAnnualSalaryResult.setText(decimalFormat.format(percentage) + "%");

                } else {
                    Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }

            // Format and display the results
            if (!(checkedId == R.id.empIncrementPercentageRadioButton)) {
                SalarySummeryTotalMonthlyDeductionResult.setText("Rs. " + decimalFormat.format(totalMonthlyDeduction));
                SalarySummeryTakeHomeAnnualSalaryResult.setText("Rs. " + decimalFormat.format(takeHomeAnnualSalary));
            }
            SalarySummeryTotalAnnualDeductionsResult.setText("Rs. " + decimalFormat.format(totalAnnualDeductions));
            SalarySummeryTakeHomeMonthlySalaryResult.setText("Rs. " + decimalFormat.format(takeHomeMonthlySalary));

            // Update the ViewModel with calculated values
            mainViewModel.setUserInputs(maturityAmt, number1Result, totalMonthlyDeduction, totalAnnualDeductions, takeHomeMonthlySalary, takeHomeAnnualSalary);

            // Show result and chart box
            mainViewModel.setResultBoxVisibility(true);
            mainViewModel.setChartBoxVisibility(true);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter numeric values.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String operation) {

        if(operation.equals(getString(R.string.empSalary))){
            // Validate the CTC input field
            String input1Str = empSalaryCTCTextBox.getText().toString();

            try {
                // Validate Deposit Amount
                if (input1Str.isEmpty()) {
                    CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_alert));
                    empSalaryCTCTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }

                float input1 = Float.parseFloat(input1Str);
                if (input1 <= 0) {
                    CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_zero_alert));
                    empSalaryCTCTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_invalid_alert));
                empSalaryCTCTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
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
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }

                    if (empSalaryRadioGroup.getCheckedRadioButtonId() == R.id.empSalaryAmtRadioButton) {
                        float input2 = Float.parseFloat(bonusInput);
                        if (input2 <= 0) {
                            CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.BonusAmt_zero_alert));
                            empSalaryBonusTextBox.requestFocus();
                            mainViewModel.setChartBoxVisibility(false);
                            mainViewModel.setResultBoxVisibility(false);
                            mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                            return false;
                        }
                    }

                    if (empSalaryRadioGroup.getCheckedRadioButtonId() == R.id.empSalaryPercentageRadioButton) {
                        float input2 = Float.parseFloat(bonusInput);
                        if (input2 > 100) {
                            CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.Emp_Bonus_Percentage_alert));
                            empSalaryBonusTextBox.requestFocus();
                            mainViewModel.setChartBoxVisibility(false);
                            mainViewModel.setResultBoxVisibility(false);
                            mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                            return false;
                        }
                        if (input2 <= 0) {
                            CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.BonusPer_zero_alert));
                            empSalaryBonusTextBox.requestFocus();
                            mainViewModel.setChartBoxVisibility(false);
                            mainViewModel.setResultBoxVisibility(false);
                            mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                            return false;
                        }
                    }
                } catch (NumberFormatException e) {
                    CommonMethod.validateInputs(empSalaryBonusTextBox, empSalaryBonusError, getString(R.string.bonus_invalid_alert));
                    empSalaryBonusTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);                    return false;
                }
            }


            // Validate empSalaryMonthlyTaxTextBox
            if (empSalaryMonthlyTaxTextBox.getText().toString().isEmpty()) {
                CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.monthly_tax_req_alert));
                empSalaryMonthlyTaxTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);                return false;
            }

            try {
                Float.parseFloat(empSalaryMonthlyTaxTextBox.getText().toString());
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.monthly_tax_invalid_alert));
                empSalaryMonthlyTaxTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);                return false;
            }

            // Validate empSalaryMonthlyEmployerPFTextBox
            if (empSalaryMonthlyEmployerPFTextBox.getText().toString().isEmpty()) {
                CommonMethod.validateInputs(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError, getString(R.string.employer_pf_req_alert));
                empSalaryMonthlyEmployerPFTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);                return false;
            }

            try {
                Float.parseFloat(empSalaryMonthlyEmployerPFTextBox.getText().toString());
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryMonthlyEmployerPFTextBox, empSalaryMonthlyEmployerPFError, getString(R.string.employer_pf_invalid_alert));
                empSalaryMonthlyEmployerPFTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                return false;
            }

            // Validate empSalaryMonthlyEmployeePFTextBox
            if (empSalaryMonthlyEmployeePFTextBox.getText().toString().isEmpty()) {
                CommonMethod.validateInputs(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError, getString(R.string.EmpMonthlyEmployeePF_req_alert));
                empSalaryMonthlyEmployeePFTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                return false;
            }

            try {
                Float.parseFloat(empSalaryMonthlyEmployeePFTextBox.getText().toString());
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryMonthlyEmployeePFTextBox, empSalaryMonthlyEmployeePFError, getString(R.string.EmpMonthlyEmployeePF_invalid_alert));
                empSalaryMonthlyEmployeePFTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                return false;
            }

        } else if (operation.equals(getString(R.string.empSalaryIncrement))) {
            checkedId = empIncrementRadioGroup.getCheckedRadioButtonId();
            String input1Str = empSalaryCTCTextBox.getText().toString();
            String input2Str = empSalaryMonthlyTaxTextBox.getText().toString();

            try {
                // Validate Deposit Amount
                if (input1Str.isEmpty()) {
                    CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_alert));
                    empSalaryCTCTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }

                float input1 = Float.parseFloat(input1Str);
                if (input1 <= 0) {
                    CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_zero_alert));
                    empSalaryCTCTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(empSalaryCTCTextBox, empSalaryCTCError, getString(R.string.EmpCTC_invalid_alert));
                empSalaryCTCTextBox.requestFocus();
                mainViewModel.setChartBoxVisibility(false);
                mainViewModel.setResultBoxVisibility(false);
                mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                return false;
            }

            if (checkedId == R.id.empIncrementAmtRadioButton) {
                try {
                    // Validate Deposit Amount
                    if (input2Str.isEmpty()) {
                        CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementPercentageEmpty));
                        empSalaryMonthlyTaxTextBox.requestFocus();
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }

                    float input2 = Float.parseFloat(input2Str);
                    if (input2 > 100) {
                        CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementPercentageExceed));
                        empSalaryMonthlyTaxTextBox.requestFocus();
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }
                    if (input2 <= 0) {
                        CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementPercentageZero));
                        empSalaryMonthlyTaxTextBox.requestFocus();
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementPercentageInvalid));
                    empSalaryMonthlyTaxTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }
            } else if (checkedId == R.id.empIncrementPercentageRadioButton) {
                try {
                    // Validate Deposit Amount
                    if (input2Str.isEmpty()) {
                        CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementAmtEmpty));
                        empSalaryMonthlyTaxTextBox.requestFocus();
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }

                    float input2 = Float.parseFloat(input2Str);
                    if (input2 <= 0) {
                        CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementAmtZero));
                        empSalaryMonthlyTaxTextBox.requestFocus();
                        mainViewModel.setChartBoxVisibility(false);
                        mainViewModel.setResultBoxVisibility(false);
                        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    CommonMethod.validateInputs(empSalaryMonthlyTaxTextBox, empSalaryMonthlyTaxError, getString(R.string.empIncrementAmtInvalid));
                    empSalaryMonthlyTaxTextBox.requestFocus();
                    mainViewModel.setChartBoxVisibility(false);
                    mainViewModel.setResultBoxVisibility(false);
                    mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);
                    return false;
                }
            }

        }

        return true;
    }

    private void clearAllFields() {
        // Clear input fields
        empSalaryCTCTextBox.setText("");
        empSalaryBonusTextBox.setText("");
        empSalaryMonthlyTaxTextBox.setText("");
        empSalaryMonthlyEmployerPFTextBox.setText("");
        empSalaryMonthlyEmployeePFTextBox.setText("");
        empSalaryAddiDeduction1TextBox.setText("");
        empSalaryAddiDeduction2TextBox.setText("");
        resultBox.setVisibility(View.GONE);
        errorAddDiduction.setText("");
        empSalaryBonusCheckBox.setChecked(false);

        empSalaryCTCTextBox.setError(null);
        empSalaryBonusTextBox.setError(null);
        empSalaryMonthlyTaxTextBox.setError(null);
        empSalaryMonthlyEmployerPFTextBox.setError(null);
        empSalaryMonthlyEmployeePFTextBox.setError(null);

        // Clear results
        SalarySummeryTotalMonthlyDeductionResult.setText("");
        SalarySummeryTotalAnnualDeductionsResult.setText("");
        SalarySummeryTakeHomeMonthlySalaryResult.setText("");
        SalarySummeryTakeHomeAnnualSalaryResult.setText("");

        empSalaryRadioGroup.check(R.id.empSalaryAmtRadioButton);

        // Clear dynamically added fields in deductionsContainer
        deductionsContainer.removeAllViews();

        // Reset deduction field count
        deductionFieldCount = 2;

        mainViewModel.setChartBoxVisibility(false);
        mainViewModel.setResultBoxVisibility(false);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton); // Reset Growth radio button

        // Display a message indicating the fields have been cleared
        //Toast.makeText(getContext(), "All fields have been cleared", Toast.LENGTH_SHORT).show();
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