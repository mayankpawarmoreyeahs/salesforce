package demo.recycle.com.salforceapp.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import demo.recycle.com.salforceapp.GlobalState;
import demo.recycle.com.salforceapp.LocationService;
import demo.recycle.com.salforceapp.R;


import demo.recycle.com.salforceapp.SqliteHelper.DatabaseHelper;
import demo.recycle.com.salforceapp.Utils.Utils;
import demo.recycle.com.salforceapp.adapter.SearchCustomContactAdapter;
import demo.recycle.com.salforceapp.adapter.SearchListViewAdapter;
import demo.recycle.com.salforceapp.pojo.PersonNames;
import demo.recycle.com.salforceapp.pojo.Taskpojo;
import demo.recycle.com.salforceapp.pojo.accountpojo;
import demo.recycle.com.salforceapp.pojo.contactpojo;
import io.github.mthli.knife.KnifeText;

public class TaskActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteContact, autoCompleteAcount;

    //class members
    String contact[] = {"Ajit", "Dhoni", "Sachin", "Ram", "Syam", "Yogesh", "Harsh", "Hanumant", "Syam"};
    //class members
    String account[] = {"ACME", "ACBE", "ACCD", "BDEF", "BCAC", "DIDR", "DCCI", "IDID", "ACID"};
    ArrayAdapter<String> adapterContact, adapterAccount;
    Button submit;
    String TAG = "Insert";
    // EditText  date;
    GlobalState globalState;
    boolean APIFlack = false;
    private JSONObject acct;
    private String errorMsg;
    ListView list;
    SearchListViewAdapter adapter;
    SearchView editsearch;
    String[] personNameList;
    ArrayList<PersonNames> arraylist = new ArrayList<PersonNames>();
    List<Taskpojo> tasklist = new ArrayList<Taskpojo>();

    private static final int CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int UPLOAD_REQUEST = 3;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    String[] ImageInString = new String[2];

    Button btnSave;
    String AccountId, ContactId;
    LinearLayout linearLayout1;
    TextView SearchAccount, SearchContact;

    String discription = "";
    String flag;
    LocationManager locationManager;
    String lat = "", lon = "";
    LatLng latLng1;
    double Currentlat;
    double Currentlng;
    private static final String BOLD = "<b>Bold</b><br><br>";
    private static final String ITALIT = "<i>Italic</i><br><br>";
    private static final String UNDERLINE = "<u>Underline</u><br><br>";
    private static final String STRIKETHROUGH = "<s>Strikethrough</s><br><br>"; // <s> or <strike> or <del>
    private static final String BULLET = "<ul><li>asdfg</li></ul>";
    private static final String QUOTE = "<blockquote>Quote</blockquote>";
    private static final String LINK = "<a href=\"https://github.com/mthli/Knife\">Link</a><br><br>";
    private static final String EXAMPLE = BOLD + ITALIT + UNDERLINE + STRIKETHROUGH + BULLET + QUOTE + LINK;
    private KnifeText knife;
    EditText EventSubject;
    TextView EventDate;
    // String sEventsubject;
    String sdate;
    String spinner1, spinner2;
    String sEvevtDate;
    String EnentAddress, sEventsubject;
    String InstanceUrl, AccessToken, UserId;
    String AccountName, ContactName;
    Utils utils;
    String flagButtonClick;
    String formattedDate;
    ProgressDialog pDialog, pDialog1,pDialog2;
    private Spinner spSelectContact, spSelectAccount;
    ArrayAdapter arrayAdapterContact, arrayAdapterAccount;
    ArrayList arrayListContact, arrayListAccount;
    private ArrayList<contactpojo> contactTypePojoArrayList;
    private ArrayList<accountpojo> accounttTypePojoArrayList;
    private RadioGroup radioGroup;
    private RadioButton radioBtnContact;
    private RadioButton radioBtnAccountt;
    static String SelectcontactAccount = "contact";
    ImageView SessionImageview;
    SearchCustomContactAdapter searchCustomContactAdapter;
    LinearLayout myLayout;
    private Uri imageUri;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    TextToSpeech t1;
    Dialog customDialog;
    ImageView thubnail, thubnail1;
    LinearLayout imagelayout, imagelayout1;
    ProgressDialog progress;
    TextView imagename, imagename1;
    private DrawerLayout mDrawerLayout;
    ExpandableListView slidemenus;
    String sucessid;
    String nameOfimage[] = new String[2];
    boolean FlagForAttachment = false;
    DatabaseHelper DB;
    List<String> listDataHeader;
    List<Taskpojo> draftlist;
    HashMap<String, List<String>> listDataChild;
    String Type = new String();
    String Id;
    TextView nameofuser;
    ImageView IMAGEofuser;
    int numberofimage;

    ImageView cross, cross1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        progress = new ProgressDialog(this);
        Type = new String();

        globalState = (GlobalState) getApplication();
        setContentView(R.layout.activity_note);
        new TaskActivity.AsyncTaskSession().execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        utils = new Utils(this);


        initialization();
        new TaskActivity.AsyncTaskGetUserInfo().execute();


        mDrawerLayout = findViewById(R.id.drawer_layout);

        //   slidemenus=findViewById(R.id.list_slidermenu);
        //    prepareListData();

        // ExpandableListAdapter   listAdapter = new ExpandableListAdapter(TaskActivity.this, listDataHeader, listDataChild);
        // setting list adapter
        //   slidemenus.setAdapter(listAdapter);

        NavigationView navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);


        nameofuser = (TextView) headerView.findViewById(R.id.name_of_user);
        IMAGEofuser = (ImageView) headerView.findViewById(R.id.imageViewofuser);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        switch (menuItem.getItemId()) {


                            case R.id.get_Task:
                                mDrawerLayout.closeDrawers();
                                if (!EventSubject.getText().toString().matches("")) {
                                    pDialog1 = new ProgressDialog(TaskActivity.this);
                                    pDialog1.setMessage("DraftSaving...");
                                    pDialog1.setIndeterminate(false);
                                    pDialog1.setCancelable(true);
                                    pDialog1.show();
                                    DB.insertDraft(AccountId, UserId, knife.getText().toString(), "Not Started", sdate, "Normal", EventSubject.getText().toString(), ContactId);
                                    pDialog1.dismiss();

                                }

                                startActivity(new Intent(TaskActivity.this, TaskList.class));

                                break;


                            case R.id.get_Draft:
                                mDrawerLayout.closeDrawers();
                                UserId = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("UserId", "");

                                if (!EventSubject.getText().toString().matches("")) {
                                    pDialog2 = new ProgressDialog(TaskActivity.this);
                                    pDialog2.setMessage("DraftSaving...");
                                    pDialog2.setIndeterminate(false);
                                    pDialog2.setCancelable(true);
                                    pDialog2.show();
                                    DB.insertDraft(AccountId, UserId, knife.getText().toString(), "Not Started", sdate, "Normal", EventSubject.getText().toString(), ContactId);
                                    pDialog2.dismiss();

                                }

                                startActivity(new Intent(TaskActivity.this, DraftList.class));

                                break;

                            case R.id.logout:

                                mDrawerLayout.closeDrawers();
                                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://login.salesforce.com/services/oauth2/callback")));
                                //  startActivity(new Intent(this,EventList.class));
                                //System.out.println("Run");


                                //     PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().clear().commit();
                                //       PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("LOGOUT","1").commit();
                                //


                                //    clearApplicationData();
                                //  finish();
                                new TaskActivity.AsyncTaskLogout().execute();
                                //  android.os.Process.killProcess(android.os.Process.myPid());
                                //   System.exit(1);

                                //  Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                                //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
                                //  startActivity(intent);
                                new TaskActivity.AsyncTaskSession().execute();

                                //  Intent intent = new Intent(TaskActivity.this, TaskActivity.class);
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


        SearchAccount = (TextView) findViewById(R.id.SearchAccount);
        SearchContact = (TextView) findViewById(R.id.SearchContact);
        customDialog = new Dialog(this, R.style.CustomDialog);
        imagename = (TextView) findViewById(R.id.image_name);
        imagename1 = (TextView) findViewById(R.id.image_name1);
        ContactName = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("ContactName", "");

        SearchAccount.setText(AccountName);
        SearchContact.setText(ContactName);

        EventSubject = (EditText) findViewById(R.id.Task_subject);
        EventDate = (TextView) findViewById(R.id.event_date);
        knife = (KnifeText) findViewById(R.id.knife);
        thubnail = (ImageView) findViewById(R.id.thubnail);
        thubnail1 = (ImageView) findViewById(R.id.thubnail1);
        imagelayout = (LinearLayout) findViewById(R.id.imagelayout);
        imagelayout1 = (LinearLayout) findViewById(R.id.imagelayout1);
        cross = findViewById(R.id.cross);
        cross1 = findViewById(R.id.cross1);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (numberofimage == 2) {

                    ImageInString[0] = ImageInString[1];
                    ImageInString[0] = ImageInString[1];
                    ImageInString[1] = "";
                    nameOfimage[1] = "";
                    numberofimage = 1;

                } else if (numberofimage == 1) {

                    ImageInString[0] = "";
                    ImageInString[0] = "";
                    ImageInString[1] = "";
                    nameOfimage[1] = "";
                    numberofimage = 0;

                }


                imagename.setText("");
                imagelayout.setVisibility(View.GONE);

            }
        });

        cross1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numberofimage == 2) {

                    ImageInString[1] = "";
                    nameOfimage[1] = "";
                    numberofimage = 1;
                } else if (numberofimage == 1) {

                    ImageInString[0] = "";
                    ImageInString[0] = "";
                    ImageInString[1] = "";
                    nameOfimage[1] = "";
                    numberofimage = 0;

                }


                imagename1.setText("");
                imagelayout1.setVisibility(View.GONE);


                Log.d("mayank3", "onClick:" + ImageInString[0]);


            }
        });


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());
        System.out.println("Date is:" + formattedDate);
       /* OAuthTokens myTokens = globalState.getAccessTokens();
        String InstanceUrl = myTokens.get_instance_url();
        String Id=  myTokens.get_id();*/

        radioGroup = (RadioGroup) findViewById(R.id.radioGorup);
        radioBtnContact = (RadioButton) findViewById(R.id.radioContact);
        radioBtnAccountt = (RadioButton) findViewById(R.id.radioAccount);
        ImageButton camera = (ImageButton) findViewById(R.id.camera);


        Intent intent = getIntent();
        if (intent != null) {
            String Activedate = intent.getStringExtra("Activedate");
            String name = intent.getStringExtra("name");
            String description = intent.getStringExtra("description");
            Type = intent.getStringExtra("type");
            Log.d("mayank", "onCreate: " + Type);

            if (Type != null) {
                if (Type.equalsIgnoreCase("draft")) {

                    Id = intent.getStringExtra("id");
                }
            }

            if (name != null) {
                EventSubject.setText(name);
            }
            if (knife != null) {
                knife.setText(description);
            }
        }


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
                showPictureDialog();
            }
        });
        ImageButton voice = (ImageButton) findViewById(R.id.voice);


        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*  String toSpeak = knife.getText().toString();
                  Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                 t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                */

                startVoiceInput();


            }
        });


        if (SelectcontactAccount.equalsIgnoreCase("contact")) {
            if (utils.isNetworkAvailable()) {
                new TaskActivity.AsyncTaskGetContact().execute("1");
            } else {
                utils.setToast(TaskActivity.this, "Please Connect to Internet");
            }
        }
        myLayout = (LinearLayout) findViewById(R.id.ler_first);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                autoCompleteContact.setText("");
                autoCompleteAcount.setText("");
                arrayListContact.clear();
                arrayListAccount.clear();
                myLayout.requestFocus();
                if (radioBtnContact.isChecked()) {
                    SelectcontactAccount = "contact";


                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetContact().execute("1");
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }

                } else if (radioBtnAccountt.isChecked()) {

                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetAccount().execute("1");
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }
                    SelectcontactAccount = "account";
                }
                // Toast.makeText(getApplicationContext(), selectedSuperStar, Toast.LENGTH_LONG).show(); // print the value of selected super star
                System.out.println("Value is :===" + SelectcontactAccount);
            }
        });

        InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
        AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");
        UserId = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("UserId", "");
       /* sEventsubject = PreferenceManager.getDefaultSharedPreferences(NoteActivity.this).getString("EventSubject", "");
        System.out.println("Subject::::" + sEventsubject);
        System.out.println("AccessToken::::" + AccessToken);
        if (sEventsubject.equalsIgnoreCase("")) {
            new NoteActivity.AsyncTaskGetEvent().execute();
        }
        */

        // knife.fromHtml(EXAMPLE);
        knife.setSelection(knife.getEditableText().length());
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);
        // Retrive the data from MainActivity.java
        final Intent i = getIntent();
        //  sEventsubject = i.getStringExtra("title");
        EnentAddress = i.getStringExtra("text");
        // EventSubject.setText(sEventsubject);
        System.out.println("sEventsubject::" + sEventsubject);

        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupClear();
        flag = "2";
        flagButtonClick = "1";
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flag = "1";
                } else {
                    flag = "2";
                }
            }
        });
        autoCompleteContact = (AutoCompleteTextView) findViewById(R.id.contact);
        autoCompleteAcount = (AutoCompleteTextView) findViewById(R.id.acount);
        autoCompleteContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoCompleteContact.showDropDown();

                }

            }
        });
        autoCompleteAcount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoCompleteAcount.showDropDown();

                }

            }
        });
        ImageView start = (ImageView) findViewById(R.id.calender);
        btnSave = (Button) findViewById(R.id.save);
        linearLayout1 = (LinearLayout) findViewById(R.id.ler1);

        //  date=(EditText)findViewById(R.id.date) ;
        //  adapterContact = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, contact);
        //  autoCompleteContact.setAdapter(adapterContact);
        //  adapterAccount = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, account);
        //  autoCompleteAcount.setAdapter(adapterAccount);

        autoCompleteContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String s = parent.getItemAtPosition(position).toString();
                String ContactId = contactTypePojoArrayList.get(position).getId();
                String ContactName = contactTypePojoArrayList.get(position).getName();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactName", ContactName).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactId", ContactId).commit();
                Log.d("mayank", "onItemClick: " + ContactId);
               /* Toast toast= Toast.makeText(getApplicationContext(), ContactId + " is clicked", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();*/

                if (SelectcontactAccount.equalsIgnoreCase("contact")) {
                    autoCompleteAcount.setText("");

                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetAccount().execute(ContactId);
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }
                } else {
                    System.out.println("No Value");
                }
            }
        });
        autoCompleteAcount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String AccountId = accounttTypePojoArrayList.get(position).getId();
                String AccountName = accounttTypePojoArrayList.get(position).getName();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountName", AccountName);
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountId", AccountId).commit();

              /*  Toast toast= Toast.makeText(getApplicationContext(), AccountId + " is clicked", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();*/

                if (SelectcontactAccount.equalsIgnoreCase("account")) {

                    autoCompleteContact.setText("");

                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetContact().execute(AccountId);
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }
                } else {
                    System.out.println("No Value");
                }

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartDate();
            }
        });

        SearchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, AccountList.class));
            }
        });
        SearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ContactList.class));
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sEventsubject = EventSubject.getText().toString();
                if (sEventsubject.equalsIgnoreCase("")) {
                    utils.setToast(TaskActivity.this, "Enter The Task Name!!!!!");
                } else {
                    sdate = EventDate.getText().toString();
                    System.out.println("Flag::" + sEventsubject);

               /* else if (AccountName.equalsIgnoreCase("")) {
                    utils.setToast(NoteActivity.this, "Please select Account name");
                } else if (ContactName.equalsIgnoreCase("")) {
                    utils.setToast(NoteActivity.this, "Please select Contact name");
                }
                else if (discription.equalsIgnoreCase("")) {
                    utils.setToast(NoteActivity.this, "Please select description");
                }
                 */

                    if (utils.isNetworkAvailable()) {


                        Intent intent = new Intent(TaskActivity.this, LocationService.class);
                        startService(intent);
                        // if (flag.equalsIgnoreCase("1")) {

                        if (sdate.equalsIgnoreCase("")) {
                            utils.setToast(TaskActivity.this, "Please select date");

                        } else {
                               /* flagButtonClick = "2";
                                progress.setTitle("LOADING");
                                progress.setMessage("Wait while loading...");
                                progress.setCancelable(false);
                                progress.show();


                                new HttpAsyncTaskCreateTask().execute();

                                */

                            Intent intentforbutton = new Intent(TaskActivity.this, CreateTaskActivity.class);
                            intentforbutton.putExtra("Activedate", EventDate.getText().toString());
                            intentforbutton.putExtra("name", EventSubject.getText().toString());
                            intentforbutton.putExtra("description", knife.getText().toString());
                            intentforbutton.putExtra("type", Type);
                            intentforbutton.putExtra("FlagForAttachment", FlagForAttachment);
                            intentforbutton.putExtra("numberofimage", numberofimage);
                            if (Type != null) {
                                if (Type.equalsIgnoreCase("draft")) {

                                    intentforbutton.putExtra("id", Id);
                                }
                            }


                            if (FlagForAttachment) {

                                if (numberofimage == 1) {
                                    intentforbutton.putExtra("nameofimage", nameOfimage[0]);
                                    intentforbutton.putExtra("body", ImageInString[0]);

                                }


                                if (numberofimage == 2) {
                                    intentforbutton.putExtra("nameofimage", nameOfimage[0]);
                                    intentforbutton.putExtra("body", ImageInString[0]);
                                    intentforbutton.putExtra("nameofimage1", nameOfimage[1]);
                                    intentforbutton.putExtra("body1", ImageInString[1]);
                                }

                            }


                            startActivity(intentforbutton);





                            /*    if(!APIFlack)
                                {
                                    new HttpAsyncTaskNote().execute();
                                }

                                else
                                {
                                    new HttpAsyncTaskAttachment().execute();
                                }

                              */
                        }


                    }
                    //else
                    //     {
                    //    if(!APIFlack)
                    //     {
                    //         new HttpAsyncTaskNote().execute();
                    //    }
                    //     else
                    //     {
                    //         new HttpAsyncTaskAttachment().execute();
//
                    //   }
                    //  }

                    else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet. Saved as Draft ");
                        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this);
                        String userid = shared.getString("UserId", "");


                        DB.insertDraft(AccountId, userid, knife.getText().toString(), "Not Started", sdate, "Normal", EventSubject.getText().toString(), ContactId);
                        flagButtonClick = "1";
                        autoCompleteContact.setText("");
                        autoCompleteAcount.setText("");
                        knife.setText("");
                        EventSubject.setText("");
                        EventDate.setText("");
                        numberofimage = 0;
                        imagelayout.setVisibility(View.GONE);
                        imagelayout1.setVisibility(View.GONE);

                        thubnail.setBackground(null);
                        thubnail1.setBackground(null);
                        imagename.setText("");
                        imagename1.setText("");


                        arrayListContact.clear();
                        arrayListAccount.clear();
                        myLayout.requestFocus();
                        progress.dismiss();

                    }

                }
            }

        });






/*

        if(utils.isNetworkAvailable()) {
            new NoteActivity.AsyncTaskGetContact().execute("1");
        }else
        {
            utils.setToast(NoteActivity.this,"Please Connect to Internet");
        }
*/

      /*  if(utils.isNetworkAvailable()) {
            new NoteActivity.AsyncTaskGetAccount().execute("1");
        }else
        {
            utils.setToast(NoteActivity.this,"Please Connect to Internet");
        }
*/
        spSelectContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // TODO Auto-generated method stub


                //  ArrayList<contactpojo> problemTypePojoArrayList=new ArrayList<>();

                //  ArrayList<ProblemTypePojo> problemCategoryArrayList=new ArrayList<>();
                //  problemCategoryArrayList=problemCategoryDb.getProblemCategory();

                for (int i = 0; i < contactTypePojoArrayList.size(); i++) {

                    if (spSelectContact.getSelectedItem().toString().equals(contactTypePojoArrayList.get(i).getName())) {
                        spinner1 = contactTypePojoArrayList.get(i).getId();
                        ContactName = contactTypePojoArrayList.get(i).getName();
                    }

                }
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactName", ContactName).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactId", spinner1).commit();
                System.out.println("ContactIdsp:::==" + spinner1);
                if (SelectcontactAccount.equalsIgnoreCase("contact")) {
                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetAccount().execute(spinner1);
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }
                } else {
                    System.out.println("No Value");
                }

             /*   int spinnerValue1 = spSelectParty.getSelectedItemPosition();
                spinner1=spinnerValue1+1;
                System.out.println("Spinner position is"+spinner1);
*/
            /*    if(singleton.isConnectingToInternet(Sales_SaleForm.this)){
                    callwsForItem();

                }
                else{
                    Toast.makeText(Sales_SaleForm.this, "No Internet", Toast.LENGTH_SHORT).show();}
                // count = position; //this would give you the id of the selected item*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spSelectAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // TODO Auto-generated method stub


                //  ArrayList<accountpojo> problemTypePojoArrayList=new ArrayList<>();

                //  ArrayList<ProblemTypePojo> problemCategoryArrayList=new ArrayList<>();
                //  problemCategoryArrayList=problemCategoryDb.getProblemCategory();

                for (int i = 0; i < accounttTypePojoArrayList.size(); i++) {

                    if (spSelectAccount.getSelectedItem().toString().equals(accounttTypePojoArrayList.get(i).getName())) {
                        spinner2 = accounttTypePojoArrayList.get(i).getId();
                        AccountName = accounttTypePojoArrayList.get(i).getName();
                    }

                }
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountName", AccountName);
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountId", spinner2).commit();
                System.out.println("AccountIdSp:::::::::" + spinner2);
                if (SelectcontactAccount.equalsIgnoreCase("account")) {
                    if (utils.isNetworkAvailable()) {
                        new TaskActivity.AsyncTaskGetContact().execute(spinner2);
                    } else {
                        utils.setToast(TaskActivity.this, "Please Connect to Internet");
                    }
                } else {
                    System.out.println("No Value");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


//Get Event here


        // Latitude and longitude send
        locationManager = (LocationManager)

                getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

        {
            //  Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else

        {
            showGPSDisabledAlertToUser();
        }

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // lat = Double.toString(location.getLatitude());
                //  lon = Double.toString(location.getLongitude());
                System.out.println("latitute is 1  ++++++++" + lat);
                Currentlat = location.getLatitude();
                Currentlng = location.getLongitude();

                // lat1 and lng1 are the values of a previously stored location
               /* if (distance(lat1, lng1, Currentlat, Currentlng) < 0.1) { // if distance < 0.1 miles we take locations as equal
                    //do what you want to do...

                    Toast.makeText(NoteActivity.this, "Event this place", Toast.LENGTH_SHORT).show();
                }
*/


                //  str=GetAddress(lat, lon);

                // Address = str.replace(",", "");
                //  System.out.println("Address of home screen is  ++++++++"+Address);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(TaskActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TaskActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }


    private void startVoiceInput() {


        customDialog.show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }


    public TaskActivity() {
        super();
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void initialization() {
        SessionImageview = (ImageView) findViewById(R.id.session);
        arrayListContact = new ArrayList();
        arrayListAccount = new ArrayList();
        arrayAdapterContact = new ArrayAdapter(TaskActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListContact);
        arrayAdapterAccount = new ArrayAdapter(TaskActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListAccount);
        spSelectContact = (Spinner) findViewById(R.id.spinner_contact);
        spSelectAccount = (Spinner) findViewById(R.id.spinner_account);
        spSelectContact.setAdapter(arrayAdapterContact);
        spSelectAccount.setAdapter(arrayAdapterAccount);
        DB = DatabaseHelper.getInstance(this);


        draftlist = DB.getAllDraft();


    }

    private void StartDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(TaskActivity.this, R.style.Dialogtheme,
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear + 1;
            int mDay = dayOfMonth;

            String mMonths = (mMonth < 10 ? "0" : "") + mMonth;
            String mDays = (mDay < 10 ? "0" : "") + mDay;
            // Month is 0 based so add 1
            // EventDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append("/").append(mYear).append(" "));
            EventDate.setText(new StringBuilder().append(mYear).append("-").append(mMonths).append("-").append(mDays));
            System.out.println(EventDate.getText().toString());

        }
    }


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    private class HttpAsyncTaskNote extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(TaskActivity.this);
            pDialog.setMessage("loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";

            // OAuthTokens myTokens = globalState.getAccessTokens();
            String AccountId1 = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccountId", "");
            String url = InstanceUrl + "/services/data/v39.0/sobjects/Note";
            discription = knife.getText().toString();
            //  String AccessToken=  myTokens.get_access_token();
            byte[] data = new byte[0];
            try {
                data = discription.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


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
                jsonObject.accumulate("Body", discription);
                jsonObject.accumulate("Title", sEventsubject);
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

                new HttpAsyncTaskAccountLink().execute(obj.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class HttpAsyncTaskAttachment extends AsyncTask<String, Void, String> {

        ProgressDialog pDialogt = new ProgressDialog(TaskActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialogt.setMessage("loading...");
            pDialogt.setIndeterminate(false);
            pDialogt.setCancelable(true);
            pDialogt.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";

            // OAuthTokens myTokens = globalState.getAccessTokens();
            String AccountId1 = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccountId", "");
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
                Log.d("TAG", "doInBackground:" + ImageInString);
                jsonObject.put("Body", ImageInString);
                jsonObject.put("Name", nameOfimage);
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


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }


    private class HttpAsyncTaskAccountLink extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // String ContantId = params[0];
            InputStream inputStream = null;
            String result = "";

            //  OAuthTokens myTokens = globalState.getAccessTokens();
            String url = InstanceUrl + "/services/data/v39.0/sobjects/Note";
            String ContactId1 = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("ContactId", "");
            //  String AccessToken=  myTokens.get_access_token();

            DefaultHttpClient client = new DefaultHttpClient();
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                // 3. build jsonObject


                JSONObject jsonObject = new JSONObject();
                //   jsonObject.accumulate("ParentId", AccountId);
                jsonObject.accumulate("ParentId", ContactId1);
                jsonObject.accumulate("Body", discription);
                jsonObject.accumulate("Title", sEventsubject);
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
            pDialog.dismiss();
            // 11. return result
            return result;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("Result AccountLink::", result);
            SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this);
            SharedPreferences.Editor editor = mySPrefs.edit();
            editor.remove("EventSubject");
            editor.remove("ContactName");
            editor.remove("AccountName");
            editor.remove("AccountId");
            editor.remove("ContactId");
            editor.apply();
                /*PreferenceManager.getDefaultSharedPreferences(NoteActivity.this).edit().clear().commit();*/
            startActivity(new Intent(TaskActivity.this, TaskActivity.class));
            Toast.makeText(getBaseContext(), "Data Sent successfully...", Toast.LENGTH_LONG).show();
        }
    }


    private class AsyncTaskGetEvent extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

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


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");

            HttpClient Client = new DefaultHttpClient();
            // URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%20FROM%20Event";
            URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%2CWhoID%2CWho.Name%2CWhatId%2CAccount.Name%20FROM%20Event%20WHERE%20ActivityDate%20%3D%20" + formattedDate;


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
            String ContactName = null, AccountName = null, WhatId, WhoId;
            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {
                    JSONObject obj = new JSONObject(s.toString());
                    JSONArray jsonArray = obj.getJSONArray("records");

                    Log.e(TAG, s.toString());

                    String totalSize = obj.getString("totalSize");
                    if (totalSize.equalsIgnoreCase("0")) {
                        utils.setToast(TaskActivity.this, "No Event today..");
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String Subject = jsonArray.getJSONObject(i).getString("Subject");
                            String Location = jsonArray.getJSONObject(i).getString("Location");
                            WhoId = jsonArray.getJSONObject(i).getString("WhoId");
                            WhatId = jsonArray.getJSONObject(i).getString("WhatId");
                            // String  AccountId=  jsonArray.getJSONObject(i).getString("Account");
                            System.out.println("Location::" + Location);
                            System.out.println("WhoId::" + WhoId);

                            if (WhoId.equalsIgnoreCase("null")) {
                                ContactName = "";
                            } else {
                                ContactName = jsonArray.getJSONObject(i).getJSONObject("Who").getString("Name");
                                System.out.println("ContactName::" + ContactName);
                            }
                            if (WhatId.equalsIgnoreCase("null")) {
                                AccountName = "";
                            } else {
                                AccountName = jsonArray.getJSONObject(i).getJSONObject("Account").getString("Name");
                                System.out.println("AccountName::" + AccountName);
                            }

                            getLocationFromAddress(getApplicationContext(), Location, Subject, AccountName, WhatId, ContactName, WhoId);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * calculates the distance between two locations in MILES
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  turnGPSOn(getBaseContext());
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                               /* Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                                intent.putExtra("enabled", true);
                                sendBroadcast(intent);*/
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // showGPSNotStartAlert();
                        Toast.makeText(TaskActivity.this, "Please Enable GPS", Toast.LENGTH_LONG).show();

                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showGPSNotStartAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Not Enable GPS...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Not Enable GPS?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
              /*  Intent intent=new Intent(LoginScreen.this, HomeActivity.class);
                startActivity(intent);*/
                // Write your code here to invoke YES event
                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Enable GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress, String Subject, String AccountName, String AccountId, String ContactName, String ContactId) {

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
            System.out.println("Latitude is::::" + location.getLatitude());


            //  latLng1 = new LatLng(location.getLatitude(),location.getLongitude());


            System.out.println("Currentlat::" + Currentlat);
            System.out.println("Currentlng::" + Currentlng);
            System.out.println("Serverlat::" + location.getLatitude());
            System.out.println("Serverlat::" + location.getLongitude());


            // lat1 and lng1 are the values of a previously stored location
            if (distance(location.getLatitude(), location.getLongitude(), Currentlat, Currentlng) < 0.1) { // if distance < 0.1 miles we take locations as equal
                //do what you want to do...
                // EventSubject.setText(Subject);
                // alertDialogEvent(strAddress);
                //notifiy

                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("EventSubject", Subject).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountName", AccountName).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountId", AccountId).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactName", ContactName).commit();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactId", ContactId).commit();
                Notification(strAddress, Subject);
                // Toast.makeText(NoteActivity.this, "Event this place", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(NoteActivity.this, "No Event this Place", Toast.LENGTH_SHORT).show();
            }
            //    LatLng latLng = new LatLng(jsonArray.getJSONObject(i).getDouble("Latitude"),jsonArray.getJSONObject(i).getDouble("Longitude"));

        } catch (IndexOutOfBoundsException ex) {

            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }


    private void alertDialogEvent(String AddressEvent) {

        System.out.println("Place:::" + AddressEvent);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_event, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        TextView eventPlace = (TextView) alertLayout.findViewById(R.id.EventPlace);
        if (AddressEvent.equalsIgnoreCase("")) {

        } else {
            eventPlace.setText(AddressEvent);
        }

        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        final Button ok = (Button) alertLayout.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    //RichText <code></code>


    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));

            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        ImageButton clear = (ImageButton) findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.clearFormats();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(TaskActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showLinkDialog() {
        final int start = knife.getSelectionStart();
        final int end = knife.getSelectionEnd();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }
                // When KnifeText lose focus, use this method
                knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

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


    /* public void clearApplicationData() {
         File cache = getCacheDir();
         File appDir = new File(cache.getParent());
         if (appDir.exists()) {
             String[] children = appDir.list();
             for (String s : children) {
                 if (!s.equals("lib")) {
                     deleteDir(new File(appDir, s));

                 }
             }
         }
     }
     public static boolean deleteDir(File dir) {
         if (dir != null && dir.isDirectory()) {
             String[] children = dir.list();
             for (int i = 0; i < children.length; i++) {
                 boolean success = deleteDir(new File(dir, children[i]));
                 if (!success) {
                     return false;
                 }
             }
         }

         return dir.delete();
     }*/
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    /*  *//* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.undo:
                knife.undo();
                break;
            case R.id.redo:
                knife.redo();
                break;
            case R.id.github:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_repo)));
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }*/
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    //Exit app code
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure want to exit app?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        String namedata = EventSubject.getText().toString();

                        if (!namedata.matches("")) {

                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this);
                            String userid = shared.getString("UserId", "");
                            Log.d("mayank2", "onClick: " + EventSubject.getText().toString());
                            DB.insertDraft(AccountId, userid, knife.getText().toString(), "Not Started", sdate, "Normal", EventSubject.getText().toString(), ContactId);

                            finish();
                        }
                    }
                }).setNegativeButton("no", null).show();
    }

    public void Notification(String Address, String EventName) {
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


    private class AsyncTaskLogout extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("LOGOUT");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;

            URL = "https://login.salesforce.com/services/oauth2/revoke?token=" + AccessToken;


            Log.e("logout", URL);


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");


            HttpClient Client = new DefaultHttpClient();


            // URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%20FROM%20Event";

            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(URL);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);

                // Show response on activity


            } catch (Exception ex) {
                Log.d("logout", "doInBackground:" + ex.toString());
                return "Exception";

            }

        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            super.onPostExecute(s);

        }
    }


    private class AsyncTaskGetAccount extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

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
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");

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

            if (search.equalsIgnoreCase("1")) {
                URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";
            } else {
                URL = InstanceUrl + "/services/data/v40.0/query?q=Select+Id%2C+Name+From+Account+Where+Id+IN+%28Select+AccountId+From+Contact+where+id+%3D%27" + search + "%27%29";

            }
            // Create URL string
         /*   if(ContactId.equalsIgnoreCase(""))
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
            }*/
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

            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {
                    JSONArray jsonArray = null;

                    JSONObject obj = new JSONObject(s.toString());
                    Log.e("All Search::", s.toString());
                    jsonArray = obj.getJSONArray("records");
                    accountpojo accountTypePojo = new accountpojo();
                    accounttTypePojoArrayList = new ArrayList<>();
                    arrayListAccount.clear();
                    String totalSize = obj.getString("totalSize");
                    if (totalSize.equalsIgnoreCase("0")) {
                        utils.setToast(TaskActivity.this, "No Contact match in A/c  or no records");
                    } else {
                        jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            accountTypePojo = new accountpojo();
                            accountTypePojo.setId(jsonArray.getJSONObject(i).getString("Id"));
                            accountTypePojo.setName(jsonArray.getJSONObject(i).getString("Name"));
                            if (SelectcontactAccount.equalsIgnoreCase("contact")) {
                                autoCompleteAcount.setText(jsonArray.getJSONObject(i).getString("Name"));
                                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountName", jsonArray.getJSONObject(i).getString("Name"));
                                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("AccountId", jsonArray.getJSONObject(i).getString("Id")).commit();
                            }
                            arrayListAccount.add(accountTypePojo.getName());
                            System.out.println("Problem type" + accountTypePojo.getId());
                            accounttTypePojoArrayList.add(accountTypePojo);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TaskActivity.this, android.R.layout.simple_dropdown_item_1line, arrayListAccount);
                            autoCompleteAcount.setAdapter(adapter);
                           /* PersonNames personNames = new PersonNames(jsonArray.getJSONObject(i).getString("Name"), jsonArray.getJSONObject(i).getString("Id"));
                            arraylist.add(personNames);*/
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            arrayAdapterAccount.notifyDataSetChanged();
        }
    }

    private class AsyncTaskGetContact extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Contact");
            pd.setCancelable(false);
            //  pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
            String search = params[0];
            // URLEncode user defined data

            /*String mobileValue    = URLEncoder.encode(mobileNo, "UTF-8");
            String fnameValue  = URLEncoder.encode(name.trim(), "UTF-8");
            String emailValue   = URLEncoder.encode(email.trim(), "UTF-8");
            String passValue    = URLEncoder.encode(password.trim(), "UTF-8");
            String companyValue   = URLEncoder.encode(companyname.trim(), "UTF-8");
            String designationValue    = URLEncoder.encode(designation.trim(), "UTF-8");*/

         /*   OAuthTokens myTokens = globalState.getAccessTokens();
            String AccessToken=  myTokens.get_access_token();
            System.out.println("Token:"+AccessToken);*/

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");
            String AccountId = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccountId", "");
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();

            if (search.equalsIgnoreCase("1")) {
                URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20Contact";
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("veloxyapptest-dev-ed.my.salesforce.com")
                        .appendPath("services")
                        .appendPath("data")
                        .appendPath("v24.0")
                        .appendPath("query")
                        .appendQueryParameter("q", "SELECT NAME,ID,OwnerId,Email FROM Contact Limit 20");

                myUrl = builder.build().toString();

            } else {
                URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT+ID%2C+NAME+FROM+Contact+where+AccountId%3D%27" + search + "%27";
            }


         /*   if(AccountId.equalsIgnoreCase(""))
            {
                // URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";
                URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20Contact";
            }

            else if(search.equalsIgnoreCase("1"))
            {
                // URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20Contact";
                URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT+ID%2C+NAME+FROM+Contact+where+AccountId%3D%27"+AccountId+"%27";
            }
            else {
                // URL =InstanceUrl+"/services/data/v39.0/search/?q=FIND%20%7B"+search+"%7D%20IN%20NAME%20FIELDS%20RETURNING%20Contact(id,name)";
                URL =InstanceUrl+"/services/data/v22.0/query/?q=SELECT+id%2C+Name+FROM+Contact+WHERE+Name+LIKE+%27"+search+"%25%27";
            }
*/
            //  String URL = myTokens.get_instance_url()+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20ACCOUNT";


            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(myUrl);
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
            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {

                    JSONArray jsonArray = null;
                    JSONObject obj = new JSONObject(s.toString());

                  /*  if(flag.equalsIgnoreCase("1")) {

                        jsonArray=obj.getJSONArray("records");

                    }else {

                         jsonArray = obj.getJSONArray("searchRecords");
                    }*/

                    //   select option add here


                    contactpojo contactTypePojo = new contactpojo();
                    contactTypePojo.setId("0");
                    contactTypePojo.setName("select");
                    arrayListContact.add(contactTypePojo.getName());
                    contactTypePojoArrayList = new ArrayList<>();
                    arrayListContact.clear();
                    String totalSize = obj.getString("totalSize");
                    if (totalSize.equalsIgnoreCase("0")) {
                        utils.setToast(TaskActivity.this, "No Contact match in A/c  or no records");
                    } else {
                        jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            contactTypePojo = new contactpojo();
                            contactTypePojo.setId(jsonArray.getJSONObject(i).getString("Id"));
                            if (SelectcontactAccount.equalsIgnoreCase("account")) {
                                autoCompleteContact.setText(jsonArray.getJSONObject(i).getString("Name"));
                                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactName", jsonArray.getJSONObject(i).getString("Name")).commit();
                                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().putString("ContactId", jsonArray.getJSONObject(i).getString("Id")).commit();
                            }
                            contactTypePojo.setName(jsonArray.getJSONObject(i).getString("Name"));
                            arrayListContact.add(contactTypePojo.getName());

                            contactTypePojoArrayList.add(contactTypePojo);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TaskActivity.this, android.R.layout.simple_dropdown_item_1line, arrayListContact);
                            autoCompleteContact.setAdapter(adapter);

                         /*   searchCustomContactAdapter = new SearchCustomContactAdapter(NoteActivity.this, contactTypePojoArrayList);
                            autoCompleteContact.setThreshold(1);//will start working from first character
                            autoCompleteContact.setAdapter(searchCustomContactAdapter);//setting the adapter data into the AutoCompleteTextView*/

                           /* PersonNames personNames = new PersonNames(jsonArray.getJSONObject(i).getString("Name"), jsonArray.getJSONObject(i).getString("Id"));
                            arraylist.add(personNames);*/
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            arrayAdapterContact.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
        }
    }

    private class AsyncTaskSession extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("check session");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");

            HttpClient Client = new DefaultHttpClient();
            // URL = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20ID%2CSubject%2CLocation%20FROM%20Event";
            URL = InstanceUrl + "/services/data/v24.0";


            Log.e("httpgetS", URL);

            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(URL);
                httpget.addHeader("Authorization", "Bearer " + AccessToken);
                Log.d("mayank", "doInBackground: " + AccessToken);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);
                // Show response on activity
            } catch (Exception ex) {

                ex.printStackTrace();
                Log.d("mayankpawar", "" + ex.toString());
                return "Exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("mayankpawar", "" + s.toString());
            pd.dismiss();
            String message = "null";
            String ContactName = null, AccountName = null, WhatId, WhoId;
            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {
                SessionImageview.setImageResource(R.drawable.red_dot);

                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().clear().commit();
                //
                //
                // clearApplicationData();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

                //  alertDialog();
                // utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {

                    JSONObject obj = new JSONObject(s.toString());
                    // JSONArray jsonArray=obj.getJSONArray("records");

                    Log.e("Check Session::", s.toString());
/*
                     message=obj.getString("message");
                    if (message.equalsIgnoreCase("null"))
                    {

                    }else
                    {
                        alertDialog();
                    }*/

                    //  utils.setToast(NoteActivity.this,message);
                  /*  if(totalSize.equalsIgnoreCase("0"))
                    {
                        utils.setToast(NoteActivity.this,"No Event today..");
                    }else {



                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void
    alertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        final Button ok = (Button) alertLayout.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).edit().clear().commit();
                //
                //
                // clearApplicationData();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });

    }

    private void showPictureDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Library", "Upload Image", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TaskActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA);
                } else if (options[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_REQUEST);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Upload Image")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, UPLOAD_REQUEST);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    customDialog.dismiss();
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    discription = knife.getText().toString();
                    discription = discription + result.get(0);
                    knife.setText(discription);
                    // knife.setText(result.get(0));
                    // discription = discription.concat(knife.getText().toString());
                }
                break;
            }

        }


        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY_REQUEST) {
            APIFlack = false;

            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
                    if (!text.isOperational()) {

                    } else {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = text.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock myitem = items.valueAt(i);
                            sb.append(myitem.getValue());
                            sb.append("\n");
                        }


                        discription = knife.getText().toString();
                        discription = discription + sb.toString();
                        knife.setText(discription);

                        //  knife.setText(sb.toString());
                        //  discription = discription.concat(knife.getText().toString());
                    }
                    //   Drawable img = new BitmapDrawable(getResources(), bitmap);
                    //     img.setBounds( 0, 0, 60, 60 );
                    //     knife.setCompoundDrawables( img, null, null, null );


                    //  knife.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
                    /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    knife.setText(encoded);*/
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TaskActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            APIFlack = false;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            //  Drawable img = new BitmapDrawable(getResources(), bitmap);
            //  img.setBounds( 0, 0, 200, 200 );
            //  knife.setCompoundDrawables( img, null, null, null );

            // knife.setBackground(new BitmapDrawable(getResources(), bitmap));
          /*)
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            knife.setText(encoded);*/
            TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!text.isOperational()) {

            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = text.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myitem = items.valueAt(i);
                    sb.append(myitem.getValue());
                    sb.append("\n");
                }

                discription = knife.getText().toString();
                discription = discription + sb.toString();
                knife.setText(discription);
                //  discription = discription.concat(knife.getText().toString());

            }

        } else if (requestCode == UPLOAD_REQUEST) {
            if (data != null) {

                APIFlack = true;


                Uri contentURI = data.getData();


                try {


                    final InputStream imageStream = getContentResolver().openInputStream(contentURI);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);





                    String encodedImage = encodeImage(selectedImage);


                    //  encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");

                    //   String encodedImage = encodeImage(selectedImage);

                    if (numberofimage == 2)

                    {
                        Toast.makeText(TaskActivity.this, "YOU CAN NOT ADD MORE THEN TWO FILE", Toast.LENGTH_LONG).show();

                    }


                    if (numberofimage == 1)

                    {
                        ImageInString[1] = encodedImage;
                        Drawable img = new BitmapDrawable(getResources(), selectedImage);
                        String s[] = contentURI.toString().split("/");
                        nameOfimage[1] = s[s.length - 1] + ".jpg";

                        if (imagename1.getText().toString() != "") {
                            imagename.setText(s[s.length - 1] + ".jpg");
                            thubnail.setBackground(img);
                            imagelayout.setVisibility(View.VISIBLE);
                        } else {

                            imagename1.setText(s[s.length - 1] + ".jpg");
                            thubnail1.setBackground(img);
                            imagelayout1.setVisibility(View.VISIBLE);

                        }
                        FlagForAttachment = true;
                        numberofimage = 2;

                    }


                    if (numberofimage == 0) {

                        ImageInString[0] = encodedImage;


                        Drawable img = new BitmapDrawable(getResources(), selectedImage);
                        String s[] = contentURI.toString().split("/");
                        nameOfimage[0] = s[s.length - 1] + ".jpg";
                        imagename.setText(s[s.length - 1] + ".jpg");
                        thubnail.setBackground(img);
                        imagelayout.setVisibility(View.VISIBLE);
                        FlagForAttachment = true;
                        numberofimage = 1;
                    }


               /*   LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.image_attachment, null);

// fill in any details dynamically here
                    TextView textView = (TextView) v.findViewById(R.id.image_name);
                    textView.setText(nameOfimage);

                    ImageView imageView=(ImageView)v.findViewById(R.id.thubnail);
                    imageView.setBackground(img);

// insert into main view
                    LinearLayout insertPoint = (LinearLayout) findViewById(R.id.imagelayout);

                    if (insertPoint != null) {
                        insertPoint.removeView(v);
                    }

                    insertPoint.addView(v, insertPoint.getChildCount()-1, new ViewGroup.LayoutParams(300, 300));

                          */


                    //  thubnail.setCompoundDrawables(img,null,null,null);
                    //  thubnail.setText("Image Inserted");
                    //  knife.setBackground(img);
                    //  knife.setText("");
                    //  discription = BitMapToString(bitmap);


                    //  img.setBounds( 0, 0, 200, 200 );
                    //  knife.setCompoundDrawables( img, null, null, null );

                } catch (IOException e) {
                    Log.d("mayank", " " + e.toString());
                    e.printStackTrace();
                }

            }


            // knife.setBackground(new BitmapDrawable(getResources(), bitmap));
          /*)
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            knife.setText(encoded);*/
            //   TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
          /*  if (!text.isOperational()) {

            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = text.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myitem = items.valueAt(i);
                    sb.append(myitem.getValue());
                    sb.append("\n");
                }
                knife.setText(sb.toString());

            } */

        }


    }




    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*  private void startVoiceInput() {
          Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
          intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
          try {
              startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
          } catch (ActivityNotFoundException a) {

          }
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

          switch (requestCode) {
              case REQ_CODE_SPEECH_INPUT: {
                  if (resultCode == RESULT_OK && null != data) {
                      ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                      knife.setText(result.get(0));
                  }
                  break;
              }

          }
      }

  */

    private class AsyncTaskGetTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

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
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL = "https://veloxyapptest-dev-ed.my.salesforce.com/services/data/v42.0/query?q=SELECT%20ActivityDate%2CSubject%2CID%2CWhatId%2CWhoId%2COwnerId%2CDescription%2CStatus%2CPriority%20FROM%20Task%20";

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

                    if (obj.getString("totalSize").equalsIgnoreCase("0")) {
                        Toast.makeText(TaskActivity.this, "Records are not found!!!", Toast.LENGTH_SHORT).show();
                    } else {

                        jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            // EventNames(String eventName, String id, String eventDate, String description, String accountid, String contactid)
                            Taskpojo eventName = new Taskpojo(jsonArray.getJSONObject(i).getString("Subject"), jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString("ActivityDate"), jsonArray.getJSONObject(i).getString("Description"), jsonArray.getJSONObject(i).getString("WhatId"), jsonArray.getJSONObject(i).getString("WhoId"));
                            DB.insertTask(jsonArray.getJSONObject(i).getString("WhatId"), UserId, jsonArray.getJSONObject(i).getString("Description"), "Not Started", jsonArray.getJSONObject(i).getString("ActivityDate"), "Normal", jsonArray.getJSONObject(i).getString("Subject"), jsonArray.getJSONObject(i).getString("WhoId"));

                        }

                        //start_date.setText("");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //    adapter.notifyDataSetChanged();
        }
    }


    public class AsyncTaskGetUserInfo extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(TaskActivity.this);

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
            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this).getString("AccessToken", "");

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            URL = "https://na1.salesforce.com/services/data/v41.0/sobjects/User/" + UserId;

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


                    String name = obj.getString("Name");
                    String photourl = obj.getString("MediumPhotoUrl");
                    Log.d(TAG, "onPostExecute:" + photourl);

                    URL url = null;
                    try {
                        url = new URL(photourl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    //   Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    //   IMAGEofuser.setImageBitmap(bmp);


                    Picasso.Builder builder = new Picasso.Builder(TaskActivity.this).downloader(new OkHttp3Downloader(TaskActivity.this, Integer.MAX_VALUE));
                    ;
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(photourl).error(R.drawable.cast_mini_controller_progress_drawable).resize(200, 200).into(IMAGEofuser);


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


    private String encodeImage(Bitmap bm) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Charset iso8859_15 = Charset.forName("ISO-8859-15");

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        byte[] encodedBytes = Base64.encodeBase64(b);


            System.out.println("encodedBytes " + new String(encodedBytes,iso8859_15));

        //   String encImage = Base64.encode(b, Base64.DEFAULT);
     //   Base64.encodeBase64("Test".getBytes());

        return new String(encodedBytes,iso8859_15);
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}


