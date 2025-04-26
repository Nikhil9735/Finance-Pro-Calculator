package com.nirvaysofttech.FinancialProCalculator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nirvaysofttech.FinancialProCalculator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllBankInterestRateFragment extends Fragment {

    private Button calculateButton;
    private Button buttonClear;
    private Spinner ageGroupDropdown;
    private Spinner selectBankDropdown;
    private TableLayout tableLayout;
    private TextView bankName;
    private TextView ageGroup;
    private View resultBox;

    // Fixed Deposit Rates Data - Organized by Bank and Age Group
    private final Map<String, Map<String, List<String[]>>> fdRatesMap = new HashMap<String, Map<String, List<String[]>>>() {{
        put("AU Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 1 month 15 days", "3.75%"},
                    new String[]{"1 month 16 days to 3 months", "5.50%"},
                    new String[]{"More than 3 months to 6 months", "5.75%"},
                    new String[]{"More than 6 months to 1 years", "7.00%"},
                    new String[]{"More than 1 years to 15 months", "7.60%"},
                    new String[]{"More than 15 months to less than 18 months", "7.40%"},
                    new String[]{"18 months", "7.75%"},
                    new String[]{"More than 18 months to 2 years", "7.50%"},
                    new String[]{"More than 2 years to 3 years", "7.50%"},
                    new String[]{"More than 3 years to 45 months", "7.50%"},
                    new String[]{"More than 45 months to 5 years", "7.25%"},
                    new String[]{"More than 5 years to 10 years", "7.25%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 1 month 15 days", "4.25%"},
                    new String[]{"1 month 16 days to 3 months", "6.00%"},
                    new String[]{"More than 3 months to 6 months", "6.25%"},
                    new String[]{"More than 6 months to 1 year", "7.50%"},
                    new String[]{"More than 1 year to 15 months", "8.10%"},
                    new String[]{"More than 15 months to less than 18 months", "7.90%"},
                    new String[]{"18 months", "8.25%"},
                    new String[]{"More than 18 months to 2 years", "8.00%"},
                    new String[]{"More than 2 years to 3 years", "8.00%"},
                    new String[]{"More than 3 years to 45 months", "8.00%"},
                    new String[]{"More than 45 months but less than 5 years", "7.75%"},
                    new String[]{"5 years to 10 years", "7.75%"}
            ));
        }});

        put("Axis Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.00%"},
                    new String[]{"30 days to 45 days", "3.50%"},
                    new String[]{"46 days to 60 days", "4.25%"},
                    new String[]{"61 days to 3 months", "4.50%"},
                    new String[]{"More than 3 months to 6 months", "4.75%"},
                    new String[]{"More than 6 months to 9 months", "5.75%"},
                    new String[]{"More than 9 months to 1 year", "6.00%"},
                    new String[]{"More than 1 year to 15 months", "6.70%"},
                    new String[]{"More than 15 months to 2 years", "7.25%"},
                    new String[]{"More than 2 years to 3 years", "7.10%"},
                    new String[]{"More than 3 years to 10 years", "7.00%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.50%"},
                    new String[]{"30 days to 45 days", "4.00%"},
                    new String[]{"46 days to 60 days", "4.75%"},
                    new String[]{"61 days to 3 months", "5.00%"},
                    new String[]{"More than 3 months to 6 months", "5.25%"},
                    new String[]{"More than 6 months to 9 months", "6.25%"},
                    new String[]{"More than 9 months to 1 year", "6.50%"},
                    new String[]{"More than 1 year to 15 months", "7.20%"},
                    new String[]{"More than 15 months to 2 years", "7.75%"},
                    new String[]{"More than 2 years to 3 years", "7.60%"},
                    new String[]{"More than 3 years to 10 years", "7.75%"}
            ));
        }});

        put("Bandhan Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to less than 2 months", "3.50%"},
                    new String[]{"2 months to less than 3 months", "4.50%"},
                    new String[]{"3 months to less than 6 months", "4.50%"},
                    new String[]{"6 months to less than 1 year", "4.50%"},
                    new String[]{"1 year", "8.05%"},
                    new String[]{"More than 1 year to 1 year 9 months", "8.00%"},
                    new String[]{"More than 1 year 9 months to 3 years", "7.25%"},
                    new String[]{"More than 3 years to 5 years", "7.25%"},
                    new String[]{"More than 5 years to 10 years", "5.85%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.75%"},
                    new String[]{"31 days to less than 2 months", "4.25%"},
                    new String[]{"2 months to less than 3 months", "5.25%"},
                    new String[]{"3 months to less than 6 months", "5.25%"},
                    new String[]{"6 months to less than 1 year", "5.25%"},
                    new String[]{"1 year", "8.55%"},
                    new String[]{"More than 1 year to 1 year 9 months", "8.50%"},
                    new String[]{"More than 1 year 9 months to 3 years", "7.75%"},
                    new String[]{"More than 3 years to 5 years", "7.75%"},
                    new String[]{"More than 5 years to 10 years", "6.60%"}
            ));
        }});

        put("Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 45 days", "3.00%"},
                    new String[]{"46 days to 179 days", "4.50%"},
                    new String[]{"180 days to less than 1 year", "6.00%"},
                    new String[]{"1 year to less than 2 years", "6.80%"},
                    new String[]{"2 years", "6.80%"},
                    new String[]{"More than 2 years to less than 3 years", "6.75%"},
                    new String[]{"3 years to less than 5 years", "6.50%"},
                    new String[]{"5 years to 10 years", "6.00%"},
                    new String[]{"400 days", "7.30%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.50%"},
                    new String[]{"31 days to 45 days", "3.50%"},
                    new String[]{"46 days to 179 days", "5.00%"},
                    new String[]{"180 days to less than 1 year", "6.50%"},
                    new String[]{"1 year to less than 2 years", "7.30%"},
                    new String[]{"2 years", "7.30%"},
                    new String[]{"More than 2 years to less than 3 years", "7.25%"},
                    new String[]{"3 years to less than 5 years", "7.00%"},
                    new String[]{"5 years to 10 years", "6.50%"},
                    new String[]{"400 days", "7.80%"}
            ));
        }});

        put("Bank of Maharashtra", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "2.75%"},
                    new String[]{"31 days to 45 days", "3.00%"},
                    new String[]{"46 days to 90 days", "4.75%"},
                    new String[]{"91 days to 119 days", "4.90%"},
                    new String[]{"120 days to 180 days", "5.10%"},
                    new String[]{"181 days to 270 days", "5.50%"},
                    new String[]{"271 days to 364 days", "5.60%"},
                    new String[]{"365 days (1 year)", "6.50%"},
                    new String[]{"More than 1 year to 2 years", "6.25%"},
                    new String[]{"More than 2 years to 3 years", "6.25%"},
                    new String[]{"More than 3 years to 5 years", "6.00%"},
                    new String[]{"5 years & above", "6.00%"}
            ));
            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.25%"},
                    new String[]{"31 days to 45 days", "3.50%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 119 days", "5.40%"},
                    new String[]{"120 days to 180 days", "5.60%"},
                    new String[]{"181 days to 270 days", "6.00%"},
                    new String[]{"271 days to 364 days", "6.10%"},
                    new String[]{"365 days (1 year)", "7.00%"},
                    new String[]{"More than 1 year to 2 years", "6.75%"},
                    new String[]{"More than 2 years to 3 years", "6.75%"},
                    new String[]{"More than 3 years to 5 years", "6.50%"},
                    new String[]{"5 years & above", "6.50%"}
            ));
        }});;

        put("Canara Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 45 days", "4.00%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 269 days", "6.15%"},
                    new String[]{"270 days to less than 1 year", "6.25%"},
                    new String[]{"1 year", "6.85%"},
                    new String[]{"More than 1 year to 2 years", "6.85%"},
                    new String[]{"2 years 1 day to 3 years", "7.30%"},
                    new String[]{"More than 3 years to 5 years", "7.40%"},
                    new String[]{"5 years to 10 years", "6.70%"},
                    new String[]{"444 days", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 45 days", "4.00%"},
                    new String[]{"46 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 269 days", "6.65%"},
                    new String[]{"270 days to less than 1 year", "6.75%"},
                    new String[]{"1 year", "7.35%"},
                    new String[]{"More than 1 year to 2 years", "7.35%"},
                    new String[]{"2 years 1 day to 3 years", "7.80%"},
                    new String[]{"More than 3 years to 5 years", "7.90%"},
                    new String[]{"5 years to 10 years", "7.20%"},
                    new String[]{"444 days", "7.75%"}
            ));
        }});

        put("Capital Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 89 Days", "4.00%"},
                    new String[]{"90 Days to 179 Days", "4.75%"},
                    new String[]{"180 Days to less than 1 Year", "5.50%"},
                    new String[]{"1 Year to less than 5 Years", "7.15%"},
                    new String[]{"5 Years and upto 10 Years", "7.10%"},
                    new String[]{"12 Months", "7.55%"},
                    new String[]{"400 Days", "7.60%"},
                    new String[]{"600 Days", "7.40%"},
                    new String[]{"900 Days", "7.30%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 89 Days", "4.50%"},
                    new String[]{"90 Days to 179 Days", "5.25%"},
                    new String[]{"180 Days to less than 1 Year", "6.00%"},
                    new String[]{"1 Year to less than 5 Years", "7.65%"},
                    new String[]{"5 Years and upto 10 Years", "7.60%"},
                    new String[]{"12 Months", "8.05%"},
                    new String[]{"400 Days", "8.10%"},
                    new String[]{"600 Days", "7.90%"},
                    new String[]{"900 Days", "7.80%"}
            ));
        }});

        put("Central Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 14 days", "3.50%"},
                    new String[]{"15 days to 30 days", "3.75%"},
                    new String[]{"31 days to 45 days", "3.75%"},
                    new String[]{"46 days to 59 days", "4.50%"},
                    new String[]{"60 days to 90 days", "4.75%"},
                    new String[]{"91 days to 179 days", "5.00%"},
                    new String[]{"180 days to 270 days", "6.00%"},
                    new String[]{"271 days to 364 days", "6.25%"},
                    new String[]{"1 year to less than 2 years", "6.75%"},
                    new String[]{"2 years to less than 3 years", "7.00%"},
                    new String[]{"3 years to less than 5 years", "6.50%"},
                    new String[]{"5 years to 10 years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 14 days", "4.00%"},
                    new String[]{"15 days to 30 days", "4.25%"},
                    new String[]{"31 days to 45 days", "4.25%"},
                    new String[]{"46 days to 59 days", "5.00%"},
                    new String[]{"60 days to 90 days", "5.25%"},
                    new String[]{"91 days to 179 days", "5.50%"},
                    new String[]{"180 days to 270 days", "6.50%"},
                    new String[]{"271 days to 364 days", "6.75%"},
                    new String[]{"1 year to less than 2 years", "7.25%"},
                    new String[]{"2 years to less than 3 years", "7.50%"},
                    new String[]{"3 years to less than 5 years", "7.00%"},
                    new String[]{"5 years to 10 years", "6.75%"}
            ));
        }});

        put("City Union Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 14 days", "5.00%"},
                    new String[]{"15 days to 45 days", "5.50%"},
                    new String[]{"46 days to 90 days", "5.75%"},
                    new String[]{"91 days to 180 days", "6.00%"},
                    new String[]{"181 days to 270 days", "6.25%"},
                    new String[]{"271 days to 332 days", "6.50%"},
                    new String[]{"333 days", "7.50%"},
                    new String[]{"334 days to 400 days", "7.00%"},
                    new String[]{"401 days to 3 years", "6.50%"},
                    new String[]{"Above 3 years upto 10 years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 14 days", "5.00%"},
                    new String[]{"15 days to 45 days", "5.50%"},
                    new String[]{"46 days to 90 days", "5.75%"},
                    new String[]{"91 days to 180 days", "6.00%"},
                    new String[]{"181 days to 270 days", "6.50%"},
                    new String[]{"271 days to 332 days", "6.75%"},
                    new String[]{"333 days", "8.00%"},
                    new String[]{"334 days to 400 days", "7.25%"},
                    new String[]{"401 days to 3 years", "6.75%"},
                    new String[]{"Above 3 years upto 10 years", "6.50%"}
            ));
        }});

        put("CSB Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 60 days", "4.50%"},
                    new String[]{"61 days to 90 days", "5.00%"},
                    new String[]{"91 days to 179 days", "5.55%"},
                    new String[]{"180 days to 190 days", "6.00%"},
                    new String[]{"191 days*", "7.25%"},
                    new String[]{"192 days to less than 1 year", "4.25%"},
                    new String[]{"1 year", "5.00%"},
                    new String[]{"Above 1 year to 500 days", "5.50%"},
                    new String[]{"1501 days*", "8.00%"},
                    new String[]{"Above 501 days to 2 years", "5.50%"},
                    new String[]{"Above 2 years to less than 750 days", "5.75%"},
                    new String[]{"1750 days*", "7.10%"},
                    new String[]{"Above 750 days to 5 years", "5.75%"},
                    new String[]{"Above 5 years to less than 7 years", "6.00%"},
                    new String[]{"17 years*", "7.00%"},
                    new String[]{"Above 7 years to 10 years", "6.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 30 days", "3.00%"},
                    new String[]{"31 days to 60 days", "4.50%"},
                    new String[]{"61 days to 90 days", "5.00%"},
                    new String[]{"91 days to 179 days", "5.55%"},
                    new String[]{"180 days to 190 days", "6.50%"},
                    new String[]{"191 days*", "7.25%"},
                    new String[]{"192 days to less than 1 year", "4.75%"},
                    new String[]{"1 year", "5.50%"},
                    new String[]{"Above 1 year to 500 days", "6.00%"},
                    new String[]{"501 days*", "8.00%"},
                    new String[]{"Above 501 days to 2 years", "6.00%"},
                    new String[]{"Above 2 years to less than 750 days", "6.25%"},
                    new String[]{"750 days*", "7.10%"},
                    new String[]{"Above 750 days to 5 years", "6.25%"},
                    new String[]{"Above 5 years to less than 7 years", "6.50%"},
                    new String[]{"7 years*", "7.00%"},
                    new String[]{"Above 7 years to 10 years", "6.50%"}
            ));
        }});

        put("DCB Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"45 days", "3.75%"},
                    new String[]{"46 days to 90 days", "4.00%"},
                    new String[]{"91 days to less than 6 months", "4.75%"},
                    new String[]{"6 months to less than 10 months", "6.25%"},
                    new String[]{"10 months to less than 12 months", "7.25%"},
                    new String[]{"12 months", "7.15%"},
                    new String[]{"12 months 1 day to 12 months 10 days", "7.85%"},
                    new String[]{"12 months 11 days to 18 months 5 days", "7.15%"},
                    new String[]{"18 months 6 days to less than 700 days", "7.50%"},
                    new String[]{"More than 700 days to less than 25 months", "7.55%"},
                    new String[]{"25 months to 26 months", "8.00%"},
                    new String[]{"More than 26 months to less than 37 months", "7.60%"},
                    new String[]{"37 months to 38 months", "7.90%"},
                    new String[]{"More than 38 months to less than 61 months", "7.40%"},
                    new String[]{"61 months", "7.65%"},
                    new String[]{"More than 61 months to 120 months", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"45 days", "4.25%"},
                    new String[]{"46 days to 90 days", "4.50%"},
                    new String[]{"91 days to less than 6 months", "5.25%"},
                    new String[]{"6 months to less than 10 months", "6.75%"},
                    new String[]{"10 months to less than 12 months", "7.75%"},
                    new String[]{"12 months", "7.65%"},
                    new String[]{"12 months 1 day to 12 months 10 days", "8.35%"},
                    new String[]{"12 months 11 days to 18 months 5 days", "7.65%"},
                    new String[]{"18 months 6 days to less than 700 days", "8.00%"},
                    new String[]{"More than 700 days to less than 25 months", "8.05%"},
                    new String[]{"25 months to 26 months", "8.60%"},
                    new String[]{"More than 26 months to less than 37 months", "8.10%"},
                    new String[]{"37 months to 38 months", "8.50%"},
                    new String[]{"More than 38 months to less than 61 months", "7.90%"},
                    new String[]{"61 months", "8.15%"},
                    new String[]{"More than 61 months to 120 months", "7.75%"}
            ));
        }});

        put("Dhanlaxmi Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.25%"},
                    new String[]{"15 Days to 45 Days", "4.75%"},
                    new String[]{"46 Days to 60 Days", "4.75%"},
                    new String[]{"61 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to less than 1 Year", "5.25%"},
                    new String[]{"300 Days", "5.25%"},
                    new String[]{"1 Year to 2 Years", "6.75%"},
                    new String[]{"400 Days", "7.25%"},
                    new String[]{"Above 2 Years to 3 Years", "6.50%"},
                    new String[]{"Above 3 Years to 5 Years", "7.00%"},
                    new String[]{"Above 5 Years to 10 Years", "6.60%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.25%"},
                    new String[]{"15 Days to 45 Days", "4.75%"},
                    new String[]{"46 Days to 60 Days", "4.75%"},
                    new String[]{"61 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to less than 1 Year", "5.25%"},
                    new String[]{"300 Days", "5.25%"},
                    new String[]{"1 Year to 2 Years", "7.25%"},
                    new String[]{"400 Days", "7.75%"},
                    new String[]{"Above 2 Years to 3 Years", "7.00%"},
                    new String[]{"Above 3 Years to 5 Years", "7.50%"},
                    new String[]{"Above 5 Years to 10 Years", "7.10%"}
            ));
        }});

        put("Equitas Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 – 14 days", "3.50%"},
                    new String[]{"15 – 29 days", "3.50%"},
                    new String[]{"30 – 45 days", "4.00%"},
                    new String[]{"46 – 62 days", "4.50%"},
                    new String[]{"63 – 90 days", "5.50%"},
                    new String[]{"91 – 120 days", "6.00%"},
                    new String[]{"121 – 180 days", "6.00%"},
                    new String[]{"181 – 210 days", "7.00%"},
                    new String[]{"211 – 270 days", "7.00%"},
                    new String[]{"271 – 364 days", "7.20%"},
                    new String[]{"1 year to 443 days", "7.90%"},
                    new String[]{"444 days", "7.90%"},
                    new String[]{"445 days to 18 months", "7.90%"},
                    new String[]{"18 months 1 day to 2 years", "7.75%"},
                    new String[]{"2 years 1 day to 776 days", "7.75%"},
                    new String[]{"777 days", "7.77%"},
                    new String[]{"778 days to 887 days", "7.75%"},
                    new String[]{"888 days", "8.05%"},
                    new String[]{"889 days to 3 years", "7.75%"},
                    new String[]{"3 years 1 day to 4 years", "7.50%"},
                    new String[]{"4 years 1 day to 5 years", "7.25%"},
                    new String[]{"5 years 1 day to 10 years", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 – 14 days", "4.00%"},
                    new String[]{"15 – 29 days", "4.00%"},
                    new String[]{"30 – 45 days", "4.50%"},
                    new String[]{"46 – 62 days", "5.00%"},
                    new String[]{"63 – 90 days", "6.00%"},
                    new String[]{"91 – 120 days", "6.50%"},
                    new String[]{"121 – 180 days", "6.50%"},
                    new String[]{"181 – 210 days", "7.50%"},
                    new String[]{"211 – 270 days", "7.50%"},
                    new String[]{"271 – 364 days", "7.70%"},
                    new String[]{"1 year to 443 days", "8.40%"},
                    new String[]{"444 days", "8.40%"},
                    new String[]{"445 days to 18 months", "8.40%"},
                    new String[]{"18 months 1 day to 2 years", "8.25%"},
                    new String[]{"2 years 1 day to 776 days", "8.25%"},
                    new String[]{"777 days", "8.27%"},
                    new String[]{"778 days to 887 days", "8.25%"},
                    new String[]{"888 days", "8.55%"},
                    new String[]{"889 days to 3 years", "8.25%"},
                    new String[]{"3 years 1 day to 4 years", "8.00%"},
                    new String[]{"4 years 1 day to 5 years", "7.75%"},
                    new String[]{"5 years 1 day to 10 years", "7.75%"}
            ));
        }});

        put("ESAF Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 14 days", "3.00%"},
                    new String[]{"15 days to 59 days", "3.50%"},
                    new String[]{"60 days to 90 days", "4.00%"},
                    new String[]{"91 days to 182 days", "4.25%"},
                    new String[]{"183 days to 1 year", "5.00%"},
                    new String[]{"1 year 1 day to 443 days", "7.50%"},
                    new String[]{"444 days", "7.75%"},
                    new String[]{"445 days to less than 2 years", "7.50%"},
                    new String[]{"2 years to less than 3 years", "7.50%"},
                    new String[]{"3 years to less than 5 years", "6.25%"},
                    new String[]{"5 years to 10 years", "6.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 14 days", "3.50%"},
                    new String[]{"15 days to 59 days", "4.00%"},
                    new String[]{"60 days to 90 days", "4.50%"},
                    new String[]{"91 days to 182 days", "4.75%"},
                    new String[]{"183 days to 1 year", "5.50%"},
                    new String[]{"1 year 1 day to 443 days", "8.00%"},
                    new String[]{"444 days", "8.25%"},
                    new String[]{"445 days to less than 2 years", "8.00%"},
                    new String[]{"2 years to less than 3 years", "8.00%"},
                    new String[]{"3 years to less than 5 years", "6.75%"},
                    new String[]{"5 years to 10 years", "6.50%"}
            ));
        }});

        put("Federal Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.00%"},
                    new String[]{"30 days to 45 days", "3.50%"},
                    new String[]{"46 days to 90 days", "4.50%"},
                    new String[]{"91 days to 180 days", "5.00%"},
                    new String[]{"181 days to 270 days", "6.00%"},
                    new String[]{"271 days to less than 1 year", "6.50%"},
                    new String[]{"1 year", "6.85%"},
                    new String[]{"Above 1 year to 443 days", "7.00%"},
                    new String[]{"444 days", "7.30%"},
                    new String[]{"445 days to 5 years", "7.00%"},
                    new String[]{"Above 5 years to 10 years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 days to 29 days", "3.50%"},
                    new String[]{"30 days to 45 days", "4.00%"},
                    new String[]{"46 days to 90 days", "5.00%"},
                    new String[]{"91 days to 180 days", "5.50%"},
                    new String[]{"181 days to 270 days", "6.50%"},
                    new String[]{"271 days to less than 1 year", "7.00%"},
                    new String[]{"1 year", "7.35%"},
                    new String[]{"Above 1 year to 443 days", "7.50%"},
                    new String[]{"444 days", "7.80%"},
                    new String[]{"445 days to 5 years", "7.50%"},
                    new String[]{"Above 5 years to 10 years", "7.00%"}
            ));
        }});

        put("HDFC Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 - 14 days", "3.00%"},
                    new String[]{"15 - 29 days", "3.00%"},
                    new String[]{"30 - 45 days", "3.50%"},
                    new String[]{"46 - 60 days", "4.50%"},
                    new String[]{"61 - 89 days", "4.50%"},
                    new String[]{"90 days to 6 months", "4.50%"},
                    new String[]{"6 months 1 day to 9 months", "5.75%"},
                    new String[]{"9 months 1 day to 1 year", "6.00%"},
                    new String[]{"1 year to 15 months", "6.60%"},
                    new String[]{"15 months to 18 months", "7.05%"},
                    new String[]{"18 months to 21 months", "7.05%"},
                    new String[]{"21 months to 2 years", "6.70%"},
                    new String[]{"2 years 1 day to 2 years 11 months", "6.90%"},
                    new String[]{"2 years 11 months to 35 months", "6.90%"},
                    new String[]{"2 years 11 months 1 day to 3 years", "6.90%"},
                    new String[]{"3 years 1 day to 4 years 7 months", "6.75%"},
                    new String[]{"4 years 7 months to 55 months", "6.75%"},
                    new String[]{"4 years 7 months 1 day to 5 years", "6.75%"},
                    new String[]{"5 years 1 day to 10 years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 - 14 days", "3.50%"},
                    new String[]{"15 - 29 days", "3.50%"},
                    new String[]{"30 - 45 days", "4.00%"},
                    new String[]{"46 - 60 days", "5.00%"},
                    new String[]{"61 - 89 days", "5.00%"},
                    new String[]{"90 days to 6 months", "5.00%"},
                    new String[]{"6 months 1 day to 9 months", "6.25%"},
                    new String[]{"9 months 1 day to 1 year", "6.50%"},
                    new String[]{"1 year to 15 months", "7.10%"},
                    new String[]{"15 months to 18 months", "7.55%"},
                    new String[]{"18 months to 21 months", "7.55%"},
                    new String[]{"21 months to 2 years", "7.20%"},
                    new String[]{"2 years 1 day to 2 years 11 months", "7.40%"},
                    new String[]{"2 years 11 months to 35 months", "7.40%"},
                    new String[]{"2 years 11 months 1 day to 3 years", "7.40%"},
                    new String[]{"3 years 1 day to 4 years 7 months", "7.25%"},
                    new String[]{"4 years 7 months to 55 months", "7.25%"},
                    new String[]{"4 years 7 months 1 day to 5 years", "7.25%"},
                    new String[]{"5 years 1 day to 10 years", "7.00%"}
            ));
        }});

        put("ICICI Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 to 45 Days", "3.00%"},
                    new String[]{"46 to 90 Days", "4.25%"},
                    new String[]{"91 to 184 Days", "4.75%"},
                    new String[]{"185 to 270 Days", "5.75%"},
                    new String[]{"271 Days to 1 Year", "6.00%"},
                    new String[]{"1 Year to 15 Months", "6.70%"},
                    new String[]{"15 Months to 18 Months", "6.80%"},
                    new String[]{"18 Months to 2 Years", "7.05%"},
                    new String[]{"2 Years 1 Day to 5 Years", "6.90%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.80%"},
                    new String[]{"5 Years (Tax Saver FD)", "6.90%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 to 45 Days", "3.50%"},
                    new String[]{"46 to 90 Days", "4.75%"},
                    new String[]{"91 to 184 Days", "5.25%"},
                    new String[]{"185 to 270 Days", "6.25%"},
                    new String[]{"271 Days to 1 Year", "6.50%"},
                    new String[]{"1 Year to 15 Months", "7.20%"},
                    new String[]{"15 Months to 18 Months", "7.30%"},
                    new String[]{"18 Months to 2 Years", "7.55%"},
                    new String[]{"2 Years 1 Day to 5 Years", "7.40%"},
                    new String[]{"5 Years 1 Day to 10 Years", "7.30%"},
                    new String[]{"5 Years (Tax Saver FD)", "7.40%"}
            ));
        }});

        put("IDBI Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 to 30 Days", "3.00%"},
                    new String[]{"31 to 45 Days", "3.25%"},
                    new String[]{"46 to 60 Days", "4.50%"},
                    new String[]{"61 to 90 Days", "4.75%"},
                    new String[]{"91 Days to 6 Months", "5.50%"},
                    new String[]{"6 Months 1 Day to 270 Days", "6.00%"},
                    new String[]{"271 Days to 1 Year", "6.25%"},
                    new String[]{"1 Year to 2 Years (except 444 Days, 555 Days and 700 Days)", "6.80%"},
                    new String[]{"2 Years 1 Day to 3 Years", "7.00%"},
                    new String[]{"3 Years to 5 Years", "6.50%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.25%"},
                    new String[]{"10 Years 1 Day to 20 Years", "4.80%"},
                    new String[]{"5 Years (Tax Saving FD)", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 to 30 Days", "3.50%"},
                    new String[]{"31 to 45 Days", "3.75%"},
                    new String[]{"46 to 60 Days", "5.00%"},
                    new String[]{"61 to 90 Days", "5.25%"},
                    new String[]{"91 Days to 6 Months", "6.00%"},
                    new String[]{"6 Months 1 Day to 270 Days", "6.50%"},
                    new String[]{"271 Days to 1 Year", "6.75%"},
                    new String[]{"1 Year to 2 Years (except 444 Days, 555 Days and 700 Days)", "7.30%"},
                    new String[]{"2 Years 1 Day to 3 Years", "7.50%"},
                    new String[]{"3 Years to 5 Years", "7.00%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.75%"},
                    new String[]{"10 Years 1 Day to 20 Years", "5.30%"},
                    new String[]{"5 Years (Tax Saving FD)", "7.00%"}
            ));
        }});

        put("IDFC FIRST Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 to 14 Days", "3.00%"},
                    new String[]{"15 to 29 Days", "3.00%"},
                    new String[]{"30 to 45 Days", "3.00%"},
                    new String[]{"46 to 90 Days", "4.50%"},
                    new String[]{"91 to 180 Days", "4.50%"},
                    new String[]{"181 Days to less than 1 Year", "5.75%"},
                    new String[]{"1 Year", "6.50%"},
                    new String[]{"1 Year 1 Day to 370 Days", "7.00%"},
                    new String[]{"371 Days to 399 Days", "7.25%"},
                    new String[]{"400 Days to 500 Days", "7.50%"},
                    new String[]{"501 Days to 2 Years", "7.00%"},
                    new String[]{"2 Years 1 Day to 3 Years", "6.50%"},
                    new String[]{"3 Years 1 Day to 5 Years", "6.25%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.25%"},
                    new String[]{"5 Years (Tax Saver FD)", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 to 14 Days", "3.50%"},
                    new String[]{"15 to 29 Days", "3.50%"},
                    new String[]{"30 to 45 Days", "3.50%"},
                    new String[]{"46 to 90 Days", "5.00%"},
                    new String[]{"91 to 180 Days", "5.00%"},
                    new String[]{"181 Days to less than 1 Year", "6.25%"},
                    new String[]{"1 Year", "7.00%"},
                    new String[]{"1 Year 1 Day to 370 Days", "7.50%"},
                    new String[]{"371 Days to 399 Days", "7.75%"},
                    new String[]{"400 Days to 500 Days", "8.00%"},
                    new String[]{"501 Days to 2 Years", "7.50%"},
                    new String[]{"2 Years 1 Day to 3 Years", "7.00%"},
                    new String[]{"3 Years 1 Day to 5 Years", "6.75%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.75%"},
                    new String[]{"5 Years (Tax Saver FD)", "6.25%"}
            ));
        }});

        put("Indian Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "2.80%"},
                    new String[]{"15 Days to 29 Days", "2.80%"},
                    new String[]{"30 Days to 45 Days", "3.00%"},
                    new String[]{"46 Days to 90 Days", "3.25%"},
                    new String[]{"91 Days to 120 Days", "3.50%"},
                    new String[]{"121 Days to 180 Days", "3.85%"},
                    new String[]{"181 Days to less than 9 Months", "4.50%"},
                    new String[]{"9 Months to 364 Days", "4.75%"},
                    new String[]{"1 Year", "6.10%"},
                    new String[]{"1 Year 1 Day to 1 Year 364 Days", "7.10%"},
                    new String[]{"2 Years to 2 Years 364 Days", "6.70%"},
                    new String[]{"3 Years to 4 Years 364 Days", "6.25%"},
                    new String[]{"5 Years", "6.25%"},
                    new String[]{"Above 5 Years to 10 Years", "6.10%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.30%"},
                    new String[]{"15 Days to 29 Days", "3.30%"},
                    new String[]{"30 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 90 Days", "3.75%"},
                    new String[]{"91 Days to 120 Days", "4.00%"},
                    new String[]{"121 Days to 180 Days", "4.35%"},
                    new String[]{"181 Days to less than 9 Months", "5.00%"},
                    new String[]{"9 Months to 364 Days", "5.25%"},
                    new String[]{"1 Year", "6.60%"},
                    new String[]{"1 Year 1 Day to 1 Year 364 Days", "7.60%"},
                    new String[]{"2 Years to 2 Years 364 Days", "7.20%"},
                    new String[]{"3 Years to 4 Years 364 Days", "6.75%"},
                    new String[]{"5 Years", "6.75%"},
                    new String[]{"Above 5 Years to 10 Years", "6.60%"}
            ));
        }});

        put("Indian Overseas Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 29 Days", "4.50%"},
                    new String[]{"30 Days to 45 Days", "4.50%"},
                    new String[]{"46 Days to 60 Days", "4.50%"},
                    new String[]{"61 Days to 90 Days", "4.25%"},
                    new String[]{"91 Days to 120 Days", "4.75%"},
                    new String[]{"121 Days to 179 Days", "4.25%"},
                    new String[]{"180 Days to 269 Days", "5.75%"},
                    new String[]{"270 Days to less than 1 Year", "5.75%"},
                    new String[]{"1 Year to less than 2 Years (Except 444 Days)", "6.90%"},
                    new String[]{"444 Days", "7.30%"},
                    new String[]{"2 Years to less than 3 Years", "6.80%"},
                    new String[]{"3 Years and Above", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 29 Days", "4.50%"},
                    new String[]{"30 Days to 45 Days", "4.50%"},
                    new String[]{"46 Days to 60 Days", "4.50%"},
                    new String[]{"61 Days to 90 Days", "4.25%"},
                    new String[]{"91 Days to 120 Days", "4.75%"},
                    new String[]{"121 Days to 179 Days", "4.25%"},
                    new String[]{"180 Days to 269 Days", "5.75%"},
                    new String[]{"270 Days to less than 1 Year", "5.75%"},
                    new String[]{"1 Year to less than 2 Years (Except 444 Days)", "7.10%"},
                    new String[]{"444 Days", "7.30%"},
                    new String[]{"2 Years to less than 3 Years", "6.80%"},
                    new String[]{"3 Years and Above", "6.50%"}
            ));
        }});

        put("IndusInd Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.75%"},
                    new String[]{"46 Days to 60 Days", "4.75%"},
                    new String[]{"61 Days to 90 Days", "4.75%"},
                    new String[]{"91 Days to 120 Days", "4.75%"},
                    new String[]{"121 Days to 180 Days", "5.00%"},
                    new String[]{"181 Days to 210 Days", "5.85%"},
                    new String[]{"211 Days to 269 Days", "6.10%"},
                    new String[]{"270 Days to 354 Days", "6.35%"},
                    new String[]{"355 Days to 364 Days", "6.50%"},
                    new String[]{"1 Year to below 1 Year 3 Months", "7.75%"},
                    new String[]{"1 Year 3 Months (15 Months) to below 1 Year 4 Months (16 Months)", "7.75%"},
                    new String[]{"1 Year 4 Months to below 1 Year 5 Months", "7.75%"},
                    new String[]{"1 Year 5 Months to below 1 Year 6 Months", "7.75%"},
                    new String[]{"1 Year 6 Months up to 2 Years", "7.75%"},
                    new String[]{"Above 2 Years to below 2 Years 6 Months", "7.25%"},
                    new String[]{"2 Years 6 Months (30 Months) to below 2 Years 7 Months (31 Months)", "7.25%"},
                    new String[]{"2 Years 7 Months to 3 Years 3 Months", "7.25%"},
                    new String[]{"Above 3 Years 3 Months to below 61 Months", "7.25%"},
                    new String[]{"61 Months and Above", "7.00%"},
                    new String[]{"Tax Saver (5 Years)", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "4.25%"},
                    new String[]{"46 Days to 60 Days", "5.25%"},
                    new String[]{"61 Days to 90 Days", "5.25%"},
                    new String[]{"91 Days to 120 Days", "5.25%"},
                    new String[]{"121 Days to 180 Days", "5.50%"},
                    new String[]{"181 Days to 210 Days", "6.35%"},
                    new String[]{"211 Days to 269 Days", "6.60%"},
                    new String[]{"270 Days to 354 Days", "6.85%"},
                    new String[]{"355 Days to 364 Days", "7.00%"},
                    new String[]{"1 Year to below 1 Year 3 Months", "8.25%"},
                    new String[]{"1 Year 3 Months (15 Months) to below 1 Year 4 Months (16 Months)", "8.25%"},
                    new String[]{"1 Year 4 Months to below 1 Year 5 Months", "8.25%"},
                    new String[]{"1 Year 5 Months to below 1 Year 6 Months", "8.25%"},
                    new String[]{"1 Year 6 Months up to 2 Years", "8.25%"},
                    new String[]{"Above 2 Years to below 2 Years 6 Months", "7.75%"},
                    new String[]{"2 Years 6 Months (30 Months) to below 2 Years 7 Months (31 Months)", "7.75%"},
                    new String[]{"2 Years 7 Months to 3 Years 3 Months", "7.75%"},
                    new String[]{"Above 3 Years 3 Months to below 61 Months", "7.75%"},
                    new String[]{"61 Months and Above", "7.50%"},
                    new String[]{"Tax Saver (5 Years)", "7.75%"}
            ));
        }});

        put("Jammu & Kashmir Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 90 Days", "4.60%"},
                    new String[]{"91 Days to 180 Days", "5.50%"},
                    new String[]{"181 Days to 270 Days", "6.25%"},
                    new String[]{"271 Days to less than 1 Year", "6.25%"},
                    new String[]{"1 Year to less than 2 Years", "7.00%"},
                    new String[]{"2 Years to less than 3 Years", "7.00%"},
                    new String[]{"3 Years to 5 Years", "6.75%"},
                    new String[]{"5 Years to less than 10 Years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "5.10%"},
                    new String[]{"91 Days to 180 Days", "6.00%"},
                    new String[]{"181 Days to 270 Days", "6.75%"},
                    new String[]{"271 Days to less than 1 Year", "6.75%"},
                    new String[]{"1 Year to less than 2 Years", "7.50%"},
                    new String[]{"2 Years to less than 3 Years", "7.50%"},
                    new String[]{"3 Years to 5 Years", "7.25%"},
                    new String[]{"5 Years to less than 10 Years", "7.00%"}
            ));
        }});

        put("Jana Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.00%"},
                    new String[]{"15 Days to 60 Days", "4.25%"},
                    new String[]{"61 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 120 Days", "6.25%"},
                    new String[]{"121 Days to 180 Days", "6.50%"},
                    new String[]{"181 Days to 365 Days", "7.50%"},
                    new String[]{"Above 1 Year to 2 Years", "8.10%"},
                    new String[]{"Above 2 Years to 3 Years", "8.25%"},
                    new String[]{"Above 3 Years to less than 5 Years", "7.25%"},
                    new String[]{"5 years (1825 days) (Tax Saver Deposit)", "8.20%"},
                    new String[]{"Above 5 Years to 10 Years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 60 Days", "4.75%"},
                    new String[]{"61 Days to 90 Days", "5.50%"},
                    new String[]{"91 Days to 120 Days", "6.75%"},
                    new String[]{"121 Days to 180 Days", "7.00%"},
                    new String[]{"181 Days to 365 Days", "8.00%"},
                    new String[]{"Above 1 Year to 2 Years", "8.60%"},
                    new String[]{"Above 2 Years to 3 Years", "8.75%"},
                    new String[]{"Above 3 Years to less than 5 Years", "7.75%"},
                    new String[]{"5 years (1825 days) (Tax Saver Deposit)", "8.20%"},
                    new String[]{"Above 5 Years to 10 Years", "7.00%"}
            ));
        }});

        put("Karur Vysya Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "5.25%"},
                    new String[]{"46 Days to 90 Days", "5.25%"},
                    new String[]{"91 Days to 120 Days", "6.00%"},
                    new String[]{"121 Days to 180 Days", "6.00%"},
                    new String[]{"181 Days to 270 Days", "6.25%"},
                    new String[]{"333 Days", "7.40%"},
                    new String[]{"1 Year to 443 Days", "7.00%"},
                    new String[]{"Above 2 Years to less than 3 Years", "7.00%"},
                    new String[]{"Above 3 Years to less than 5 Years", "6.50%"},
                    new String[]{"5 Years and Above", "6.25%"},
                    new String[]{"444 Days", "7.50%"},
                    new String[]{"445 Days to 2 Years", "7.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "5.25%"},
                    new String[]{"46 Days to 90 Days", "5.25%"},
                    new String[]{"91 Days to 120 Days", "6.00%"},
                    new String[]{"121 Days to 180 Days", "6.00%"},
                    new String[]{"181 Days to 270 Days", "6.25%"},
                    new String[]{"333 Days", "7.80%"},
                    new String[]{"1 Year to 443 Days", "7.40%"},
                    new String[]{"Above 2 Years to less than 3 Years", "7.40%"},
                    new String[]{"Above 3 Years to less than 5 Years", "6.90%"},
                    new String[]{"5 Years and Above", "6.65%"},
                    new String[]{"444 Days", "8.00%"},
                    new String[]{"445 Days to 2 Years", "7.40%"}
            ));
        }});

        put("Karnataka Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 90 Days", "4.00%"},
                    new String[]{"91 Days to 179 Days", "5.25%"},
                    new String[]{"180 Days to less than 1 Year", "6.25%"},
                    new String[]{"1 Year to 398 Days", "7.00%"},
                    new String[]{"399 Days", "7.25%"},
                    new String[]{"400 Days to 2 Years", "7.00%"},
                    new String[]{"Above 2 Years to 3 Years", "6.50%"},
                    new String[]{"Above 3 Years to 5 Years", "6.50%"},
                    new String[]{"Above 5 Years to 10 Years", "5.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "3.75%"},
                    new String[]{"46 Days to 90 Days", "4.25%"},
                    new String[]{"91 Days to 179 Days", "5.50%"},
                    new String[]{"180 Days to less than 1 Year", "6.50%"},
                    new String[]{"1 Year to 398 Days", "7.50%"},
                    new String[]{"399 Days", "7.75%"},
                    new String[]{"400 Days to 2 Years", "7.50%"},
                    new String[]{"Above 2 Years to 3 Years", "7.00%"},
                    new String[]{"Above 3 Years to 5 Years", "7.00%"},
                    new String[]{"Above 5 Years to 10 Years", "6.00%"}
            ));
        }});

        put("Kotak Mahindra Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "2.75%"},
                    new String[]{"15 Days to 30 Days", "3.00%"},
                    new String[]{"31 Days to 45 Days", "3.25%"},
                    new String[]{"46 Days to 90 Days", "3.50%"},
                    new String[]{"91 Days to 120 Days", "4.00%"},
                    new String[]{"121 Days to 179 Days", "4.25%"},
                    new String[]{"180 Days", "7.00%"},
                    new String[]{"181 Days to 269 Days", "6.00%"},
                    new String[]{"270 Days", "6.00%"},
                    new String[]{"271 Days to 363 Days", "6.00%"},
                    new String[]{"364 Days", "6.50%"},
                    new String[]{"365 Days to 389 Days", "6.80%"},
                    new String[]{"390 Days (12 months 25 days)", "7.00%"},
                    new String[]{"391 Days to Less than 23 Months", "7.15%"},
                    new String[]{"23 Months", "7.15%"},
                    new String[]{"23 Months 1 Day to less than 2 Years", "7.05%"},
                    new String[]{"2 Years to less than 3 Years", "6.90%"},
                    new String[]{"3 Years and above but less than 4 Years", "6.90%"},
                    new String[]{"4 Years and above but less than 5 Years", "6.90%"},
                    new String[]{"5 Years and above up to and inclusive of 10 Years", "6.20%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.25%"},
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.75%"},
                    new String[]{"46 Days to 90 Days", "4.00%"},
                    new String[]{"91 Days to 120 Days", "4.50%"},
                    new String[]{"121 Days to 179 Days", "4.75%"},
                    new String[]{"180 Days", "7.50%"},
                    new String[]{"181 Days to 269 Days", "6.50%"},
                    new String[]{"270 Days", "6.50%"},
                    new String[]{"271 Days to 363 Days", "6.50%"},
                    new String[]{"364 Days", "7.00%"},
                    new String[]{"365 Days to 389 Days", "7.30%"},
                    new String[]{"390 Days (12 months 25 days)", "7.50%"},
                    new String[]{"391 Days to Less than 23 Months", "7.65%"},
                    new String[]{"23 Months", "7.65%"},
                    new String[]{"23 Months 1 Day to less than 2 Years", "7.55%"},
                    new String[]{"2 Years to less than 3 Years", "7.40%"},
                    new String[]{"3 Years and above but less than 4 Years", "7.40%"},
                    new String[]{"4 Years and above but less than 5 Years", "7.40%"},
                    new String[]{"5 Years and above up to and inclusive of 10 Years", "6.70%"}
            ));
        }});

        put("Nainital Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.25%"},
                    new String[]{"15 Days to 30 Days", "3.25%"},
                    new String[]{"31 Days to 45 Days", "3.25%"},
                    new String[]{"46 Days to 90 Days", "4.25%"},
                    new String[]{"91 Days to 179 Days", "4.25%"},
                    new String[]{"180 Days to less than 270 Days", "4.95%"},
                    new String[]{"270 Days to less than 1 Year", "5.75%"},
                    new String[]{"1 Year to 18 Months", "6.70%"},
                    new String[]{"Above 18 Months to 2 Years", "6.25%"},
                    new String[]{"Above 2 Years to 3 Years", "6.25%"},
                    new String[]{"Above 3 Years to 5 Years", "5.75%"},
                    new String[]{"Above 5 Years to 10 Years", "5.35%"},
                    new String[]{"Naini 2023 Deposit Scheme – 400 Days", "7.05%"},
                    new String[]{"5 Years to 10 Years (Tax Saver Deposit)", "5.75%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.75%"},
                    new String[]{"15 Days to 30 Days", "3.75%"},
                    new String[]{"31 Days to 45 Days", "3.75%"},
                    new String[]{"46 Days to 90 Days", "4.75%"},
                    new String[]{"91 Days to 179 Days", "4.75%"},
                    new String[]{"180 Days to less than 270 Days", "5.45%"},
                    new String[]{"270 Days to less than 1 Year", "6.25%"},
                    new String[]{"1 Year to 18 Months", "7.20%"},
                    new String[]{"Above 18 Months to 2 Years", "6.75%"},
                    new String[]{"Above 2 Years to 3 Years", "6.75%"},
                    new String[]{"Above 3 Years to 5 Years", "6.25%"},
                    new String[]{"Above 5 Years to 10 Years", "5.85%"},
                    new String[]{"Naini 2023 Deposit Scheme – 400 Days", "7.55%"},
                    new String[]{"5 Years to 10 Years (Tax Saver Deposit)", "5.75%"}
            ));
        }});

        put("North East Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7-29 Days", "3.50%"},
                    new String[]{"30-45 Days", "4.25%"},
                    new String[]{"46 Days to 3 Months", "4.75%"},
                    new String[]{"3 Months 1 Day to 6 Months", "6.25%"},
                    new String[]{"6 Months 1 Day to 1 Year", "7.00%"},
                    new String[]{"1 Year 1 Day to 1.5 Years", "8.75%"},
                    new String[]{"1.5 Years 1 Day to 3 Years", "9.00%"},
                    new String[]{"3 Years 1 Day to 5 Years", "8.00%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7-29 Days", "4.00%"},
                    new String[]{"30-45 Days", "4.75%"},
                    new String[]{"46 Days to 3 Months", "5.25%"},
                    new String[]{"3 Months 1 Day to 6 Months", "6.75%"},
                    new String[]{"6 Months 1 Day to 1 Year", "7.50%"},
                    new String[]{"1 Year 1 Day to 1.5 Years", "8.75%"},
                    new String[]{"1.5 Years 1 Day to 3 Years", "9.00%"},
                    new String[]{"3 Years 1 Day to 5 Years", "8.50%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.75%"}
            ));
        }});

        put("Punjab & Sind Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 120 Days", "4.50%"},
                    new String[]{"121 Days to 150 Days", "4.50%"},
                    new String[]{"151 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 364 Days", "5.25%"},
                    new String[]{"1 Year", "6.30%"},
                    new String[]{"Above 1 Year to 374 Days", "6.00%"},
                    new String[]{"375 Days", "7.25%"},
                    new String[]{"375 Days to 443 Days", "6.00%"},
                    new String[]{"444 Days", "7.10%"},
                    new String[]{"445 Days to Less Than 22 Months", "6.00%"},
                    new String[]{"22 Months (PSB Green Earth)", "6.10%"},
                    new String[]{"Above 22 Months to Less Than 2 Years", "6.00%"},
                    new String[]{"2 Years to 776 Days", "6.30%"},
                    new String[]{"777 Days", "6.50%"},
                    new String[]{"778 Days to 998 Days", "6.30%"},
                    new String[]{"999 Days", "6.35%"},
                    new String[]{"1000 Days to Less Than 3 Years", "6.30%"},
                    new String[]{"3 Years to Less Than 44 Months", "6.00%"},
                    new String[]{"44 Months (PSB Green Earth)", "6.10%"},
                    new String[]{"Above 44 Months to Less Than 5 Years", "6.00%"},
                    new String[]{"5 Years", "6.50%"},
                    new String[]{"Above 5 Years to Less Than 66 Months", "6.25%"},
                    new String[]{"66 Months (PSB Green Earth)", "6.35%"},
                    new String[]{"Above 66 Months to 10 Years", "6.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 120 Days", "4.50%"},
                    new String[]{"121 Days to 150 Days", "4.50%"},
                    new String[]{"151 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 364 Days", "5.75%"},
                    new String[]{"1 Year", "6.80%"},
                    new String[]{"Above 1 Year to 374 Days", "6.50%"},
                    new String[]{"375 Days", "7.75%"},
                    new String[]{"375 Days to 443 Days", "6.50%"},
                    new String[]{"444 Days", "7.60%"},
                    new String[]{"445 Days to Less Than 22 Months", "6.50%"},
                    new String[]{"22 Months (PSB Green Earth)", "6.60%"},
                    new String[]{"Above 22 Months to Less Than 2 Years", "6.50%"},
                    new String[]{"2 Years to 776 Days", "6.80%"},
                    new String[]{"777 Days", "7.00%"},
                    new String[]{"778 Days to 998 Days", "6.80%"},
                    new String[]{"999 Days", "6.85%"},
                    new String[]{"1000 Days to Less Than 3 Years", "6.80%"},
                    new String[]{"3 Years to Less Than 44 Months", "6.50%"},
                    new String[]{"44 Months (PSB Green Earth)", "6.60%"},
                    new String[]{"Above 44 Months to Less Than 5 Years", "6.50%"},
                    new String[]{"5 Years", "7.00%"},
                    new String[]{"Above 5 Years to Less Than 66 Months", "6.75%"},
                    new String[]{"66 Months (PSB Green Earth)", "6.85%"},
                    new String[]{"Above 66 Months to 10 Years", "6.75%"}
            ));
        }});

        put("Punjab National Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 29 Days", "3.50%"},
                    new String[]{"30 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 179 Days", "5.50%"},
                    new String[]{"180 Days to 270 Days", "6.25%"},
                    new String[]{"271 Days to 299 Days", "6.50%"},
                    new String[]{"300 Days", "6.50%"},
                    new String[]{"301 Days to 302 Days", "6.50%"},
                    new String[]{"303 Days", "6.40%"},
                    new String[]{"304 Days to Less Than 1 Year", "6.50%"},
                    new String[]{"1 Year", "6.80%"},
                    new String[]{"Above 1 Year to 389 Days", "6.80%"},
                    new String[]{"390 Days", "7.10%"},
                    new String[]{"391 Days to 399 Days", "6.80%"},
                    new String[]{"400 Days", "6.80%"},
                    new String[]{"401 Days to 505 Days", "6.80%"},
                    new String[]{"506 Days", "6.70%"},
                    new String[]{"507 Days to 2 Years", "6.80%"},
                    new String[]{"Above 2 Years to 3 Years", "6.75%"},
                    new String[]{"Above 3 Years to 1203 Days", "6.25%"},
                    new String[]{"1204 Days", "6.15%"},
                    new String[]{"1205 Days to 5 Years", "6.25%"},
                    new String[]{"Above 5 Years to 1894 Days", "6.00%"},
                    new String[]{"1895 Days", "5.85%"},
                    new String[]{"1896 Days to 10 Years", "6.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 29 Days", "4.00%"},
                    new String[]{"30 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 270 Days", "6.75%"},
                    new String[]{"271 Days to 299 Days", "7.00%"},
                    new String[]{"300 Days", "7.00%"},
                    new String[]{"301 Days to 302 Days", "7.00%"},
                    new String[]{"303 Days", "6.90%"},
                    new String[]{"304 Days to Less Than 1 Year", "7.00%"},
                    new String[]{"1 Year", "7.30%"},
                    new String[]{"Above 1 Year to 389 Days", "7.30%"},
                    new String[]{"390 Days", "7.60%"},
                    new String[]{"391 Days to 399 Days", "7.30%"},
                    new String[]{"400 Days", "7.30%"},
                    new String[]{"401 Days to 505 Days", "7.30%"},
                    new String[]{"506 Days", "7.20%"},
                    new String[]{"507 Days to 2 Years", "7.30%"},
                    new String[]{"Above 2 Years to 3 Years", "7.25%"},
                    new String[]{"Above 3 Years to 1203 Days", "6.75%"},
                    new String[]{"1204 Days", "6.65%"},
                    new String[]{"1205 Days to 5 Years", "6.75%"},
                    new String[]{"Above 5 Years to 1894 Days", "6.80%"},
                    new String[]{"1895 Days", "6.65%"},
                    new String[]{"1896 Days to 10 Years", "6.80%"}
            ));
        }});

        put("RBL Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 180 Days", "4.75%"},
                    new String[]{"181 Days to 240 Days", "5.50%"},
                    new String[]{"241 Days to Less Than 1 Year", "6.05%"},
                    new String[]{"1 Year to Less Than 15 Months", "7.50%"},
                    new String[]{"15 Months to 16 Months 14 Days", "7.50%"},
                    new String[]{"500 Days", "7.75%"},
                    new String[]{"16 Months 16 Days to Less Than 18 Months", "7.50%"},
                    new String[]{"18 Months to 2 Years", "7.50%"},
                    new String[]{"2 Years 1 Day to 3 Years", "7.50%"},
                    new String[]{"3 Years 1 Day to 5 Years 1 Day", "7.10%"},
                    new String[]{"5 Years 2 Days to 10 Years", "7.00%"},
                    new String[]{"Tax Savings Fixed Deposits (5 Years)", "7.10%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 45 Days", "4.50%"},
                    new String[]{"46 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 180 Days", "5.25%"},
                    new String[]{"181 Days to 240 Days", "6.00%"},
                    new String[]{"241 Days to Less Than 1 Year", "6.55%"},
                    new String[]{"1 Year to Less Than 15 Months", "8.00%"},
                    new String[]{"15 Months to 16 Months 14 Days", "8.00%"},
                    new String[]{"500 Days", "8.25%"},
                    new String[]{"16 Months 16 Days to Less Than 18 Months", "8.00%"},
                    new String[]{"18 Months to 2 Years", "8.00%"},
                    new String[]{"2 Years 1 Day to 3 Years", "8.00%"},
                    new String[]{"3 Years 1 Day to 5 Years 1 Day", "7.60%"},
                    new String[]{"5 Years 2 Days to 10 Years", "7.50%"},
                    new String[]{"Tax Savings Fixed Deposits (5 Years)", "7.60%"}
            ));
        }});

        put("South Indian Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 30 Days", "2.90%"},
                    new String[]{"31 Days to 90 Days", "3.50%"},
                    new String[]{"91 Days to 99 Days", "4.50%"},
                    new String[]{"100 Days", "5.50%"},
                    new String[]{"101 Days to 180 Days", "4.50%"},
                    new String[]{"181 Days to Less Than 1 Year", "5.00%"},
                    new String[]{"1 Year", "6.70%"},
                    new String[]{"1 Year 1 Day", "7.00%"},
                    new String[]{"1 Year 2 Days to 399 Days", "6.50%"},
                    new String[]{"400 Days", "7.25%"},
                    new String[]{"401 Days to Less Than 2.5 Years", "6.50%"},
                    new String[]{"2.5 Years (30 Months)", "7.00%"},
                    new String[]{"Above 2.5 Years to Less Than 5 Years", "6.70%"},
                    new String[]{"5 Years to Less Than 5.5 Years", "6.00%"},
                    new String[]{"5.5 Years (66 Months) - Green Deposit", "6.50%"},
                    new String[]{"Above 5.5 Years to 10 Years", "6.00%"},
                    new String[]{"Tax Gain (5 Years)", "6.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 30 Days", "3.40%"},
                    new String[]{"31 Days to 90 Days", "4.00%"},
                    new String[]{"91 Days to 99 Days", "5.00%"},
                    new String[]{"100 Days", "6.00%"},
                    new String[]{"101 Days to 180 Days", "5.00%"},
                    new String[]{"181 Days to Less Than 1 Year", "5.50%"},
                    new String[]{"1 Year", "7.20%"},
                    new String[]{"1 Year 1 Day", "7.50%"},
                    new String[]{"1 Year 2 Days to 399 Days", "7.00%"},
                    new String[]{"400 Days", "7.75%"},
                    new String[]{"401 Days to Less Than 2.5 Years", "7.00%"},
                    new String[]{"2.5 Years (30 Months)", "7.50%"},
                    new String[]{"Above 2.5 Years to Less Than 5 Years", "7.20%"},
                    new String[]{"5 Years to Less Than 5.5 Years", "6.50%"},
                    new String[]{"5.5 Years (66 Months) - Green Deposit", "7.00%"},
                    new String[]{"Above 5.5 Years to 10 Years", "6.50%"},
                    new String[]{"Tax Gain (5 Years)", "6.50%"}
            ));
        }});

        put("Shivalik Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 29 Days", "3.75%"},
                    new String[]{"30 Days to 90 Days", "4.25%"},
                    new String[]{"91 Days to 180 Days", "4.75%"},
                    new String[]{"6 Months to Less Than 9 Months", "6.00%"},
                    new String[]{"9 Months to 12 Months", "6.00%"},
                    new String[]{"12 Months 1 Day to Less Than 18 Months", "8.30%"},
                    new String[]{"18 Months to 2 Years", "8.30%"},
                    new String[]{"2 Years 1 Day to 3 Years", "7.50%"},
                    new String[]{"3 Years 1 Day to 5 Years", "6.50%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.25%"},
                    new String[]{"Tax Saver FD (5 Years)", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 29 Days", "4.25%"},
                    new String[]{"30 Days to 90 Days", "4.75%"},
                    new String[]{"91 Days to 180 Days", "5.25%"},
                    new String[]{"6 Months to Less Than 9 Months", "6.50%"},
                    new String[]{"9 Months to 12 Months", "6.50%"},
                    new String[]{"12 Months 1 Day to Less Than 18 Months", "8.80%"},
                    new String[]{"18 Months to 2 Years", "8.80%"},
                    new String[]{"2 Years 1 Day to 3 Years", "8.00%"},
                    new String[]{"3 Years 1 Day to 5 Years", "7.00%"},
                    new String[]{"5 Years 1 Day to 10 Years", "6.75%"},
                    new String[]{"Tax Saver FD (5 Years)", "7.00%"}
            ));
        }});

        put("State Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 179 Days", "5.50%"},
                    new String[]{"180 Days to 210 Days", "6.25%"},
                    new String[]{"211 Days to Less Than 1 Year", "6.50%"},
                    new String[]{"1 Year to Less Than 2 Years", "6.80%"},
                    new String[]{"2 Years to Less Than 3 Years", "7.00%"},
                    new String[]{"3 Years to Less Than 5 Years", "6.75%"},
                    new String[]{"5 Years and up to 10 Years", "6.50%"},
                    new String[]{"5 years (SBI Tax Savings Scheme)", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 210 Days", "6.75%"},
                    new String[]{"211 Days to Less Than 1 Year", "7.00%"},
                    new String[]{"1 Year to Less Than 2 Years", "7.30%"},
                    new String[]{"2 Years to Less Than 3 Years", "7.50%"},
                    new String[]{"3 Years to Less Than 5 Years", "7.25%"},
                    new String[]{"5 Years and up to 10 Years", "7.50%"},
                    new String[]{"5 years (SBI Tax Savings Scheme)", "7.50%"}
            ));
        }});

        put("Suryoday Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 45 Days", "4.25%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 6 Months", "5.00%"},
                    new String[]{"6 Months 1 Day", "7.25%"},
                    new String[]{"Above 6 Months 1 Day to 9 Months", "5.50%"},
                    new String[]{"Above 9 Months to Less Than 1 Year", "6.00%"},
                    new String[]{"1 Year", "8.25%"},
                    new String[]{"Above 1 Year to 15 Months", "8.25%"},
                    new String[]{"Above 15 Months to 2 Years", "8.25%"},
                    new String[]{"Above 2 Years to 3 Years", "8.25%"},
                    new String[]{"Above 3 Years to Less Than 5 Years", "6.75%"},
                    new String[]{"5 Years", "8.60%"},
                    new String[]{"Above 5 Years to 10 Years", "7.25%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.50%"},
                    new String[]{"15 Days to 45 Days", "4.75%"},
                    new String[]{"46 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 6 Months", "5.50%"},
                    new String[]{"6 Months 1 Day", "7.75%"},
                    new String[]{"Above 6 Months 1 Day to 9 Months", "6.00%"},
                    new String[]{"Above 9 Months to Less Than 1 Year", "6.50%"},
                    new String[]{"1 Year", "8.75%"},
                    new String[]{"Above 1 Year to 15 Months", "8.75%"},
                    new String[]{"Above 15 Months to 2 Years", "8.75%"},
                    new String[]{"Above 2 Years to 3 Years", "8.75%"},
                    new String[]{"Above 3 Years to Less Than 5 Years", "7.25%"},
                    new String[]{"5 Years", "9.10%"},
                    new String[]{"Above 5 Years to 10 Years", "7.75%"}
            ));
        }});

        put("Tamilnad Mercantile Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "5.25%"},
                    new String[]{"15 Days to 29 Days", "5.25%"},
                    new String[]{"30 Days to 45 Days", "5.25%"},
                    new String[]{"46 Days to 60 Days", "5.25%"},
                    new String[]{"61 Days to 90 Days", "5.25%"},
                    new String[]{"91 Days to 120 Days", "5.75%"},
                    new String[]{"121 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 270 Days", "6.00%"},
                    new String[]{"271 Days to Less Than 1 Year", "6.00%"},
                    new String[]{"1 Year to Less Than 400 Days", "7.00%"},
                    new String[]{"400 Days", "7.50%"},
                    new String[]{"Above 400 Days to Less Than 20 Months 20 Days", "7.00%"},
                    new String[]{"20 Months 20 Days", "7.00%"},
                    new String[]{"Above 20 Months 20 Days to Less Than 2 Years", "7.00%"},
                    new String[]{"2 Years to Less Than 3 Years", "6.75%"},
                    new String[]{"3 Years to 10 Years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "5.25%"},
                    new String[]{"15 Days to 29 Days", "5.25%"},
                    new String[]{"30 Days to 45 Days", "5.25%"},
                    new String[]{"46 Days to 60 Days", "5.25%"},
                    new String[]{"61 Days to 90 Days", "5.25%"},
                    new String[]{"91 Days to 120 Days", "5.75%"},
                    new String[]{"121 Days to 179 Days", "6.00%"},
                    new String[]{"180 Days to 270 Days", "6.00%"},
                    new String[]{"271 Days to Less Than 1 Year", "6.00%"},
                    new String[]{"1 Year to Less Than 400 Days", "7.50%"},
                    new String[]{"400 Days", "8.00%"},
                    new String[]{"Above 400 Days to Less Than 20 Months 20 Days", "7.50%"},
                    new String[]{"20 Months 20 Days", "7.50%"},
                    new String[]{"Above 20 Months 20 Days to Less Than 2 Years", "7.50%"},
                    new String[]{"2 Years to Less Than 3 Years", "7.25%"},
                    new String[]{"3 Years to 10 Years", "7.00%"}
            ));
        }});

        put("Union Bank of India", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.50%"},
                    new String[]{"15 Days to 30 Days", "3.50%"},
                    new String[]{"31 Days to 45 Days", "3.50%"},
                    new String[]{"46 Days to 90 Days", "4.50%"},
                    new String[]{"91 Days to 120 Days", "4.80%"},
                    new String[]{"121 Days to 180 Days", "5.00%"},
                    new String[]{"181 Days to Less Than 1 Year", "6.25%"},
                    new String[]{"1 Year", "6.75%"},
                    new String[]{"Above 1 Year to 398 Days", "6.75%"},
                    new String[]{"399 Days", "6.75%"},
                    new String[]{"400 Days", "6.90%"},
                    new String[]{"401 Days to 455 Days", "6.60%"},
                    new String[]{"456 Days", "7.15%"},
                    new String[]{"457 Days to 2 Years", "6.60%"},
                    new String[]{"Above 2 Years to 996 Days", "6.60%"},
                    new String[]{"997 Days", "6.40%"},
                    new String[]{"Above 998 Days to Less Than 3 Years", "6.60%"},
                    new String[]{"3 Years", "6.70%"},
                    new String[]{"Above 3 Years to 5 Years", "6.50%"},
                    new String[]{"Above 5 Years to 10 Years", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.00%"},
                    new String[]{"15 Days to 30 Days", "4.00%"},
                    new String[]{"31 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 120 Days", "5.30%"},
                    new String[]{"121 Days to 180 Days", "5.50%"},
                    new String[]{"181 Days to Less Than 1 Year", "6.75%"},
                    new String[]{"1 Year", "7.25%"},
                    new String[]{"Above 1 Year to 398 Days", "7.25%"},
                    new String[]{"399 Days", "7.25%"},
                    new String[]{"400 Days", "7.40%"},
                    new String[]{"401 Days to 455 Days", "7.10%"},
                    new String[]{"456 Days", "7.65%"},
                    new String[]{"457 Days to 2 Years", "7.10%"},
                    new String[]{"Above 2 Years to 996 Days", "7.10%"},
                    new String[]{"997 Days", "6.90%"},
                    new String[]{"Above 998 Days to Less Than 3 Years", "7.10%"},
                    new String[]{"3 Years", "7.20%"},
                    new String[]{"Above 3 Years to 5 Years", "7.00%"},
                    new String[]{"Above 5 Years to 10 Years", "7.00%"}
            ));
        }});

        put("UCO Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "2.90%"},
                    new String[]{"15 Days to 29 Days", "2.90%"},
                    new String[]{"30 Days to 45 Days", "3.00%"},
                    new String[]{"46 Days to 60 Days", "3.50%"},
                    new String[]{"61 Days to 90 Days", "3.50%"},
                    new String[]{"91 Days to 120 Days", "4.50%"},
                    new String[]{"121 Days to 150 Days", "4.50%"},
                    new String[]{"151 Days to 180 Days", "5.00%"},
                    new String[]{"181 Days to 332 Days", "5.50%"},
                    new String[]{"333 Days", "7.30%"},
                    new String[]{"334 Days to 364 Days", "5.50%"},
                    new String[]{"1 Year", "6.50%"},
                    new String[]{"Above 1 Year to 399 Days", "6.50%"},
                    new String[]{"400 Days", "7.05%"},
                    new String[]{"401 Days to 443 Days", "6.50%"},
                    new String[]{"444 Days", "7.30%"},
                    new String[]{"445 Days to 2 Years", "6.50%"},
                    new String[]{"Above 2 Years to 3 Years", "6.30%"},
                    new String[]{"Above 3 Years to 5 Years", "6.20%"},
                    new String[]{"Above 5 Years", "6.10%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.15%"},
                    new String[]{"15 Days to 29 Days", "3.15%"},
                    new String[]{"30 Days to 45 Days", "3.25%"},
                    new String[]{"46 Days to 60 Days", "3.75%"},
                    new String[]{"61 Days to 90 Days", "3.75%"},
                    new String[]{"91 Days to 120 Days", "4.75%"},
                    new String[]{"121 Days to 150 Days", "4.75%"},
                    new String[]{"151 Days to 180 Days", "5.25%"},
                    new String[]{"181 Days to 332 Days", "5.75%"},
                    new String[]{"333 Days", "7.55%"},
                    new String[]{"334 Days to 364 Days", "5.75%"},
                    new String[]{"1 Year", "6.75%"},
                    new String[]{"Above 1 Year to 399 Days", "7.00%"},
                    new String[]{"400 Days", "7.55%"},
                    new String[]{"401 Days to 443 Days", "7.00%"},
                    new String[]{"444 Days", "7.80%"},
                    new String[]{"445 Days to 2 Years", "7.00%"},
                    new String[]{"Above 2 Years to 3 Years", "6.80%"},
                    new String[]{"Above 3 Years to 5 Years", "6.70%"},
                    new String[]{"Above 5 Years", "6.60%"}
            ));
        }});

        put("Ujjivan Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 29 Days", "3.75%"},
                    new String[]{"30 Days to 89 Days", "4.25%"},
                    new String[]{"90 Days to 180 Days", "4.75%"},
                    new String[]{"6 Months to less than 12 Months", "7.00%"},
                    new String[]{"12 Months to less than 18 Months", "7.90%"},
                    new String[]{"18 Months", "8.05%"},
                    new String[]{"18 Months 1 Day to 990 Days", "7.75%"},
                    new String[]{"991 Days to 60 Months", "7.20%"},
                    new String[]{"60 Months 1 Day to 120 Months", "6.50%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 29 Days", "4.25%"},
                    new String[]{"30 Days to 89 Days", "4.75%"},
                    new String[]{"90 Days to 180 Days", "5.25%"},
                    new String[]{"6 Months to less than 12 Months", "7.50%"},
                    new String[]{"12 Months to less than 18 Months", "8.40%"},
                    new String[]{"18 Months", "8.55%"},
                    new String[]{"18 Months 1 Day to 990 Days", "8.25%"},
                    new String[]{"991 Days to 60 Months", "7.70%"},
                    new String[]{"60 Months 1 Day to 120 Months", "7.00%"}
            ));
        }});

        put("Unity Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.50%"},
                    new String[]{"15 Days to 45 Days", "4.75%"},
                    new String[]{"46 Days to 60 Days", "5.75%"},
                    new String[]{"61 Days to 90 Days", "6.00%"},
                    new String[]{"91 Days to 164 Days", "6.25%"},
                    new String[]{"165 Days to 6 Months", "6.25%"},
                    new String[]{"More than 6 Months to 201 Days", "7.00%"},
                    new String[]{"202 Days to 364 Days", "7.00%"},
                    new String[]{"1 Year", "7.25%"},
                    new String[]{"1 Year 1 Day", "7.50%"},
                    new String[]{"More than 1 Year 1 Day to 500 Days", "7.50%"},
                    new String[]{"501 Days", "8.00%"},
                    new String[]{"502 Days to 18 Months", "7.75%"},
                    new String[]{"More than 18 Months to 700 Days", "7.75%"},
                    new String[]{"701 Days", "8.25%"},
                    new String[]{"702 Days to 1000 Days", "7.75%"},
                    new String[]{"1001 Days", "8.60%"},
                    new String[]{"1002 Days to 3 Years", "8.15%"},
                    new String[]{"More than 3 Years to 5 Years", "8.15%"},
                    new String[]{"More than 5 Years to 10 Years", "7.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "4.50%"},
                    new String[]{"15 Days to 45 Days", "4.75%"},
                    new String[]{"46 Days to 60 Days", "6.25%"},
                    new String[]{"61 Days to 90 Days", "6.50%"},
                    new String[]{"91 Days to 164 Days", "6.75%"},
                    new String[]{"165 Days to 6 Months", "6.75%"},
                    new String[]{"More than 6 Months to 201 Days", "7.50%"},
                    new String[]{"202 Days to 364 Days", "7.50%"},
                    new String[]{"1 Year", "7.75%"},
                    new String[]{"1 Year 1 Day", "8.00%"},
                    new String[]{"More than 1 Year 1 Day to 500 Days", "8.00%"},
                    new String[]{"501 Days", "8.50%"},
                    new String[]{"502 Days to 18 Months", "8.25%"},
                    new String[]{"More than 18 Months to 700 Days", "8.25%"},
                    new String[]{"701 Days", "8.75%"},
                    new String[]{"702 Days to 1000 Days", "8.25%"},
                    new String[]{"1001 Days", "9.10%"},
                    new String[]{"1002 Days to 3 Years", "8.65%"},
                    new String[]{"More than 3 Years to 5 Years", "8.65%"},
                    new String[]{"More than 5 Years to 10 Years", "7.50%"}
            ));
        }});

        put("YES Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.25%"},
                    new String[]{"15 Days to 45 Days", "3.70%"},
                    new String[]{"46 Days to 90 Days", "5.00%"},
                    new String[]{"91 Days to 120 Days", "5.00%"},
                    new String[]{"121 Days to 180 Days", "5.00%"},
                    new String[]{"181 Days to 271 Days", "6.25%"},
                    new String[]{"272 Days to less than 12 Months", "6.50%"},
                    new String[]{"12 Months", "7.00%"},
                    new String[]{"12 Months 1 Day to less than 36 Months", "7.50%"},
                    new String[]{"36 Months to less than 60 Months", "7.50%"},
                    new String[]{"60 Months", "7.50%"},
                    new String[]{"60 Months 1 Day to upto 120 Months", "7.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 14 Days", "3.75%"},
                    new String[]{"15 Days to 45 Days", "4.20%"},
                    new String[]{"46 Days to 90 Days", "5.50%"},
                    new String[]{"91 Days to 120 Days", "5.50%"},
                    new String[]{"121 Days to 180 Days", "5.50%"},
                    new String[]{"181 Days to 271 Days", "6.75%"},
                    new String[]{"272 Days to less than 12 Months", "7.00%"},
                    new String[]{"12 Months", "7.50%"},
                    new String[]{"12 Months 1 Day to less than 36 Months", "8.00%"},
                    new String[]{"36 Months to less than 60 Months", "8.25%"},
                    new String[]{"60 Months", "8.25%"},
                    new String[]{"60 Months 1 Day to upto 120 Months", "7.75%"}
            ));
        }});

        put("Utkarsh Small Finance Bank", new HashMap<String, List<String[]>>() {{
            put("Adult (Below 60 Years)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "4.00%"},
                    new String[]{"46 Days to 90 Days", "4.75%"},
                    new String[]{"91 Days to 180 Days", "5.50%"},
                    new String[]{"181 Days to 364 Days", "6.50%"},
                    new String[]{"365 Days to 699 Days", "8.00%"},
                    new String[]{"700 Days to less than 2 Years", "8.25%"},
                    new String[]{"2 Years to 3 Years", "8.50%"},
                    new String[]{"3 Years to less than 4 Years", "8.25%"},
                    new String[]{"4 Years to 5 Years", "7.50%"},
                    new String[]{"5 Years to 10 Years", "7.00%"}
            ));

            put("Senior Citizen (60 Years and Above)", Arrays.asList(
                    new String[]{"7 Days to 45 Days", "4.60%"},
                    new String[]{"46 Days to 90 Days", "5.35%"},
                    new String[]{"91 Days to 180 Days", "6.10%"},
                    new String[]{"181 Days to 364 Days", "7.10%"},
                    new String[]{"365 Days to 699 Days", "8.60%"},
                    new String[]{"700 Days to less than 2 Years", "8.85%"},
                    new String[]{"2 Years to 3 Years", "9.10%"},
                    new String[]{"3 Years to less than 4 Years", "8.85%"},
                    new String[]{"4 Years to 5 Years", "8.10%"},
                    new String[]{"5 Years to 10 Years", "7.60%"}
            ));
        }});

    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_bank_interest_rate, container, false);

        // Bind UI elements
        calculateButton = view.findViewById(R.id.calculateButton);
        buttonClear = view.findViewById(R.id.buttonClear);
        ageGroupDropdown = view.findViewById(R.id.ageGroupDropdown);
        selectBankDropdown = view.findViewById(R.id.selectBankDropdown);
        tableLayout = view.findViewById(R.id.tableLayout);
        bankName = view.findViewById(R.id.bankName);
        ageGroup = view.findViewById(R.id.selectedAgeGroup);
        resultBox = view.findViewById(R.id.resultBox);

        // Set up button click listener for 'Calculate'
        calculateButton.setOnClickListener(v -> {
            resultBox.setVisibility(View.VISIBLE);
            updateFDTable();
        });

        // Set up buttonClear functionality
        buttonClear.setOnClickListener(v -> resetDropdownsAndTable());

        return view;
    }

    private void updateFDTable() {
        tableLayout.removeAllViews();

        // Add the heading row
        TableRow headingRow = new TableRow(getActivity());
        headingRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView daysHeadingTextView = new TextView(getActivity());
        daysHeadingTextView.setText("Days");
        daysHeadingTextView.setPadding(16, 16, 16, 16);
        daysHeadingTextView.setTextSize(18);
        daysHeadingTextView.setGravity(Gravity.CENTER);
        daysHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        daysHeadingTextView.setTypeface(null, Typeface.BOLD);
        daysHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        daysHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        TextView annualRateHeadingTextView = new TextView(getActivity());
        annualRateHeadingTextView.setText("Annual Interest (%)");
        annualRateHeadingTextView.setPadding(16, 16, 16, 16);
        annualRateHeadingTextView.setTextSize(18);
        annualRateHeadingTextView.setGravity(Gravity.CENTER);
        annualRateHeadingTextView.setTextColor(getResources().getColor(android.R.color.black));
        annualRateHeadingTextView.setTypeface(null, Typeface.BOLD);
        annualRateHeadingTextView.setBackgroundResource(R.drawable.cell_border);
        annualRateHeadingTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

        headingRow.addView(daysHeadingTextView);
        headingRow.addView(annualRateHeadingTextView);
        tableLayout.addView(headingRow);

        // Get selected bank and age group
        String selectedBank = selectBankDropdown.getSelectedItem().toString();
        String selectedAgeGroup = ageGroupDropdown.getSelectedItem().toString();

        bankName.setText(selectedBank);
        ageGroup.setText(selectedAgeGroup);

        // Get FD rates based on bank and age group
        List<String[]> selectedRates = fdRatesMap.getOrDefault(selectedBank, new HashMap<>())
                .getOrDefault(selectedAgeGroup, new ArrayList<>());

        // Add FD Rates dynamically to the table
        for (String[] rate : selectedRates) {
            TableRow row = new TableRow(getActivity());
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView daysTextView = new TextView(getActivity());
            daysTextView.setText(rate[0]);
            daysTextView.setPadding(16, 16, 16, 16);
            daysTextView.setTextSize(16);
            daysTextView.setGravity(Gravity.CENTER);
            daysTextView.setBackgroundResource(R.drawable.cell_border);
            daysTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            TextView annualRateTextView = new TextView(getActivity());
            annualRateTextView.setText(rate[1]);
            annualRateTextView.setPadding(16, 16, 16, 16);
            annualRateTextView.setTextSize(16);
            annualRateTextView.setGravity(Gravity.CENTER);
            annualRateTextView.setBackgroundResource(R.drawable.cell_border);
            annualRateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));

            row.addView(daysTextView);
            row.addView(annualRateTextView);
            tableLayout.addView(row);
        }
    }

    private void resetDropdownsAndTable() {
        selectBankDropdown.setSelection(0);
        ageGroupDropdown.setSelection(0);
        resultBox.setVisibility(View.GONE);
        tableLayout.removeAllViews();
    }
}
