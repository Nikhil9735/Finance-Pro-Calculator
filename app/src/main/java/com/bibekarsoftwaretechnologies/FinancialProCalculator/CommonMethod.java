package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CommonMethod {

    public static void addClearErrorTextWatcher(EditText editText1, TextView errorText1) {
        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear error messages
                if (errorText1 != null) errorText1.setText("");
                //if (errorText2 != null) errorText2.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Attach the TextWatcher to the EditText fields
        if (editText1 != null) editText1.addTextChangedListener(clearErrorWatcher);
        //if (editText2 != null) editText2.addTextChangedListener(clearErrorWatcher);
    }

    static boolean validateInputs(EditText editText, TextView errorTextView, String errorMessage) {
        // Get the values from the EditTexts
        String input1Str = editText.getText().toString();
        //String input2Str = editText2.getText().toString();

        // Validate input1 (editText1)
        errorTextView.setText(errorMessage);
        errorTextView.setVisibility(View.VISIBLE);
        editText.setError(errorMessage);

        return false;
    }
}
