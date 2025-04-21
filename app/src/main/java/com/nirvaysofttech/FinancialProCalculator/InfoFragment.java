package com.nirvaysofttech.FinancialProCalculator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.nirvaysofttech.FinancialProCalculator.R;

public class InfoFragment extends Fragment {

    private TextView infoHeading;
    private LinearLayout operationLayout;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize the layout
        infoHeading = view.findViewById(R.id.infoHeading);
        operationLayout = view.findViewById(R.id.operationLayout);

        // Initialize ViewModel
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe operation changes
        mainViewModel.getOperation().observe(getViewLifecycleOwner(), operation -> {
            if (operation != null) {
                updateUIBasedOnOperation(operation, inflater);
            }
        });

        return view;
    }

    // Update UI elements based on the selected operation
    private void updateUIBasedOnOperation(String operation, LayoutInflater inflater) {
        // Clear any existing views
        operationLayout.removeAllViews();

        // Fetch the headings and descriptions for the selected operation
        String[] headings = getHeadings(operation);
        String[] descriptions = getDescriptions(operation);

        // Add buttons dynamically
        addButtons(inflater, operation, headings, descriptions);
    }

    // Add buttons dynamically to the layout
    private void addButtons(LayoutInflater inflater, String operation, String[] headings, String[] descriptions) {
        for (int i = 0; i < headings.length; i++) {
            View expandableButton = inflater.inflate(R.layout.expandable_button, null);
            setupExpandableButton(operation, expandableButton, headings[i], descriptions[i]);
            operationLayout.addView(expandableButton);
        }
    }

    // Method to setup each expandable button
    private void setupExpandableButton(String operation, View expandableButton, String heading, String description) {
        infoHeading.setText(operation);

        TextView buttonHeading = expandableButton.findViewById(R.id.buttonHeading);
        buttonHeading.setText(heading);

        LinearLayout descriptionLayout = expandableButton.findViewById(R.id.llDescription);
        descriptionLayout.setVisibility(View.GONE); // Initially hidden

        TextView descriptionView = expandableButton.findViewById(R.id.descriptionTextView);

        // Use HtmlCompat.fromHtml to parse and display HTML
        descriptionView.setText(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY));

        ImageView arrowDown = expandableButton.findViewById(R.id.arrowDown);
        ImageView arrowUp = expandableButton.findViewById(R.id.arrowUp);
        arrowUp.setVisibility(View.GONE); // Initially hidden

        // Add OnClickListener only to the header section
        RelativeLayout headerLayout = expandableButton.findViewById(R.id.infoBox2);

        headerLayout.setOnClickListener(v -> {
            // Toggle visibility of the description layout
            if (descriptionLayout.getVisibility() == View.GONE) {
                descriptionLayout.setVisibility(View.VISIBLE);
                arrowDown.setVisibility(View.GONE);
                arrowUp.setVisibility(View.VISIBLE);

                // Apply expanded styling
                Typeface expandedTypeface = ResourcesCompat.getFont(requireContext(), R.font.bold);
                buttonHeading.setTypeface(expandedTypeface);
                buttonHeading.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkGreen));
                buttonHeading.setTextSize(20);
            } else {
                descriptionLayout.setVisibility(View.GONE);
                arrowDown.setVisibility(View.VISIBLE);
                arrowUp.setVisibility(View.GONE);

                // Apply collapsed styling
                Typeface collapsedTypeface = ResourcesCompat.getFont(requireContext(), R.font.regular);

                // Resolve the primary text color from the current theme
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
                int color = typedValue.data;
                buttonHeading.setTextColor(color);

                buttonHeading.setTypeface(collapsedTypeface);
                buttonHeading.setTextSize(18);
            }
        });

        // Allow `llDescription` to be interactive without triggering the toggle
        descriptionLayout.setOnClickListener(null);
    }

    // Method to get headings for each operation
    private String[] getHeadings(String operation) {
        switch (operation) {
            case "Recurring Deposit (RD)":
                return new String[]{
                        "What is a Post Office RD?",
                        "How does a Post Office RD work?",
                        "What is the interest rate on Post Office RD?",
                        "What is the minimum and maximum deposit?",
                        "What is the tenure of Post Office RD?",
                        "Can I change the deposit amount?",
                        "Can I withdraw money before maturity?",
                        "What happens if I miss a monthly installment?",
                        "Is there a penalty for late payment?",
                        "How is interest calculated?",
                        "Can I open a Post Office RD account online?",
                        "What documents are required?",
                        "Can I open a joint Post Office RD account?",
                        "What happens after maturity?",
                        "Is Post Office RD a taxable scheme?",
                        "Can I transfer my Post Office RD to another branch?",
                        "What is the minimum period?",
                        "Can I nominate someone for my account?",
                        "Is it a safe investment?",
                        "How can I check my RD status?",
                        "Can I avail a loan against my RD?",
                        "Can I open an RD for a minor?",
                        "What happens if I close my RD early?",
                        "How is the interest rate decided?"
                };
            case "Time Deposit (TD)":
                return new String[]{
                        "What is Post Office TD?",
                        "How does Post Office TD work?",
                        "What is the interest rate?",
                        "Minimum deposit required?",
                        "What is the tenure for TD?",
                        "Can I break my TD early?",
                        "Loan against TD?",
                        "Is TD interest taxable?",
                        "Can I nominate someone?",
                        "How to open a TD account?",
                        "Can I transfer TD?",
                        "What if I miss a deposit?",
                        "Can I close TD early?",
                        "Is there a penalty for late deposit?",
                        "What are the benefits?",
                        "Can I change the deposit amount?",
                        "Is Post Office TD a good option?",
                        "Can I get monthly payouts?",
                        "Can I change the tenure?",
                        "What is the maturity procedure?"
                };
            case "Monthly Income Scheme (MIS)":
                return new String[]{
                        "What is MIS?",
                        "How does MIS work?",
                        "What is the MIS interest rate?",
                        "What is the minimum/maximum deposit?",
                        "What is the MIS tenure?",
                        "Can I close MIS early?",
                        "Is MIS interest taxable?",
                        "Can I nominate for MIS?",
                        "How to open an MIS account?",
                        "Can I transfer MIS to another post office?",
                        "What if I miss an MIS deposit?",
                        "Can I change the MIS nominee?",
                        "What are MIS benefits?",
                        "Can I change the deposit during MIS?",
                        "Is MIS safe?",
                        "Can I make lump-sum withdrawals?",
                        "Can I transfer MIS to someone else?",
                        "What happens at MIS maturity?"
                };
            case "National Savings Certificate (NSC)":
                return new String[]{
                        "What is NSC?",
                        "What is the tenure of NSC?",
                        "What is the current NSC interest rate?",
                        "What is the minimum and maximum NSC deposit?",
                        "Does NSC offer tax benefits?",
                        "Is NSC safe?",
                        "How can I purchase NSC?",
                        "Can I nominate someone for NSC?",
                        "Can NSC be transferred to another person?",
                        "Is early withdrawal allowed for NSC?",
                        "Can NSC be used as loan collateral?",
                        "How does interest work for NSC?",
                        "What happens at NSC maturity?",
                        "Can NSC be transferred between post offices?",
                        "Who should invest in NSC?"
                };
            default:
                return new String[0]; // Empty array for unrecognized operations
        }
    }

    // Method to get descriptions for each operation
    private String[] getDescriptions(String operation) {
        switch (operation) {
            case "Recurring Deposit (RD)":
                return new String[]{
                        "A Post Office <b>Recurring Deposit (RD)</b> is a savings scheme where individuals deposit a fixed amount monthly for a certain period, earning interest at a predetermined rate.<br><br> The maturity amount is received at the end of the term, which includes both the principal and the accumulated interest.<br><br> The interest earned on the deposit is compounded quarterly, ensuring that your investment grows steadily over time. <b>The interest rate remains fixed for the entire tenure of the deposit.</b>",
                        "You deposit a fixed amount each month in a Post Office RD account.<br><br> The total deposited amount and interest accrued are paid out at the end of the term.<br><br> The interest is compounded quarterly and is calculated on the balance at the end of each quarter. This means that the interest is added to the principal at regular intervals, which in turn earns more interest, making it a powerful savings tool for long-term growth. <b>It provides guaranteed returns on your savings.</b>",
                        "The interest rate on Post Office RD is typically updated quarterly by the government and is currently around <b>7.0%</b> (as of October 2024).<br><br> The rate is fixed for the entire term of the deposit, ensuring that the returns are predictable and secure. <b>The rate may vary depending on government monetary policy, but once the rate is set, it remains constant throughout the tenure of the RD.</b>",
                        "- <strong>Minimum deposit:</strong> ₹100 per month (and in multiples of ₹10).<br><br>- <strong>Maximum deposit:</strong> There is no upper limit.<br><br><b>This allows you to choose a deposit amount that fits your budget and savings goals.</b> You can increase or decrease the amount as needed, as long as it adheres to the rules of the scheme.",
                        "The tenure of a Post Office RD scheme is generally <b>5 years</b>.<br><br> However, it is possible to extend the RD after the initial 5 years for additional terms. You can continue saving and growing your investment by extending the RD for further periods of 5 years, thus allowing the power of compound interest to work over an even longer time frame.",
                        "Once you open an RD account, you cannot change the amount of your monthly deposit.<br><br> The amount must remain consistent throughout the term. However, you can choose to close the account or open a new RD if you wish to adjust your deposit amount. <b>The purpose of this rule is to ensure that the deposit remains stable and that interest calculations are consistent.</b>",
                        "Premature withdrawal is allowed in a Post Office RD account, but it comes with a penalty.<br><br> If you withdraw the funds before completing one year, the interest rate will be significantly reduced.<br><br> After the completion of one year, partial withdrawals are allowed, but the interest may still be reduced depending on how early you withdraw the funds. <b>The penalty helps to discourage early withdrawals.</b>",
                        "If you miss a monthly installment, your RD account will be considered a discontinued account.<br><br> However, you can regularize it by paying the arrears along with the interest.<br><br> If the payments are not made for more than 12 months, the account will be closed, and you will lose the benefits of interest for the months you missed. <b>It’s crucial to keep track of your payments.</b>",
                        "Yes, there is a penalty of ₹1 for every ₹100 deposited if you miss a monthly payment.<br><br> This penalty is deducted from the interest earned. <b>If you continue to miss payments or fail to regularize the account, the penalties will accumulate, reducing your final maturity amount.</b>",
                        "Interest on Post Office RD is compounded quarterly, and the rate is fixed at the time of account opening.<br><br> The accumulated amount with interest is paid at maturity. Interest is calculated on the basis of the balance at the end of each quarter and is added to the principal amount, helping to build wealth over time. <b>Compounding helps maximize your returns.</b>",
                        "Yes, you can open a Post Office RD account online through the India Post website or app.<br><br> Alternatively, you can visit your nearest post office. <b>Opening an account online is easy and requires the same documentation as in-person registration.</b> The process typically includes verifying your identity and making the initial deposit through bank transfer or other payment options.",
                        "To open an RD account, you will need valid identity proof, address proof, and passport-sized photographs.<br><br> Documents like Aadhar card, voter ID, or PAN card are generally accepted as identity proof, while utility bills, Aadhar card, or ration cards can be used as address proof. <b>Be sure to have these documents ready when opening the account.</b>",
                        "Yes, you can open a joint Post Office RD account with up to two account holders.<br><br> The interest earned is taxable based on individual income tax slab. In the case of joint accounts, all account holders will be jointly responsible for maintaining the RD and making monthly payments. <b>Each holder shares equal responsibility for the account.</b>",
                        "After maturity, the Post Office RD will pay the principal along with the accumulated interest.<br><br> You can either withdraw the amount or reinvest it. The amount is typically transferred to your savings account or a new RD, depending on your preference. <b>Reinvesting the maturity amount helps you keep earning interest without any interruptions.</b>",
                        "Yes, the interest earned on a Post Office RD is taxable as per your income tax slab.<br><br> However, you can claim a tax deduction under Section 80C for the amount deposited in a 5-year RD. <b>This can help you save on taxes, making it an attractive option for long-term tax planning.</b>",
                        "Yes, you can transfer your Post Office RD account from one post office to another within India.<br><br> A transfer request must be submitted to the respective post office. The transfer process is usually straightforward and can be completed within a few days. <b>The facility makes it easy for you to manage your RD account, even when relocating.</b>",
                        "The minimum period for a Post Office RD is 1 year.<br><br> However, you must deposit regularly for a minimum of 5 years to avail the complete benefits of the scheme. While you can withdraw after one year, it is recommended to keep your RD for the full 5-year term to take full advantage of the interest. <b>Longer terms yield higher returns due to compounding.</b>",
                        "Yes, you can nominate a person at the time of account opening or during the term of your RD account.<br><br> This ensures that the funds from your RD are transferred to the nominee in case of your unfortunate demise. <b>It’s a good practice to nominate someone to ensure a smooth transfer of funds.</b>",
                        "Yes, Post Office RD is backed by the Government of India, making it one of the safest investment options with guaranteed returns.<br><br> There is no risk involved as the government guarantees the returns and ensures the safety of your deposited funds. <b>The government’s backing makes it one of the most secure savings options available.</b>",
                        "You can check your Post Office RD account balance and status by visiting the post office, using the India Post website, or by checking through the India Post mobile app.<br><br> You will need to provide the account number or other identification details to access your account information. <b>Checking your account regularly ensures that your deposits are on track.</b>",
                        "Yes, you can avail of a loan or overdraft against your Post Office RD account after 6 months of completion.<br><br> The loan amount is usually a percentage of the RD balance and can be used for personal or other financial needs. However, the interest rate on the loan will be higher than the RD interest rate. <b>Loans are subject to approval based on your RD balance.</b>",
                        "Yes, you can open a Post Office RD account in the name of a minor.<br><br> The guardian must operate the account until the minor reaches adulthood. Once the minor becomes an adult, they can continue managing the account independently. <b>It’s an excellent way to save for a child's future.</b>",
                        "If you close your RD before the completion of 1 year, you will receive a reduced interest rate.<br><br> After 1 year, you can close the account with penalty charges for premature withdrawal. The penalty varies depending on when you close the account during the term. <b>It’s best to keep the account for the entire term to avoid penalties.</b>",
                        "The interest rate on Post Office RD is set by the Government of India and reviewed quarterly.<br><br> The rate remains constant for the entire tenure of the account after it is opened. <b>This provides stability to your investment, regardless of market fluctuations.</b>",
                        "Yes, you can make online payments for your monthly deposits.<br><br> The India Post website and mobile app allow you to make payments directly from your bank account. <b>This makes the process more convenient and saves you time compared to manual payments.</b>"
                };
            case "Time Deposit (TD)":
                return new String[]{
                        "A Post Office <b>Time Deposit (TD)</b> is a government-backed savings scheme where you deposit a lump sum amount for a fixed tenure, earning a predetermined interest rate.<br><br> The amount, including both principal and interest, is paid at the end of the term.<br><br> This scheme is <b>considered safe</b> as it is backed by the <b>Government of India</b>.",
                        "In a Post Office TD, you deposit a fixed amount for a term ranging from 1 to 5 years.<br><br> Interest is <b>compounded quarterly</b>, and the maturity amount is received at the end of the term.<br><br> The interest rate is fixed for the entire term, providing <b>guaranteed returns</b>.",
                        "The interest rates on Post Office TD vary based on the term.<br><br> As of October 2024, the rates are as follows:<br><b>6.9% for 1-year</b>, <b>7.0% for 2-year</b>, <b>7.1% for 3-year</b>, and <b>7.5% for 5-year</b>.<br><br> <b>The rates are reviewed quarterly by the government.</b>",
                        "The minimum deposit required for a Post Office TD is ₹1,000.<br><br> There is no upper limit for the deposit amount, but each deposit must be made in multiples of ₹100.<br><br> <b>You can make deposits in one go for the entire amount.</b>",
                        "The available tenures for a Post Office TD are 1, 2, 3, and 5 years.<br><br> Each term has a fixed interest rate, and you can choose a tenure based on your financial needs and goals.<br><br> <b>The longer the term, the higher the interest rate generally is.</b>",
                        "Yes, you can break your Post Office TD before maturity.<br><br> If you break it within 6 months, you will not receive any interest.<br><br> If the TD is broken after 6 months, you will receive interest at a reduced rate based on the duration it remained active. <b>It is best to avoid breaking your TD before maturity to avoid penalties.</b>",
                        "Yes, you can avail of a loan against your Post Office TD after 6 months of the deposit.<br><br> The loan amount can be up to 90% of the deposit amount.<br><br> <b>The interest rate for loans is typically higher than the TD interest rate.</b>",
                        "Yes, the interest earned on Post Office TD is taxable as per your income tax slab.<br><br> However, if you have a 5-year TD, you can claim tax benefits under Section 80C.<br><br> <b>If your interest exceeds ₹10,000 in a year, TDS (Tax Deducted at Source) will be applied.</b>",
                        "Yes, you can nominate one or more individuals to receive the maturity amount in case of your demise.<br><br> The nominee can claim the funds without any legal hurdles.<br><br> <b>It is important to nominate to ensure a smooth transfer of the investment.</b>",
                        "To open a Post Office TD account, you need to visit any post office and fill out the required forms.<br><br> You will also need identity proof, address proof, and photographs.<br><br> <b>Some post offices allow online account opening through the India Post website or app.</b>",
                        "Yes, you can transfer your Post Office TD from one post office to another.<br><br> You need to submit a request at the post office where the account is currently held.<br><br> <b>The transfer process is usually completed in a few days.</b>",
                        "If you miss a deposit for a particular month, the TD account will not earn interest for that month.<br><br> However, there is no penalty for late deposits, but it is important to make timely deposits to ensure interest accrual.<br><br> <b>Always try to deposit within the calendar month to maximize the interest.</b>",
                        "Yes, you can close your Post Office TD account before maturity, but there is a penalty.<br><br> If the TD is closed after 6 months but before maturity, you will receive a reduced interest rate.<br><br> <b>To avoid penalties, it is advisable to keep your TD for the entire term.</b>",
                        "There is no penalty for a late deposit in Post Office TD, but you must deposit within the month.<br><br> If you miss a deposit, the account will not earn interest for that month.<br><br> <b>Ensure regular deposits to maximize returns.</b>",
                        "The benefits of a Post Office TD include guaranteed returns with fixed interest rates.<br><br> It is a safe investment, backed by the government, and is ideal for conservative investors.<br><br> <b>Interest is compounded quarterly, leading to better growth of your savings.</b>",
                        "Once a Post Office TD account is opened, the deposit amount cannot be changed during the term.<br><br> If you want to make additional deposits, you need to open a new account.<br><br> <b>Each account will earn interest separately based on the deposit amount.</b>",
                        "Yes, Post Office TD is a safe and reliable investment option.<br><br> It provides fixed, guaranteed returns with minimal risk.<br><br> <b>If you are looking for stable growth, this is a good choice.</b>",
                        "No, the Post Office TD is designed for lump-sum deposits.<br><br> If you need monthly payouts, you can opt for the Post Office Monthly Income Scheme (MIS).<br><br> <b>The TD is intended for long-term savings that earn interest on maturity.</b>",
                        "No, once you open a Post Office TD account, you cannot change the tenure.<br><br> If you wish to change the term, you would need to close the existing account and open a new one.<br><br> <b>It is important to select the appropriate tenure at the time of opening the account.</b>",
                        "At maturity, the principal and interest are paid to you.<br><br> The maturity amount will be transferred to your savings account or given in cash, depending on your preference.<br><br> <b>You can also choose to reinvest the maturity amount into another TD account.</b>"
                };
            case "Monthly Income Scheme (MIS)":
                return new String[]{
                        "The <b>Post Office Monthly Income Scheme (MIS)</b> is a government-backed savings scheme where you deposit a lump sum and receive monthly interest payouts.<br><br> The scheme provides <b>guaranteed monthly income</b> for a fixed term of 5 years, making it an ideal choice for those seeking regular income.",
                        "Under the Post Office MIS, you invest a lump sum for a 5-year term.<br><br> The interest is paid monthly and remains fixed throughout the term, offering <b>stable and predictable returns</b>.",
                        "The current interest rate for Post Office MIS is <b>7.4% per annum</b> (as of October 2024).<br><br> <b>The government reviews the rate quarterly</b>, ensuring it remains competitive.",
                        "The minimum deposit for Post Office MIS is ₹1,000.<br><br> The maximum deposit limit is ₹9 lakhs for a single account and ₹15 lakhs for a joint account.<br><br> Deposits must be made in <b>multiples of ₹1,000</b>.",
                        "The tenure of the Post Office MIS is fixed at <b>5 years</b>, during which you receive monthly interest payouts.<br><br> It is an excellent option for individuals who need <b>regular income</b>.",
                        "Yes, premature closure of a Post Office MIS account is allowed.<br><br> If closed within 1 year, no interest is paid. If closed between 1 and 3 years, 2% of the principal is deducted. For closures after 3 years but before maturity, 1% of the principal is deducted.<br><br> <b>To maximize benefits, hold the account for the full term.</b>",
                        "Yes, the interest earned from Post Office MIS is <b>fully taxable</b> as per your income tax slab.<br><br> <b>No TDS is deducted at source</b>, but the income must be declared while filing taxes.",
                        "Yes, you can <b>nominate one or more individuals</b> when opening an MIS account.<br><br> In case of the depositor's demise, the nominee can claim the funds without legal complications.",
                        "To open an MIS account, visit any post office with your identity proof, address proof, and passport-size photographs.<br><br> Some post offices also offer <b>online account opening</b> through the India Post portal or app.",
                        "You can transfer your MIS account from one post office to another if required.<br><br> A transfer request must be submitted, and the process is usually <b>completed within a few days</b>.",
                        "No penalty is imposed for missing monthly interest payouts; however, <b>the interest does not earn additional returns</b> if left unclaimed.<br><br> Ensure timely collection to maximize benefits.",
                        "You can update or change the nominee for your MIS account by submitting a nomination form at the post office.<br><br> <b>Keeping the nomination updated ensures smooth fund transfer.</b>",
                        "Post Office MIS offers <b>secure and fixed monthly payouts</b>, making it ideal for retirees or those seeking a stable income source.<br><br> The scheme is backed by the government, ensuring <b>safety and reliability</b>.",
                        "Once the deposit is made, the amount cannot be altered during the tenure.<br><br> To make additional deposits, you must open a new account.<br><br> <b>Each account will generate separate monthly interest.</b>",
                        "Yes, Post Office MIS is one of the safest investment options.<br><br> It offers <b>government-backed security</b> and fixed monthly returns, making it ideal for risk-averse investors.",
                        "No lump-sum withdrawals are allowed during the tenure.<br><br> However, premature closure is permitted with penalties after 1 year.<br><br> <b>The scheme ensures consistent monthly payouts.</b>",
                        "No, MIS accounts cannot be transferred to another person.<br><br> However, the account can be shifted to another post office branch upon request.",
                        "At maturity, the principal is returned, and any unclaimed interest is paid.<br><br> You can opt to <b>reinvest the maturity amount</b> into a new MIS account or other savings schemes for continued benefits."
                };
            case "National Savings Certificate (NSC)":
                return new String[]{
                        "The <b>National Savings Certificate (NSC)</b> is a government-backed savings scheme designed to encourage secure and tax-efficient investments.<br><br> It is a fixed-income instrument offering <b>guaranteed returns</b> and tax benefits under Section 80C of the Income Tax Act.",
                        "NSC has a <b>5-year tenure</b>, making it ideal for medium-term financial goals.<br><br> The invested amount, along with compounded interest, is paid as a <b>lump sum at maturity</b>.",
                        "The current interest rate on NSC is <b>7.7% per annum</b> (as of October 2024).<br><br> The interest is <b>compounded annually</b> and added to the principal. Rates are <b>reviewed quarterly</b> by the government.",
                        "The minimum investment for NSC is <b>₹1,000</b>, and there is <b>no maximum limit</b>.<br><br> However, investments must be made in <b>multiples of ₹100</b>.",
                        "NSC offers <b>tax deductions up to ₹1.5 lakh per financial year</b> under Section 80C of the Income Tax Act.<br><br> However, the <b>interest earned is taxable</b> as per the investor's income tax slab.",
                        "NSC is a <b>safe and secure investment</b> backed by the <b>Government of India</b>.<br><br> It is suitable for <b>risk-averse investors</b> looking for <b>assured returns</b> and tax savings.",
                        "NSC can be purchased at any <b>post office branch</b> in India.<br><br> To open an account, you need to fill out an application form and submit <b>KYC documents</b>, such as identity and address proof.",
                        "Yes, you can <b>nominate one or more individuals</b> for your NSC.<br><br> In the unfortunate event of the holder's demise, the <b>nominee can claim the maturity amount</b> without legal complications.",
                        "Yes, NSC certificates can be <b>transferred from one individual to another</b> under specific conditions.<br><br> A joint application must be made by both the <b>transferor and transferee</b> at the post office.",
                        "Premature withdrawal of NSC is <b>not permitted</b>, except in exceptional cases such as the <b>death of the holder</b>, court orders, or forfeiture by a pledgee.<br><br> <b>Otherwise, NSC must remain locked for 5 years.</b>",
                        "NSC can be <b>pledged as collateral</b> for loans from banks and other financial institutions.<br><br> The post office will issue a <b>pledge certificate</b> to facilitate the loan process.",
                        "The <b>interest on NSC is compounded annually</b> and reinvested into the principal.<br><br> This reinvested amount qualifies for <b>additional tax benefits under Section 80C</b>, enhancing its overall tax-saving potential.",
                        "At maturity, the <b>principal and accumulated interest</b> are paid out to the investor.<br><br> The maturity amount can also be <b>reinvested in a new NSC</b> for continued savings.",
                        "Yes, you can <b>transfer your NSC account from one post office to another</b> if needed.<br><br> A transfer request must be submitted at the original post office, and the process is usually <b>completed within a few days</b>.",
                        "NSC is ideal for <b>individuals seeking a secure and tax-efficient investment</b> option.<br><br> It is particularly suitable for <b>conservative investors</b> and those with <b>long-term savings goals</b>."
                };
            default:
                return new String[0]; // Empty array for unrecognized operations
        }
    }

}
