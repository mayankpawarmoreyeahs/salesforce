package demo.recycle.com.salforceapp.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.SqliteHelper.DatabaseHelper;
import io.github.mthli.knife.KnifeText;

public class CreateTaskActivity extends AppCompatActivity {

    Button createtask,quicktask,createnotes,alltask;
    String InstanceUrl, AccessToken, UserId,Type,id;
    String imageinstring,imageinstring1;
     int       numberofimage;
    Boolean flagforattachment;
    String Activedate;
    String name;
    String description;
    Dialog pDialog;
    DatabaseHelper DB;
    String  sucessid;
    DrawerLayout mDrawerLayout;

    String nameofimage,nameofimage1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Intent intent = getIntent();
         Activedate = intent.getStringExtra("Activedate");
         name = intent.getStringExtra("name");
         description = intent.getStringExtra("description");
         Type=intent.getStringExtra("type");
         flagforattachment=intent.getBooleanExtra("FlagForAttachment",false);
         numberofimage=intent.getIntExtra("numberofimage",0);

        Log.d("mayank", "mayank"+numberofimage+flagforattachment);

        if(flagforattachment) {


       //     if (numberofimage == 1) {

      //          imageinstring = intent.getStringExtra("body");
      //          nameofimage = intent.getStringExtra("nameofimage");

     //       }

     //       if (numberofimage == 2) {

                imageinstring = intent.getStringExtra("body");
                nameofimage = intent.getStringExtra("nameofimage");
                Log.d("mayank0", "mayank"+nameofimage+imageinstring);

                imageinstring1=intent.getStringExtra("body1");
                nameofimage1 = intent.getStringExtra("nameofimage1");
                Log.d("mayank1", "mayank"+nameofimage1+imageinstring1);




     //       }

        }

        if (Type != null) {
            if (Type.equalsIgnoreCase("draft")) {

              id  =intent.getStringExtra("id");;
            }
        }








         intialization();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;




        }

        return true;
    }

    public void intialization()
  {

       pDialog = new ProgressDialog(CreateTaskActivity.this);
       DB=new DatabaseHelper(this);
      InstanceUrl = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("InstanceUrl", "");
      AccessToken = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("AccessToken", "");
      UserId = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("UserId", "");
      createtask=(Button)findViewById(R.id.createtask);
      quicktask=(Button)findViewById(R.id.quicktask);
      createnotes=(Button)findViewById(R.id.createnote);
      alltask=(Button)findViewById(R.id.alltask);

      createtask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {



              pDialog.setTitle("LOADING");
              pDialog.setCancelable(false);
              pDialog.show();

                                new HttpAsyncTaskCreateTask().execute();






          }
      });

      quicktask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              pDialog.setTitle("LOADING");
              pDialog.setCancelable(false);
              pDialog.show();

              new HttpAsyncTaskCreateTask().execute();


          }
      });

      createnotes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              new HttpAsyncTaskNote().execute();

          }
      });

      alltask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              CreateTaskActivity.this.startActivity(new Intent(CreateTaskActivity.this, TaskList.class));


          }
      });


      mDrawerLayout = findViewById(R.id.drawer_layout);


      NavigationView navigationView = findViewById(R.id.navigation);
      navigationView.setNavigationItemSelectedListener(
              new NavigationView.OnNavigationItemSelectedListener() {
                  @Override
                  public boolean onNavigationItemSelected(MenuItem menuItem) {
                      // set item as selected to persist highlight
                      switch (menuItem.getItemId()) {


                          case R.id.get_Task:
                              mDrawerLayout.closeDrawers();
                              CreateTaskActivity.this.startActivity(new Intent(CreateTaskActivity.this, TaskList.class));

                              break;

                          case R.id.get_Draft:
                              mDrawerLayout.closeDrawers();
                              UserId = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("UserId", "");



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


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      ActionBar actionbar = getSupportActionBar();
      actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_blac);
      actionbar.setDisplayHomeAsUpEnabled(true);


  }




    public  class HttpAsyncTaskCreateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";




            // OAuthTokens myTokens = globalState.getAccessTokens();
            String url = InstanceUrl + "/services/data/v40.0/sobjects/Task";
            // String AccessToken=  myTokens.get_access_token();
            //  String userString=myTokens.get_id();
            //UserId Splite
            // String UserId = userString.substring(Math.max(userString.length() - 18, 0));
            System.out.println("UserId::::" + UserId);
            String AccountId1 = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("AccountId", "");
            System.out.println("AccountId1::::" + AccountId1);
            String ContactId1 = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("ContactId", "");
            byte[] data = new byte[0];





            DefaultHttpClient client = new DefaultHttpClient();
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                // 3. build jsonObject
                System.out.println("ContactId:::" + ContactId1);
                System.out.println("AccountId:::" + AccountId1);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("WhatId", AccountId1);
                jsonObject.accumulate("WhoId", ContactId1);
                jsonObject.accumulate("OwnerId", UserId);
                jsonObject.accumulate("Description",description);
                jsonObject.accumulate("Status", "Not Started");
                jsonObject.accumulate("Priority", "Normal");
                jsonObject.accumulate("Subject", name);
                jsonObject.accumulate("ActivityDate",Activedate);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();
                System.out.println("Json Value send::" + json);
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Authorization", "Bearer " + AccessToken);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

              pDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(result.toString());
             sucessid = obj.getString("id");


                //  WhatId,OwnerId,Description,Status,ActivityDate,Priority,Subject,WhoId


                if (flagforattachment) {

                    if (numberofimage == 1) {

                        new HttpAsyncTaskAttachment().execute(imageinstring,nameofimage);


                    }

                    if (numberofimage == 2) {

                        new HttpAsyncTaskAttachment().execute(imageinstring,nameofimage);
                        new HttpAsyncTaskAttachment().execute(imageinstring1,nameofimage1);



                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            if(Type!=null) {

                if (Type.equalsIgnoreCase("draft")) {
                    DB.DeleteDraft(id);
                }

            }


              Intent intent=new Intent(CreateTaskActivity.this,TaskActivity.class);
               startActivity(intent);

            Toast.makeText(getBaseContext(), "Data Sent!" + result, Toast.LENGTH_LONG).show();
        }
    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }





    private class HttpAsyncTaskAttachment extends AsyncTask<String, Void, String> {

        ProgressDialog pDialogt = new ProgressDialog(CreateTaskActivity.this);



        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            pDialogt.setMessage("loading...");
            pDialogt.setIndeterminate(false);
            pDialogt.setCancelable(true);
            pDialogt.show();

        }

        @Override
        protected String doInBackground(String... param) {
            String body=param[0];
            Log.d("mayank", "body"+body);
            String name=param[1];
            InputStream inputStream = null;
            String result = "";

            // OAuthTokens myTokens = globalState.getAccessTokens();
            String AccountId1 = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("AccountId", "");
            String url = InstanceUrl + "/services/data/v42.0/sobjects/Attachment";
            //  String AccessToken=  myTokens.get_access_token();
          /*  byte[] data = new byte[0];
            try {
                data = ImageInString.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

           */
            DefaultHttpClient client = new DefaultHttpClient();
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);

                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("ParentId", sucessid);

                jsonObject.put("Body",body);
                jsonObject.put("Name",name);
                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                System.out.println("Json Value send::" + json);
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Authorization", "Bearer " + AccessToken);

                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            pDialogt.dismiss();
            System.out.println("Result Note::" + result);
            try {
                JSONObject obj = new JSONObject(result.toString());

                //  Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

                //  new HttpAsyncTaskAccountLink().execute(obj.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }







    private class HttpAsyncTaskNote extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CreateTaskActivity.this);

            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";

            // OAuthTokens myTokens = globalState.getAccessTokens();
            String AccountId1 = PreferenceManager.getDefaultSharedPreferences(CreateTaskActivity.this).getString("AccountId", "");
            String url = InstanceUrl + "/services/data/v39.0/sobjects/Note";

            DefaultHttpClient client = new DefaultHttpClient();
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("ParentId", AccountId1);
                jsonObject.accumulate("Body", description);
                jsonObject.accumulate("Title", name);
                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                System.out.println("Json Value send::" + json);
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Authorization", "Bearer " + AccessToken);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            System.out.println("Result Note::" + result);
            try {
                JSONObject obj = new JSONObject(result.toString());

                //  Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
                Log.d("Result CreateNote::", result);

              //  new TaskActivity.HttpAsyncTaskAccountLink().execute(obj.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }






















}
