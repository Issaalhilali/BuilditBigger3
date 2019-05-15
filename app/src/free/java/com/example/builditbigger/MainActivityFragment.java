package com.example.builditbigger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jokefactory.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Calendar;
import java.util.Date;


public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.DataReceivedCallback {

    private static final int AD_PERIOD = 30000;
    public static final String JOKE_TAG = "joke";

    private InterstitialAd mInterstitialAd;
    private Date AdShown;
    private ProgressBar pbLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdOpened() {
                AdShown = Calendar.getInstance().getTime();
                super.onAdOpened();
            }

            @Override
            public void onAdClosed() {
                requestJoke();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        root.findViewById(R.id.TellJoke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date timeNow = Calendar.getInstance().getTime();

                if (mInterstitialAd.isLoaded() &&
                        ((AdShown == null) || (timeNow.getTime() - AdShown.getTime() > AD_PERIOD))) {
                    mInterstitialAd.show();
                } else {
                    requestJoke();
                }
            }
        });

        pbLoad = root.findViewById(R.id.pbLoad);

        return root;
    }

    private void requestJoke() {
        pbLoad.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onDataReceived(Exception error, String data) {
        pbLoad.setVisibility(View.GONE);
        if (error != null) {
            Toast.makeText(getActivity(), getResources().getText(R.string.retrieval_error), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), JokeActivity.class);
            intent.putExtra(JOKE_TAG, data);
            startActivity(intent);
        }
    }
}
