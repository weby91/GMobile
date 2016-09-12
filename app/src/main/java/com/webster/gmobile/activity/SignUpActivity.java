package com.webster.gmobile.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.webster.gmobile.gmobile.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import com.webster.gmobile.util.JSONParser;
import com.webster.gmobile.util.myCore;

/**
 * Created by weby on 12/14/2015.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener  {

    public EditText nm_dpn, nm_blkg, username, password, conf_password,  email, mobile_no;
    public String strNm_dpn, strNm_blkg, strUsername, strPassword, strConfPassword, strDob, strEmail, strMobile_no, strGender, strKomisi, strCabang;
    public Spinner ddlGender, ddlKomisi, ddlCabang;
    public static EditText dob;
    private ProgressDialog pDialog;
    private static final String LOGIN_URL = myCore.connectToDB("registration");
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static String app_ver = "";
    // JSON parser class

    JSONParser jsonParser = new JSONParser();


    //region onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        Button register = (Button)findViewById(R.id.register);
        dob = (EditText)findViewById(R.id.dob);
        TextView link_login = (TextView)findViewById(R.id.link_login);
        // Create underline
        SpannableString content = new SpannableString("Login");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        link_login.setText(content);

        // Gender Drop Down List
        Spinner dropdownGender = (Spinner)findViewById(R.id.ddlGender);
        String[] itemsGender = new String[]{"Pria", "Wanita"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsGender);
        dropdownGender.setAdapter(adapterGender);

        // Komisi Drop Down List
        Spinner dropdownKomisi = (Spinner)findViewById(R.id.ddlKomisi);
        String[] itemsKomisi = new String[]{"Komisi Sekolah Minggu", "Komisi Remaja", "Komisi Pemuda", "Komisi Wanita",
                "Komisi Pasutri", "Komisi Usia Emas"};
        ArrayAdapter<String> adapterKomisi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsKomisi);
        dropdownKomisi.setAdapter(adapterKomisi);

        // Cabang Drop Down List
        Spinner dropdownCabang = (Spinner)findViewById(R.id.ddlCabang);
        String[] itemsCabang = new String[]{"GKR Angke", "GKR Gedong", "GKR Kupang", "GKR PIK", "GKR Teluk Gong"};
        ArrayAdapter<String> adapterCabang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsCabang);
        dropdownCabang.setAdapter(adapterCabang);

        nm_dpn = (EditText)findViewById(R.id.nm_dpn);
        nm_blkg = (EditText)findViewById(R.id.nm_blkg);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        conf_password = (EditText)findViewById(R.id.confirm_password);
        dob = (EditText)findViewById(R.id.dob);
        email = (EditText)findViewById(R.id.email);
        mobile_no = (EditText)findViewById(R.id.mobile_no);
        ddlGender = (Spinner)findViewById(R.id.ddlGender);
        ddlKomisi = (Spinner)findViewById(R.id.ddlKomisi);
        ddlCabang = (Spinner)findViewById(R.id.ddlCabang);

        //get App's version
        try {
            app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch(Exception e){
            System.out.println(e.toString());
        }


        dob.setOnClickListener(this);
        register.setOnClickListener(this);
        link_login.setOnClickListener(this);
    }
    //endregion

    //region Create Datepicker
    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("ValidFragment")
    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(SignUpActivity.this, this, startYear, startMonth, startDay);
            return dialog;
        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            String strYear, strMonth, strDay;
            strYear = String.valueOf(year);
            strMonth = String.valueOf(monthOfYear + 1);
            strDay = String.valueOf(dayOfMonth);
            if(strMonth.length() == 1)
                strMonth = "0" + strMonth;
            if(strDay.length() == 1)
                strDay = "0" + strDay;
            dob.setText(strMonth + "/" + strDay + "/" + strYear);
        }
    }

    public void showStartDateDialog(View v){
        DialogFragment dialogFragment = new StartDatePicker();
        dialogFragment.show(getFragmentManager(), "start_date_picker");

        //updateStartDateDisplay();
    }
    //endregion

    //region onClick
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.dob) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showStartDateDialog(v);
            }
            else if(v.getId() == R.id.register){
                strNm_dpn = nm_dpn.getText().toString().trim();
                strNm_blkg = nm_blkg.getText().toString().trim();
                strUsername = username.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                strConfPassword = conf_password.getText().toString().trim();
                strDob = dob.getText().toString().trim();
                strEmail = email.getText().toString().trim();
                strMobile_no = mobile_no.getText().toString().trim();
                strGender = ddlGender.getSelectedItem().toString().trim();
                strKomisi = ddlKomisi.getSelectedItem().toString().trim();
                strCabang = ddlCabang.getSelectedItem().toString().trim();

                String strReturnValue = myCore.validateRegistrationStrings(strNm_dpn, strNm_blkg, strUsername, strPassword,
                                                                        strConfPassword, strDob,
                                                                        strEmail, strMobile_no);

                //region check connection
                boolean chkInetCon = false;
                Context sContext = getApplicationContext();
                chkInetCon = myCore.checkInternetConnection(sContext);

                if(chkInetCon == true)
                    if (strReturnValue.equals(""))
                        new CreateUser().execute();
                    else
                        Toast.makeText(this, strReturnValue, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SignUpActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                //endregion

            }else if(v.getId() == R.id.link_login){
                Intent i = new Intent(SignUpActivity.this, LogInPageActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    //endregion

    //region CreateUser
    class CreateUser extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Please Wait...");
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
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("nm_dpn", strNm_dpn));
                params.add(new BasicNameValuePair("nm_blkg", strNm_blkg));
                params.add(new BasicNameValuePair("username", strUsername));
                params.add(new BasicNameValuePair("password", strPassword));
                params.add(new BasicNameValuePair("dob", strDob));
                params.add(new BasicNameValuePair("gender", strGender));
                params.add(new BasicNameValuePair("cabang", strCabang));
                params.add(new BasicNameValuePair("komisi", strKomisi));
                params.add(new BasicNameValuePair("email", strEmail));
                params.add(new BasicNameValuePair("mobile_no", strMobile_no));
                params.add(new BasicNameValuePair("app_v", app_ver));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(SignUpActivity.this, ReadComments.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
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
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SignUpActivity.this, LogInPageActivity.class);
            finish();
            startActivity(i);
            /*if (file_url != null){
                Toast.makeText(RegistrationActivity.this, file_url, Toast.LENGTH_LONG).show();
            }*/

        }

    }
    //endregion




}
