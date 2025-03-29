package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private String operation; // Store the operation type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        // Get the operation passed from MainActivity
        operation = getIntent().getStringExtra("operation");

        Toolbar toolbar = findViewById(R.id.toolbar); // Make sure your TabActivity has a Toolbar with this ID
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(operation);
            // Set custom content description for the navigate up button
            getSupportActionBar().setHomeActionContentDescription(R.string.nav_close);
        }

        // Set the operation in the ViewModel
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setOperation(operation); // Pass the operation to ViewModel

        // Find ViewPager2 and TabLayout by ID
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Set the ViewPager2 Adapter using an embedded adapter class
        viewPager.setAdapter(new ViewPager2Adapter());

        // Set up TabLayoutMediator to sync TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (operation.equals("Recurring Deposit (RD)") || operation.equals("Time Deposit (TD)") || operation.equals("Monthly Income Scheme (MIS)") || operation.equals("National Savings Certificate (NSC)")
                    || operation.equals("Mahila Samman Savings Certificate (MSSC)") || operation.equals("Bank Recurring Deposit (RD)") || operation.equals("Fixed Deposit - STDR (Cumulative)") ||
                    operation.equals("Fixed Deposit - TDR (Interest Payout)") || operation.equals("Simple Loan")) {
                // For RD, TD, MIS, NSC show 4 tabs
                switch (position) {
                    case 0:
                        tab.setText("INPUT");
                        break;
                    case 1:
                        tab.setText("CHART");
                        break;
                    case 2:
                        tab.setText("TABLE");
                        break;
                    case 3:
                        tab.setText("ABOUT");
                        break;
                }
            } else if (operation.equals("Employee Salary")) {
                // For Employee Salary, show 3 tabs
                switch (position) {
                    case 0:
                        tab.setText("SALARY");
                        break;
                    case 1:
                        tab.setText("SALARY CHART");
                        break;
                }
            } else if (operation.equals("Employee Increment")) {
                // For Employee Salary, show 3 tabs
                switch (position) {
                    case 0:
                        tab.setText("INCREMENT");
                        break;
                    case 1:
                        tab.setText("INCREMENT CHART");
                        break;
                }
            } else if (operation.equals("Currency Denomination")) {
                // For Dinominator operation show 2 tabs (Preset and History)
                switch (position) {
                    case 0:
                        tab.setText("COUNTER");
                        break;
                    case 1:
                        tab.setText("CASH BREAKDOWN");
                        break;
                }
            } else if (operation.equals("All Bank Interest Rate (%)")) {
                // For Interest operation show 2 tabs (Local and Metro)
                switch (position) {
                    case 0:
                        tab.setText("SELECT BANK");
                        break;
                    case 1:
                        tab.setText("ABOUT");
                        break;
                }
            }
        }).attach();
    }

    // Inner class for ViewPager2 Adapter
    private class ViewPager2Adapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

        public ViewPager2Adapter() {
            super(TabActivity.this); // Use the activity context
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (operation.equals("Recurring Deposit (RD)") || operation.equals("Time Deposit (TD)") || operation.equals("Monthly Income Scheme (MIS)") || operation.equals("National Savings Certificate (NSC)")
            || operation.equals("Mahila Samman Savings Certificate (MSSC)")) {
                // For RD, TD, MIS, NSC show 4 tabs
                switch (position) {
                    case 0:
                        return new InputFragment();
                    case 1:
                        return new ChartFragment();
                    case 2:
                        return new TableFragment();
                    case 3:
                        return new InfoFragment();
                    default:
                        return new InputFragment();
                }
            }  else if (operation.equals("Employee Salary")) {
                // For Employee Salary, show 3 tabs
                switch (position) {
                    case 0:
                        return new EMPSalaryInput();
                    case 1:
                        return new ChartFragment();
                    default:
                        return new EMPSalaryInput();
                }
            }  else if (operation.equals("Employee Increment")) {
                // For Employee Salary, show 3 tabs
                switch (position) {
                    case 0:
                        return new EMPIncrementFragment();
                    case 1:
                        return new ChartFragment();
                    default:
                        return new EMPIncrementFragment();
                }
            } else if (operation.equals("Currency Denomination")) {
                // For Dinominator operation show 2 tabs (Preset and History)
                switch (position) {
                    case 0:
                        return new DenominationFragment(); // Create PresetFragment
                    case 1:
                        return new DenominationBrkFragment(); // Create HistoryFragment
                    default:
                        return new DenominationFragment();
                }
            }
            else if (operation.equals("All Bank Interest Rate (%)")) {
                // For Interest operation show 2 tabs (Local and Metro)
                switch (position) {
                    case 0:
                        return new AllBankInterestRateFragment(); // Create LocalFragment
                    case 1:
                        return new AllBankInterestInfoFragment(); // Create MetroFragment
                    default:
                        return new AllBankInterestRateFragment();
                }
            }
            else if (operation.equals("Bank Recurring Deposit (RD)") || operation.equals("Fixed Deposit - STDR (Cumulative)") || operation.equals("Fixed Deposit - TDR (Interest Payout)") ||
                    operation.equals("Simple Loan")) {
                // For Interest operation show 2 tabs (Local and Metro)
                switch (position) {
                    case 0:
                        return new BKRDFragment();
                    case 1:
                        return new ChartFragment();
                    case 2:
                        return new TableFragment();
                    case 3:
                        return new InfoFragment();
                    default:
                        return new InputFragment();
                }
            }
            return new InputFragment();
        }

        @Override
        public int getItemCount() {
            if (operation.equals("Recurring Deposit (RD)") || operation.equals("Time Deposit (TD)") || operation.equals("Monthly Income Scheme (MIS)") || operation.equals("National Savings Certificate (NSC)")
            || operation.equals("Mahila Samman Savings Certificate (MSSC)") || operation.equals("Bank Recurring Deposit (RD)") || operation.equals("Fixed Deposit - STDR (Cumulative)") ||
                    operation.equals("Fixed Deposit - TDR (Interest Payout)") || operation.equals("Simple Loan")) {
                return 4; // Show 4 tabs for RD, TD, MIS, NSC
            } else if (operation.equals("Employee Salary") || operation.equals("Employee Increment")) {
                return 2; // Show 3 tabs for Employee Salary
            }  else if (operation.equals("Currency Denomination")) {
                return 2; // Show 2 tabs for Dinominator
            } else if (operation.equals("All Bank Interest Rate (%)")) {
                return 2; // Show 2 tabs for Interest
            }
            return 4; // Default to 4 tabs
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
