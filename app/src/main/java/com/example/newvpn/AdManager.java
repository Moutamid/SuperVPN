package com.example.newvpn;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdManager {

    private InterstitialAd adMobInterstitialAd;
    private com.facebook.ads.InterstitialAd fBInterstitial;
    private UnifiedNativeAd nativeAd;
    private AdLoader adLoader;
    BottomSheetDialog bottomSheetDialog;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private boolean isAdLoad = true;


    public AdManager(Context context) {
        loadInterstitialAds(context);
        loadNativeAd(context);


    }

    private static volatile AdManager instance;

    public  static  AdManager getInstance(Context context) {
        if (instance == null) {//Check for the first time
            synchronized (AdManager.class) {   //Check for the second time.
                if (instance == null) instance = new AdManager(context);
            }
        }
        return instance;
    }

    public void showBannerAd(Activity activity) {
        AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        FrameLayout adContainer = activity.findViewById(R.id.ad_container);
        adContainer.addView(mAdView);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                loadFbBannerAd(adContainer, activity);
            }
        });
    }

    private void loadFbBannerAd(FrameLayout adContainer, Activity activity) {
        com.facebook.ads.AdView fBAdView = new com.facebook.ads.AdView(activity,
                "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adContainer.addView(fBAdView);
        fBAdView.loadAd();
    }

    public void showInterstitialAd(Context context) {
        // Show the ad if it's ready. Otherwise toast and restart.
        if (adMobInterstitialAd != null && adMobInterstitialAd.isLoaded()) {
            adMobInterstitialAd.show();
            loadInterstitialAds(context);
            return;
        }

        // Check if interstitialAd has been loaded successfully
        if(fBInterstitial == null || !fBInterstitial.isAdLoaded()) {
            loadInterstitialAds(context);
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if(fBInterstitial.isAdInvalidated()) {
            loadInterstitialAds(context);
            return;
        }
        // Show the ad
        fBInterstitial.show();
        loadInterstitialAds(context);
    }

    private void loadInterstitialAds(Context context) {
        adMobInterstitialAd = new InterstitialAd(context);
        adMobInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        adMobInterstitialAd.loadAd(new AdRequest.Builder().build());
        loadFbInterstitial(context);
    }

    private void loadFbInterstitial(Context context) {
        fBInterstitial = new com.facebook.ads.InterstitialAd(context,
                context.getString(R.string.Fb_interstitial_id));
        fBInterstitial.loadAd(
                fBInterstitial.buildLoadAdConfig()
                        .build());

    }


    public void showNativeAd(Context context){
        if (isAdLoad) {

                bottomSheetDialog.show();
//                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        loadNativeAd(context);
//                    }
//                });

                bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        loadNativeAd(context);
                    }
                });
            }
            else {

                ((Activity)context).finish();
            }
            TextView exit= bottomSheetDialog.findViewById(R.id.tvexit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    ((Activity)context).finish();
                }
            });
            final ImageButton imageButton= bottomSheetDialog.findViewById(R.id.cancelexit);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });



        }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setMediaView((com.google.android.gms.ads.formats.MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getVideoController();

    }
    public  void loadNativeAd(Context context){
        bottomSheetDialog=new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.exitdialog);
        AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {


                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = bottomSheetDialog.findViewById(R.id.unifiedadview);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                adView.setVisibility(View.VISIBLE);
//
            }

        });
        adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                ((Activity)context).finish();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                isAdLoad = true;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                isAdLoad = false;
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    static boolean isPurchased() {
        return MainActivity.isPurchased;
    }

    public InterstitialAd getAdMobInterstitialAd() {
        return adMobInterstitialAd;
    }

    public com.facebook.ads.InterstitialAd getFBInterstitial() {
        return fBInterstitial;
    }

}
