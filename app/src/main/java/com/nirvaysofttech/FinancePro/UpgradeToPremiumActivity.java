package com.nirvaysofttech.FinancePro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.billingclient.api.*;
import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.List;

public class UpgradeToPremiumActivity extends AppCompatActivity {

    private static final String PREMIUM_PRODUCT_ID = "finance_pro_premium_lifetime";
    private static final String PREFS_NAME = "finance_pro_prefs";
    private static final String PREF_PREMIUM = "is_premium";

    private BillingClient billingClient;
    private ProductDetails productDetails;
    private MaterialButton upgradeButton;
    private MaterialButton restoreButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.nav_close);
            getSupportActionBar().setTitle(R.string.premium_title);
        }

        upgradeButton = findViewById(R.id.btn_upgrade);
        restoreButton = findViewById(R.id.btn_restore);

        if (isPremiumUser()) {
            upgradeButton.setEnabled(false);
            upgradeButton.setText("Already Premium");
            hideAds();
        } else {
            AdHelper.loadBannerAd(this);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });

        setupBilling();

        upgradeButton.setOnClickListener(v -> launchPurchaseFlow());
        restoreButton.setOnClickListener(v -> restorePurchase());
    }

    private boolean isPremiumUser() {
        return sharedPreferences.getBoolean(PREF_PREMIUM, false);
    }

    private void setPremiumUser(boolean isPremium) {
        sharedPreferences.edit().putBoolean(PREF_PREMIUM, isPremium).apply();
    }

    private void hideAds() {
        View adView = findViewById(R.id.bottom_ad_view);
        if (adView != null) {
            adView.setVisibility(View.GONE);
        }
    }

    private void setupBilling() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this::onPurchaseUpdated)
                .build();

        connectBillingClient();
    }

    private void connectBillingClient() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails();
                    checkExistingPurchases();
                } else {
                    Toast.makeText(UpgradeToPremiumActivity.this,
                            "Billing setup failed: " + billingResult.getDebugMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Reconnect with a slight delay or retry mechanism
                Toast.makeText(UpgradeToPremiumActivity.this,
                        "Billing service disconnected. Reconnecting...",
                        Toast.LENGTH_SHORT).show();

                billingClient.startConnection(this); // Reconnect immediately
            }
        });
    }

    private void queryProductDetails() {
        if (billingClient == null || !billingClient.isReady()) return;

        QueryProductDetailsParams.Product product =
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build();

        QueryProductDetailsParams params =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(Collections.singletonList(product))
                        .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && productDetailsList != null && !productDetailsList.isEmpty()) {
                productDetails = productDetailsList.get(0);
            } else {
                Toast.makeText(this, "Unable to fetch product details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExistingPurchases() {
        if (billingClient == null || !billingClient.isReady()) return;

        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                (billingResult, purchasesList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : purchasesList) {
                            if (purchase.getProducts().contains(PREMIUM_PRODUCT_ID)) {
                                handlePurchase(purchase);
                                break;
                            }
                        }
                    }
                });
    }

    private void launchPurchaseFlow() {
        if (productDetails == null || billingClient == null || !billingClient.isReady()) {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
            return;
        }

        BillingFlowParams billingFlowParams =
                BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(Collections.singletonList(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .build()
                        ))
                        .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    private void restorePurchase() {
        if (billingClient == null || !billingClient.isReady()) {
            Toast.makeText(this, "Billing service not available", Toast.LENGTH_SHORT).show();
            return;
        }

        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                (billingResult, purchasesList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        boolean found = false;
                        for (Purchase purchase : purchasesList) {
                            if (purchase.getProducts().contains(PREMIUM_PRODUCT_ID)) {
                                handlePurchase(purchase);
                                found = true;
                                Toast.makeText(this, "Purchase restored", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (!found) {
                            Toast.makeText(this, "No previous purchase found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Restore failed: " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onPurchaseUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Purchase failed: " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgeParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                billingClient.acknowledgePurchase(acknowledgeParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        onPremiumUnlocked();
                    }
                });
            } else {
                onPremiumUnlocked();
            }
        }
    }

    private void onPremiumUnlocked() {
        setPremiumUser(true);
        upgradeButton.setEnabled(false);
        upgradeButton.setText("Already Premium");
        hideAds();
        Toast.makeText(this, "Premium unlocked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishWithAnimation();
        return true;
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
