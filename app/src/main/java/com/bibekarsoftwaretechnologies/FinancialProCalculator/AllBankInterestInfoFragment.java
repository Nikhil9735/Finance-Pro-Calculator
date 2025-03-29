package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bibekarsoftwaretechnologies.FinancialProCalculator.R;

public class AllBankInterestInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_bank_interest_info, container, false);

        // Initialize TextViews
        TextView publicSectorBanks = view.findViewById(R.id.publicSectorBanks);
        TextView privateSectorBanks = view.findViewById(R.id.privateSectorBanks);
        TextView smallFinanceBanks = view.findViewById(R.id.smallFinanceBanks);

        // Set data with styled bank names
        publicSectorBanks.setText(getFormattedPublicSectorBanks());
        privateSectorBanks.setText(getFormattedPrivateSectorBanks());
        smallFinanceBanks.setText(getFormattedSmallFinanceBanks());

        return view;
    }

    private CharSequence getFormattedPublicSectorBanks() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addBank(builder, "Bank of Baroda", " (includes Vijaya Bank and Dena Bank after the merger)");
        addBank(builder, "Bank of India", "");
        addBank(builder, "Bank of Maharashtra", "");
        addBank(builder, "Canara Bank", " (incorporates Syndicate Bank following the merger)");
        addBank(builder, "Central Bank of India", "");
        addBank(builder, "Indian Bank", " (merged with Allahabad Bank)");
        addBank(builder, "Indian Overseas Bank", "");
        addBank(builder, "Punjab & Sind Bank", "");
        addBank(builder, "Punjab National Bank", " (now includes Oriental Bank of Commerce and United Bank of India post-merger)");
        addBank(builder, "State Bank of India", " (integrates State Bank of Bikaner & Jaipur, State Bank of Hyderabad, State Bank of Indore, State Bank of Mysore, State Bank of Patiala, State Bank of Saurashtra, State Bank of Travancore, and Bhartiya Mahila Bank)");
        addBank(builder, "UCO Bank", "");
        addBank(builder, "Union Bank of India", "");
        return builder;
    }

    private CharSequence getFormattedPrivateSectorBanks() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addBank(builder, "Axis Bank", "");
        addBank(builder, "Bandhan Bank", "");
        addBank(builder, "City Union Bank", "");
        addBank(builder, "CSB Bank", "");
        addBank(builder, "DCB Bank", "");
        addBank(builder, "Dhanlaxmi Bank", "");
        addBank(builder, "Federal Bank", "");
        addBank(builder, "HDFC Bank", "");
        addBank(builder, "ICICI Bank", "");
        addBank(builder, "IDBI Bank", "");
        addBank(builder, "IDFC FIRST Bank", "");
        addBank(builder, "IndusInd Bank", "");
        addBank(builder, "Jammu & Kashmir Bank", "");
        addBank(builder, "Karnataka Bank", "");
        addBank(builder, "Karur Vysya Bank", "");
        addBank(builder, "Kotak Mahindra Bank", "");
        addBank(builder, "Nainital Bank", "");
        addBank(builder, "RBL Bank", "");
        addBank(builder, "South Indian Bank", "");
        addBank(builder, "Tamilnad Mercantile Bank", "");
        addBank(builder, "YES Bank", "");
        return builder;
    }

    private CharSequence getFormattedSmallFinanceBanks() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addBank(builder, "AU Small Finance Bank", " (now includes Fincare Small Finance Bank)");
        addBank(builder, "Capital Small Finance Bank", "");
        addBank(builder, "Equitas Small Finance Bank", "");
        addBank(builder, "ESAF Small Finance Bank", "");
        addBank(builder, "Jana Small Finance Bank", "");
        addBank(builder, "North East Small Finance Bank", "");
        addBank(builder, "Shivalik Small Finance Bank", "");
        addBank(builder, "Suryoday Small Finance Bank", "");
        addBank(builder, "Ujjivan Small Finance Bank", "");
        addBank(builder, "Unity Small Finance Bank", "");
        addBank(builder, "Utkarsh Small Finance Bank", "");
        return builder;
    }

    private void addBank(SpannableStringBuilder builder, String bankName, String details) {
        int start = builder.length();
        builder.append("• ").append(bankName);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, start + bankName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(details).append("\n");
    }
}
