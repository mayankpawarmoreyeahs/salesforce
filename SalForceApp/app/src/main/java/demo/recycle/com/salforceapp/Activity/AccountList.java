package demo.recycle.com.salforceapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import demo.recycle.com.salforceapp.GlobalState;
import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.Utils.Utils;
import demo.recycle.com.salforceapp.adapter.SearchListViewAdapter;
import demo.recycle.com.salforceapp.pojo.PersonNames;

public class AccountList extends AppCompatActivity {
    ArrayList<PersonNames> arraylist = new ArrayList<PersonNames>();
    ListView list;
    SearchListViewAdapter adapter;
    SearchView editsearch;
    String[] personNameList;
    GlobalState globalState;
    String TAG="Insert";
    String flag;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalState = (GlobalState) getApplication();
        setContentView(R.layout.activity_account_list);
        utils=new Utils(this);
        // Locate the ListViewWithWeb in listview_main.xml
        list = (ListView) findViewById(R.id.listview);



        // Pass results to ListViewAdapter Class
        adapter = new SearchListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListViewWithWeb
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String text = newText;
                adapter.filter(text);
                flag="2";
                new AccountList.AsyncTaskGetAccount().execute(text);
                return false;

                //adapter.getFilter().filter(newText);
                //return false;

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonNames pojo = arraylist.get(position);
                System.out.println("List click PersonName=="+pojo.getPersonName());
                editsearch.setQuery(pojo.getPersonName(), false);
                Intent intent = new Intent(AccountList.this, TaskActivity.class);
                PreferenceManager.getDefaultSharedPreferences(AccountList.this).edit().putString("AccountName",pojo.getPersonName()).commit();
                PreferenceManager.getDefaultSharedPreferences(AccountList.this).edit().putString("AccountId",pojo.getPersonId()).commit();
               // intent.putExtra("PERSON_NAME",pojo.getPersonName());
                startActivity(intent);
            }
        });
        flag="1";
        if(utils.isNetworkAvailable()) {
            new AccountList.AsyncTaskGetAccount().execute("1");
        }else
        {
            utils.setToast(AccountList.this,"Please Connect to Internet");
        }

    }

    private class AsyncTaskGetAccount extends AsyncTask<String,Void,String> {

        ProgressDialog pd=new ProgressDialog(AccountList.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Account");
            pd.setCancelable(false);
            //  pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
            String search = params[0];
            // URLEncode user defined data
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(AccountList.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(AccountList.this).getString("AccessToken", "");
            String ContactId = PreferenceManager.getDefaultSharedPreferences(AccountList.this).getString("ContactId", "");
            /*String mobileValue    = URLEncoder.encode(mobileNo, "UTF-8");
            String fnameValue  = URLEncoder.encode(name.trim(), "UTF-8");
            String emailValue   = URLEncoder.encode(email.trim(), "UTF-8");
            String passValue    = URLEncoder.encode(password.trim(), "UTF-8");
            String companyValue   = URLEncoder.encode(companyname.trim(), "UTF-8");
            String designationValue    = URLEncoder.encode(designation.trim(), "UTF-8");*/
           /* OAuthTokens myTokens = globalState.getAccessTokens();
            String AccessToken=  myTokens.get_access_token();
            System.out.println("Token:"+AccessToken);*/


            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            if(ContactId.equalsIgnoreCase(""))
            {
                URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";
            }
           else if(search.equalsIgnoreCase("1"))
            {
                 URL =InstanceUrl+"/services/data/v40.0/query?q=Select+Id%2C+Name+From+Account+Where+Id+IN+%28Select+AccountId+From+Contact+where+id+%3D%27"+ContactId+"%27%29";
            }
            else {
                // URL =InstanceUrl+"/services/data/v39.0/search/?q=FIND%20%7B" + search + "%7D%20IN%20ALL%20FIELDS%20RETURNING%20Account(id,name)";
                URL =InstanceUrl+"/services/data/v22.0/query/?q=SELECT+id%2C+Name+FROM+Account+WHERE+Name+LIKE+%27"+search+"%25%27";
            }
           //  String URL = myTokens.get_instance_url()+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";

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
            pd.dismiss();

            if((s.equalsIgnoreCase("Exception"))||(s.equalsIgnoreCase("UnsupportedEncodingException"))){

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            }else{

                try {
                    JSONArray jsonArray = null;

                    JSONObject obj=new JSONObject(s.toString());
                    Log.e("All Search::",s.toString());
                    jsonArray=obj.getJSONArray("records");
                  /*  if(flag.equalsIgnoreCase("1")) {

                        jsonArray=obj.getJSONArray("records");

                    }else{

                        jsonArray=obj.getJSONArray("records");
                    }
               */

                    String totalSize=obj.getString("totalSize");
                    if(totalSize.equalsIgnoreCase("0"))
                    {
                        utils.setToast(AccountList.this,"No Account match in Contact id or no records");
                    }else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            PersonNames personNames = new PersonNames(jsonArray.getJSONObject(i).getString("Name"), jsonArray.getJSONObject(i).getString("Id"));
                            arraylist.add(personNames);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


}
