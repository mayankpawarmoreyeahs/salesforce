package demo.recycle.com.salforceapp.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_list);
        // Locate the ListViewWithWeb in listview_main.xml
        listview = (ListView) findViewById(R.id.listview);
        db=DatabaseHelper.getInstance(this);
        list=db.getAllDraft();

        adapter = new ListViewAdapter(this,list,"draft");
        adapter.setViewClick(this);

        // Binds the Adapter to the ListViewWithWeb
        listview.setAdapter(adapter);






























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


}
