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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.SqliteHelper.DatabaseHelper;
import demo.recycle.com.salforceapp.Utils.Utils;
import demo.recycle.com.salforceapp.adapter.ListViewAdapter;
import demo.recycle.com.salforceapp.pojo.Taskpojo;

public class DraftList extends AppCompatActivity implements ListViewAdapter.ViewClick {

    ListView listview;
    ListViewAdapter adapter;
    DatabaseHelper db;
    List<Taskpojo> list;
    private DrawerLayout mDrawerLayout;
    TextView nameofuser;
    ImageView IMAGEofuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_list);
        // Locate the ListViewWithWeb in listview_main.xml
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.listview);
        db=DatabaseHelper.getInstance(this);
        list=db.getAllDraft();
        new DraftList.AsyncTaskGetUserInfo().execute();

        adapter = new ListViewAdapter(this,list,"draft");
        adapter.setViewClick(this);

        // Binds the Adapter to the ListViewWithWeb
        listview.setAdapter(adapter);


        NavigationView navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        nameofuser=(TextView) headerView.findViewById(R.id.name_of_user);
        IMAGEofuser=(ImageView)headerView.findViewById(R.id.imageViewofuser);





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
                                DraftList.this.startActivity(new Intent(DraftList.this, TaskList.class));

                                break;

                            case R.id.get_Draft:
                                startActivity(new Intent(DraftList.this, TaskActivity.class));
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

    @Override
    public void move(int position,String Type)
    {

        Intent i=new Intent(this,TaskActivity.class);
        i.putExtra("Activedate", list.get(position).getEventDate()+"");
        i.putExtra("name", list.get(position).getTaskName());
        i.putExtra("description", list.get(position).getDescription());
        i.putExtra("contactid", list.get(position).getContactid());
        i.putExtra("accountid", list.get(position).getAccountid());
        i.putExtra("id",list.get(position).getId());
        i.putExtra("type",Type);
        this.startActivity(i);
        finish();

    }


    public  class AsyncTaskGetUserInfo extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(DraftList.this);

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
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(DraftList.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(DraftList.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL = "https://na1.salesforce.com/services/data/v41.0/sobjects/User/"+PreferenceManager.getDefaultSharedPreferences(DraftList.this).getString("UserId", "");;

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


                    Picasso.Builder builder = new Picasso.Builder(DraftList.this).downloader(new OkHttp3Downloader(DraftList.this,Integer.MAX_VALUE));;
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



}
