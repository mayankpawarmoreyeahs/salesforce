package demo.recycle.com.salforceapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import demo.recycle.com.salforceapp.Activity.TaskActivity;


public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final long TEN_MINUTES = 1000 * 10 * 60;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    Intent intent;
    int counter = 0;
    String lat = "", lon = "";
    String Address, str;
    String statusId = "2";
    double Currentlat;
    double Currentlng;
    private String TAG = "Location";
    GlobalState globalState;
    String formattedDate;
    @Override
    public void onCreate() {

        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        System.out.println("Run Service under1");
        globalState = (GlobalState) getApplication();
      /*  Intent intent=getIntent();
        statusId = intent.getStringExtra("StatusId");
        System.out.println("flag is=====" + statusId);*/

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         formattedDate = df.format(c.getTime());
        System.out.println("Date is:"+formattedDate);

    }


    @Override
    public void onStart(Intent intent, int startId) {
        try {
            System.out.println("Run Service onStart");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            listener = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TEN_MINUTES, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TEN_MINUTES, 0, listener);

            startTimer();
            // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 60000, 60000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                if(isNetworkAvailable()){
               new AsyncTaskGetEvent().execute();
                }else{
                    Toast.makeText(LocationService.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TEN_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TEN_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        try {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);
    }catch (Exception e){

        }
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    //Current address get
    public String GetAddress(String lat, String lon)
    {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        String ret = "";
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if(addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("\n");
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                ret = strReturnedAddress.toString();
            }
            else{
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }

    public Intent getIntent() {
        return intent;
    }

    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location loc)
        {
            System.out.println("Inside on location changed");

            // Log.i("**************************************", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {

                Currentlat =loc.getLatitude();
                Currentlng = loc.getLongitude();
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                System.out.println("Latitude is :" + loc.getLatitude());

                //  Address=GetAddress(lat, lon);
               // str=GetAddress(lat, lon);

              //  Address = str.replace(",", "");
            //    System.out.println("Your Current address is==========" + Address);
             //  Toast.makeText(LocationService.this,"Latitude "+loc.getLatitude(),Toast.LENGTH_SHORT).show();
                try {

                  //  new AsyncTaskGetEvent().execute();

                   /* if (utils.isNetworkAvailable()) {
                        new AsyncTaskLatLongPost().execute(loc.getLatitude()+"",loc.getLongitude()+"");
                    } else {
                        utils.setToast(LocationService.this, "Please Connect to Internet");
                    }*/
                }catch (Exception e){
                    e.printStackTrace();
                }
                sendBroadcast(intent);
            }
        }

        public void onProviderDisabled(String provider)
        {
           // Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }
        public void onProviderEnabled(String provider)
        {
           // Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
    private class AsyncTaskGetEvent extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(LocationService.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(LocationService.this).getString("AccessToken", "");




            HttpClient Client = new DefaultHttpClient();


          //  URL = InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%20FROM%20Event";
         //  URL = InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%2CWhoID%2CWho.Name%2CWhatId%2CAccount.Name%20FROM%20Event";
            URL = InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%2CWhoID%2CWho.Name%2CWhatId%2CAccount.Name%20FROM%20Event%20WHERE%20ActivityDate%20%3D%20"+formattedDate;


            Log.e("httpget", URL);

            try
            {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(URL);
                httpget.addHeader("Authorization", "Bearer "+AccessToken);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);

                // Show response on activity


            }
            catch(Exception ex)
            {
                return "Exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String ContactName=null,AccountName = null,WhatId,WhoId;
            if((s.equalsIgnoreCase("Exception"))||(s.equalsIgnoreCase("UnsupportedEncodingException"))){

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            }else{

                try {
                    JSONObject obj=new JSONObject(s.toString());
                    Log.e(TAG,s.toString());




                    JSONArray jsonArray=obj.getJSONArray("records");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        String Subject=  jsonArray.getJSONObject(i).getString("Subject");
                        String Location=  jsonArray.getJSONObject(i).getString("Location");
                         WhoId=  jsonArray.getJSONObject(i).getString("WhoId");
                         WhatId=  jsonArray.getJSONObject(i).getString("WhatId");
                      // String  AccountId=  jsonArray.getJSONObject(i).getString("Account");
                        System.out.println("Location::"+Location);
                        System.out.println("WhoId::"+WhoId);

                 if(WhoId.equalsIgnoreCase("null"))
                 {
                      ContactName="";
                 }else
                 {
                      ContactName=  jsonArray.getJSONObject(i).getJSONObject("Who").getString("Name");
                     System.out.println("ContactName::"+ContactName);
                 }
                 if (WhatId.equalsIgnoreCase("null"))
                  {
                      AccountName="";
                     }
                   else
                     {
                  AccountName=  jsonArray.getJSONObject(i).getJSONObject("Account").getString("Name");
              System.out.println("AccountName::"+AccountName);
                   }



                        getLocationFromAddress(getApplicationContext(),Location,Subject,AccountName,WhatId,ContactName,WhoId);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public LatLng getLocationFromAddress(Context context, String strAddress, String Subject,String AccountName,String AccountId, String ContactName, String ContactId) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;
        LatLng p2 = null;
        LatLng p3 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            System.out.println("Latitude is::::"+ location.getLatitude());

          //  latLng1 = new LatLng(location.getLatitude(),location.getLongitude());


            System.out.println("Currentlat::"+Currentlat);
            System.out.println("Currentlng::"+Currentlng);
            System.out.println("Serverlat::"+location.getLatitude());
            System.out.println("Serverlat::"+location.getLongitude());


            // lat1 and lng1 are the values of a previously stored location
            if (distance(location.getLatitude(), location.getLongitude(), Currentlat, Currentlng) < 0.1) { // if distance < 0.1 miles we take locations as equal
                //do what you want to do...
               // EventSubject.setText(Subject);
               // alertDialogEvent(strAddress);
                //notifiy

                PreferenceManager.getDefaultSharedPreferences(LocationService.this).edit().putString("EventSubject",Subject).commit();
                PreferenceManager.getDefaultSharedPreferences(LocationService.this).edit().putString("AccountName",AccountName).commit();
                PreferenceManager.getDefaultSharedPreferences(LocationService.this).edit().putString("AccountId",AccountId).commit();
                PreferenceManager.getDefaultSharedPreferences(LocationService.this).edit().putString("ContactName",ContactName).commit();
                PreferenceManager.getDefaultSharedPreferences(LocationService.this).edit().putString("ContactId",ContactId).commit();
                Notification(strAddress,Subject);
                // Toast.makeText(NoteActivity.this, "Event this place", Toast.LENGTH_SHORT).show();
            }else
            {
                // Toast.makeText(NoteActivity.this, "No Event this Place", Toast.LENGTH_SHORT).show();
            }
            //    LatLng latLng = new LatLng(jsonArray.getJSONObject(i).getDouble("Latitude"),jsonArray.getJSONObject(i).getDouble("Longitude"));

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }
    public void Notification(String Address,String EventName) {
        // Set Notification Title
        String strtitle = EventName;
        // Set Notification Text
        String strtext = Address;

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, TaskActivity.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.sales)
                // Set Ticker Message
                .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle(strtitle)
                // Set Text
                .setContentText(strtext)
                // Add an Action Button below Notification
                //   .addAction(R.drawable.ic_launcher, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}