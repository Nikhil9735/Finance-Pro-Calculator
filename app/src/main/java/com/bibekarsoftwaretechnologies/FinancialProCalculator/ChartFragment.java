package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartFragment extends Fragment {

    private RadioButton growthRadioButton, returnRadioButton;
    private PieChart pieChart;
    private LineChart lineChart;
    private MainViewModel mainViewModel;
    private String Label1;
    private String Label2;
    private String Label3;
    private String Label4;


    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        View chartBox = view.findViewById(R.id.chartBox);

        growthRadioButton = view.findViewById(R.id.growthRadioButton);
        returnRadioButton = view.findViewById(R.id.returnRadioButton);
        pieChart = view.findViewById(R.id.pieChart);
        lineChart = view.findViewById(R.id.lineChart);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Set default radio button to Growth when fragment is created
        returnRadioButton.setChecked(true);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);

        // Observe chartBox visibility
        mainViewModel.getChartBoxVisibility().observe(getViewLifecycleOwner(), visible -> {
            chartBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        });

        // Observe selected radio button from ViewModel
        mainViewModel.getSelectedRadioButton().observe(getViewLifecycleOwner(), radioButtonId -> {
            if (radioButtonId != null) {
                if (radioButtonId == R.id.returnRadioButton) {
                    returnRadioButton.setChecked(true);
                    showPieChart();
                } else if (radioButtonId == R.id.growthRadioButton) {
                    growthRadioButton.setChecked(true);
                    showLineChart();
                }
            }
        });

        // Observe user inputs to update the charts
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> {
            mainViewModel.getNumber1ChartResult().observe(getViewLifecycleOwner(), value1 -> {
                mainViewModel.getMaturityAmountResult().observe(getViewLifecycleOwner(), value2 -> {
                    mainViewModel.getTotalInterestResult().observe(getViewLifecycleOwner(), value3 -> {
                        mainViewModel.getTotalDepositResult().observe(getViewLifecycleOwner(), value4 -> {
                            if (value1 != null && value2 != null && value3 != null && value4 != null && operation != null) {
                                updateCharts(operation, value1, value2, value3, value4);
                            }
                        });
                    });
                });
            });
        });

        // On click listener for Growth radio button
        returnRadioButton.setOnClickListener(v -> {
            showPieChart(); // Display PieChart when Growth is selected
        });

        // On click listener for Return radio button
        growthRadioButton.setOnClickListener(v -> {
            growthRadioButton.setChecked(true);
            showLineChart(); // Display LineChart when Return is selected
        });

        return view;
    }

    private void updateCharts(String operation, float value1, float value2, float value3, float value4) {

        if ("Recurring Deposit (RD)".equals(operation) || "Time Deposit (TD)".equals(operation) ||
                "Monthly Income Scheme (MIS)".equals(operation) || "National Savings Certificate (NSC)".equals(operation) ||
                operation.equals("Mahila Samman Savings Certificate (MSSC)") || operation.equals("Bank Recurring Deposit (RD)") ||
                operation.equals("Fixed Deposit - STDR (Cumulative)") || operation.equals("Fixed Deposit - TDR (Interest Payout)") ||
                operation.equals("Simple Loan")) {
            value1 = 0;
            value4 = 0;
        }

        // Update PieChart
        updatePieChart(operation, value1, value2, value3, value4);

        // Update LineChart (optional, based on requirements)
        displayLineChart(operation, value1, value2, value3, value4);

        // Refresh charts
        pieChart.invalidate();  // Redraw PieChart
        lineChart.invalidate(); // Redraw LineChart

        // Ensure the Return radio button is selected after every calculation
        returnRadioButton.setChecked(true);
        mainViewModel.setSelectedRadioButton(R.id.returnRadioButton);

    }

    public void updatePieChart(String operation, float value1, float value2, float value3, float value4) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");
        DecimalFormat percentageFormat = new DecimalFormat("0.0%");

        setLabels(operation);

        float total = value1 + value2 + value3 + value4;

        ArrayList<Integer> colors = new ArrayList<>();

        if (value1 > 0) {
            entries.add(new PieEntry(value1, Label1));
            colors.add(ContextCompat.getColor(requireContext(), R.color.red)); // Add red for value1
        }
        if (value2 > 0) {
            entries.add(new PieEntry(value2, Label2));
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange)); // Add orange for value2
        }
        if (value3 > 0) {
            entries.add(new PieEntry(value3, Label3));
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue2)); // Add blue2 for value3
        }
        if (value4 > 0) {
            entries.add(new PieEntry(value4, Label4));
            colors.add(ContextCompat.getColor(requireContext(), R.color.darkGreen)); // Add dark green for value4
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors); // Set dynamically generated colors
        dataSet.setValueTextSize(13f);
        dataSet.setValueTextColor(Color.WHITE);

        //        pieChart.getDescription().setEnabled(true);  // Make sure description is enabled
//        pieChart.getDescription().setText("Description Lebel");  // Set the text for the description
//        pieChart.getDescription().setPosition(200f, 1850f);
//        pieChart.getDescription().setTextSize(10f);

        // Set up PieDataSet and PieChart properties
//        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.red),
//                ContextCompat.getColor(requireContext(), R.color.orange),
//                ContextCompat.getColor(requireContext(), R.color.blue2),
//                ContextCompat.getColor(requireContext(), R.color.darkGreen));

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                float percentage = value / total;
                return decimalFormat.format(value) + " (" + percentageFormat.format(percentage) + ")";
            }
        });

        pieChart.setData(pieData);
        // Customize the chart
        pieChart.setCenterText("Financial Pro Calculator");  // No text in the center
        TypedValue textColorPrimarytypedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, textColorPrimarytypedValue, true);
        int textColorPrimary = textColorPrimarytypedValue.data;
        pieChart.setCenterTextColor(textColorPrimary);
        // Resolve the theme attribute color (e.g., ?attr/colorSecondary)
        TypedValue colorOnBackgroundtypedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnBackground, colorOnBackgroundtypedValue, true);
        int colorOnBackground = colorOnBackgroundtypedValue.data;
        pieChart.setHoleColor(colorOnBackground);
        pieChart.setCenterTextSize(18f);
        // Hide the default description label
        pieChart.getDescription().setEnabled(false);

        // Set legend text color
        Legend legend = pieChart.getLegend();
        legend.setTextColor(textColorPrimary);  // Apply textColorPrimary to the legend

        // Refresh the chart
        pieChart.invalidate();
        pieChart.setVisibility(View.VISIBLE);
    }

    private void displayLineChart(String operation, float value1, float value2, float value3, float value4) {
        DecimalFormat decimalFormat = new DecimalFormat("##,##,##0.00");
        // Set labels based on the operation
        setLabels(operation);

        // Resolve theme's textColorPrimary
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        int textColorPrimary = typedValue.data;

        // Initialize the list for LineDataSets
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        // Add DataSet for Num2 if applicable
        if (getString(R.string.empSalary).equals(operation) || getString(R.string.empIncrementAmount).equals(operation) || getString(R.string.empIncrementPercent).equals(operation)) {
            ArrayList<Entry> entries4 = new ArrayList<>();
            entries4.add(new Entry(0f, 0f));
            entries4.add(new Entry(5f, value1));
            LineDataSet dataSet1 = new LineDataSet(entries4, Label1);
            dataSet1.setColor(ContextCompat.getColor(getContext(), R.color.red));
            dataSet1.setCircleColor(ContextCompat.getColor(getContext(), R.color.red));
            dataSet1.setLineWidth(2f);
            dataSet1.setValueTextColor(textColorPrimary);
            dataSet1.setValueTextSize(9f);
            dataSets.add(dataSet1);
        }

        // Add DataSet for Total Deposit
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));
        entries.add(new Entry(5f, value2));
        LineDataSet dataSet2 = new LineDataSet(entries, Label2);
        dataSet2.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet2.setCircleColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet2.setLineWidth(2f);
        dataSet2.setValueTextColor(textColorPrimary);
        dataSet2.setValueTextSize(9f);
        dataSets.add(dataSet2);

        // Add DataSet for Total Interest
        ArrayList<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0f, 0f));
        entries2.add(new Entry(5f, value3));
        LineDataSet dataSet3 = new LineDataSet(entries2, Label3);
        dataSet3.setColor(ContextCompat.getColor(getContext(), R.color.blue2));
        dataSet3.setCircleColor(ContextCompat.getColor(getContext(), R.color.blue2));
        dataSet3.setLineWidth(2f);
        dataSet3.setValueTextColor(textColorPrimary);
        dataSet3.setValueTextSize(9f);
        dataSets.add(dataSet3);

        if (getString(R.string.empSalary).equals(operation) || getString(R.string.empIncrementAmount).equals(operation) || getString(R.string.empIncrementPercent).equals(operation)) {
            // Add DataSet for Maturity Amount
            ArrayList<Entry> entries3 = new ArrayList<>();
            entries3.add(new Entry(0f, 0f));
            entries3.add(new Entry(5f, value4));
            LineDataSet dataSet4 = new LineDataSet(entries3, Label4);
            dataSet4.setColor(ContextCompat.getColor(getContext(), R.color.darkGreen));
            dataSet4.setCircleColor(ContextCompat.getColor(getContext(), R.color.darkGreen));
            dataSet4.setLineWidth(2f);
            dataSet4.setValueTextColor(textColorPrimary);
            dataSet4.setValueTextSize(9f);
            dataSets.add(dataSet4);
        }

        // Combine all data sets into LineData
        LineData lineData = new LineData(dataSets);

        // Apply a ValueFormatter for displaying formatted values
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPointLabel(Entry entry) {
                return decimalFormat.format(entry.getY());
            }
        });

        lineChart.setData(lineData);

        // Configure chart properties
        lineChart.getDescription().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(textColorPrimary);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(textColorPrimary);
        leftAxis.setTextSize(12f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false); // Disable right axis

        // Set Y-axis range dynamically based on the highest value
        float maxValue = Math.max(Math.max(Math.max(value4, value2), value3), value1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(maxValue + 100);

        // Set legend text color
        Legend legend = lineChart.getLegend();
        legend.setTextColor(textColorPrimary);

        // Refresh the chart
        lineChart.invalidate();
    }

    private void setLabels(String operation) {
        if (getString(R.string.empSalary).equals(operation)) {
            Label1 = getString(R.string.empSalaryChartIndication1);
            Label2 = getString(R.string.empSalaryChartIndication2);//getString(R.string.SalarySummeryTotalAnnualDeductions);
            Label3 = getString(R.string.empSalaryChartIndication3);
            Label4 = getString(R.string.empSalaryChartIndication4);
        } else if (getString(R.string.empIncrementAmount).equals(operation)) {
            Label1 = getString(R.string.empIncrementChartIndicator1);
            Label2 = getString(R.string.empIncrementChartIndicator2);
            Label3 = getString(R.string.empIncrementChartIndicator3);
            Label4 = getString(R.string.empIncrementChartIndicator4);
        } else if (getString(R.string.empIncrementPercent).equals(operation)) {
            Label1 = "Prev. CTC Percentage";
            Label2 = "";
            Label3 = "";
            Label4 = "Increment Percentage";
        } else {
            Label1 = "";
            Label2 = "Total Deposit";
            Label3 = "Total Interest";
            Label4 = "";
        }
    }

    private void showPieChart() {
        pieChart.setVisibility(View.VISIBLE); // Show PieChart
        lineChart.setVisibility(View.GONE);   // Hide LineChart
    }

    private void showLineChart() {
        pieChart.setVisibility(View.GONE);    // Hide PieChart
        lineChart.setVisibility(View.VISIBLE); // Show LineChart
    }
}