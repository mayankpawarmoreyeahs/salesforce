package demo.recycle.com.salforceapp.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import demo.recycle.com.salforceapp.GlobalState;
import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.Utils.Utils;
import demo.recycle.com.salforceapp.adapter.ListViewAdapter;
import demo.recycle.com.salforceapp.pojo.Taskpojo;

public class EventList extends AppCompatActivity {
    ArrayList<Taskpojo> arraylist = new ArrayList<Taskpojo>();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        utils=new Utils(this);
        // Locate the ListViewWithWeb in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        start_date = (TextView) findViewById(R.id.start_date);
        btnEvent = (Button) findViewById(R.id.getEvent);
         start=(ImageView) findViewById(R.id.startImage);


        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist,"event");

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




                if(date.equalsIgnoreCase(""))
                {

                    utils.setToast(EventList.this,"Please select date");
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


                    if(utils.isNetworkAvailable()) {
                        new EventList.AsyncTaskGetEvent().execute(formattedDate);
                    }else
                    {
                        utils.setToast(EventList.this,"Please Connect to Internet");
                    }

                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StartDate();
            }
        });
    }

    private class AsyncTaskGetEvent extends AsyncTask<String,Void,String> {

        ProgressDialog pd=new ProgressDialog(EventList.this);
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
            String date = params[0];
            // URLEncode user defined data
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(EventList.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(EventList.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL =InstanceUrl+"/services/data/v40.0/query?q=SELECT%20Id%2C%20Subject%2C%20ActivityDate%20%2C%20Account.Name%20FROM%20Event%20WHERE%20ActivityDate%20%3D%20"+date;

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
                        Toast.makeText(EventList.this, "Records are not found!!!", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        jsonArray=obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Taskpojo eventName = new Taskpojo(jsonArray.getJSONObject(i).getString("Id"),jsonArray.getJSONObject(i).getString("Subject"),jsonArray.getJSONObject(i).getString("ActivityDate"));
                            arraylist.add(eventName);
                        }

                        start_date.setText("");
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
        DatePickerDialog dialog = new DatePickerDialog(EventList.this, new EventList.mDateSetListener(), mYear, mMonth, mDay);dialog.show();
    }
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            // Month is 0 based so add 1
            // EventDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));
            start_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
            System.out.println(start_date.getText().toString());


        }
    }


}
