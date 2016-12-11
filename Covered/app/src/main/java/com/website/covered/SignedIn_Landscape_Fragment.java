package com.website.covered;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by jayashreemadhanraj on 11/24/16.
 */


public class SignedIn_Landscape_Fragment extends Fragment implements LocationListener {

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    RadioGroup radioGroup;
    RadioButton homeRadio;
    RadioButton destRadio;
    RadioButton currentLocRadio;
    DataHelper dataHelper;
    String location;
    WeatherAPI.placeIdTask asyncTask;
    LocationManager locationManager;
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    Typeface weatherFont;
    Location locationCurrent;
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.landscapesignedinfragment, container, false);


        dataHelper = new DataHelper(getActivity());
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        homeRadio = (RadioButton) v.findViewById(R.id.radioButton2);
        destRadio = (RadioButton) v.findViewById(R.id.radioButton);
        currentLocRadio = (RadioButton) v.findViewById(R.id.radioButton3);

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView) v.findViewById(R.id.city_field);
        updatedField = (TextView) v.findViewById(R.id.updated_field);
        detailsField = (TextView) v.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) v.findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) v.findViewById(R.id.humidity_field);
        pressure_field = (TextView) v.findViewById(R.id.pressure_field);
        weatherIcon = (TextView) v.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                asyncTask = new WeatherAPI.placeIdTask(new WeatherAPI.AsyncResponse() {
                    public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {
                        Log.d("Test in async", "in async");
                        cityField.setText(weather_city);
                        updatedField.setText(weather_updatedOn);
                        detailsField.setText(weather_description);
                        currentTemperatureField.setText(weather_temperature);
                        humidity_field.setText("Humidity: " + weather_humidity);
                        pressure_field.setText("Pressure: " + weather_pressure);
                        weatherIcon.setText(Html.fromHtml(weather_iconText));

                    }
                });
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb.getId() == R.id.radioButton){
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                    }
                    location = dataHelper.readDestAddress();
                    double latitude = dataHelper.readDestLatitude();
                    double longitude = dataHelper.readDestLongitude();
                    Log.d("DEst Latitude", Double.toString(latitude));
                    Log.d("DEst Longitude", Double.toString(longitude));
                    asyncTask.execute(Double.toString(latitude), Double.toString(longitude));
                }
                else if(rb.getId() == R.id.radioButton2){
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                    }

                    location = dataHelper.readHomeAddress();
                    double latitude = dataHelper.readHomeLatitude();
                    double longitude = dataHelper.readHomeLongitude();
                    Log.d("homeLatitude", Double.toString(latitude));
                    Log.d("homeLongitude", Double.toString(longitude));

                    asyncTask.execute(Double.toString(latitude), Double.toString(longitude));


                }
                else if(rb.getId() == R.id.radioButton3){
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                    }
                    getLocationFunction();

                }
            }
        });
    }

    public void getLocationFunction(){
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider = locationManager.getBestProvider(criteria, false);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            this.canGetLocation = true;
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDialog();

                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Log.d("Network", "Network Enabled");
                if (locationManager != null) {
                    locationCurrent = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (locationCurrent != null) {
                        asyncTask.execute(Double.toString(locationCurrent.getLatitude()), Double.toString(locationCurrent.getLongitude()));
                        Log.v("Latitude for current:", Double.toString(locationCurrent.getLatitude()));
                        Log.v("Longitude for current", Double.toString(locationCurrent.getLongitude()));
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDialog();

                }
                if (locationCurrent == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        locationCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (locationCurrent != null) {
                            asyncTask.execute(Double.toString(locationCurrent.getLatitude()), Double.toString(locationCurrent.getLongitude()));
                            Log.v("Latitude for current:", Double.toString(locationCurrent.getLatitude()));
                            Log.v("Longitude for current", Double.toString(locationCurrent.getLongitude()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void showPermissionDialog() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }
    }

    /**
     * Called when the location has changed.
     * <p>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }
}
