package com.website.covered;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import static com.google.android.gms.internal.zzs.TAG;
import static com.website.covered.R.id.t;

/**
 * Created by jayashreemadhanraj on 11/20/16.
 */

public class AddressActivity extends FragmentActivity implements PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {

    private GoogleMap mMap;
    String address;
    DataHelper dataHelper;
    int address_type;
    LatLng latlng;
    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHelper = new DataHelper(this);
        setContentView(R.layout.activity_address);

        Intent i = getIntent();
        address_type = i.getIntExtra("address_type", -1);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment.setOnPlaceSelectedListener(this);

    }

    /**
     * Callback invoked when a place has been selected from the homeAddressActivity.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());
        address = place.getAddress().toString();
        latlng = place.getLatLng();
        latitude = latlng.latitude;
        longitude = latlng.longitude;
        Log.v(TAG, "LatLog: "+ latitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
        mMap.addMarker(new MarkerOptions().title("Home Address").position(place.getLatLng()));


    }

    /**
     * Callback invoked when homeAddressActivity encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void goBackToSettingActivity(View v){
        if(address_type == 1) {
            dataHelper.updateHomeAddress(address, latitude, longitude);


        }
        else if(address_type == 0){
            dataHelper.updateDestAddress(address, latitude, longitude);
        }
        else{
            Toast.makeText(this, "Do not recognize address_type", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
