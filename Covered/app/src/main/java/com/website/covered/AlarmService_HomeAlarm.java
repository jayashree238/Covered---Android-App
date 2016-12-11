package com.website.covered;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jayashreemadhanraj on 11/26/16.
 * Reference: http://javapapers.com/android/android-alarm-clock-tutorial/
 */

public class AlarmService_HomeAlarm extends IntentService {
    private NotificationManager alarmNotificationManager;
    DataHelper dataHelper;
    Double homeLatitude;
    Double homeLongitude;
    Double destLatitude;
    Double destLongitude;
    ArrayList<String> weather = new ArrayList<>();
    String weather_type;
    String weather_icon;
    String weather_icon_final;


    private static final String OPEN_WEATHER_MAP_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API = "1416457803dc4cf41795818d3a9d1375";

    String msg = "";

    public AlarmService_HomeAlarm(){
        super("Alarm Service");
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataHelper = new DataHelper(this);
        homeLatitude = dataHelper.readHomeLatitude();
        homeLongitude = dataHelper.readHomeLongitude();
        destLatitude = dataHelper.readDestLatitude();
        destLongitude = dataHelper.readDestLongitude();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getWeatherJSON_First(Double.toString(homeLatitude), Double.toString(homeLongitude));
                getWeatherJSON_Second(Double.toString(destLatitude), Double.toString(destLongitude));
                sendNotification(msg, weather_icon_final);
            }
        }).start();

    }

    private void sendNotification(String message, String icon) {

        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SignedIn_Portrait_Fragment.class), 0);

        String uri = "@drawable/" + icon;

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Weather Alert!").setSmallIcon(imageResource).
                setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true);


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        alarmNotificationBuilder.setAutoCancel(true);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService_HomeAlarm", "Notification sent.");
    }

    //Report for First Location
    private JSONObject getWeatherJSON_First(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
            processJSON_First(data);
            return data;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void processJSON_First(JSONObject json) {
        try {
            if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");

                String main_id = details.getString("id");

                weather = dataHelper.getWeatherType(Integer.parseInt(main_id));
                weather_type = weather.get(0);
                weather_icon = weather.get(1);

                msg = "Current Location Weather: " + weather_type + ". ";
                weather_icon_final = "ic_notify";

                //Checking the weather type and giving instructions to users accordingly
                if(main_id.substring(0, 1).equals("2")){
                    msg = msg + "\n Don't forget your " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("3")){
                    msg = msg + " \n You might want your " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("5")){
                    msg = msg + "Don't forget your " + weather_icon;
                }
                else if (main_id.substring(0, 1).equals("6")){
                    msg = msg + "Don't forget your " + weather_icon;
                }
                else if (main_id.substring(0, 1).equals("7")){
                    msg = msg + weather_icon + " for instructions!";
                }
                else if(main_id.substring(0, 1).equals("8")){
                    msg = msg + " " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("9")){
                    if(main_id.equals("900") || main_id.equals("901") || main_id.equals("902")){
                        msg = msg + weather_icon;
                    }
                    else{
                        msg = msg + "Don't forget your " + weather_icon;
                    }

                }
                Log.v("Weather Report", msg);
            }
        } catch (JSONException e) {
            Log.v("Exception", "Cannot process JSON results", e);
        }
    }

    //Report for Second Location
    private JSONObject getWeatherJSON_Second(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
            processJSON_Second(data);
            return data;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void processJSON_Second(JSONObject json) {
        try {
            if (json != null) {
                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");

                String main_id = details.getString("id");

                weather = dataHelper.getWeatherType(Integer.parseInt(main_id));
                weather_type = weather.get(0);
                weather_icon = weather.get(1);

                msg = msg + "\n" + "Destination Weather: " + weather_type + ". ";
                weather_icon_final = "ic_notify";

                //Checking the weather type and giving instructions to users accordingly
                if(main_id.substring(0, 1).equals("2")){
                    msg = msg + "\n Don't forget your " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("3")){
                    msg = msg + " \n You might want your " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("5")){
                    msg = msg + "Don't forget your " + weather_icon;
                }
                else if (main_id.substring(0, 1).equals("6")){
                    msg = msg + "Don't forget your " + weather_icon;
                }
                else if (main_id.substring(0, 1).equals("7")){
                    msg = msg + weather_icon + " for instructions!";
                }
                else if(main_id.substring(0, 1).equals("8")){
                    msg = msg + " " + weather_icon;
                }
                else if(main_id.substring(0, 1).equals("9")){
                    if(main_id.equals("900") || main_id.equals("901") || main_id.equals("902")){
                        msg = msg + weather_icon;
                    }
                    else{
                        msg = msg + "Don't forget your " + weather_icon;
                    }

                }
                Log.v("Weather Report", msg);
            }
        } catch (JSONException e) {
            Log.v("Exception", "Cannot process JSON results", e);
        }
    }
}
