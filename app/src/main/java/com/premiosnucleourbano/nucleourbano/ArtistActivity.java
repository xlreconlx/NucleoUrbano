package com.premiosnucleourbano.nucleourbano;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ArtistActivity extends BaseActivity {
    private static final String TAG = "ArtistActivity";

    public static final String EXTRA_CATE_KEY = "cate_key";

    private DatabaseReference mArtistReference;
    private DatabaseReference mVoteReference;
    private DatabaseReference mVoteUserReference;
    private String mPostKey;
    private RecyclerView mArtistRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8470610934699118~5467241221");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
