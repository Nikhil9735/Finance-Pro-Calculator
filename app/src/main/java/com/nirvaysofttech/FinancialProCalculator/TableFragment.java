package com.nirvaysofttech.FinancialProCalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nirvaysofttech.FinancialProCalculator.R;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ReportAdapter adapter = new ReportAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel.getUserInput1().observe(getViewLifecycleOwner(), deposit -> {
            viewModel.getUserInput2().observe(getViewLifecycleOwner(), interestRate -> {
                List<String[]> tableData = calculateTableData(deposit, interestRate);
                adapter.updateData(tableData);
            });
        });

        return view;
    }

    private List<String[]> calculateTableData(double monthlyDeposit, double annualInterestRate) {
        List<String[]> data = new ArrayList<>();
        double totalDeposited = 0;
        double totalInterest = 0;
        double balance = 0;

        for (int year = 1; year <= 5; year++) {
            double yearlyDeposit = monthlyDeposit * 12;
            totalDeposited += yearlyDeposit;

            double interestEarned = balance * (annualInterestRate / 100);
            totalInterest += interestEarned;

            balance += yearlyDeposit + interestEarned;

            data.add(new String[]{
                    String.valueOf(year),
                    String.format("%.2f", yearlyDeposit),
                    String.format("%.2f", interestEarned),
                    String.format("%.2f", balance)
            });
        }

        data.add(new String[]{
                "Total",
                String.format("%.2f", totalDeposited),
                String.format("%.2f", totalInterest),
                String.format("%.2f", balance)
        });
        return data;
    }

    static class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
        private List<String[]> data;

        public ReportAdapter(List<String[]> data) {
            this.data = data;
        }

        public void updateData(List<String[]> newData) {
            this.data = newData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String[] rowData = data.get(position);

            holder.textYear.setText(rowData[0]);
            holder.textAmountDeposited.setText(rowData[1]);
            holder.textInterestEarned.setText(rowData[2]);
            holder.textYearEndBalance.setText(rowData[3]);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textYear, textAmountDeposited, textInterestEarned, textYearEndBalance;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textYear = itemView.findViewById(R.id.textYear);
                textAmountDeposited = itemView.findViewById(R.id.textAmountDeposited);
                textInterestEarned = itemView.findViewById(R.id.textInterestEarned);
                textYearEndBalance = itemView.findViewById(R.id.textYearEndBalance);
            }
        }
    }

}
