package com.premiosnucleourbano.nucleourbano;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ander on 12/08/2017.
 */

public class NucleoFirebaseInstanceIdService extends FirebaseInstanceIdService {
public static final String TAG = "IdService";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token: " + token);
    }
}
