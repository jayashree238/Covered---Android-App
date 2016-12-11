package com.website.covered;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeByteArray;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.website.covered.R.id.f;
import static com.website.covered.R.id.homeAddress;
import static com.website.covered.R.id.s;

/**
 * Created by jayashreemadhanraj on 11/21/16.
 */

public class DataHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DataHelper.class.getName();
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "carryAnUmbrella";

    private static final String HOMEADDR = "Home_addr_Table";
    private static final String DESTADDR = "Dest_addr_Table";
    private static final String ALARMTABLE = "Alarm_Table";
    private static final String USERTABLE = "User_Table";
    private static final String WEATHERTABLE = "Weather_Table";
    private static final String BACKGROUNDTABLE = "BAckground_Table";

    private static final String TABLE_HOME_ADDRESS =
            "CREATE TABLE " + HOMEADDR + " (_ID INTEGER, HOME_ADDRESS TEXT, LATITUDE REAL, LONGITUDE REAL)";
    private static final String TABLE_DEST_ADDRESS =
            "CREATE TABLE " + DESTADDR + " (_ID INTEGER, DEST_ADDRESS TEXT, LATITUDE REAL, LONGITUDE REAL)";
    private static final String TABLE_ALARM_CREATE =
            "CREATE TABLE " + ALARMTABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, HOME_ADDRESS TEXT, HOME_LATITUDE REAL, HOME_LONGITUDE REAL, DEST_ADDRESS TEXT, DEST_LATITUDE REAL, DEST_LONGITUDE REAL, HOMEALARM INTEGER, DESTALARM INTEGER, MON BOOL, TUES BOOL, WED BOOL, THUR BOOL, FRI BOOL, SAT BOOL, SUN BOOL)";
    private static final String TABLE_USER_CREATE =
            "CREATE TABLE " + USERTABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT)";
    private static final String TABLE_WEATHER_CREATE =
            "CREATE TABLE " + WEATHERTABLE + "(_ID INTEGER, WEATHER_ID TEXT, WEATHER_TYPE TEXT, WEATHER_ICON TEXT)";
    private static final String BACKGROUND_TABLE_CREATE =
            "CREATE TABLE " + BACKGROUNDTABLE + "(_ID INTEGER, BACKGROUNDIMAGE BLOB)";


    DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_HOME_ADDRESS);
        db.execSQL(TABLE_DEST_ADDRESS);
        db.execSQL(TABLE_ALARM_CREATE);
        db.execSQL(TABLE_USER_CREATE);
        db.execSQL(TABLE_WEATHER_CREATE);
        db.execSQL(BACKGROUND_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Save Alarm Function
    public Boolean saveAlarmTable(Alarm finalAlarm){
        SQLiteDatabase db = getWritableDatabase();
        String insert = "INSERT INTO " + ALARMTABLE + " (HOME_ADDRESS, HOME_LATITUDE, HOME_LONGITUDE, DEST_ADDRESS, DEST_LATITUDE, DEST_LONGITUDE, HOMEALARM, DESTALARM, MON, TUES, WED, THUR, FRI, SAT, SUN) VALUES ('" + finalAlarm.getHomeAddress() + "', " + finalAlarm.getHomeLat() + ", " + finalAlarm.getHomeLng() + ", '" + finalAlarm.getDestAddress() + "'," + finalAlarm.getDestLat() + " , " + finalAlarm.getDestLng() + ", " + finalAlarm.getHomeAlarm() + "," + finalAlarm.getDestAlarm() + ", '" + finalAlarm.getMon() + "', '" + finalAlarm.getTues() + "', '" + finalAlarm.getWed() + "', '" + finalAlarm.getThur() + "' ,'" + finalAlarm.getFri() + "', '" + finalAlarm.getSat() + "', '" + finalAlarm.getSun() +"');";
        db.execSQL(insert);
        db.close();
        return true;
    }

    //Home Address
    public Boolean updateHomeAddress(String homeAddress, Double latitude, Double longitude){
        SQLiteDatabase db = getWritableDatabase();
        String query;
        Cursor c = db.rawQuery("SELECT * FROM " + HOMEADDR, null);
        c.moveToFirst();
        if (c.getCount() == 0){
            query = "INSERT INTO " + HOMEADDR + " (HOME_ADDRESS, LATITUDE, LONGITUDE) VALUES ('" + homeAddress + "', " + latitude + "," + longitude + ");";
        }
        else{
            query = "UPDATE " + HOMEADDR + " SET HOME_ADDRESS='" + homeAddress + "', LATITUDE = " + latitude + ", LONGITUDE = " + longitude + ";";
        }
        db.execSQL(query);
        c.close();
        db.close();

        return true;
    }

    public String readHomeAddress(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + HOMEADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(1).equals(" ")) {
                return "None";
            } else {
                db.close();
                return c.getString(1);
            }
        }
        else {
            c.close();
            db.close();
            return "Long Click to Select";
        }

    }

    public Double readHomeLatitude(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + HOMEADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(2).equals(" ")) {
                return 0.0;
            } else {
                db.close();
                return c.getDouble(2);
            }
        }
        else {
            c.close();
            db.close();
            return 0.0;
        }

    }
    public Double readHomeLongitude(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + HOMEADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(3).equals(" ")) {
                return 0.0;
            } else {
                db.close();
                return c.getDouble(3);
            }
        }
        else {
            c.close();
            db.close();
            return 0.0;
        }

    }

    //Destination Address
    public Boolean updateDestAddress(String destAddress, Double latitude, Double longitude){
        SQLiteDatabase db = getWritableDatabase();
        String query;
        Cursor c = db.rawQuery("SELECT * FROM " + DESTADDR, null);
        c.moveToFirst();
        if (c.getCount() == 0){
            query = "INSERT INTO " + DESTADDR + " (DEST_ADDRESS, LATITUDE, LONGITUDE) VALUES ('" + destAddress + "', " + latitude + "," + longitude + ");";
        }
        else{
            query = "UPDATE " + DESTADDR + " SET DEST_ADDRESS='" + destAddress + "', LATITUDE = " + latitude + ", LONGITUDE = " + longitude + ";";
        }
        db.execSQL(query);
        c.close();
        db.close();
        return true;
    }

    public String readDestAddress(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DESTADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(1).equals(" ")) {
                return "None";
            } else {
                db.close();
                return c.getString(1);
            }
        }
        else {
            c.close();
            db.close();
            return "Long Click To Select";
        }
    }

    public Double readDestLatitude(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DESTADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(2).equals(" ")) {
                return 0.0;
            } else {
                db.close();
                return c.getDouble(2);
            }
        }
        else {
            c.close();
            db.close();
            return 0.0;
        }

    }
    public Double readDestLongitude(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DESTADDR, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if (c.getString(3).equals(" ")) {
                return 0.0;
            } else {
                db.close();
                return c.getDouble(3);
            }
        }
        else {
            c.close();
            db.close();
            return 0.0;
        }

    }

    public ArrayList<Integer> getAlarm1Alarm2(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Integer> alarms = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT HOMEALARM, DESTALARM FROM " + ALARMTABLE, null);
        c.moveToFirst();
        if(c.getCount() > 0){
                alarms.add(c.getInt(0));
                alarms.add(c.getInt(1));
        }
        c.close();
        return alarms;
    }

    //Alarm Delete

   public void deleteAlarm(){
       SQLiteDatabase db = getWritableDatabase();
       String delete = "DELETE FROM " + ALARMTABLE + ";";
       db.execSQL(delete);
   }

    //User Activity

    public void saveUser(String name){
        SQLiteDatabase db = getWritableDatabase();
        String insert = "INSERT INTO " + USERTABLE + " (USERNAME) VALUES ('" + name + "');";
        db.execSQL(insert);

    }

    public String getUser(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT USERNAME FROM " + USERTABLE, null);
        c.moveToFirst();
        return c.getString(0);
    }

    public void deleteUser(){
        SQLiteDatabase db = getWritableDatabase();
        String delete = "DELETE FROM " + USERTABLE + ";";
        db.execSQL(delete);
    }

    //Function to get Repeating alarm days

    public ArrayList<Integer> getRepeatDays(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Integer> daysSet = new ArrayList<>();
        int dayInt = 1;
        Cursor c = db.rawQuery("SELECT SUN, MON, TUES, WED, THUR, FRI, SAT FROM " + ALARMTABLE, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            for (int i = 0; i <= 6; i++) {
                if (c.getString(i).equals("true")) {
                    daysSet.add(dayInt);

                }
                dayInt++;
            }
        }
        return daysSet;
    }

    //Weather Data
    public void insertWeatherValues(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + WEATHERTABLE, null);
        c.moveToFirst();
        if(c.getCount() ==0){
            String insert = "INSERT INTO " + WEATHERTABLE + " (WEATHER_ID, WEATHER_TYPE, WEATHER_ICON) VALUES (200,'thunderstorm with light rain','umbrella or raincoat'), (201,'thunderstorm with rain','umbrella or raincoat'),(202,'thunderstorm with heavy rain','umbrella or raincoat'),(210,'light thunderstorm','umbrella or raincoat'),(211,'thunderstorm','umbrella or raincoat'),(212,'heavy thunderstorm','umbrella or raincoat'),(221,'ragged thunderstorm','umbrella or raincoat'),(230,'thunderstorm with light drizzle','umbrella'),(231,'thunderstorm with drizzle','umbrella'),(232,'thunderstorm with heavy drizzle','umbrella'),(300,'light intensity drizzle','umbrella'),(301,'drizzle','umbrella'),(302,'heavy intensity drizzle','umbrella'),(310,'light intensity drizzle rain','umbrella'),(311,'drizzle rain','umbrella'),(312,'heavy intensity drizzle rain','umbrella and coat'),(313,'shower rain and drizzle','umbrella'),(314,'heavy shower rain and drizzle','umbrella'),(321,'shower drizzle','umbrella'),(500,'light rain','umbrella'),(501,'moderate rain','umbrella and coat'),(502,'heavy intensity rain','umbrella and coat'),(503,'very heavy rain','umbrella and coat'),(504,'extreme rain','umbrella and coat'),(511,'freezing rain','umbrella and coat'),(520,'light intensity shower rain','umbrella'),(521,'shower rain','umbrella'),(522,'heavy intensity shower rain','umbrella and coat'),(531,'ragged shower rain','umbrella and coat'),(600,'light snow','Coat, Scarf & Gloves'),(601,'snow','Coat, Scarf & Gloves'),(602,'heavy snow','Coat, Scarf & Gloves'),(611,'sleet','Coat, Scarf & Gloves'),(612,'shower sleet','Umbrella, Coat, Scarf & Gloves'),(615,'light rain and snow','Umbrella, Coat, Scarf & Gloves'),(616,'rain and snow','Umbrella, Coat, Scarf & Gloves'),(620,'light shower snow','Umbrella, Coat, Scarf & Gloves'),(621,'shower snow','Umbrella, Coat, Scarf & Gloves'),(622,'heavy shower snow','Umbrella, Coat, Scarf & Gloves'),(900,'tornado','Stay at Home'),(901,'tropical storm','Stay at Home'), (902,'hurricane','Stay at Home'),(903,'cold','Coat, Scarf & Gloves'),(904,'hot','Light clothes'),(905,'windy','Coat & Scarf'),(906,'hail','Umbrella, Coat, Scarf & Gloves'),(801,'few clouds','Enjoy the Weather'),(802,'scattered clouds','Enjoy the Weather'),(803,'broken clouds','Enjoy the Weather'),(804,'overcast clouds','Consider taking a coat'), (800,'clear sky','Enjoy the Weather'),(701,'mist','Consider taking a coat & check local news'),(711,'smoke','Check local news'),(721,'haze','Check local news'),(731,'sand, dust whirls','Check local news'),(741,'fog','Consider taking a coat & check local news'),(751,'sand','Check local news'),(761,'dust','Check local news'),(762,'volcanic ash','Check local news'),(771,'squalls','Check local news'),(781,'tornado','Check local news')";

            db.execSQL(insert);
        }

        db.close();
    }

    public ArrayList<String> getWeatherType(int weather_id){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> weather = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT WEATHER_ID, WEATHER_TYPE, WEATHER_ICON FROM " + WEATHERTABLE + " WHERE WEATHER_ID = " + weather_id, null);
        c.moveToFirst();
        weather.add(c.getString(1));
        weather.add(c.getString(2));

        return weather;
    }

    public void insertBackground(Bitmap bitmap){
        SQLiteDatabase db = getWritableDatabase();
        String insert = "INSERT INTO " + BACKGROUNDTABLE + " (BACKGROUNDIMAGE) VALUES ('" + bitmap + "');";
        db.execSQL(insert);
        db.close();
    }

    public Bitmap getBackgroundFromDB(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT BACKGROUNDIMAGE FROM " + BACKGROUNDTABLE, null);
        c.moveToFirst();
        return decodeByteArray(c.getBlob(0), 0, c.getBlob(0).length);
    }

    public void deleteBackground(){
        SQLiteDatabase db = getWritableDatabase();
        String delete = "DELETE FROM " + BACKGROUNDTABLE + ";";
        db.execSQL(delete);
    }

    public int countBackground(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BACKGROUNDTABLE, null);
        c.moveToFirst();
        if(c.getCount() > 0){
            return 1;
        }
        else {
            return 0;
        }
    }
}
