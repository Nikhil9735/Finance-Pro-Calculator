package com.nirvaysofttech.FinancePro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class Bank_LoanSaveFragment extends Fragment {

    DatabaseHelper dbHelper;
    LinearLayout loanRecordContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank__loansave, container, false);

        loanRecordContainer = view.findViewById(R.id.loanRecordContainer);
        dbHelper = new DatabaseHelper(getActivity());

        loadAllLoanEntries();

        return view;
    }

    private void loadAllLoanEntries() {
        loanRecordContainer.removeAllViews(); // clear previous if any
        LayoutInflater inflater = LayoutInflater.from(getContext());

        List<Map<String, String>> allRecords = dbHelper.getAllEntriesAsMap();
        for (Map<String, String> record : allRecords) {
            View item = inflater.inflate(R.layout.loan_record_item, loanRecordContainer, false);

            ((TextView) item.findViewById(R.id.loanAmtValue)).setText("Rs. "+record.get("loan_amt"));
            ((TextView) item.findViewById(R.id.interestRateAmtValue)).setText(record.get("interest_rate") + " %");
            ((TextView) item.findViewById(R.id.loanTermValue)).setText(record.get("loan_term") + " " + record.get("term_unit"));
            ((TextView) item.findViewById(R.id.emiAmtValue)).setText("Rs. "+record.get("emi_amt"));

            loanRecordContainer.addView(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllLoanEntries();
    }
}
