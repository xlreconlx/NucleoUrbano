package com.premiosnucleourbano.nucleourbano;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.premiosnucleourbano.nucleourbano.models.User;


public class SignInActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SignInActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private boolean isEndVotacion;
    private  LoginButton loginButton;
    private TextView txtVotacion;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mProgressBar =(ProgressBar)findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        txtVotacion = (TextView) findViewById(R.id.end_votacion);
        NucleoFirebaseRemoteConfig nucleoRemote = new NucleoFirebaseRemoteConfig(getApplicationContext());
        firebaseRemoteConfig = nucleoRemote.remoteConfig();
        firebaseRemoteConfig.fetch(nucleoRemote.cacheExpiration())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseRemoteConfig.activateFetched();
                        isEndVotacion = firebaseRemoteConfig.getBoolean("end_votaciones");
                        loginButton.setVisibility(isEndVotacion? View.GONE:View.VISIBLE);
                        txtVotacion.setVisibility(isEndVotacion? View.VISIBLE:View.GONE);
                        if(isEndVotacion){
                            if(mAuth.getCurrentUser()!=null){
                                signOut();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                     @Override
                      public void onFailure(@NonNull Exception e) {
                      Log.d("ERROR","FETCH FAILED");
                     }
        });


        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

public void showProgress(){
    mProgressBar.setVisibility(View.VISIBLE);
}

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        showProgress();
        loginButton.setVisibility(View.GONE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:Correcto");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:Fallo", task.getException());
                            Toast.makeText(SignInActivity.this, "Error Conectando con Facebook",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // [END auth_with_facebook]

    //Cierra la Sesion
    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }


    @Override
    public void onStart() {
        super.onStart();
        // revisa si el usuario esta logeado
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        writeNewUser(user.getUid(), user.getDisplayName(), user.getEmail());

        // Abrimos la actividad principal
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    // Crea nuevo usuario
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("registeredUsers").child(userId).setValue(user);
    }



    @Override
    public void onClick(View view) {

    }
}
