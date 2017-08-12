package com.premiosnucleourbano.nucleourbano;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * Created by ander on 12/08/2017.
 */

public class NucleoFirebaseRemoteConfig {
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private final boolean state = true;
    private Context context;

    public NucleoFirebaseRemoteConfig(Context context){
        this.context = context;
    }

    public FirebaseRemoteConfig remoteConfig(){
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(state).build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config);
        return firebaseRemoteConfig;
    }

    public long cacheExpiration(){
        long expiration = 3600;
        if(firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            return 0;
        }
        return expiration;
    }
}
