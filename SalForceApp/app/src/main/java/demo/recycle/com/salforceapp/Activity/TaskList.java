package demo.recycle.com.salforceapp.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import demo.recycle.com.salforceapp.GlobalState;
import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.SqliteHelper.DatabaseHelper;
import demo.recycle.com.salforceapp.Utils.Utils;
import demo.recycle.com.salforceapp.adapter.ListViewAdapter;
import demo.recycle.com.salforceapp.pojo.*;

public class TaskList extends AppCompatActivity implements ListViewAdapter.ViewClick {


    List<Taskpojo> arraylist = new ArrayList<Taskpojo>();
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] personNameList;
    GlobalState globalState;
    String TAG="Insert";
    String flag;
    Utils utils;
    TextView start_date;
    Button btnEvent;
    ImageView start;
    DatabaseHelper DB;
    ProgressDialog progress;
    private DrawerLayout mDrawerLayout;
    TextView nameofuser;
    ImageView IMAGEofuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        progress = new ProgressDialog(this);






        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        NavigationView navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        nameofuser=(TextView) headerView.findViewById(R.id.name_of_user);
        IMAGEofuser=(ImageView)headerView.findViewById(R.id.imageViewofuser);

        new TaskList.AsyncTaskGetUserInfo().execute();




        utils=new Utils(this);
        // Locate the ListViewWithWeb in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        start_date = (TextView) findViewById(R.id.start_date);
        btnEvent = (Button) findViewById(R.id.getTask);
        start=(ImageView) findViewById(R.id.startImage);


        DateFormat originalFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);//These format come to server
        DateFormat targetFormat=new SimpleDateFormat("yyyy-MM-dd");
        //change to new format here

        Date date1= null;

        String formattedDate=targetFormat.format(new Date());  //result
        start_date.setText(formattedDate);
        System.out.println("Date format is::::"+formattedDate);






      //  arraylist=DB.getAllTask(formattedDate);
     //   new TaskList.AsyncTaskGetTask().equals("0");



        if(utils.isNetworkAvailable()) {
            new TaskList.AsyncTaskGetTask().execute(formattedDate);
        }else
        {
            utils.setToast(TaskList.this,"Please Connect to Internet");
        }


        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist,"task");
        adapter.setViewClick(this);

        // Binds the Adapter to the ListViewWithWeb
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Taskpojo pojo = arraylist.get(position);

                System.out.println("List click PersonName=="+pojo.getId());
                //  editsearch.setQuery(pojo.getEventName(), false);
                // Intent intent = new Intent(EventList.this, NoteActivity.class);
                // PreferenceManager.getDefaultSharedPreferences(EventList.this).edit().putString("AccountName",pojo.getPersonName()).commit();
                //   PreferenceManager.getDefaultSharedPreferences(EventList.this).edit().putString("AccountId",pojo.getPersonId()).commit();
                // intent.putExtra("PERSON_NAME",pojo.getPersonName());
                // startActivity(intent);
            }
        });
        flag="1";



        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date=start_date.getText().toString();
                Log.d("mayank", "onClick: "+date);




                if(date.equalsIgnoreCase(""))
                {

                    utils.setToast(TaskList.this,"Please select date");
                }else
                {

                    DateFormat originalFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);//These format come to server
                    DateFormat targetFormat=new SimpleDateFormat("yyyy-MM-dd");  //change to new format here
                    Date date1= null;
                    try {
                        date1 = originalFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate=targetFormat.format(date1);  //result
                    System.out.println("Date format is::::"+formattedDate);

                    progress.setTitle("LOADING");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false);
                    progress.show();



                   // arraylist=DB.getAllTask(formattedDate);

                  //  new TaskList.AsyncTaskGetTask().equals("0");

                    progress.dismiss();

                       if(utils.isNetworkAvailable()) {
                      new TaskList.AsyncTaskGetTask().execute(formattedDate);
                       }
                       else    {
                            utils.setToast(TaskList.this,"Please Connect to Internet");
                        }


                    // Pass results to ListViewAdapter Class
                    adapter = new ListViewAdapter(TaskList.this, arraylist,"task");
                    adapter.setViewClick(TaskList.this);

                    // Binds the Adapter to the ListViewWithWeb
                    list.setAdapter(adapter);

                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StartDate();
            }
        });


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        switch (menuItem.getItemId()) {

                            case android.R.id.home:
                                mDrawerLayout.openDrawer(GravityCompat.START);

                                break;

                            case R.id.get_Task:
                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(TaskList.this, TaskActivity.class));

                                break;

                            case R.id.get_Draft:
                                startActivity(new Intent(TaskList.this, DraftList.class));
                                finish();
                                break;

                            //   case R.id.logout:

                            //      mDrawerLayout.closeDrawers();
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://login.salesforce.com/services/oauth2/callback")));
                            //  startActivity(new Intent(this,EventList.class));
                            //System.out.println("Run");


                            //     PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().clear().commit();
                            //       PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("LOGOUT","1").commit();
                            //


                            //    clearApplicationData();
                            //  finish();
                            //     new  TaskActivity.AsyncTaskLogout().execute();
                            //    android.os.Process.killProcess(android.os.Process.myPid());
                            //    System.exit(1);

                            //  Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                            //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
                            //  startActivity(intent);


                            //  Intent intent = new Intent(TaskActivity.this, SfdcRestSample.class);
                            //  startActivity(intent);


                            //     break;
                            default:
                                break;


                            // close drawer when item is tapped


                            // Add code here to update the UI based on the item selected
                            // For example, swap UI fragments here


                        }
                        return true;
                    }
                });


        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_blac);
        actionbar.setDisplayHomeAsUpEnabled(true);





    }





    @Override
    public void move(int position,String type) {
       /* Intent myIntent = new Intent(TaskList.this, NoteActivity.class);
        myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
        */

        Intent i=new Intent(this,ShowTaskDetailActivity.class);
        i.putExtra("Activedate", arraylist.get(position).getEventDate()+"");
        i.putExtra("name", arraylist.get(position).getTaskName());
        i.putExtra("description", arraylist.get(position).getDescription());
        i.putExtra("contactid", arraylist.get(position).getContactid());
        i.putExtra("accountid", arraylist.get(position).getAccountid());
        i.putExtra("id",arraylist.get(position).getId());
        i.putExtra("type",type);

        this.startActivity(i);



    }

    private class AsyncTaskGetTask extends AsyncTask<String,Void,String> {

        ProgressDialog pd=new ProgressDialog(TaskList.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Loading");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
              pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
            String date = params[0];
            // URLEncode user defined data
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskList.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskList.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL ="https://veloxyapptest-dev-ed.my.salesforce.com/services/data/v42.0/query?q=SELECT%20ActivityDate%2CSubject%2CID%2CWhatId%2CWhoId%2COwnerId%2CDescription%2CStatus%2CPriority%20FROM%20Task%20WHERE%20ActivityDate%20%3D%20"+date;

            //  String URL = myTokens.get_instance_url()+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";

            Log.e("mayank", URL);

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
            pd.dismiss();
            Log.e("Date Event::",s.toString());
            if((s.equalsIgnoreCase("Exception"))||(s.equalsIgnoreCase("UnsupportedEncodingException"))){

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            }else{

                try {
                    JSONArray jsonArray = null;
                    JSONObject obj=new JSONObject(s.toString());
                    arraylist.clear();
                    if(obj.getString("totalSize").equalsIgnoreCase("0"))
                    {
                        Toast.makeText(TaskList.this, "Records are not found!!!", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        jsonArray=obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                           // EventNames(String eventName, String id, String eventDate, String description, String accountid, String contactid)
                            Taskpojo eventName = new Taskpojo(jsonArray.getJSONObject(i).getString("Subject"),jsonArray.getJSONObject(i).getString("Id"),jsonArray.getJSONObject(i).getString("ActivityDate"),jsonArray.getJSONObject(i).getString("Description"),jsonArray.getJSONObject(i).getString("WhatId"),jsonArray.getJSONObject(i).getString("WhoId"));
                            arraylist.add(eventName);

                        }

                        Log.d("mayank", "onPostExecute: "+arraylist.size());

                        //start_date.setText("");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    private void StartDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(TaskList.this, new TaskList.mDateSetListener(), mYear, mMonth, mDay);dialog.show();
    }
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear+ 1;
            int mDay = dayOfMonth;

            String mMonths = (mMonth < 10 ? "0" : "") + mMonth;
            String mDays = (mDay < 10 ? "0" : "") + mDay;
            // Month is 0 based so add 1
            // EventDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));
            start_date.setText(new StringBuilder().append(mYear).append("-").append(mMonths).append("-").append(mDays));
            System.out.println(start_date.getText().toString());


        }
    }



    public  class AsyncTaskGetUserInfo extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskList.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Loading");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;

            // URLEncode user defined data
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskList.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskList.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL = "https://na1.salesforce.com/services/data/v41.0/sobjects/User/"+PreferenceManager.getDefaultSharedPreferences(TaskList.this).getString("UserId", "");;

            //  String URL = myTokens.get_instance_url()+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";

            Log.e("httpget", URL);

            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(URL);
                httpget.addHeader("Authorization", "Bearer " + AccessToken);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);

                // Show response on activity


            } catch (Exception ex) {
                return "Exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.e("Date Event::", s.toString());
            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {
                    JSONArray jsonArray = null;
                    JSONObject obj = new JSONObject(s.toString());


                    String name=obj.getString("Name");
                    String photourl=obj.getString("MediumPhotoUrl");
                    Log.d("TAG", "onPostExecute:"+photourl);

                    URL url = null;
                    try {
                        url = new URL(photourl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    //   Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    //   IMAGEofuser.setImageBitmap(bmp);


                    Picasso.Builder builder = new Picasso.Builder(TaskList.this).downloader(new OkHttp3Downloader(TaskList.this,Integer.MAX_VALUE));;
                    builder.listener(new Picasso.Listener()
                    {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(photourl).error(R.drawable.cast_mini_controller_progress_drawable).resize(200,200).into(IMAGEofuser);


                 /*   Picasso.with(getApplicationContext()).load(photourl) .error(R.drawable.arrow).resize(30,30).centerCrop().into(IMAGEofuser, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
                    */
                    //    Glide.with(getApplicationContext())
                    //              .load(photourl).into(IMAGEofuser);


                    //     RequestOptions options = new RequestOptions()
                    //              .centerCrop()
                    //             .placeholder(R.drawable.arrow);
//



                    //     Glide.with(getApplicationContext()).load("https://veloxyapptest-dev-ed--c.ap5.content.force.com/profilephoto/005/F").apply(options).into(IMAGEofuser);


                    nameofuser.setText(name);



                    //start_date.setText("");



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //    adapter.notifyDataSetChanged();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;


            case R.id.get_Task:
                startActivity(new Intent(this, TaskList.class));
                break;

            //    case R.id.get_event:
            //        startActivity(new Intent(this, EventList.class));
            //        break;
            //     case R.id.logout:
            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://login.salesforce.com/services/oauth2/callback")));
            //  startActivity(new Intent(this,EventList.class));
            //System.out.println("Run");


            //        PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().clear().commit();
            //  PreferenceManager.getDefaultSharedPreferences(NoteActivity.this).edit().putString("LOGOUT","1").commit();
            //       clearApplicationData();
            //         new  TaskActivity.AsyncTaskLogout().execute();;

             /*  Intent intent = new Intent(NoteActivity.this, LoginActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
               startActivity(intent);*/
            //    android.os.Process.killProcess(android.os.Process.myPid());
            //     System.exit(1);

             /*  Intent intent = new Intent(NoteActivity.this, LoginActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                       | Intent.FLAG_ACTIVITY_CLEAR_TASK
                       | Intent.FLAG_ACTIVITY_NEW_TASK);*/
            //         break;
            default:
                break;
        }

        return true;
    }











}
