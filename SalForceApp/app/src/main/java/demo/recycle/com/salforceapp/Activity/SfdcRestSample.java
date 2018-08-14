package demo.recycle.com.salforceapp.Activity;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import demo.recycle.com.salforceapp.GlobalState;
import demo.recycle.com.salforceapp.OAuthTokens;
import demo.recycle.com.salforceapp.R;

public class SfdcRestSample extends AppCompatActivity {

    WebView webview;
    String callbackUrl;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SalesForce");
        setSupportActionBar(toolbar);
        /* As per the OAuth 2.0 User-Agent Flow supported by Salesforce, we pass along the Client Id (aka Consumer Key) as a GET 
         * parameter. We also pass along a special String as the redirect URI so that we can verify it when Salesforce redirects
         * the user back to the mobile device
         */

        String consumerKey = this.getResources().getString(R.string.consumerKey).toString();
        String url = this.getResources().getString(R.string.oAuthUrl).toString();
        callbackUrl = this.getResources().getString(R.string.callbackUrl).toString();
        String reqUrl = url + "/services/oauth2/authorize?response_type=token&display=touch&client_id=" + consumerKey + "&redirect_uri=" + callbackUrl;

        Log.d("mayank", "" + reqUrl);

        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new HelloWebViewClient(this));
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(reqUrl);
    }


    private class HelloWebViewClient extends WebViewClient {

        Activity act;

        public HelloWebViewClient(Activity myAct) {
            act = myAct;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String LOGOUT = PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this).getString("LOGOUT", "");
            Log.d("Sfdc-sample", "Redirect URL: " + url);
            /* Check to make sure that the redirect URI from Salesforce contains the 'special' text that we passed as the
	         * redirect URI in the initial OAuth request. This confirms that the redirect is indeed from Salesforce.
	         */
         /*  if(LOGOUT.equalsIgnoreCase("1"))
		   {
			   Intent intent = new Intent(act, SfdcRestSample.class);
			   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					   | Intent.FLAG_ACTIVITY_CLEAR_TASK
					   | Intent.FLAG_ACTIVITY_NEW_TASK);
		   }else {*/
            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this);
            Log.e("mayank", sp.getBoolean("LOGGED", false) + " ");

            if (sp.getBoolean("LOGGED", false)) {
                startActivity(new Intent(SfdcRestSample.this, TaskActivity.class));
            } else {
                Log.d("Sfdc-sample", "Redirect URL: " + url);
                if (url.startsWith(callbackUrl)) {

                    Log.e("TAG", "mayank" + callbackUrl);
                    parseToken(url);
                    Intent i = new Intent(act, TaskActivity.class);
                    startActivity(i);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
            // }
            return false;

        }

        /* Per the OAuth 2.0 Use Agent flow supported by Salesforce, the redirect URI will contain the access token (among other
         * other things) after the '#' sign. This method extracts those values and stores them in the GlobalState class. The
         * GolbalState class extends the 'Application' object and can therefore be used to share state between different activities 
         * within the same Android application
         */
        private void parseToken(String url) {
            String temp = url.split("#")[1];
            String[] keypairs = temp.split("&");
            OAuthTokens myTokens = new OAuthTokens();
            for (int i = 0; i < keypairs.length; i++) {
                String[] onepair = keypairs[i].split("=");
                System.out.println("OnePaire::" + onepair);
                if (onepair[0].equals("access_token")) {
                    myTokens.set_access_token(URLDecoder.decode(onepair[1]));
                } else if (onepair[0].equals("refresh_token")) {
                    myTokens.set_refresh_token(onepair[1]);
                } else if (onepair[0].equals("instance_url")) {
                    myTokens.set_instance_url(onepair[1]);
                    myTokens.set_instance_url(myTokens.get_instance_url().replaceAll("%2F", "/"));
                    myTokens.set_instance_url(myTokens.get_instance_url().replaceAll("%3A", ":"));
                } else if (onepair[0].equals("id")) {
                    myTokens.set_id(onepair[1]);
                } else if (onepair[0].equals("issued_at")) {
                    myTokens.set_issued_at(Long.valueOf(onepair[1]));
                } else if (onepair[0].equals("signature")) {
                    myTokens.set_signature(onepair[1]);
                }
            }
            GlobalState gs = (GlobalState) getApplication();
            gs.setAccessTokens(myTokens);

            System.out.println("AccessToken:: OnSfdcRestPage::" + myTokens.get_access_token());
            PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this).edit().putString("InstanceUrl", myTokens.get_instance_url()).commit();
            PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this).edit().putString("AccessToken", myTokens.get_access_token()).commit();
            PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this).edit().putBoolean("LOGGED", true).commit();
            String userString = myTokens.get_id();
            System.out.println("Id::::::::::::::::" + userString);
            //UserId Splite
            String UserId = userString.substring(Math.max(userString.length() - 18, 0));
            System.out.println("UserId::::" + UserId);
            PreferenceManager.getDefaultSharedPreferences(SfdcRestSample.this).edit().putString("UserId", UserId).commit();
        }

    }

}