package demo.recycle.com.salforceapp.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import org.apache.commons.codec.binary.Base64;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpClientStack;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.pojo.contactpojo;
import io.github.mthli.knife.KnifeText;


public class ShowTaskDetailActivity extends AppCompatActivity {


    private static final int CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int UPLOAD_REQUEST = 3;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    TextView event_date;
    TextView search_contact, search_account,nameofattachment,nameofattachment1;
    RadioButton radiocontact, radioaccount;
    EditText task_subject;
    KnifeText knife;
    String Contactid;
    String Accountid;
    String id;
    Button edit, delete,download;
    ImageButton cameraButton, voiceButton, boldButton, italicButton, underlineButton;
    String discription;
    ImageView calenderIcon,imageicon,imageicon1;
    LinearLayout attachmentlayout,attachmentlayout1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        Initialization();

        Intent intent = getIntent();
        String Activedate = intent.getStringExtra("Activedate");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        Contactid = intent.getStringExtra("contactid");
        Accountid = intent.getStringExtra("accountid");
        id = intent.getStringExtra("id");
        new ShowTaskDetailActivity.AsyncTaskGetContact().execute("0");
        Log.d("mayank", "onCreate: "+id);
        new ShowTaskDetailActivity.AsyncTaskFeatchAttachment().execute();
        event_date.setText(Activedate);
        task_subject.setText(name);
        knife.setText(description);

    }


    public void Initialization() {
        event_date = (TextView) findViewById(R.id.event_date);
        search_account = (TextView) findViewById(R.id.acount);
        search_contact = (TextView) findViewById(R.id.contact);
        radioaccount = (RadioButton) findViewById(R.id.radioAccount);
        radiocontact = (RadioButton) findViewById(R.id.radioContact);
        task_subject = (EditText) findViewById(R.id.Task_subject);
        knife = (KnifeText) findViewById(R.id.knife);
        cameraButton = (ImageButton) findViewById(R.id.camera);
        voiceButton = (ImageButton) findViewById(R.id.voice);
        boldButton = (ImageButton) findViewById(R.id.bold);
        italicButton = (ImageButton) findViewById(R.id.italic);
        underlineButton = (ImageButton) findViewById(R.id.underline);
        edit = (Button) findViewById(R.id.editbutton);
        delete = (Button) findViewById(R.id.deletebutton);
        calenderIcon = (ImageView) findViewById(R.id.calender);
        attachmentlayout=(LinearLayout)findViewById(R.id.attachmentlayout);
        attachmentlayout1=(LinearLayout)findViewById(R.id.attachmentlayout1);
        nameofattachment=(TextView)findViewById(R.id.nameofattachment);
        imageicon=(ImageView) findViewById(R.id.imageicon);
        nameofattachment1=(TextView)findViewById(R.id.nameofattachment1);
        imageicon1=(ImageView) findViewById(R.id.imageicon1);
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupLink();
        setupQuote();

        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartDate();


            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermission();
                showPictureDialog();

            }
        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVoiceInput();

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ShowTaskDetailActivity.AsyncTaskEditTask().execute("0");

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new ShowTaskDetailActivity.AsyncTaskDeleteTask().execute("0");

            }
        });


    }

    private void startVoiceInput() {


        // customDialog.show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }


    private void showPictureDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Library", "Upload Image", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShowTaskDetailActivity.this);
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

    private void setupBold() {
        boldButton = (ImageButton) findViewById(R.id.bold);

        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));

            }
        });

        boldButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        italicButton = (ImageButton) findViewById(R.id.italic);

        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        underlineButton = (ImageButton) findViewById(R.id.underline);

        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underlineButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShowTaskDetailActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
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


    private class AsyncTaskGetContact extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);

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

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
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
                myUrl = InstanceUrl + "/services/data/v24.0/query?q=SELECT+ID%2C+NAME%2C+Account.Name+FROM+Contact+where+Id%3D%27" + Contactid + "%27";
                Log.d("TAG", "doInBackground:" + myUrl);
            }


            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(myUrl);
                httpget.addHeader("Authorization", "Bearer " + AccessToken);
                Log.d("mayank", "doInBackground: " + AccessToken);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);

                // Show response on activity


            } catch (Exception ex) {
                Log.e("mayank", ex.toString());
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


                    String totalSize = obj.getString("totalSize");
                    if (totalSize.equalsIgnoreCase("0")) {

                    } else {
                        jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            contactpojo contactTypePojo = new contactpojo();
                            contactTypePojo.setId(jsonArray.getJSONObject(i).getString("Id"));

                            contactTypePojo.setName(jsonArray.getJSONObject(i).getString("Name"));

                            if (jsonArray.getJSONObject(i).getString("Id").equalsIgnoreCase(Contactid)) {

                                search_contact.setText(contactTypePojo.getName());
                                search_account.setText(jsonArray.getJSONObject(i).getJSONObject("Account").getString("Name"));

                                break;
                            }


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            adapter.notifyDataSetChanged();
        }


    }

    private class AsyncTaskDeleteTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Please Wait...");
            pd.setMessage("Deleting task");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
            String search = params[0];

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();


            myUrl = InstanceUrl + "/services/data/v42.0/sobjects/Task/" + id;
            Log.d("TAG", "doInBackground:" + myUrl);


            try {
                String SetServerString = "";

                // Create Request to server and get response

                HttpDelete httpdelete = new HttpDelete(myUrl);

                httpdelete.addHeader("Authorization", "Bearer " + AccessToken);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpdelete, responseHandler);

                // Show response on activity


            } catch (Exception ex) {

                return "Exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            Intent sendIntent = new Intent(ShowTaskDetailActivity.this, TaskActivity.class);


            startActivity(sendIntent);


        }

    }

    private class AsyncTaskEditTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Please Wait...");
            pd.setMessage("Updating task");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
            String search = params[0];

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();


            myUrl = InstanceUrl + "/services/data/v42.0/sobjects/Task/" + id;
            Log.d("mayank", "doInBackground:" + myUrl);


            try {
                String SetServerString = "";


                JSONObject jsonObject = new JSONObject();
                // jsonObject.accumulate("WhatId", AccountId);
                // jsonObject.accumulate("WhoId", ContactId);
                jsonObject.accumulate("Description", knife.getText());
                jsonObject.accumulate("Subject", task_subject.getText());
                jsonObject.accumulate("ActivityDate", event_date.getText().toString());


                String json = jsonObject.toString();

                Log.d("mayank", "" + json);

                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity


                // Create Request to server and get response

                HttpClientStack.HttpPatch httppatch = new HttpClientStack.HttpPatch(myUrl);
                httppatch.setHeader("Content-type", "application/json");
                httppatch.setEntity(se);

                httppatch.addHeader("Authorization", "Bearer " + AccessToken);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httppatch, responseHandler);

                // 9. receive response as inputStream

                // 11. return result


                // Show response on activity


            } catch (Exception ex) {

                Log.d("mayank", "doInBackground:" + ex.toString());

                return "Exception";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();

            Intent sendIntent = new Intent(ShowTaskDetailActivity.this, TaskActivity.class);


            startActivity(sendIntent);


        }

    }


    private class AsyncTaskFeatchAttachment extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Please Wait...");
            pd.setMessage("Showing task");
            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();


            myUrl = InstanceUrl + "/services/data/v24.0/query?q=SELECT%20id%2C%20%28Select%20id%2Cname%20from%20Attachments%29%20FROM%20task%20WHERE%20id=%20%27"+id+"%27";




            try {
                String SetServerString = "";


                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);
                // 5. set json to StringEntity

                // 6. set httpPost Entity


                // Create Request to server and get response

                HttpGet httpget = new HttpGet(myUrl);
                httpget.addHeader("Authorization", "Bearer " + AccessToken);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return SetServerString = Client.execute(httpget, responseHandler);


                // 9. receive response as inputStream

                // 11. return result


                // Show response on activity


            } catch (Exception ex) {


                Log.d("mayank", "exception"+ex.toString());

                return "Exception";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            String ContactName = null, AccountName = null, WhatId, WhoId;
            if ((s.equalsIgnoreCase("Exception")) || (s.equalsIgnoreCase("UnsupportedEncodingException"))) {

                //  utils.setToast(NoteActivity.this,"Error in Registrating,Please Try Later");

            } else {

                try {
                    JSONObject obj = new JSONObject(s.toString());
                    JSONArray jsonArray = obj.getJSONArray("records");


                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject records = jsonArray.getJSONObject(i);
                        JSONObject attachment = records.getJSONObject("Attachments");

                        if(attachment==null)
                        {

                        }
                     else
                        {
                            String name="";



                            JSONArray jsonAr = attachment.getJSONArray("records");
                            for (int j = 0; j < jsonAr.length(); j++) {

                                JSONObject record = jsonAr.getJSONObject(j);
                                JSONObject attribute = record.getJSONObject("attributes");

                               name  = record.getString("Name")+"\r\n";
                                String url = attribute.getString("url");
                             //   new ShowTaskDetailActivity.AsyncTaskFetchAttachmentBodyFromUrl().execute(url);
                                Log.d("TAG", ""+j+name);

                                if(j==0) {

                                    nameofattachment.setText(" " + name);
                                    Log.d("TAG0", ""+j+name);
                                    new ShowTaskDetailActivity.AsyncTaskFetchAttachmentBodyFromUrl().execute(url);
                                }

                                if(j==1)
                                {

                                    nameofattachment1.setText(" " + name);
                                    Log.d("TAG0", ""+j+name);
                                    new ShowTaskDetailActivity.AsyncTaskFetchAttachmentBodyFromUrlExtra().execute(url);
                                }


                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private class AsyncTaskFetchAttachmentBodyFromUrl extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Please Wait...");
            pd.setMessage("Showing Task");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String URL = params[0];


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();


            myUrl ="https://na1.salesforce.com"+URL+"/Body/";
            Log.d("TAG", "doInBackground:" + myUrl);
            Bitmap b = null;

            try {
                 b=   downloadBitmap(myUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }



        return b;

        }


        private Bitmap downloadBitmap(String url) throws IOException {

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            HttpUriRequest request = new HttpGet(url.toString());
            request.addHeader("Authorization", "Bearer " + AccessToken);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                return bitmap;
            } else {
                throw new IOException("Download failed, HTTP response code "
                        + statusCode + " - " + statusLine.getReasonPhrase());
            }
        }



        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);

            pd.dismiss();


                attachmentlayout.setVisibility(View.VISIBLE);

                imageicon.setImageBitmap(s);








        }

    }


    private class AsyncTaskFetchAttachmentBodyFromUrlExtra extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pd = new ProgressDialog(ShowTaskDetailActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Please Wait...");
            pd.setMessage("Showing Task");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String URL = params[0];


            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            String AccountId = Accountid;
            // Create http cliient object to send request to server
            String myUrl = "";
            HttpClient Client = new DefaultHttpClient();


            myUrl ="https://na1.salesforce.com"+URL+"/Body/";
            Log.d("TAG", "doInBackground:" + myUrl);
            Bitmap b = null;

            try {
                b=   downloadBitmap(myUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }



            return b;

        }


        private Bitmap downloadBitmap(String url) throws IOException {

            String InstanceUrl = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("InstanceUrl", "");
            String AccessToken = PreferenceManager.getDefaultSharedPreferences(ShowTaskDetailActivity.this).getString("AccessToken", "");
            HttpUriRequest request = new HttpGet(url.toString());
            request.addHeader("Authorization", "Bearer " + AccessToken);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                return bitmap;
            } else {
                throw new IOException("Download failed, HTTP response code "
                        + statusCode + " - " + statusLine.getReasonPhrase());
            }
        }



        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);

            pd.dismiss();




                attachmentlayout1.setVisibility(View.VISIBLE);

                imageicon1.setImageBitmap(s);







        }

    }










    private void StartDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(ShowTaskDetailActivity.this, R.style.Dialogtheme,
                new ShowTaskDetailActivity.mDateSetListener(), mYear, mMonth, mDay);
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
            event_date.setText(new StringBuilder().append(mYear).append("-").append(mMonths).append("-").append(mDays));
            System.out.println(event_date.getText().toString());

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    //  customDialog.dismiss();
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
                    Toast.makeText(ShowTaskDetailActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {


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


                Uri contentURI = data.getData();
                Bitmap bitmap;


                try {

                  /*  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                 //   to encode base64 from byte array use following method

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                     */

                    final InputStream imageStream = getContentResolver().openInputStream(contentURI);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                /*    String encodedImage = encodeImage(selectedImage);
                    ImageInString = new String(encodedImage);
                    Drawable img = new BitmapDrawable(getResources(),selectedImage);
                    String s[]=contentURI.toString().split("/");
                    nameOfimage= s[s.length-1]+".jpg";
                    imagename.setText(s[s.length-1]+".jpg");
                    thubnail.setBackground(img);
                    imagelayout.setVisibility(View.VISIBLE);
                    */

                    //  thubnail.setCompoundDrawables(img,null,null,null);
                    //  thubnail.setText("Image Inserted");
                    //  knife.setBackground(img);
                    //  knife.setText("");
                    //  discription = BitMapToString(bitmap);


                    //  img.setBounds( 0, 0, 200, 200 );
                    //  knife.setCompoundDrawables( img, null, null, null );

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }


    }






}
