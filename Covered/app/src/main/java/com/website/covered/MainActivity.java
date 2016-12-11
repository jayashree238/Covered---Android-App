package com.website.covered;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by jayashreemadhanraj on 11/20/16.
 * Reference 1: https://www.youtube.com/watch?v=gNgWuBXDWis
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    private SignInButton signInButton;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;

    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;

    private static final int REQUEST_CODE_LOGIN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.compass);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).addApi(Plus.API).build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(googleSignInOptions.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signIn, REQUEST_CODE_LOGIN);
            }
        });

    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_LOGIN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            String name = googleSignInAccount.getDisplayName();
            Intent sendData = new Intent(MainActivity.this, SignedInActivity.class);
            sendData.putExtra("p_Name", name);

            startActivity(sendData);

        }
        else{
            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void infoPopUp(View v){
        Intent intent = new Intent(MainActivity.this, AuthorPopUp.class);
        startActivity(intent);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated

        float degree = Math.round(event.values[0]);

        // create a rotation animation (reverse turn degree degrees)

        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        ra.setDuration(210);
        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
