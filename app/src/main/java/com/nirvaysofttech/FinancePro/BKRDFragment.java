package com.nirvaysofttech.FinancePro;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.text.DecimalFormat;

public class BKRDFragment extends Fragment {

    private EditText editTextNumber1, editTextNumber2, editTextNumber3, editTextRepaymenetEmi;
    private Spinner spinner, interestRecievedDropdown, simpleLoanCalculateDropdown;
    private TextView textViewHeading, editText1Heading, errorTextEditTextNumber1, editText2Heading, errorTextEditTextNumber2, textViewTerm, errorTextEditTextNumber3, errorTextEditTextNumber4, totalDepositHeading,
            annualInterestError, totalDepositResult, yearlyInterestHeading, yearlyInterestResult, totalInterestHeading,
            totalInterestResult, maturityAmountHeading, maturityAmountResult, editTextRepaymenetEmiHeading;
    private MainViewModel mainViewModel;
    private LinearLayout resultBox, SaveAndDocsBox, simpleLoanDropdownLayout;
    private Button findInterestRate, buttonCalculate, button_saveCalculation;
    String selectedOption = ""; // Declare as String
    // Check visibility of userEnterEMI field before parsing
    float userEnterEMI = 0.0f;
    double annualInterestRatee;
    // Format the result as "X Years & Y Months (Z Months)"
    String loanTermResult;
    DatabaseHelper dbHelper;
    // Get the selected term unit
    String selectedTermUnit = ""; // Declare as String
    private boolean isRecalculating = false;

    public static BKRDFragment instance;
    private String currentSaveRecordName = null;

    // Other class-level fields
    private float totalInterest = 0;
    private float totalDeposit = 0;
    private float maturityAmount = 0;
    private float yearlyInterest = 0;

    public BKRDFragment() {
        instance = this;
    }

    public static BKRDFragment getInstance() {
        return instance;
    }

    public void loadValuesFromRecalculate(String selectedOption, String save_record_nam, String loanAmt, String interestRate, String loanTerm, String termUnit, String emi_amt) {
        isRecalculating = true; // 🔐 prevent reset temporarily
        // Set spinner selection based on termUnit
        ArrayAdapter<String> adapterSelectedOption = (ArrayAdapter<String>) simpleLoanCalculateDropdown.getAdapter();
        if (adapterSelectedOption != null) {
            int position = adapterSelectedOption.getPosition(selectedOption);
            if (position >= 0) {
                simpleLoanCalculateDropdown.setSelection(position);
            }
        }

//        editTextNumber1.setText(String.valueOf(loanAmt));
//        editTextNumber2.setText(String.valueOf(interestRate));
//        editTextNumber3.setText(String.valueOf(loanTerm));
//        editTextRepaymenetEmi.setText(String.valueOf(emi_amt));

        if (selectedOption.equalsIgnoreCase("Monthly Repayment (EMI)")) {
            editTextNumber1.setText(String.valueOf(loanAmt));
            editTextNumber2.setText(String.valueOf(interestRate));
            editTextNumber3.setText(String.valueOf(loanTerm));
        } else if (selectedOption.equalsIgnoreCase("Loan Amount")) {
            editTextNumber1.setText(String.valueOf(emi_amt));
            editTextNumber2.setText(String.valueOf(interestRate));
            editTextNumber3.setText(String.valueOf(loanTerm));
        } else if (selectedOption.equalsIgnoreCase("Annual Interest Rate (%)")) {
            editTextRepaymenetEmi.setVisibility(View.VISIBLE);
            editTextNumber1.setText(String.valueOf(loanAmt));
            editTextNumber3.setText(String.valueOf(loanTerm));
            editTextRepaymenetEmi.setText(String.valueOf(emi_amt));
        } else if (selectedOption.equalsIgnoreCase("Loan Term")) {
            editTextRepaymenetEmi.setVisibility(View.VISIBLE);
            editTextNumber1.setText(String.valueOf(loanAmt));
            editTextNumber2.setText(String.valueOf(interestRate));
            editTextRepaymenetEmi.setText(String.valueOf(emi_amt));
        }

        // Set spinner selection based on termUnit
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(termUnit);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }

        // Optionally trigger auto-calculation
        buttonCalculate.performClick();
        currentSaveRecordName = save_record_nam; // ✅ Store active record name

        // 🕓 Delay turning off the flag to allow dropdown to finish processing
        new Handler(Looper.getMainLooper()).postDelayed(() -> isRecalculating = false, 200);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bkrd, container, false);

        // Initialize views
        editTextNumber1 = view.findViewById(R.id.editTextNumber1);
        errorTextEditTextNumber1 = view.findViewById(R.id.errorTextEditTextNumber1);
        editTextNumber2 = view.findViewById(R.id.editTextNumber2);
        errorTextEditTextNumber2 = view.findViewById(R.id.errorTextEditTextNumber2);
        editTextNumber3 = view.findViewById(R.id.editTextNumber3);
        spinner = view.findViewById(R.id.spinner_row_count);
        TextView interestRecievedText = view.findViewById(R.id.interestRecievedText); // Add this
        interestRecievedDropdown = view.findViewById(R.id.interestRecievedDropdown);
        simpleLoanCalculateDropdown = view.findViewById(R.id.simpleLoanCalculateDropdown);
        textViewHeading = view.findViewById(R.id.textViewHeading);
        editText1Heading = view.findViewById(R.id.editText1Heading);
        editText2Heading = view.findViewById(R.id.editText2Heading);
        findInterestRate = view.findViewById(R.id.findInterestRate);
        textViewTerm = view.findViewById(R.id.textViewTerm);
        errorTextEditTextNumber3 = view.findViewById(R.id.errorTextEditTextNumber3);
        editTextRepaymenetEmiHeading = view.findViewById(R.id.editTextRepaymenetEmiHeading);
        editTextRepaymenetEmi = view.findViewById(R.id.editTextRepaymenetEmi);
        errorTextEditTextNumber4 = view.findViewById(R.id.errorTextEditTextNumber4);
        annualInterestError = view.findViewById(R.id.annualInterestError);
        totalDepositHeading = view.findViewById(R.id.totalDepositHeading);
        totalDepositResult = view.findViewById(R.id.totalDepositResult);
        yearlyInterestHeading = view.findViewById(R.id.yearlyInterestHeading);
        yearlyInterestResult = view.findViewById(R.id.yearlyInterestResult);
        totalInterestHeading = view.findViewById(R.id.totalInterestHeading);
        totalInterestResult = view.findViewById(R.id.totalInterestResult);
        maturityAmountHeading = view.findViewById(R.id.maturityAmountHeading);
        maturityAmountResult = view.findViewById(R.id.maturityAmountResult);
        simpleLoanDropdownLayout = view.findViewById(R.id.simpleLoanDropdownLayout);
        resultBox = view.findViewById(R.id.resultBox);
        SaveAndDocsBox = view.findViewById(R.id.SaveAndDocsBox);
        // Set OnClickListener for the Calculate button
        buttonCalculate = view.findViewById(R.id.button_calculate);
        button_saveCalculation = view.findViewById(R.id.button_saveCalculation);
        dbHelper = new DatabaseHelper(getActivity());


//        AdHelper.loadBannerAd(this, view); // Load banner ad for fragment

        // Initialize ViewModel
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe the operation and update UI accordingly
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> {
            textViewHeading.setText(operation);
            editText1Heading.setText("Loan Amount");
            if (operation != null) {
                switch (operation) {
                    case "Basic Loan":
                        simpleLoanDropdownLayout.setVisibility(View.VISIBLE);
                        simpleLoanCalculateDropdown.setVisibility(View.VISIBLE);
                        setupSpinner(spinner, R.array.term_options); // Load FD options
                        setupSpinner(simpleLoanCalculateDropdown, R.array.simpleLoanDropdownOption);

                        // Add OnItemSelectedListener to simpleLoanCalculateDropdown
                        simpleLoanCalculateDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Get the selected item from the dropdown
                                String selectedOption = parent.getItemAtPosition(position).toString();

                                // Update the heading text
                                textViewHeading.setText(selectedOption);

                                switch (selectedOption) {
                                    case "Monthly Repayment (EMI)":
                                        if (!isRecalculating) resetFields(); // ✅ Only skip this part at the time of recalculation
                                        editText1Heading.setText("Loan Amount");
                                        textViewTerm.setText("Loan Period");
                                        totalDepositHeading.setText("Principal Paid (P)");
                                        yearlyInterestHeading.setText("Monthly Repayment (EMI)");
                                        totalInterestHeading.setText("Interest Paid (I)");
                                        maturityAmountHeading.setText("Total Repayments Paid (P + I)");
                                        yearlyInterestHeading.setVisibility(View.VISIBLE);
                                        yearlyInterestResult.setVisibility(View.VISIBLE);
                                        editText2Heading.setVisibility(View.VISIBLE);
                                        editTextNumber2.setVisibility(View.VISIBLE);
                                        textViewTerm.setVisibility(View.VISIBLE);
                                        editTextNumber3.setVisibility(View.VISIBLE);
                                        spinner.setVisibility(View.VISIBLE);
                                        findInterestRate.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmiHeading.setVisibility(View.GONE);
                                        editTextRepaymenetEmi.setVisibility(View.GONE);
                                        break;

                                    case "Loan Amount":
                                        if (!isRecalculating) resetFields(); // ✅ Only skip this part
                                        editText1Heading.setText("Monthly Repayment (EMI)");
                                        yearlyInterestHeading.setVisibility(View.GONE);
                                        yearlyInterestResult.setVisibility(View.GONE);
                                        totalDepositHeading.setText("Loan Amount (P)");
                                        totalInterestHeading.setText("Interest Paid (I)");
                                        maturityAmountHeading.setText("Total Repayments Paid (P + I)");
                                        editText2Heading.setVisibility(View.VISIBLE);
                                        editTextNumber2.setVisibility(View.VISIBLE);
                                        textViewTerm.setVisibility(View.VISIBLE);
                                        editTextNumber3.setVisibility(View.VISIBLE);
                                        spinner.setVisibility(View.VISIBLE);
                                        findInterestRate.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmiHeading.setVisibility(View.GONE);
                                        editTextRepaymenetEmi.setVisibility(View.GONE);
                                        break;

                                    case "Annual Interest Rate (%)":
                                        if (!isRecalculating) resetFields(); // ✅ Only skip this part
                                        editText1Heading.setText("Loan Amount");
                                        totalDepositHeading.setText("Principal Paid (P)");
                                        yearlyInterestHeading.setText("Annual Interest Rate (%)");
                                        editText2Heading.setVisibility(View.GONE);
                                        editTextNumber2.setVisibility(View.GONE);
                                        findInterestRate.setVisibility(View.GONE);
                                        errorTextEditTextNumber3.setVisibility(View.GONE);
                                        textViewTerm.setVisibility(View.VISIBLE);
                                        editTextNumber3.setVisibility(View.VISIBLE);
                                        spinner.setVisibility(View.VISIBLE);
                                        yearlyInterestHeading.setVisibility(View.VISIBLE);
                                        yearlyInterestResult.setVisibility(View.VISIBLE);
                                        annualInterestError.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmiHeading.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmi.setVisibility(View.VISIBLE);
                                        errorTextEditTextNumber4.setVisibility(View.VISIBLE);
                                        break;
                                    case "Loan Term":
                                        if (!isRecalculating) resetFields(); // ✅ Only skip this part
                                        totalDepositHeading.setText("Principal Paid (P)");
                                        yearlyInterestHeading.setText("Loan Term");
                                        editText2Heading.setVisibility(View.VISIBLE);
                                        editTextNumber2.setVisibility(View.VISIBLE);
                                        findInterestRate.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmiHeading.setVisibility(View.VISIBLE);
                                        editTextRepaymenetEmi.setVisibility(View.VISIBLE);
                                        textViewTerm.setVisibility(View.GONE);
                                        editTextNumber3.setVisibility(View.GONE);
                                        spinner.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Do nothing
                            }
                        });
                        break;
                    case "Bank Recurring Deposit (RD)":
                        editText1Heading.setText("Monthly Deposit Amount");
                        setupSpinner(spinner, R.array.term_options); // Load RD options
                        interestRecievedDropdown.setVisibility(View.GONE);
                        interestRecievedText.setVisibility(View.GONE);
                        break;
                    case "Fixed Deposit - STDR (Cumulative)":
                        editText1Heading.setText("Lump Sum Deposit Amount");
                        setupSpinner(spinner, R.array.term_options_for_fd); // Load FD options
                        interestRecievedDropdown.setVisibility(View.GONE);
                        interestRecievedText.setVisibility(View.GONE);
                        break;
                    case "Fixed Deposit - TDR (Interest Payout)":
                        editText1Heading.setText("Lump Sum Deposit Amount");
                        yearlyInterestHeading.setVisibility(View.VISIBLE);
                        yearlyInterestResult.setVisibility(View.VISIBLE);
                        setupSpinner(spinner, R.array.term_options_for_fd);
                        setupSpinner(interestRecievedDropdown, R.array.interestRecievedDropdownOption);
                        interestRecievedDropdown.setVisibility(View.VISIBLE);
                        interestRecievedText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        CommonMethod.addClearErrorTextWatcher(editTextNumber1, errorTextEditTextNumber1);
        CommonMethod.addClearErrorTextWatcher(editTextNumber2, errorTextEditTextNumber2);
        CommonMethod.addClearErrorTextWatcher(editTextNumber3, errorTextEditTextNumber3);
        CommonMethod.addClearErrorTextWatcher(editTextRepaymenetEmi, errorTextEditTextNumber4);

        buttonCalculate.setOnClickListener(v -> calculateRD());
        button_saveCalculation.setOnClickListener(v -> saveLoanCalculation());

        // Observe visibility for resultBox
        mainViewModel.getResultBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            resultBox.setVisibility(visible ? View.VISIBLE : View.GONE);
            SaveAndDocsBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        // Set OnClickListener for the Reset button
        Button buttonReset = view.findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(v -> resetFields());

        // Set OnClickListener for findInterestRate
        TextView findInterestRate = view.findViewById(R.id.findInterestRate);
        findInterestRate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TabActivity.class);
            intent.putExtra("operation", "All Bank Interest Rate (%)");
            startActivity(intent);
        });

        return view;
    }

    private void setupSpinner(Spinner targetSpinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                arrayResource,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSpinner.setAdapter(adapter);
    }

    private void calculateRD() {
        String operation = mainViewModel.getOperation().getValue();

        if(!isRecalculating){
            // Call the validation method
            if (!validateInputsNum(operation)) {
                return; // Exit if validation fails
            }
        }

        // Format the results
        DecimalFormat df = new DecimalFormat("##,##,##0.00");
        mainViewModel.hideKeyboard(requireContext());

        try {
            // Get user inputs
            float principal = Float.parseFloat(editTextNumber1.getText().toString());

            int term = 0;
            if (editTextNumber3.getVisibility() == View.VISIBLE) {
                term = editTextNumber3.getText().toString().isEmpty() ? 0 : Integer.parseInt(editTextNumber3.getText().toString());
            }

            float annualInterestRate = 0.0f;
            if (editTextNumber2.getVisibility() == View.VISIBLE) {
                    annualInterestRate = editTextNumber2.getText().toString().isEmpty() ? 0.0f : Float.parseFloat(editTextNumber2.getText().toString());
            }


            if (editTextRepaymenetEmi.getVisibility() == View.VISIBLE) {
                userEnterEMI = editTextRepaymenetEmi.getText().toString().isEmpty() ? 0.0f : Float.parseFloat(editTextRepaymenetEmi.getText().toString());
            }

            if (spinner.getVisibility() == View.VISIBLE) {
                selectedTermUnit = spinner.getSelectedItem().toString(); // Directly get the selected item as a String
            }

            if (simpleLoanCalculateDropdown.getVisibility() == View.VISIBLE) {
                selectedOption = simpleLoanCalculateDropdown.getSelectedItem().toString();
            }

            // Convert term to months based on the selected unit
            float termInMonths;
            if (selectedTermUnit.equals("Years")) {
                termInMonths = term * 12; // Convert years to months
            } else if (selectedTermUnit.equals("Months")) {
                termInMonths = term; // Term is already in months
            } else if (selectedTermUnit.equals("Days")) {
                termInMonths = term / 30f; // Approximate days to months
            } else {
                termInMonths = term; // Default to months
            }

            // Reset values
            totalInterest = 0;
            totalDeposit = 0;
            maturityAmount = 0;
            yearlyInterest = 0;
            String  loanTermResultText= "";

            if (operation != null) {
                switch (operation) {
                    case "Basic Loan":

                        // Calculate monthly interest rate
                        float monthlyInterestRateSimpleLoan = (annualInterestRate / 100f) / 12;

                        switch (selectedOption) {
                            case "Monthly Repayment (EMI)":
                                float emi;

                                if (monthlyInterestRateSimpleLoan == 0f) {
                                    // If interest rate is 0, EMI = principal / term
                                    emi = principal / termInMonths;
                                    totalInterest = 0;
                                } else {
                                    emi = (float) (principal * (monthlyInterestRateSimpleLoan * Math.pow(1 + monthlyInterestRateSimpleLoan, termInMonths)) /
                                            (Math.pow(1 + monthlyInterestRateSimpleLoan, termInMonths) - 1));
                                    totalInterest = (emi * termInMonths) - principal;
                                }

                                totalDeposit = principal;
                                yearlyInterest = emi;
                                maturityAmount = emi * termInMonths;

                                yearlyInterestResult.setText("Rs. " + df.format(yearlyInterest));
                                break;

                            case "Loan Amount":
                                yearlyInterest = principal;
                                float emiInput = principal; // principal is used as EMI input
                                float loanAmount;

                                if (monthlyInterestRateSimpleLoan == 0f) {
                                    // If interest rate is 0%, Loan = EMI * Term
                                    loanAmount = emiInput * termInMonths;
                                    totalInterest = 0;
                                } else {
                                    // Use EMI-based formula for loan amount
                                    loanAmount = (float) ((emiInput * (Math.pow(1 + monthlyInterestRateSimpleLoan, termInMonths) - 1)) /
                                            (monthlyInterestRateSimpleLoan * Math.pow(1 + monthlyInterestRateSimpleLoan, termInMonths)));
                                    totalInterest = (emiInput * termInMonths) - loanAmount;
                                }

                                totalDeposit = loanAmount;
                                maturityAmount = totalDeposit + totalInterest;

                                break;

                            case "Annual Interest Rate (%)":
                                // Calculate absolute minimum EMI (principal only, 0% interest)
                                float absoluteMinEMI = principal / termInMonths;

                                // Binary search setup with realistic bounds (0% to 2400% annual)
                                double low = 0.0;          // 0% monthly (0% annual)
                                double high = 200.0;       // 100% monthly (1200% annual)
                                double precision = 0.0001; // 0.01% precision
                                int maxIterations = 1000;
                                int iterations = 0;

                                // Binary search to find interest rate
                                while (high - low > precision && iterations < maxIterations) {
                                    double mid = (low + high) / 2;
                                    double r = mid / 100.0; // Convert percentage to decimal

                                    double pow = Math.pow(1 + r, termInMonths);

                                    // Handle overflow cases
                                    if (Double.isInfinite(pow) || Double.isNaN(pow)) {
                                        high = mid;
                                        iterations++;
                                        continue;
                                    }

                                    double calculatedEMI = principal * (r * pow) / (pow - 1);

                                    if (Double.isNaN(calculatedEMI) || Double.isInfinite(calculatedEMI)) {
                                        high = mid;
                                        iterations++;
                                        continue;
                                    }

                                    // Adjust search bounds
                                    if (calculatedEMI < userEnterEMI) {
                                        low = mid;
                                    } else {
                                        high = mid;
                                    }

                                    iterations++;
                                }

                                // Calculate annual interest rate
                                annualInterestRatee = ((low + high) / 2.0) * 12;

                                setResultHeadingsVisibility(View.GONE);
                                annualInterestError.setVisibility(View.VISIBLE);

                                // Validate and display results
                                if (userEnterEMI < absoluteMinEMI) {
                                    annualInterestError.setText("The current Monthly Repayment (EMI) amount is too low to repay the loan within the selected tenure.\n\n" +
                                            "Please increase your Monthly Repayment (EMI) to at least ₹" + df.format(absoluteMinEMI));
                                    break;
                                } else if (userEnterEMI > principal * 1) { // Reasonable upper bound (5x principal)
                                    annualInterestError.setText("The Monthly Repayment (EMI) amount entered is too high and unrealistic.\n\nMaximum suggested Monthly Repayment (EMI) is ₹" + df.format(principal * 1) +
                                            ". Ensure your Monthly Repayment (EMI) stays under this threshold.");
                                    break;
                                } else if (annualInterestRatee >= 1200.0) {
                                    annualInterestError.setText("Annual interest rate exceeds the maximum limit (1200%).\n" +
                                            "Please reduce your Monthly Repayment (EMI) amount");
                                } else {
                                    annualInterestError.setText("");
                                    annualInterestError.setVisibility(View.GONE);
                                    // Calculate financial summary
                                    totalDeposit = principal;
                                    totalInterest = (userEnterEMI * termInMonths) - principal;
                                    maturityAmount = userEnterEMI * termInMonths;
                                    yearlyInterestResult.setText(df.format(annualInterestRatee));
                                    setResultHeadingsVisibility(View.VISIBLE);
                                }

                                break;

                            case "Loan Term":
                                // Calculate monthly interest rate
                                float monthlyInterestRateLoanTerm = (annualInterestRate / 100) / 12;

                                float emiLoanTerm = userEnterEMI; // User-entered EMI

                                // Check if EMI is too low
                                absoluteMinEMI = (float) (principal * monthlyInterestRateLoanTerm);

                                // Calculate loan term in months using the formula
                                float numerator = (float) Math.log(emiLoanTerm / (emiLoanTerm - principal * monthlyInterestRateLoanTerm));
                                float denominator = (float) Math.log(1 + monthlyInterestRateLoanTerm);
                                float loanTermInMonths = numerator / denominator;

                                // Round loan term to the nearest whole number of months
                                int totalMonths = (int) Math.round(loanTermInMonths);

                                // Convert total months to years and months
                                int years = totalMonths / 12;
                                int months = totalMonths % 12;

                                if (years == 0) {
                                    loanTermResult = months + " Months (" + totalMonths + " Months)";
                                } else if (months == 0) {
                                    loanTermResult = years + " Years (" + totalMonths + " Months)";
                                } else {
                                    loanTermResult = years + " Years & " + months + " Months (" + totalMonths + " Months)";
                                }

                                setResultHeadingsVisibility(View.GONE);
                                annualInterestError.setVisibility(View.VISIBLE);

                                if (emiLoanTerm <= absoluteMinEMI) {
                                    // Set error message
                                    annualInterestError.setText("The entered Monthly Repayment (EMI) amount is too low to calculate a valid loan term for the given loan amount.\n\n" +
                                            "Please increase your Monthly Repayment (EMI) to at least ₹" + df.format(absoluteMinEMI) + " or reduce your Loan Amount.");
                                    break;
                                } else {
                                    annualInterestError.setText("");
                                    annualInterestError.setVisibility(View.GONE);
                                    totalDeposit = principal;
                                    yearlyInterestResult.setText(loanTermResult);
                                    totalInterest = (emiLoanTerm * totalMonths) - principal; // Interest Paid (I)
                                    maturityAmount = emiLoanTerm * totalMonths; // Total Repayments Paid (P + I)
                                    setResultHeadingsVisibility(View.VISIBLE);
                                }

                                break;
                        }
                        break;

                    case "Fixed Deposit - TDR (Interest Payout)":
                        // Get the selected interest payout frequency
                        String interestPayoutFrequency = interestRecievedDropdown.getSelectedItem().toString();
                        if (interestPayoutFrequency.equals("Monthly")) {
                            yearlyInterestHeading.setText("Monthly Interest");
                            // Calculate monthly interest
                            float monthlyInterestRateTDR = (annualInterestRate / 100) / 12;
                            float monthlyInterest = principal * monthlyInterestRateTDR; // Monthly interest payout
                            totalDeposit = principal; // Monthly interest payout
                            yearlyInterestResult.setText("Rs. " + df.format(monthlyInterest));
                            // Calculate total interest based on term in months
                            totalInterest = monthlyInterest * termInMonths;
                        } else if (interestPayoutFrequency.equals("Quarterly")) {
                            yearlyInterestHeading.setText("Quarterly Interest");
                            // Calculate quarterly interest
                            float quarterlyInterestRate = (annualInterestRate / 100) / 4;
                            float quarterlyInterest = principal * quarterlyInterestRate; // Quarterly interest payout

                            // Calculate total interest based on term in quarters
                            float termInQuarters = termInMonths / 3; // Convert months to quarters
                            totalInterest = quarterlyInterest * termInQuarters;
                            totalDeposit = principal; // Monthly interest payout

                            // Set totalDeposit based on term
                            if (termInMonths < 3) {
                                yearlyInterestResult.setText("Rs. " + df.format(quarterlyInterest * termInQuarters));
                            } else {
                                yearlyInterestResult.setText("Rs. " + df.format(quarterlyInterest));
                            }
                        }

                        // Calculate maturity amount
                        maturityAmount = principal + totalInterest;
                        break;

                    case "Fixed Deposit - STDR (Cumulative)":
                        // Calculate maturity amount for FD
                        int compoundingFrequency = 4; // Quarterly compounding
                        float rate = annualInterestRate / 100; // Convert percentage to decimal
                        maturityAmount = (float) (principal * Math.pow(1 + (rate / compoundingFrequency), compoundingFrequency * (termInMonths / 12)));
                        totalInterest = maturityAmount - principal;
                        totalDeposit = principal;
                        break;
                    case "Bank Recurring Deposit (RD)":
                        // Calculate total deposit
                        totalDeposit = principal * termInMonths; // term is in months

                        // Calculate maturity amount and total interest
                        float monthlyInterestRate = (annualInterestRate / 100) / 12;
                        maturityAmount = (float) (principal * ((Math.pow(1 + monthlyInterestRate, termInMonths) - 1) / monthlyInterestRate) * (1 + monthlyInterestRate));
                        totalInterest = maturityAmount - totalDeposit;
                        break;
                }
            }

            totalDepositResult.setText("Rs. " + df.format(totalDeposit));
            totalInterestResult.setText("Rs. " + df.format(totalInterest));
            maturityAmountResult.setText("Rs. " + df.format(maturityAmount));

            // Update results in ViewModel
            mainViewModel.setUserInputs(0, 0, principal, totalDeposit, totalInterest, maturityAmount);

            // Show result and chart box
            mainViewModel.setResultBoxVisibility(true);
            mainViewModel.setChartBoxVisibility(true);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter numeric values.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoanCalculation() {
        // Handle update if in recalculation mode
        if (currentSaveRecordName != null) {
            String loanAmt = editTextNumber1.getText().toString().trim();
            String interestRate = editTextNumber2.getText().toString().trim();
            String loanTerm = editTextNumber3.getText().toString().trim();
            String termUnit = spinner.getSelectedItem().toString();
            String emiAmt = String.format("%.2f", yearlyInterest);

            if (selectedOption.equalsIgnoreCase("Loan Amount")){
                loanAmt = String.format("%.2f", totalDeposit);
            } else if (selectedOption.equalsIgnoreCase("Annual Interest Rate (%)")){
                interestRate = String.format("%.2f", annualInterestRatee);
                emiAmt = String.format("%.2f", userEnterEMI);
            } else if (selectedOption.equalsIgnoreCase("Loan Term")){
                emiAmt = String.format("%.2f", userEnterEMI);
                loanTerm = String.format(loanTermResult);
            }
            

            if (!loanAmt.isEmpty() && !interestRate.isEmpty() && !loanTerm.isEmpty() && !termUnit.isEmpty() && !emiAmt.isEmpty()) {
                boolean updated = dbHelper.updateRecord(selectedOption, currentSaveRecordName, loanAmt, interestRate, loanTerm, termUnit, emiAmt);
                if (updated) {
                    Toast.makeText(getContext(), currentSaveRecordName + " was updated successfully", Toast.LENGTH_SHORT).show();
                    Bank_LoanSaveFragment loanSaveFragment = Bank_LoanSaveFragment.instance;
                    if (loanSaveFragment != null && loanSaveFragment.isAdded()) {
                        loanSaveFragment.loadAllLoanEntries();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to update " + currentSaveRecordName, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter all 3 inputs", Toast.LENGTH_SHORT).show();
            }

            return; // Skip dialog
        }

        // If not recalculation mode, show save dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_record_name_save, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        dialog.getWindow().setAttributes(params);

        EditText input = dialogView.findViewById(R.id.saveRecordNameEditText);
        Button okButton = dialogView.findViewById(R.id.button_ok);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);

        okButton.setOnClickListener(v -> {
            String name = input.getText().toString().trim().toUpperCase();

            if (name.isEmpty()) {
                input.setError("Please enter record name.");
                Toast.makeText(getContext(), "Please enter record name.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkNameExists(name)) {
                input.setError("This name already exists in records.");
                Toast.makeText(getContext(), "This name already exists in records.", Toast.LENGTH_SHORT).show();
            } else {
                String loanAmt = editTextNumber1.getText().toString().trim();
                String interestRate = editTextNumber2.getText().toString().trim();
                String loanTerm = editTextNumber3.getText().toString().trim();
                String termUnit = spinner.getSelectedItem().toString();
                String emiAmt = String.format("%.2f", yearlyInterest);

                if (selectedOption.equalsIgnoreCase("Loan Amount")){
                    loanAmt = String.format("%.2f", totalDeposit);
                } else if (selectedOption.equalsIgnoreCase("Annual Interest Rate (%)")){
                    interestRate = String.format("%.2f", annualInterestRatee);
                    emiAmt = String.format("%.2f", userEnterEMI);
                } else if (selectedOption.equalsIgnoreCase("Loan Term")){
                    emiAmt = String.format("%.2f", userEnterEMI);
                    loanTerm = String.format(loanTermResult);
                }

                if (!loanAmt.isEmpty() && !interestRate.isEmpty() && !loanTerm.isEmpty() && !termUnit.isEmpty() && !emiAmt.isEmpty()) {
                    boolean saved = dbHelper.insertValues(selectedOption, name, loanAmt, interestRate, loanTerm, termUnit, emiAmt);
                    if (saved) {
                        currentSaveRecordName = name;
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        // Refresh Tab 4 records
                        Bank_LoanSaveFragment loanSaveFragment = Bank_LoanSaveFragment.instance;
                        if (loanSaveFragment != null && loanSaveFragment.isAdded()) {
                            loanSaveFragment.loadAllLoanEntries();
                        }

                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter all 3 inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void setResultHeadingsVisibility(int visibility) {

        totalDepositHeading.setVisibility(visibility);
        yearlyInterestHeading.setVisibility(visibility);
        totalInterestHeading.setVisibility(visibility);
        maturityAmountHeading.setVisibility(visibility);

        totalDepositResult.setVisibility(visibility);
        yearlyInterestResult.setVisibility(visibility);
        totalInterestResult.setVisibility(visibility);
        maturityAmountResult.setVisibility(visibility);

    }

    private void resetFields() {
        mainViewModel.hideKeyboard(requireContext());
        currentSaveRecordName = null;
        // Clear all input fields and results
        editTextNumber1.setText("");
        editTextNumber2.setText("");
        editTextNumber3.setText("");
        spinner.setSelection(0); // Reset spinner to default position
        editTextRepaymenetEmi.setText("");

        totalDepositResult.setText("");
        yearlyInterestResult.setText("");
        totalInterestResult.setText("");
        maturityAmountResult.setText("");
        annualInterestError.setText("");


        editTextNumber1.setError(null);
        editTextNumber2.setError(null);
        editTextNumber3.setError(null);
        editTextRepaymenetEmi.setError(null);

        mainViewModel.setChartBoxVisibility(false);
        mainViewModel.setResultBoxVisibility(false);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton); // Reset Growth radio button
    }

    private boolean validateInputsNum(String operation) {
        String input1Str = editTextNumber1.getText().toString();
        String input2Str = editTextNumber2.getText().toString();
        String input3Str = editTextNumber3.getText().toString();
        String input4Str = editTextRepaymenetEmi.getText().toString();
        String termUnit = spinner.getSelectedItem().toString();

        String selectedOption = ""; // Declare as String
        if (simpleLoanCalculateDropdown.getVisibility() == View.VISIBLE) {
            selectedOption = simpleLoanCalculateDropdown.getSelectedItem().toString();
        }

        float userEnterEMI = 0.0f;
        if (editTextRepaymenetEmi.getVisibility() == View.VISIBLE) {
            userEnterEMI = editTextRepaymenetEmi.getText().toString().isEmpty() ? 0.0f : Float.parseFloat(editTextRepaymenetEmi.getText().toString());
        }

        if (operation != null) {
            switch (operation) {
                case "Basic Loan":

                    switch (selectedOption) {
                        case "Monthly Repayment (EMI)":
                            try {
                                // Validate Deposit Amount
                                if (input1Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a loan amount.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input1 = Float.parseFloat(input1Str);
                                if (input1 <= 0) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Loan amount cannot be zero.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid loan amount.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Interest Rate
                                if (input2Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input2 = Float.parseFloat(input2Str);
                                if (input2 > 100) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not exceed 100%.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter a valid interest rate.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Term Period
                                if (input3Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a loan period.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input3 = Float.parseFloat(input3Str);

                                // Define minimum & maximum limits based on term unit (Only Years & Months)
                                int minLimit = termUnit.equals("Years") ? 1 : 1;  // 1 year for Years, 6 months for Months
                                int maxLimit = termUnit.equals("Years") ? 40 : 480;  // 20 years for Years, 240 months for Months

                                // Minimum term validation
                                if (input3 < minLimit) {
                                    String minErrorMessage = termUnit.equals("Years") ? "Loan Period should not be zero." :
                                            "Loan Period must be at least 1 months.";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                // Maximum term validation
                                if (input3 > maxLimit) {
                                    String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 40 years." :
                                            "Term should not exceed 480 months (40 yr).";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter valid numbers.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }
                            break;

                        case "Loan Amount":
                            try {
                                // Validate Deposit Amount
                                if (input1Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a Monthly Repayment (EMI) amount.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input1 = Float.parseFloat(input1Str);
                                if (input1 <= 0) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Monthly Repayment (EMI) amount cannot be zero.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid Monthly Repayment (EMI) amount.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Interest Rate
                                if (input2Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input2 = Float.parseFloat(input2Str);
                                if (input2 > 100) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not exceed 100%.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter a valid interest rate.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Term Period
                                if (input3Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a loan period.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input3 = Float.parseFloat(input3Str);

                                // Define minimum & maximum limits based on term unit (Only Years & Months)
                                int minLimit = termUnit.equals("Years") ? 1 : 1;  // 1 year for Years, 6 months for Months
                                int maxLimit = termUnit.equals("Years") ? 40 : 480;  // 20 years for Years, 240 months for Months

                                // Minimum term validation
                                if (input3 < minLimit) {
                                    String minErrorMessage = termUnit.equals("Years") ? "Loan Period should not be zero." :
                                            "Loan Period must be at least 1 months.";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                // Maximum term validation
                                if (input3 > maxLimit) {
                                    String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 40 years." :
                                            "Term should not exceed 480 months (40 yr).";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter valid numbers.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }
                            break;

                        case "Annual Interest Rate (%)":
                            try {
                                // Validate Deposit Amount
                                if (input1Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a loan amount.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input1 = Float.parseFloat(input1Str);
                                if (input1 <= 0) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Loan amount cannot be zero.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid loan amount.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Term Period
                                if (input3Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a loan period.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input3 = Float.parseFloat(input3Str);

                                // Define minimum & maximum limits based on term unit (Only Years & Months)
                                int minLimit = termUnit.equals("Years") ? 1 : 1;  // 1 year for Years, 6 months for Months
                                int maxLimit = termUnit.equals("Years") ? 40 : 480;  // 20 years for Years, 240 months for Months

                                // Minimum term validation
                                if (input3 < minLimit) {
                                    String minErrorMessage = termUnit.equals("Years") ? "Loan Period should not be zero." :
                                            "Loan Period must be at least 1 months.";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                // Maximum term validation
                                if (input3 > maxLimit) {
                                    String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 40 years." :
                                            "Term should not exceed 480 months (40 yr).";  // Only Months case remains

                                    CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter valid numbers.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Interest Rate
                                if (input4Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextRepaymenetEmi, errorTextEditTextNumber4, "Please enter an Monthly Repayment (EMI).");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                if (userEnterEMI <= 0) {
                                    CommonMethod.validateInputs(
                                            editTextRepaymenetEmi,
                                            errorTextEditTextNumber4,
                                            "Monthly Repayment (EMI) cannot be zero or negative."
                                    );
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                } else if (userEnterEMI >= 99_999_999_999L) {
                                    CommonMethod.validateInputs(
                                            editTextRepaymenetEmi,
                                            errorTextEditTextNumber4,
                                            "Monthly Repayment (EMI) cannot be more than ₹99,99,99,99,999.00."
                                    );
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextRepaymenetEmi, errorTextEditTextNumber4, "Please enter a valid Monthly Repayment (EMI).");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }
                            break;

                        case "Loan Term":
                            try {
                                // Validate Deposit Amount
                                if (input1Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a loan amount.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input1 = Float.parseFloat(input1Str);
                                if (input1 <= 0) {
                                    CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Loan amount cannot be zero.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid loan amount.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Interest Rate
                                if (input2Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                float input2 = Float.parseFloat(input2Str);
                                if (input2 > 100) {
                                    CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not exceed 100%.");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter a valid interest rate.");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            try {
                                // Validate Interest Rate
                                if (input4Str.isEmpty()) {
                                    CommonMethod.validateInputs(editTextRepaymenetEmi, errorTextEditTextNumber4, "Please enter an Monthly Repayment (EMI).");
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }

                                if (userEnterEMI <= 0) {
                                    CommonMethod.validateInputs(
                                            editTextRepaymenetEmi,
                                            errorTextEditTextNumber4,
                                            "Monthly Repayment (EMI) cannot be zero or negative."
                                    );
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                } else if (userEnterEMI >= 99_999_999_999L) {
                                    CommonMethod.validateInputs(
                                            editTextRepaymenetEmi,
                                            errorTextEditTextNumber4,
                                            "Monthly Repayment (EMI) cannot be more than ₹99,99,99,99,999.00."
                                    );
                                    mainViewModel.setResultBoxVisibility(false);
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                CommonMethod.validateInputs(editTextRepaymenetEmi, errorTextEditTextNumber4, "Please enter a valid Monthly Repayment (EMI).");
                                mainViewModel.setResultBoxVisibility(false);
                                return false;
                            }

                            break;
                    }
                    break;

                case "Fixed Deposit - TDR (Interest Payout)":

                    try {
                        // Validate Deposit Amount
                        if (input1Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a lumpsum deposit amount.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input1 = Float.parseFloat(input1Str);
                        if (input1 <= 0) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Lumpsum Deposit amount cannot be zero or negative.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid deposit amount.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        // Validate Interest Rate
                        if (input2Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input2 = Float.parseFloat(input2Str);
                        if (input2 == 0) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Interest rate should not be zero %.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        } else if (input2 > 100) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not exceed 100%.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter a valid interest rate.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        // Validate Term Period
                        if (input3Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a term period.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input3 = Float.parseFloat(input3Str);

                        // Define minimum & maximum limits based on term unit
                        int minLimit = termUnit.equals("Days") ? 7 : 1; // 7 days min for Days, 1 for Year/Month
                        int maxLimit = termUnit.equals("Years") ? 20 : termUnit.equals("Months") ? 240 : 7300;

                        // Minimum term validation
                        if (input3 < minLimit) {
                            String minErrorMessage = termUnit.equals("Days") ? "Term must be a minimum of 7 days." :
                                    termUnit.equals("Years") ? "Term Period should not be zero." :
                                            "Term Period should not be zero.";

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }


                        // Maximum term validation
                        if (input3 > maxLimit) {
                            String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 20 years." :
                                    termUnit.equals("Months") ? "Term should not exceed 240 months (20 yr)." :
                                            "Term should not exceed 7300 days (20 yr).";

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a valid term period.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    break;
                case "Fixed Deposit - STDR (Cumulative)":

                    try {
                        // Validate Deposit Amount
                        if (input1Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a lumpsum deposit amount.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input1 = Float.parseFloat(input1Str);
                        if (input1 <= 0) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Lumpsum Deposit amount cannot be zero or negative.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a valid deposit amount.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        // Validate Interest Rate
                        if (input2Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input2 = Float.parseFloat(input2Str);
                        if (input2 == 0) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Interest rate should not be zero %.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        } else if (input2 > 100) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not exceed 100%.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter a valid interest rate.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        // Validate Term Period
                        if (input3Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a term period.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input3 = Float.parseFloat(input3Str);

                        // Define minimum & maximum limits based on term unit
                        int minLimit = termUnit.equals("Years") ? 1 : termUnit.equals("Months") ? 6 : 180;
                        int maxLimit = termUnit.equals("Years") ? 20 : termUnit.equals("Months") ? 240 : 7300;

                        // Minimum term validation
                        if (input3 < minLimit) {
                            String minErrorMessage = termUnit.equals("Years") ? "Term Period should not be zero." :
                                    termUnit.equals("Months") ? "Term must be at least 6 months." :
                                            "Term must be at least 180 days.";

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        // Maximum term validation
                        if (input3 > maxLimit) {
                            String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 20 years." :
                                    termUnit.equals("Months") ? "Term should not exceed 240 months (20 yr)." :
                                            "Term should not exceed 7300 days (20 yr).";

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Invalid input. Please enter a valid number.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    break;
                case "Bank Recurring Deposit (RD)":

                    try {
                        if (input1Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter a monthly deposit amount.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input1 = Float.parseFloat(input1Str);
                        if (input1 <= 0) {
                            CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Monthly Deposit amount cannot be zero.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber1, errorTextEditTextNumber1, "Please enter valid numbers.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        if (input2Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input2 = Float.parseFloat(input2Str);
                        if (input2 == 0) {
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Interest rate should not be zero %.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        } else if (input2 > 100){
                            CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Annual Interest Rate must not be more than 100%.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber2, errorTextEditTextNumber2, "Please enter valid numbers.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    try {
                        // Validate Term Period
                        if (input3Str.isEmpty()) {
                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter a term period.");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        float input3 = Float.parseFloat(input3Str);

                        // Define minimum & maximum limits based on term unit (Only Years & Months)
                        int minLimit = termUnit.equals("Years") ? 1 : 6;  // 1 year for Years, 6 months for Months
                        int maxLimit = termUnit.equals("Years") ? 20 : 240;  // 20 years for Years, 240 months for Months

                        // Minimum term validation
                        if (input3 < minLimit) {
                            String minErrorMessage = termUnit.equals("Years") ? "Term Period should not be zero." :
                                    "Term must be at least 6 months.";  // Only Months case remains

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, minErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        // Months should be a multiple of 3
                        if (termUnit.equals("Months") && input3 % 3 != 0) {
                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Term Period in months should be a multiple of 3 (e.g. 6, 9, 12...).");
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }

                        // Maximum term validation
                        if (input3 > maxLimit) {
                            String maxErrorMessage = termUnit.equals("Years") ? "Term should not exceed 20 years." :
                                    "Term should not exceed 240 months (20 yr).";  // Only Months case remains

                            CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, maxErrorMessage);
                            mainViewModel.setResultBoxVisibility(false);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        CommonMethod.validateInputs(editTextNumber3, errorTextEditTextNumber3, "Please enter valid numbers.");
                        mainViewModel.setResultBoxVisibility(false);
                        return false;
                    }

                    break;
            }
        }
        
        return true;
    }
}