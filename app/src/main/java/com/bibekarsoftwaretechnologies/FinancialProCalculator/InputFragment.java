package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

import java.text.DecimalFormat;

public class InputFragment extends Fragment {

    private EditText editText1, editText2;
    private Button calculateButton, resetButton;
    private TextView yearlyInterestHeading, yearlyInterestTextView, totalDepositTextView, totalInterestTextView, maturityAmountTextView,
            textViewHeading, editText1Heading, errorTextEditTextNumber1, errorTextEditTextNumber2, textViewFixInterest, termYear, textViewTerm, textViewTermValue;
    private MainViewModel mainViewModel;
    private Spinner spinnerRowCount;
    private LinearLayout resultBox;

    public InputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        // Initialize views
        textViewHeading = view.findViewById(R.id.textViewHeading);
        editText1Heading = view.findViewById(R.id.editText1Heading);
        editText1 = view.findViewById(R.id.editTextNumber1);
        errorTextEditTextNumber1 = view.findViewById(R.id.errorTextEditTextNumber1);
        termYear = view.findViewById(R.id.termYear);
        spinnerRowCount = view.findViewById(R.id.spinner_row_count);
        editText2 = view.findViewById(R.id.editTextNumber2);
        textViewFixInterest = view.findViewById(R.id.textViewFixInterest);
        errorTextEditTextNumber2 = view.findViewById(R.id.errorTextEditTextNumber2);
        textViewTerm = view.findViewById(R.id.textViewTerm);
        textViewTermValue = view.findViewById(R.id.textViewTermValue);
        calculateButton = view.findViewById(R.id.button_calculate);
        resetButton = view.findViewById(R.id.button_reset);
        resultBox = view.findViewById(R.id.resultBox);

        yearlyInterestHeading = view.findViewById(R.id.yearlyInterestHeading);
        yearlyInterestTextView = view.findViewById(R.id.yearlyInterestResult);
        totalInterestTextView = view.findViewById(R.id.totalInterestResult);
        totalDepositTextView = view.findViewById(R.id.totalDepositResult);
        maturityAmountTextView = view.findViewById(R.id.maturityAmountResult);

        // Initialize ViewModel
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe the operation from ViewModel
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> {
            // Call the method to update UI based on operation
            viewHide(operation);
        });

        CommonMethod.addClearErrorTextWatcher(editText1, errorTextEditTextNumber1);
        CommonMethod.addClearErrorTextWatcher(editText2, errorTextEditTextNumber2);

        // Set onClickListener for the calculate button
        calculateButton.setOnClickListener(v -> {
            String operation = mainViewModel.getOperation().getValue();

            mainViewModel.hideKeyboard(requireContext());

            // Call the validation method
            if (!validateInputs(operation)) {
                return; // Exit if validation fails
            }

            // Continue with the calculation logic if validation passes
            String input1Str = editText1.getText().toString();
            String input2Str = null;
            if (operation.equals("Mahila Samman Savings Certificate (MSSC)")) {
                input2Str = "0";
            } else {
                input2Str = editText2.getText().toString();
            }

            // Continue processing after validation
            float input1 = Float.parseFloat(input1Str);
            float input2 = Float.parseFloat(input2Str);

            // Continue your existing logic for calculations...
            float totalInterest = 0;
            float totalDeposite = 0;
            float maturityAmt = 0;
            float yearlyInterestAmt = 0;
            int compoundingFrequency = 4;
            int numberOfYears = 5;
            float temp = 0;

            if (operation != null) {
                switch (operation) {
                    case "Recurring Deposit (RD)":
                        int n = numberOfYears * 4;
                        float i = input2 / 400;
                        maturityAmt = (float) (input1 * (Math.pow(1 + i, n) - 1) / (1 - Math.pow(1 + i, -1.0 / 3.0)));
                        totalDeposite = input1 * 12 * numberOfYears;
                        totalInterest = maturityAmt - totalDeposite;
                        break;
                    case "Time Deposit (TD)":
                        // Parsing the selected year from the spinner
                        int selectedTime = Integer.parseInt(spinnerRowCount.getSelectedItem().toString().split(" ")[0]);
                        double num2 = input2 / 100;
                        float yearlyInterest = (float) (input1 * (Math.pow(1 + (num2 / compoundingFrequency), compoundingFrequency) - 1));
                        totalInterest = yearlyInterest * selectedTime;
                        maturityAmt = input1 + totalInterest;
                        totalDeposite = input1;
                        yearlyInterestAmt = yearlyInterest;
                        break;
                    case "Monthly Income Scheme (MIS)":
                        temp =input2 / 100;
                        yearlyInterestAmt = (input1 * temp) / 12;
                        totalInterest = yearlyInterestAmt * 12 * numberOfYears;
                        totalDeposite = input1;
                        maturityAmt = input1 + totalInterest;
                        break;
                    case "National Savings Certificate (NSC)":
                        temp= input2 / 100;
                        maturityAmt = input1 * (float) Math.pow(1 + temp, numberOfYears);
                        totalInterest = maturityAmt - input1;
                        totalDeposite = input1;
                        break;
                    case "Mahila Samman Savings Certificate (MSSC)":
                        float annualInterestRate = 7.5f / 100f;  // Convert percentage to decimal (float)
                        int years = 2;  // For 2 years
                        maturityAmt = (float) (input1 * Math.pow(1 + (annualInterestRate / compoundingFrequency), compoundingFrequency * years));
                        totalInterest = maturityAmt - input1;
                        totalDeposite = input1;
                        break;
                }
            }
            float Input1 =0;
            float Input2 =0;

            // Update results in ViewModel
            mainViewModel.setUserInputs(Input1, Input2, input1, totalDeposite, totalInterest, maturityAmt);

            yearlyInterestTextView.setText("Rs. " + decimalFormat.format(yearlyInterestAmt));
            totalInterestTextView.setText("Rs. " + decimalFormat.format(totalInterest));
            totalDepositTextView.setText("Rs. " + decimalFormat.format(totalDeposite));
            maturityAmountTextView.setText("Rs. " + decimalFormat.format(maturityAmt));

            // Show result and chart box
            mainViewModel.setResultBoxVisibility(true);
            mainViewModel.setChartBoxVisibility(true);

        });

        // Observe visibility for resultBox
        mainViewModel.getResultBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            resultBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        // Reset button functionality
        resetButton.setOnClickListener(v -> {
            mainViewModel.hideKeyboard(requireContext());

            // Reset visibility and clear inputs
            totalDepositTextView.setText("");
            totalInterestTextView.setText("");
            maturityAmountTextView.setText("");
            editText1.setText("");
            spinnerRowCount.setSelection(0);
            editText2.setText("");

            // Clear the error messages from the EditText fields
            editText1.setError(null);  // Clear error for editText1
            editText2.setError(null);  // Clear error for editText2

            mainViewModel.setChartBoxVisibility(false);
            mainViewModel.setResultBoxVisibility(false);
            mainViewModel.setSelectedRadioButton(R.id.returnRadioButton); // Reset Growth radio button
        });

        return view;
    }

    // Refactored method to handle UI changes based on the operation
    private void viewHide(String operation) {
        textViewHeading.setText(operation);
        switch (operation) {
            case "Recurring Deposit (RD)":
                // Handle the "addition" case (You may leave it empty if not needed)
                break;

            case "Time Deposit (TD)":
                editText1Heading.setText("Lumpsum Deposit Amount");
                spinnerRowCount.setVisibility(View.VISIBLE);
                termYear.setVisibility(View.VISIBLE);
                textViewTerm.setVisibility(View.GONE);
                textViewTermValue.setVisibility(View.GONE);
                yearlyInterestHeading.setVisibility(View.VISIBLE);
                yearlyInterestTextView.setVisibility(View.VISIBLE);

                // Populate the spinner with "1 year" to "5 years" options
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.year_options, // Ensure this array exists in `res/values/strings.xml`
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRowCount.setAdapter(adapter);

                // Spinner item selection listener
                spinnerRowCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedYear = parent.getItemAtPosition(position).toString();
                        Log.d("InputFragment", "Selected year: " + selectedYear);

                        // Update hint based on selected year
                        switch (selectedYear) {
                            case "1 Year":
                                editText2.setHint("6.9% Currently");
                                break;
                            case "2 Years":
                                editText2.setHint("7.0% Currently");
                                break;
                            case "3 Years":
                                editText2.setHint("7.1% Currently");
                                break;
                            case "5 Years":
                                editText2.setHint("7.5% Currently");
                                break;
                            default:
                                editText2.setHint("6.9% Currently");
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Default hint if nothing is selected
                        editText2.setHint("6.9% Currently");
                    }
                });
                break;
            case "Monthly Income Scheme (MIS)":
                yearlyInterestHeading.setVisibility(View.VISIBLE);
                yearlyInterestTextView.setVisibility(View.VISIBLE);
                break;

            case "National Savings Certificate (NSC)":
                // Handle the "5 Years" case if necessary
                break;

            case "Mahila Samman Savings Certificate (MSSC)":
                // Handle the "5 Years" case if necessary
                textViewFixInterest.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.GONE);
                errorTextEditTextNumber2.setVisibility(View.GONE);
                textViewTermValue.setText("2 Years");
                break;

            default:
                // Handle default case if needed
                break;
        }
    }

    private boolean validateInputs(String operation) {
        // Get the values from the EditTexts
        String input1Str = editText1.getText().toString();
        String input2Str = editText2.getText().toString();

        // Validate input1 (editText1)
        if (input1Str.isEmpty()) {
            CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, operation.equals("Recurring Deposit (RD)") ?
                    "Please enter a deposit amount." : "Please enter a lumpsum deposit amount.");
            return false;
        }

        // Parse input1 after confirming it's not empty
        float input1;
        try {
            input1 = Float.parseFloat(input1Str); // Safe float parsing

            // Validate input1 based on the operation
            if (operation.equals("Recurring Deposit (RD)")) {
                if (input1 % 10 != 0) {
                    CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, "Monthly Deposit amount should be in multiple of 10.");
                    return false;
                }
                if (input1 < 100) {
                    CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, "Monthly Deposit amount must be at least Rs. 100.");
                    return false;
                }
            } else if (operation.equals("Time Deposit (TD)")) {
                if (input1 % 100 != 0) {
                    CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, "Lumpsum Deposit amount should be in multiple of 100.");
                    return false;
                }
                if (input1 < 1000) {
                    CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, "Lumpsum Deposit amount must be at least Rs. 1000.");
                    return false;
                }
            } else if (operation.equals("Mahila Samman Savings Certificate (MSSC)")) {
                // No specific validation for input1 in MSSC
            }
        } catch (NumberFormatException e) {
            CommonMethod.validateInputs(editText1, errorTextEditTextNumber1, "Please enter valid numbers.");
            return false;
        }

        // Skip validation for input2 (editText2) if operation is MSSC
        if (!operation.equals("Mahila Samman Savings Certificate (MSSC)")) {
            // Validate input2 (editText2)
            if (input2Str.isEmpty()) {
                CommonMethod.validateInputs(editText2, errorTextEditTextNumber2, "Please enter an annual interest rate.");
                return false;
            }

            // Parse input2 after confirming it's not empty
            float input2;
            try {
                input2 = Float.parseFloat(input2Str);

                // Validate input2 based on the operation
                if (input2 == 0) {
                    CommonMethod.validateInputs(editText2, errorTextEditTextNumber2, "Interest rate should not be zero %.");
                    return false;
                } else if ((operation.equals("Recurring Deposit (RD)") && input2 > 20) || (operation.equals("Time Deposit (TD)") && input2 > 15)) {
                    String errorMsg = operation.equals("Recurring Deposit (RD)") ?
                            "Annual interest rate Should not exceed 20%." :
                            "Annual interest rate Should not exceed 15%.";
                    CommonMethod.validateInputs(editText2, errorTextEditTextNumber2, errorMsg);
                    return false;
                }
            } catch (NumberFormatException e) {
                CommonMethod.validateInputs(editText2, errorTextEditTextNumber2, "Please enter valid numbers.");
                return false;
            }
        }

        return true; // If all validations pass
    }
}
