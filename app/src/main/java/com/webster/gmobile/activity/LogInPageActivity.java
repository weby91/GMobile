package com.webster.gmobile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.util.JSONParser;
import com.webster.gmobile.util.myCore;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class LogInPageActivity extends AppCompatActivity implements OnClickListener {

    public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.gkrgedong.LogInPageAndLoadersActivity";
    public static final String DARK = "Dark";
    public static final String LIGHT = "Light";
    public final static String EXTRA_MESSAGE = "com.csform.android.gkrgedong.MESSAGE";
    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.btn_login)
    Button btn_login;
    @InjectView(R.id.link_signup)
    TextView link_signup;
    public String strUsername, strPassword, app_ver, cabang;
    public static boolean isCheckUser;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    String strJSONReturnMsg, strErrorMsg;
    boolean boolLogin;

    //php login script location:

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";

    //testing on Emulator:
    //private static final String LOGIN_URL = "http://gkrgedongmobile.com/getInfo.php";
    private static final String LOGIN_URL = myCore.connectToDB("getInfo");

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        //new updateAppVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        TextView link_signup = (TextView) findViewById(R.id.link_signup);
        // Create underline
        SpannableString content = new SpannableString("Create One");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        link_signup.setText(content);

        try {
            app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error", e.getMessage());
        }

        // startVolley();
        boolLogin = checkIfUserIsLoggedIn();
        if (boolLogin == true) {
            Intent i = new Intent(LogInPageActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        }

        TextView login;
        login = (Button) findViewById(R.id.btn_login);


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        //nanti di delete
        username.setText("weby91");
        password.setText("jameslebron2");

        login.setOnClickListener(this);
        link_signup.setOnClickListener(this);
    }
    //endregion

    //region Disable back press
    /* @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK
                || KeyEvent.KEYCODE_HOME == keyCode
                || KeyEvent.KEYCODE_SEARCH == keyCode)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    } */
    //endregion

    //region checkIfUserIsLoggedIn
    private boolean checkIfUserIsLoggedIn() {
        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        String app_v = prefs.getString("app_v", "");
        String cabang = prefs.getString("cabang", "");
        if (username.trim().equals("") || password.trim().equals("") || app_v.trim().equals("") || cabang.trim().equals("")) {
            return false;
        } else {
            // ========================== Volley Start ==============================
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = myCore.connectToDB("getInfo");
            url = url + "?username=" + username + "&password=" + password + "&app_v=" + app_ver;


            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // TODO Auto-generated method stub
                    //username.setText("Response => "+response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    //username.setText("Response => "+error.toString());
                }

            });


            queue.add(jsObjRequest);

            // ========================== Volley End ===============================
            return true;
        }
    }
    //endregion

    //region VolleyCallback
    public interface VolleyCallback {
        void onSuccess(String result);
    }
    //endregion

    //region saveToSharedPreferences userDetails
    private void saveToSharedPreferences(String username, String password, String role_cd, String app_ver, String cabang) {
        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("username", username);
        prefsEditor.putString("password", password);
        prefsEditor.putString("role_cd", role_cd);
        prefsEditor.putString("app_v", app_ver);
        prefsEditor.putString("cabang", cabang);
        prefsEditor.commit();
    }
    //endregion

    //region onClick
    @Override
    public void onClick(View v) {
        strUsername = username.getText().toString();
        strPassword = password.getText().toString();

        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            //Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
        }
        String strReturnValue = myCore.validateLoginStrings(strUsername, strPassword);
        if (strReturnValue.equals(""))
            switch (v.getId()) {
                case R.id.btn_login:
                    //region check connection
                    boolean chkInetCon = false;
                    Context sContext = getApplicationContext();
                    chkInetCon = myCore.checkInternetConnection(sContext);

                    if(chkInetCon == true)
                        if (strReturnValue.equals(""))
                            new CheckUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        else
                            Toast.makeText(this, strReturnValue, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    //endregion
                    break;
            }
        else
            Toast.makeText(this, strReturnValue, Toast.LENGTH_SHORT).show();
        if (v.getId() == R.id.link_signup) {
            Intent i = new Intent(LogInPageActivity.this, SignUpActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        // determine which button was pressed:

            /*case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break; */
    }
    //endregion

    //region goToRegistration
    /* public void goToRegistration(View view) {
        //Intent intent = new Intent(this, RegistrationActivity.class);
        //startActivity(intent);
    } */
    //endregion

    //region CheckUser
    class CheckUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        String nm_dpn = "";
        String role_cd = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogInPageActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", strUsername));
                params.add(new BasicNameValuePair("password", strPassword));
                params.add(new BasicNameValuePair("app_v", app_ver));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "GET", params);

                strJSONReturnMsg = json.toString().trim();
                if(strJSONReturnMsg.contains("success")) {
                    JSONArray jArray = json.getJSONArray("info");
                    JSONObject msg = jArray.getJSONObject(0);

                    strUsername = msg.getString("username");
                    strPassword = msg.getString("password");
                    nm_dpn = msg.getString("nm_dpn");
                    role_cd = msg.getString("role_cd");
                    app_ver = msg.getString("app_v");
                    cabang = msg.getString("cabang");
                    strErrorMsg = "Hi, " + nm_dpn;
                    saveToSharedPreferences(strUsername, strPassword, role_cd, app_ver, cabang);
                }
                if (!strJSONReturnMsg.equals("")){
                    if(strJSONReturnMsg.contains("|"))
                        strErrorMsg = strJSONReturnMsg.split("\\|")[1].trim();
                }
                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(LogInPageActivity.this, ReadComments.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

                pDialog.dismiss();
                Toast.makeText(LogInPageActivity.this, strErrorMsg, Toast.LENGTH_LONG).show();
                if (strJSONReturnMsg.contains("success")) {
                    Intent i = new Intent(LogInPageActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                } else {
                    Toast.makeText(LogInPageActivity.this, strErrorMsg, Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                }
        }
    }
    //endregion

    //region update App Version
    class updateAppVersion extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", strUsername));
                params.add(new BasicNameValuePair("password", strPassword));
                params.add(new BasicNameValuePair("app_v", app_ver));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        myCore.connectToDB("update_app_version"), "POST", params);
                //JSONArray jArray = json.getJSONArray("msg");
                //JSONObject msg = jArray.getJSONObject(0);
                strJSONReturnMsg = json.toString().trim();
                if (strJSONReturnMsg.contains("Error Update"))
                    strErrorMsg = "Username dan password yang anda masukkan salah.";
                else if (strJSONReturnMsg.contains("Invalid"))
                    strErrorMsg = "Silahkan mengupdate aplikasi GKR Mobile anda terlebih dahulu di Play Store.";
                else {
                    strErrorMsg = "";
                }

                if (strErrorMsg.equals(""))
                    isCheckUser = true;
                else
                    isCheckUser = false;
                // check your log for json response
                Log.d("Update attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Update Successful!", json.toString());
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Update Failed!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            if (!strErrorMsg.equals("")) {
                Toast.makeText(LogInPageActivity.this, strErrorMsg, Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
                username.requestFocus();
            }

        }

    }//endregion
}

