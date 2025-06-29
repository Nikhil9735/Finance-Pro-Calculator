package com.nirvaysofttech.FinancePro;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class Bank_LoanSaveFragment extends Fragment {

    DatabaseHelper dbHelper;
    LinearLayout loanRecordContainer;
    public static Bank_LoanSaveFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        View view = inflater.inflate(R.layout.fragment_bank__loansave, container, false);

        loanRecordContainer = view.findViewById(R.id.loanRecordContainer);
        dbHelper = new DatabaseHelper(getActivity());

        loadAllLoanEntries();

        return view;
    }


    void loadAllLoanEntries() {
        Context context = getContext();
        if (context == null || loanRecordContainer == null) return;

        loanRecordContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);

        List<Map<String, String>> allRecords = dbHelper.getAllEntriesAsMap();
        for (Map<String, String> record : allRecords) {
            View item = inflater.inflate(R.layout.loan_record_item, loanRecordContainer, false);

            // Set values into views...
            ((TextView) item.findViewById(R.id.typeOfCalculateHeading)).setText(record.get("selectedOption"));
            ((TextView) item.findViewById(R.id.loanRecordHeading)).setText(record.get("save_record_name"));
            ((TextView) item.findViewById(R.id.loanAmtValue)).setText("Rs. " + record.get("loan_amt"));
            ((TextView) item.findViewById(R.id.interestRateAmtValue)).setText(record.get("interest_rate") + " %");

            String recordOption = record.get("selectedOption");
            if (recordOption.equalsIgnoreCase("Loan Term")) {
                ((TextView) item.findViewById(R.id.loanTermValue)).setText(record.get("loan_term"));
            } else {
                ((TextView) item.findViewById(R.id.loanTermValue)).setText(record.get("loan_term") + " " + record.get("term_unit"));
            }

            ((TextView) item.findViewById(R.id.emiAmtValue)).setText("Rs. " + record.get("emi_amt"));

            // RECALCULATE button
            Button recalculateButton = item.findViewById(R.id.button_recalculate);
            recalculateButton.setOnClickListener(v -> {
                TabActivity.getViewPager().setCurrentItem(0, true);
                // Parse values from string to appropriate types
                String selectedOption = record.get("selectedOption");
                String save_record_name = record.get("save_record_name");
                String loanAmt = record.get("loan_amt");
                String interestRate = record.get("interest_rate");
                String loanTerm = record.get("loan_term");
                String termUnit = record.get("term_unit");
                String emi_amt = record.get("emi_amt");


                // Delay the call to ensure BKRDFragment is attached
                loanRecordContainer.postDelayed(() -> {
                    BKRDFragment bkrdFragment = BKRDFragment.getInstance();
                    if (bkrdFragment != null && bkrdFragment.isAdded()) {
                        bkrdFragment.loadValuesFromRecalculate(
                                selectedOption, save_record_name, loanAmt, interestRate, loanTerm, termUnit, emi_amt
                        );
                    }
                }, 200); // Delay enough to let the fragment attach
            });

            // Handle DELETE button
            Button deleteButton = item.findViewById(R.id.button_deleteRecord);
            deleteButton.setOnClickListener(v -> {
                String save_record_nameId = record.get("save_record_name");
                boolean deleted = dbHelper.deleteRecordById(save_record_nameId);
                if (deleted) {
                    Toast.makeText(getContext(), "Record deleted", Toast.LENGTH_SHORT).show();
                    loadAllLoanEntries(); // Refresh the UI
                } else {
                    Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            });

            loanRecordContainer.addView(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadAllLoanEntries();
    }
}
