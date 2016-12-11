package com.website.covered;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;
import static com.website.covered.R.id.imageView;

/**
 * Created by jayashreemadhanraj on 11/24/16.
 */

public class SignedIn_Portrait_Fragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener{

    TextView nameText;
    ImageView settings;
    Intent i;
    DataHelper dataHelper;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PendingIntent pendingIntent1;
    private static  SignedIn_Portrait_Fragment inst;
    ToggleButton toggleButton;
    String selectedAlarmTime1;
    String selectedAlarmTime2;
    String alarm1Minute;
    String alarm1Hour;
    String alarm2Minute;
    String alarm2Hour;
    AlertDialog alertDialog;
    TextView finalAlarm1;
    TextView finalAlarm2;
    String homeLatitude;
    String homeLongitude;
    private String imageUri = null;
    RelativeLayout layout;

    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";
//    File imageFile;

    private static final int SELECT_PICTURE = 1;

    public static SignedIn_Portrait_Fragment instance(){
        return inst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("state", "onCreate");
        dataHelper = new DataHelper(getContext());
        dataHelper.insertWeatherValues();

        //Getting home address
        homeLatitude = Double.toString(dataHelper.readHomeLatitude());
        homeLongitude = Double.toString(dataHelper.readHomeLongitude());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
        Log.v("state", "onCreateView");
        View v =  inflater.inflate(R.layout.portraitsignedinfragment, container, false);
        layout = (RelativeLayout) v.findViewById(R.id.activity_signed_in);
        if(dataHelper.countBackground() == 1){
            Bitmap imageUri = dataHelper.getBackgroundFromDB();
            dataHelper.deleteBackground();
            Drawable d = new BitmapDrawable(getResources(), imageUri);
            layout.setBackground(d);
        }
        

        //Retrieve from SharedPreferences
        SharedPreferences settings2;
        String text;
        settings2 = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        text = settings2.getString(PREFS_KEY, null); //2
        byte[] imageAsBytes;
        if(text != null) {
            imageAsBytes = Base64.decode(text.getBytes(), 0);
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            Drawable d = new BitmapDrawable(getResources(), bitmap2);
            layout.setBackground(d);
        }
        //End SharedPreferences

        nameText = (TextView) v.findViewById(R.id.nameText);
        settings = (ImageView) v.findViewById(R.id.settings);
//        alarmTextChange = (TextView) v.findViewById(R.id.alarmText);
        finalAlarm1 = (TextView) v.findViewById(R.id.alarm1);
        finalAlarm2 = (TextView) v.findViewById(R.id.alarm2);

        i = getActivity().getIntent();



        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
              getActivity().startActivity(intent);
                return false;
            }
        });

        ImageButton changeBackground = (ImageButton) v.findViewById(R.id.changeBackground);
        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                dataHelper.deleteUser();
                startActivityForResult(i, SELECT_PICTURE);
            }
        });
        return v;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("state", "onActivityResult");
        if(resultCode == RESULT_OK){
            try {
                Uri imageUriTemp = data.getData();
                imageUri = getPathFromURI(imageUriTemp);
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUriTemp);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                //Using SharedPreferences to store Background

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                editor = settings.edit(); //2

                editor.putString(PREFS_KEY, encoded); //3
                editor.commit(); //4

                //End use of SharedPreferences


                dataHelper.insertBackground(selectedImage);
                Drawable d = new BitmapDrawable(getResources(), selectedImage);
                layout.setBackground(d);

            } catch (FileNotFoundException e) {
                Log.e("error",e.getMessage());
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v("state", "onActivityCreated");
        String returnName = i.getStringExtra("p_Name");
        if(returnName != null){
            nameText.setText("Hi " + returnName + "!");

            dataHelper.saveUser(nameText.getText().toString());
        }
        else{
            nameText.setText(dataHelper.getUser());
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        Log.v("state", "onStart");
        inst = this;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("state", "onAttach");
        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("state", "onResume");
        toggleButton = (ToggleButton) getActivity().findViewById(R.id.alarmOnOff);
        toggleButton.setOnCheckedChangeListener(this);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        finalAlarm1.setText("00:00");
        finalAlarm2.setText("00:00");

        //Initialize the Alarm values from the Database
        if(dataHelper.getAlarm1Alarm2().size() > 0) {
            selectedAlarmTime1 = dataHelper.getAlarm1Alarm2().get(0).toString();
            selectedAlarmTime2 = dataHelper.getAlarm1Alarm2().get(1).toString();

            finalAlarm1.setText(alarm1Hour + ":" + alarm1Minute);
            finalAlarm2.setText(alarm2Hour + ":" + alarm2Minute);

            //Getting and setting Home Alarm
            if(String.valueOf(selectedAlarmTime1).length() == 2){
                alarm1Minute = selectedAlarmTime1.substring(Math.max(selectedAlarmTime1.length(), 0));
                alarm1Minute = "0" + alarm1Minute;
                alarm1Hour = selectedAlarmTime1.substring(0, 1);
                alarm1Hour = "0" + alarm1Hour;
            }
            else if(String.valueOf(selectedAlarmTime1).length() == 3){
                alarm1Minute = selectedAlarmTime1.substring(Math.max(selectedAlarmTime1.length() - 2, 0));
                alarm1Hour = selectedAlarmTime1.substring(0, 1);
                alarm1Hour = "0" + alarm1Hour;
            }
            else{
                alarm1Minute = selectedAlarmTime1.substring(Math.max(selectedAlarmTime1.length() - 2, 0));
                alarm1Hour = selectedAlarmTime1.substring(0, 2);
            }

            //Getting and Setting Destination Alarm
           if(String.valueOf(selectedAlarmTime2).length() == 2){
                alarm2Minute = selectedAlarmTime2.substring(Math.max(selectedAlarmTime2.length(), 0));
                alarm2Minute = "0" + alarm2Minute;
                alarm2Hour = selectedAlarmTime2.substring(0, 1);
                alarm2Hour = "0" + alarm2Hour;
            }
            else if(String.valueOf(selectedAlarmTime2).length() == 3){
                alarm2Minute = selectedAlarmTime2.substring(Math.max(selectedAlarmTime2.length() - 2, 0));
                alarm2Hour = selectedAlarmTime2.substring(0, 1);
                alarm2Hour = "0" + alarm2Hour;
            }
            else{
                alarm2Minute = selectedAlarmTime2.substring(Math.max(selectedAlarmTime2.length() - 2, 0));
                alarm2Hour = selectedAlarmTime2.substring(0, 2);
            }

            finalAlarm1.setText(alarm1Hour + ":" + alarm1Minute);
            finalAlarm2.setText(alarm2Hour + ":" + alarm2Minute);
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("state", "onPAuse");
        if(alertDialog != null) {
            alertDialog.dismiss();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.v("state", "onCheckedChanged");
        if(isChecked) {
            Toast.makeText(getActivity(), "Alarm ON", Toast.LENGTH_SHORT).show();
            runAlarm();
        }
        else{
            alarmManager.cancel(pendingIntent);
            alarmManager.cancel(pendingIntent1);
            Toast.makeText(getActivity(), "Alarm OFF", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void runAlarm() {
        if (dataHelper.getAlarm1Alarm2().size() > 0) {
            ArrayList<Integer> daysSet = dataHelper.getRepeatDays();

            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();

            //Check if Alarm is set for 00 in 24 hours status.
            if(alarm1Hour.equals("24")){
                alarm1Hour = "00";
            }
            if(alarm2Hour.equals("24")){
                alarm2Hour = "00";
            }

            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm1Hour));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(alarm1Minute));
            calendar1.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm2Hour));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(alarm2Minute));
            calendar2.set(Calendar.SECOND, 0);


            //Calling the Alarm Receiver using intent and passing required data through intents
            Intent myIntent = new Intent(getActivity(), AlarmReceiver_HomeAlarm.class);
            Intent myIntent1 = new Intent(getActivity(), AlarmReceiver_DestAlarm.class);
            myIntent.putIntegerArrayListExtra("days", daysSet);
            myIntent1.putIntegerArrayListExtra("days", daysSet);

            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent1 = PendingIntent.getBroadcast(getActivity(), 1, myIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        }
    }
}
