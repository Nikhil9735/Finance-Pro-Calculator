package com.nirvaysofttech.FinancePro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Bank_LoanSaveFragment extends Fragment {

    ListView listView;
    ArrayAdapter<String> adapter;
    DatabaseHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank__loansave, container, false);

        listView = view.findViewById(R.id.list_view);
        dbHelper = new DatabaseHelper(getActivity());

        loadData();

        return view;
    }

    private void loadData() {
        List<String> data = dbHelper.getAllEntries();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(); // refresh list when user comes back
    }
}
