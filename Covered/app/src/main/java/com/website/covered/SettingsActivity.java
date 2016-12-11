package com.website.covered;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import static com.website.covered.R.id.alarmTimeDest;
import static com.website.covered.R.id.alarmTimeHome;
import static com.website.covered.R.id.f;
import static com.website.covered.R.id.homeAddress;

public class SettingsActivity extends Activity implements View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String LOG_TAG = "SettingsActivity";
    DataHelper dataHelper;
    ArrayList<Integer> alarms = new ArrayList<>();

    static final int DIALOG_ID = 0;
    int hour_x;
    int minute_x;
    int alarm_type;
    TextView homeAddress;
    TextView destAddress;
    TextView alarmTimeHome;
    TextView alarmTimeDest;
    Switch everydaySwitch;
    Switch weekdaysSwitch;
    TextView homeLat;
    TextView homeLong;
    TextView destLat;
    TextView destLong;
    String alarm1a;
    String alarm1b;
    String alarm2a;
    String alarm2b;
    ArrayList<Integer> repeatDay = new ArrayList<>();

    CheckBox mon, tues, wed, thur, fri, sat, sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataHelper = new DataHelper(this);

        homeAddress = (TextView) findViewById(R.id.homeAddress);
        destAddress = (TextView) findViewById(R.id.destAddress);
        everydaySwitch = (Switch) findViewById(R.id.everydayAlarm);
        weekdaysSwitch = (Switch) findViewById(R.id.weekdaysSwitch);
        alarmTimeDest = (TextView) findViewById(R.id.alarmTimeDest);
        alarmTimeHome = (TextView) findViewById(R.id.alarmTimeHome);
        homeLat = (TextView) findViewById(R.id.lat1Text);
        homeLong = (TextView) findViewById(R.id.long1Text);
        destLat = (TextView) findViewById(R.id.lat2Text);
        destLong = (TextView) findViewById(R.id.long2Text);

        mon = (CheckBox) findViewById(R.id.m);
        tues = (CheckBox) findViewById(R.id.t);
        wed = (CheckBox) findViewById(R.id.w);
        thur = (CheckBox) findViewById(R.id.th);
        fri = (CheckBox) findViewById(f);
        sat = (CheckBox) findViewById(R.id.sat);
        sun = (CheckBox) findViewById(R.id.s);

        everydaySwitch.setOnCheckedChangeListener(this);
        weekdaysSwitch.setOnCheckedChangeListener(this);

        String newHomeAddress = dataHelper.readHomeAddress();
        String newDestAddress = dataHelper.readDestAddress();

        repeatDay = dataHelper.getRepeatDays();
        for(int day: repeatDay){
            switch(day){
                case 1: sun.setChecked(true);break;
                case 2: mon.setChecked(true); break;
                case 3: tues.setChecked(true); break;
                case 4: wed.setChecked(true); break;
                case 5: thur.setChecked(true); break;
                case 6: fri.setChecked(true); break;
                case 7: sat.setChecked(true); break;
            }
        }

        alarms = dataHelper.getAlarm1Alarm2();
        if(alarms.size() > 0){
            String alarm1 = Integer.toString(alarms.get(0));
            if(String.valueOf(alarm1).length() == 2){
                alarm1a = alarm1.substring(Math.max(alarm1.length(), 0));
                alarm1a = "0" + alarm1a;
                alarm1b = alarm1.substring(0, 1);
                alarm1b = "0" + alarm1b;
            }
            else if(String.valueOf(alarm1).length() == 3){
                alarm1a = alarm1.substring(Math.max(alarm1.length() - 2, 0));
                alarm1b = alarm1.substring(0, 1);
                alarm1b = "0" + alarm1b;
            }
            else{
                alarm1a = alarm1.substring(Math.max(alarm1.length() - 2, 0));
                alarm1b = alarm1.substring(0, 2);
            }

            String alarm2 = Integer.toString(alarms.get(1));
            if(String.valueOf(alarm2).length() == 2){
                alarm2a = alarm2.substring(Math.max(alarm2.length(), 0));
                alarm2a = "0" + alarm2a;
                alarm2b = alarm2.substring(0, 1);
                alarm2b = "0" + alarm2b;
            }
            else if(String.valueOf(alarm2).length() == 3){
                alarm2a = alarm2.substring(Math.max(alarm2.length() - 2, 0));
                alarm2b = alarm2.substring(0, 1);
                alarm2b = "0" + alarm2b;
            }
            else{
                alarm2a = alarm2.substring(Math.max(alarm2.length() - 2, 0));
                alarm2b = alarm2.substring(0, 2);
            }

            alarmTimeHome.setText(alarm1b + ":" + alarm1a);
            alarmTimeDest.setText(alarm2b + ":" + alarm2a);
        }

        homeAddress.setText(newHomeAddress);
        destAddress.setText(newDestAddress);
        homeLat.setText(Double.toString(dataHelper.readHomeLatitude()));
        homeLong.setText(Double.toString(dataHelper.readHomeLongitude()));
        destLat.setText(Double.toString(dataHelper.readDestLatitude()));
        destLong.setText(Double.toString(dataHelper.readDestLongitude()));

        homeAddress.setOnLongClickListener(this);
        destAddress.setOnLongClickListener(this);
        showTimePickerDialog();

    }

    public void showTimePickerDialog(){

        alarmTimeDest.setOnLongClickListener(this);
        alarmTimeHome.setOnLongClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
            //Settings values after closing the Google PlaceAutoCompleter
            String newHomeAddress = dataHelper.readHomeAddress();
        homeAddress.setText(newHomeAddress);


            String newDestAddress = dataHelper.readDestAddress();
        destAddress.setText(newDestAddress);
        homeLat.setText(Double.toString(dataHelper.readHomeLatitude()));
        homeLong.setText(Double.toString(dataHelper.readHomeLongitude()));
        destLat.setText(Double.toString(dataHelper.readDestLatitude()));
        destLong.setText(Double.toString(dataHelper.readDestLongitude()));


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID){
            return new TimePickerDialog(SettingsActivity.this, kTimePickerListener, hour_x, minute_x, true);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.v(LOG_TAG, "selected time:" + hourOfDay + ":" + minute);
                    if(hourOfDay == 0){
                        hour_x = 24;
                    }
                    else {
                        hour_x = hourOfDay;
                    }
                     minute_x = minute;

                    if(alarm_type == 0) {
                        if(String.valueOf(hour_x).length() < 2 && String.valueOf(minute_x).length() < 2){
                                alarmTimeDest.setText("0" + hour_x + ":" + "0" + minute_x);
                        }
                        else if(String.valueOf(hour_x).length() < 2){
                            alarmTimeDest.setText("0" + hour_x + ":" + minute_x);
                        }
                        else if(String.valueOf(minute_x).length() < 2){
                            alarmTimeDest.setText(hour_x + ":" + "0" + minute_x);
                        }
                        else{
                                alarmTimeDest.setText(hour_x + ":" + minute_x);
                            }
                    }
                   else if (alarm_type == 1){

                        if(String.valueOf(hour_x).length() < 2 && String.valueOf(minute_x).length() < 2){
                            alarmTimeHome.setText("0" + hour_x + ":" + "0" + minute_x);
                        }
                        else if(String.valueOf(hour_x).length() < 2){
                            alarmTimeHome.setText("0" + hour_x + ":" + minute_x);
                        }
                        else if(String.valueOf(minute_x).length() < 2){
                            alarmTimeHome.setText(hour_x + ":" + "0" + minute_x);
                        }
                        else{
                            alarmTimeHome.setText(hour_x + ":" + minute_x);
                        }
                    }

                }
            };

    @Override
    public boolean onLongClick(View v) {

        if(v == destAddress){
            Intent intent = new Intent(SettingsActivity.this, AddressActivity.class);
            intent.putExtra("address_type", 0);
            startActivity(intent);

        }
        else if (v == homeAddress){
            Intent intent = new Intent(SettingsActivity.this, AddressActivity.class);
            intent.putExtra("address_type", 1);
            startActivity(intent);

        }
        else if (v == alarmTimeDest){
            alarm_type = 0;
            showDialog(DIALOG_ID);

        }
        else if(v == alarmTimeHome){
            alarm_type = 1;
            showDialog(DIALOG_ID);

        }
        return true;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView == everydaySwitch){
            if(!isChecked){

                mon.setChecked(false);
                tues.setChecked(false);
                wed.setChecked(false);
                thur.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);
            }
            else{
                mon.setChecked(true);
                tues.setChecked(true);
                wed.setChecked(true);
                thur.setChecked(true);
                fri.setChecked(true);
                sat.setChecked(true);
                sun.setChecked(true);
            }

        }
        else if(buttonView == weekdaysSwitch){
            if(!isChecked){

                mon.setChecked(false);
                tues.setChecked(false);
                wed.setChecked(false);
                thur.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);
            }
            else{
                mon.setChecked(true);
                tues.setChecked(true);
                wed.setChecked(true);
                thur.setChecked(true);
                fri.setChecked(true);
                sat.setChecked(false);
                sun.setChecked(false);

            }
        }
    }

    public void saveAlarm(View view){
        String[] finalAlarmHome = alarmTimeHome.getText().toString().split(":");
        String finalAlarmHome2 = finalAlarmHome[0] + finalAlarmHome[1];
        int finalAlarmHome3 = Integer.parseInt(finalAlarmHome2);

        String[] finalAlarmDest = alarmTimeDest.getText().toString().split(":");
        String finalAlarmDest2 = finalAlarmDest[0] + finalAlarmDest[1];
        int finalAlarmDest3 = Integer.parseInt(finalAlarmDest2);
        dataHelper.deleteAlarm();
//
        Alarm finalAlarm = new Alarm(homeAddress.getText().toString(), Double.parseDouble(homeLat.getText().toString()),Double.parseDouble(homeLong.getText().toString()), destAddress.getText().toString(), Double.parseDouble(destLat.getText().toString()), Double.parseDouble(destLong.getText().toString()), finalAlarmHome3, finalAlarmDest3, mon.isChecked(), tues.isChecked(), wed.isChecked(), thur.isChecked(), fri.isChecked(), sat.isChecked(), sun.isChecked());
        dataHelper.saveAlarmTable(finalAlarm);
        finish();

    }

    public void showHelpDialog(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle("Help");
        alertDialog.setMessage("Long click the form field to set required values.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
