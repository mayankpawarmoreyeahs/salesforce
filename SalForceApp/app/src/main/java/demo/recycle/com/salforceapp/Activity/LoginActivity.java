package demo.recycle.com.salforceapp.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import demo.recycle.com.salforceapp.R;
import demo.recycle.com.salforceapp.Utils.Utils;

public class LoginActivity extends AppCompatActivity {
    Button b1,b2;
    EditText username,password,ed2;

    TextView tx1;
    int counter = 3;
    Button login;
    String sUserName,sPassword;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.btn_login);
        username=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        utils=new Utils(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sUserName = username.getText().toString().trim();
                sPassword = password.getText().toString().trim();


                if((sUserName.trim().length()==0)
                        &&(sPassword.trim().length()==0)){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please Enter Username Id & Password",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                    return;
                }
                if(sUserName.trim().length()==0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please enter username",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                    return;
                }
                if(sPassword.trim().length()==0){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please enter password",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                    return;
                }

                if(utils.isValidEmail(sUserName.trim())){

//WS Login call
                    if(utils.isNetworkAvailable()) {

                        new AsyncTaskLogin().execute();
                    }else{
                        utils.setToast(LoginActivity.this,"Please Connect to Internet");
                    }

                }else{
                    utils.setToast(LoginActivity.this,"Please enter valid username");
                }


            }
        });
    }
    private class AsyncTaskLogin extends AsyncTask<String,Void,String> {

        ProgressDialog pd=new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Contact");
            pd.setCancelable(false);
             pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL;
         //   String search = params[0];


                     String InstanceUrl="";
            HttpClient Client = new DefaultHttpClient();

                URL =InstanceUrl+"/services/data/v24.0/query?q=SELECT%20ID%2CNAME%20FROM%20Contact";



            Log.e("httpget", URL);

            try
            {
                String SetServerString = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(URL);
               // httpget.addHeader("Authorization", "Bearer "+AccessToken);
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
                    Log.e("LoginResponse:",s.toString());


                    String totalSize=obj.getString("totalSize");
                    if(totalSize.equalsIgnoreCase("0"))
                    {
                        utils.setToast(LoginActivity.this,"No Contact match in A/c  or no records");
                    }else {
                        jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}


