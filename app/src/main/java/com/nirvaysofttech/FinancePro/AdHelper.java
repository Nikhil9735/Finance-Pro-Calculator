package com.nirvaysofttech.FinancePro;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
        AdView adView = activity.findViewById(R.id.bottom_ad_view);
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "Banner ad loaded successfully.");
                    bannerRetryCount = 0;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    Log.e(TAG, "Banner ad failed to load: " + adError.getMessage());

                    if (bannerRetryCount < MAX_RETRIES) {
                        bannerRetryCount++;
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            adView.loadAd(new AdRequest.Builder().build());
                        }, RETRY_DELAY_MS);
                    } else {
                        Log.e(TAG, "Banner ad: Max retry reached.");
                    }
                }
            });

            adView.loadAd(adRequest);
        }
    }

    // Load BANNER Ad in Fragment with retry
    public static void loadBannerAd(Fragment fragment, View view) {
        AdView adView = view.findViewById(R.id.bottom_ad_view);
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "Fragment banner ad loaded successfully.");
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
                        Log.e(TAG, "Fragment banner ad: Max retry reached.");
                    }
                }
            });

            adView.loadAd(adRequest);
        }
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
