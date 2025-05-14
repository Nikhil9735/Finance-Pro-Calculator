package com.nirvaysofttech.FinancePro;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<String> operation = new MutableLiveData<>();
    private final MutableLiveData<Float> userInput1 = new MutableLiveData<>();
    private final MutableLiveData<Float> userInput2 = new MutableLiveData<>();
    private final MutableLiveData<Float> value1Result = new MutableLiveData<>();
    private final MutableLiveData<Float> value2Result = new MutableLiveData<>();
    private final MutableLiveData<Float> value3Result = new MutableLiveData<>();
    private final MutableLiveData<Float> value4Result = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedRadioButtonId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isResultBoxVisible = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isChartBoxVisible = new MutableLiveData<>(false);

    // Set user inputs and results
    public void setUserInputs(float input1, float input2, float value1, float value2, float value3, float value4) {
        userInput1.setValue(input1);
        userInput2.setValue(input2);
        value1Result.setValue(value1);
        value2Result.setValue(value2);
        value3Result.setValue(value3);
        value4Result.setValue(value4);
    }

    public LiveData<Float> getUserInput1() {
        return userInput1;
    }

    public LiveData<Float> getUserInput2() {
        return userInput2;
    }

    public LiveData<Float> getNumber1ChartResult() {
        return value1Result;
    }
    public LiveData<Float> getMaturityAmountResult() {
        return value2Result;
    }
    public LiveData<Float> getTotalInterestResult() {
        return value3Result;
    }
    public LiveData<Float> getTotalDepositResult() {
        return value4Result;
    }

    // Set the selected radio button
    public void setSelectedRadioButton(int radioButtonId) {
        selectedRadioButtonId.setValue(radioButtonId);
    }

    // Get the selected radio button
    public LiveData<Integer> getSelectedRadioButton() {
        return selectedRadioButtonId;
    }

    // Visibility state for resultBox
    public LiveData<Boolean> getResultBoxVisibility() {
        return isResultBoxVisible;
    }

    public void setResultBoxVisibility(boolean isVisible) {
        isResultBoxVisible.setValue(isVisible);
    }

    // Visibility state for chartBox
    public LiveData<Boolean> getChartBoxVisibility() {
        return isChartBoxVisible;
    }

    public void setChartBoxVisibility(boolean isVisible) {
        isChartBoxVisible.setValue(isVisible);
    }

    // Getter and setter for operation
    public LiveData<String> getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.setValue(operation);
    }

    public void hideKeyboard(Context context) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}