package com.nirvaysofttech.FinancePro;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdHelper {

    private static final String TAG = "AdHelper";
    private static final int RETRY_DELAY_MS = 5000;
    private static final int MAX_RETRIES = 3;

    private static int bannerRetryCount = 0;
    private static int squareRetryCount = 0;

    // Load BANNER Ad in Activity with retry
    public static void loadBannerAd(Activity activity) {
        FrameLayout adContainer = activity.findViewById(R.id.bottom_ad_view);
        if (adContainer == null) return;

        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.admob_banner_ad_unit));

        // Calculate ad width
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int adWidth = (int) (metrics.widthPixels / metrics.density);

        // Set adaptive ad size
        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
        adView.setAdSize(adSize);

        adContainer.removeAllViews();
        adContainer.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Adaptive banner ad loaded.");
                bannerRetryCount = 0;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Adaptive banner ad failed: " + adError.getMessage());
                if (bannerRetryCount < MAX_RETRIES) {
                    bannerRetryCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        adView.loadAd(new AdRequest.Builder().build());
                    }, RETRY_DELAY_MS);
                } else {
                    Log.e(TAG, "Max retries reached for banner ad.");
                }
            }
        });

        adView.loadAd(adRequest);
    }

    // Load BANNER Ad in Fragment with retry
    public static void loadBannerAd(Fragment fragment, View view) {
        FrameLayout adContainer = view.findViewById(R.id.bottom_ad_view);
        if (adContainer == null) return;

        AdView adView = new AdView(fragment.requireContext());
        adView.setAdUnitId(fragment.getString(R.string.admob_banner_ad_unit));

        DisplayMetrics metrics = new DisplayMetrics();
        fragment.requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int adWidth = (int) (metrics.widthPixels / metrics.density);

        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(fragment.requireContext(), adWidth);
        adView.setAdSize(adSize);

        adContainer.removeAllViews();
        adContainer.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Adaptive banner in Fragment loaded.");
                bannerRetryCount = 0;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Fragment banner ad failed: " + adError.getMessage());
                if (bannerRetryCount < MAX_RETRIES) {
                    bannerRetryCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        adView.loadAd(new AdRequest.Builder().build());
                    }, RETRY_DELAY_MS);
                } else {
                    Log.e(TAG, "Max retries reached for fragment banner ad.");
                }
            }
        });

        adView.loadAd(adRequest);
    }

    // Load MEDIUM_RECTANGLE Ad with retry
    public static void loadMidiumSquareAd(Activity activity, LinearLayout container) {
        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(activity.getString(R.string.admob_square_ad_unit));

        LinearLayout.LayoutParams adParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        adParams.setMargins(0, 24, 0, 24);
        adView.setLayoutParams(adParams);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Square ad loaded.");
                squareRetryCount = 0;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Square ad failed: " + adError.getMessage());

                if (squareRetryCount < MAX_RETRIES) {
                    squareRetryCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        adView.loadAd(new AdRequest.Builder().build());
                    }, RETRY_DELAY_MS);
                } else {
                    Log.e(TAG, "Square ad: Max retry reached.");
                }
            }
        });

        adView.loadAd(adRequest);
        container.addView(adView);
    }
}
