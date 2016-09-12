package com.webster.gmobile.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.util.myCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditJadwalPelayananActivity extends AppCompatActivity implements View.OnClickListener  {

    public EditText edt_service_dt, edt_additional_svcr, edt_theme, edt_preacher_nm, edt_worship_ldr, edt_pemusik, edt_usher,
            edt_singers, edt_kolektan, edt_video, edt_lcd;
    public String strServiceDt, strAdditionalSvcr, strJnsPlynan, strTheme, strPreacherNm, strUsername, strWorshipLdr,
            strPemusik, strUsher, strSingers, strKolektan, strVideo, strLCD, strCabang;
    public StringBuilder sb_additional_svcr = new StringBuilder();
    public Spinner ddlJenisPelayanan;
    public String[] arrservice_dt, arrservice_type, arrtheme, arrpreacher_nm, arrworship_ldr, arrpemusik,
            arrusher, arrsingers, arrkolektan, arrvideo, arrlcd, arradditional_svcr, arrid, itemsJnsPlynan;
    private FloatingActionButton  widget_edit_jadwal;
    public String username, password, role_cd, cabang, sService_dt, sService_type;
    Spinner dropdownJnsPlynan;
    ArrayAdapter<String> adapterJnsPlynan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jadwal_pelayanan);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        TextView Submit;
        Submit = (TextView) findViewById(R.id.submit);

        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        strUsername = prefs.getString("username", "");
        strCabang = prefs.getString("cabang", "");

        edt_service_dt = (EditText)findViewById(R.id.service_dt);
        ddlJenisPelayanan = (Spinner)findViewById(R.id.ddlJenisPelayanan);
        edt_theme = (EditText)findViewById(R.id.theme);
        edt_preacher_nm = (EditText)findViewById(R.id.preacher_nm);
        edt_additional_svcr = (EditText)findViewById(R.id.additional_svcr);
        edt_worship_ldr = (EditText)findViewById(R.id.worship_ldr);
        edt_pemusik = (EditText)findViewById(R.id.pemusik);
        edt_usher = (EditText)findViewById(R.id.usher);
        edt_singers = (EditText)findViewById(R.id.singers);
        edt_kolektan = (EditText)findViewById(R.id.kolektan);
        edt_video = (EditText)findViewById(R.id.video);
        edt_lcd = (EditText)findViewById(R.id.lcd);


        itemsJnsPlynan = new String[]{"KU 1", "KU 2", "Remaja", "Pemuda", "Youth (Gabungan)",
                "Komisi Pasutri", "Komisi Wanita", "Komisi Usia Emas", "Persekutuan Doa", "MPD", "Natal", "Jumat Agung",
                "Paskah", "Tahun Baru", "KKR", "Retreat"};
        dropdownJnsPlynan = (Spinner)findViewById(R.id.ddlJenisPelayanan);
        adapterJnsPlynan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsJnsPlynan);
        dropdownJnsPlynan.setAdapter(adapterJnsPlynan);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sService_dt = extras.getString("service_dt");
            sService_type = extras.getString("service_type");
        }

        try {
            getDetailJadwalPelayanan(sService_dt, sService_type) ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        edt_service_dt.setOnClickListener(this);
        Submit.setOnClickListener(this);
    }

    public void onClick(View v) {
        try {
            if (v.getId() == R.id.submit) {
                strServiceDt = edt_service_dt.getText().toString().trim();
                strJnsPlynan = ddlJenisPelayanan.getSelectedItem().toString();
                strTheme = edt_theme.getText().toString().trim();
                strPreacherNm = edt_preacher_nm.getText().toString().trim();
                strWorshipLdr = edt_worship_ldr.getText().toString().trim();
                strPemusik = edt_pemusik.getText().toString().trim();
                strUsher = edt_usher.getText().toString().trim();
                strSingers = edt_singers.getText().toString().trim();
                strKolektan = edt_kolektan.getText().toString().trim();
                strVideo = edt_video.getText().toString().trim();
                strLCD = edt_lcd.getText().toString().trim();
                strAdditionalSvcr = edt_additional_svcr.getText().toString().trim();
                String strReturnMsg = myCore.validateServiceScheduleStrings(strServiceDt, strTheme, strPreacherNm, strWorshipLdr);
                if(strReturnMsg.equals("")){
                    updateJadwalPelayanan(strServiceDt, strJnsPlynan, strTheme.trim(), strPreacherNm.trim(), strWorshipLdr.trim(),
                            strPemusik.trim(), strUsher.trim(), strSingers.trim(), strKolektan.trim(),
                            strVideo.trim(), strLCD.trim(), sb_additional_svcr.toString().trim(), arrid[0]);
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            strReturnMsg,
                            Toast.LENGTH_LONG).show();
                }
            }
            else if (v.getId() == R.id.service_dt) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showStartDateDialog(v);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    //region Get Detail Jadwal Pelayanan
    private void getDetailJadwalPelayanan(final String service_dt, final String service_type) throws UnsupportedEncodingException {
        // ========================== Volley Start ==============================
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = myCore.connectToDB("getDetailJadwal");
        url = url + "?service_dt=" + service_dt + "&service_type=" + URLEncoder.encode(service_type, "UTF-8")
                + "&cabang=" + URLEncoder.encode(strCabang, "UTF-8");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total;
                    int countPipe = 0;
                    String[] service_dt, service_type, theme, preacher_nm, worship_ldr, pemusik, usher,
                            singers, kolektan, video, lcd, additional_svcr, id;
                    Log.d("Response", response.toString());
                    JSONArray jArray = response.getJSONArray("infojadwal");
                    JSONObject msg = jArray.getJSONObject(0);

                    total = Integer.parseInt(msg.getString("total"));
                    if(total > 0){

                        arrservice_dt = service_dt = new String[total];
                        arrservice_type = service_type = new String[total];
                        arrtheme = theme = new String[total];
                        arrpreacher_nm = preacher_nm = new String[total];
                        arrworship_ldr = worship_ldr = new String[total];
                        arrpemusik = pemusik = new String[total];
                        arrusher = usher = new String[total];
                        arrsingers = singers = new String[total];
                        arrkolektan = kolektan = new String[total];
                        arrvideo = video = new String[total];
                        arrlcd = lcd = new String[total];
                        arradditional_svcr = additional_svcr = new String[total];
                        arrid = id = new String[total];

                        TextView[] tvdetailtanggal = new TextView[total];
                        TextView[] tvdetails = new TextView[total];
                        LinearLayout ll =(LinearLayout) findViewById(R.id.ll_detail);
                        LinearLayout ll2 =(LinearLayout) findViewById(R.id.ll_detail2);

                        Calendar c = Calendar.getInstance();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date date;
                        String day = "";
                        for(int i = 0;i < total; i++){

                            JSONObject newmsg = jArray.getJSONObject(i);
                            arrservice_dt[i] = service_dt[i] = newmsg.getString("service_dt");
                            arrservice_type[i] = service_type[i] = newmsg.getString("service_type");
                            arrtheme[i] = theme[i] = newmsg.getString("theme");
                            arrpreacher_nm[i] = preacher_nm[i] = newmsg.getString("preacher_nm");
                            arrworship_ldr[i] = worship_ldr[i] = newmsg.getString("worship_ldr");
                            arrpemusik[i] = pemusik[i] = newmsg.getString("pemusik");
                            arrusher[i] = usher[i] = newmsg.getString("usher");
                            arrsingers[i] = singers[i] = newmsg.getString("singers");
                            arrkolektan[i] = kolektan[i] = newmsg.getString("kolektan");
                            arrvideo[i] = video[i] = newmsg.getString("video");
                            arrlcd[i] = lcd[i] = newmsg.getString("lcd");
                            arradditional_svcr[i] = additional_svcr[i] = newmsg.getString("additional_svcr");
                            arrid[i] = id[i] = newmsg.getString("id");

                            date = df.parse(service_dt[i].replace("/","-"));
                            c.setTime(date);
                            day = String.format(c.getDisplayName(c.DAY_OF_WEEK, c.SHORT, Locale.ENGLISH), date);
                            switch (day){
                                case "Mon":
                                    day = "Senin";
                                    break;
                                case "Tue":
                                    day = "Selasa";
                                    break;
                                case "Wed":
                                    day = "Rabu";
                                    break;
                                case "Thu":
                                    day = "Kamis";
                                    break;
                                case "Fri":
                                    day = "Jumat";
                                    break;
                                case "Sat":
                                    day = "Sabtu";
                                    break;
                                case "Sun":
                                    day = "Minggu";
                                    break;
                            }

                            edt_service_dt.setText(arrservice_dt[i]);
                            // Set Spinner/Drop down list value
                            String compareValue = arrservice_type[i];
                            if (!compareValue.equals(null)) {
                                int spinnerPosition = adapterJnsPlynan.getPosition(compareValue);
                                dropdownJnsPlynan.setSelection(spinnerPosition);
                            }
                            edt_theme.setText(arrtheme[i]);
                            edt_preacher_nm.setText(arrpreacher_nm[i]);
                            edt_worship_ldr.setText(arrworship_ldr[i]);
                            edt_pemusik.setText(arrpemusik[i]);
                            edt_usher.setText(arrusher[i]);
                            edt_singers.setText(arrsingers[i]);
                            edt_kolektan.setText(arrkolektan[i]);
                            edt_video.setText(arrvideo[i]);
                            edt_lcd.setText(arrlcd[i]);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // TODO Auto-generated method stub
                //username.setText("Response => "+response.toString());

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Response", error.toString());
                //username.setText("Response => "+error.toString());
            }

        });


        queue.add(jsObjRequest);

        // ========================== Volley End ===============================
    }
    //endregion

    //region Date Picker Dialog
    public void showStartDateDialog(View v){
        DialogFragment dialogFragment = new StartDatePickers();
        dialogFragment.show(getFragmentManager(), "start_date_picker");

        //updateStartDateDisplay();
    }

    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("ValidFragment")
    public class StartDatePickers extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(EditJadwalPelayananActivity.this, this, startYear, startMonth, startDay);
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
            edt_service_dt.setText(strMonth + "/" + strDay + "/" + strYear);
            //showStartDateDialog();

        }
    }
    //endregion

    //region Set Jadwal Pelayanan
    private void updateJadwalPelayanan(final String service_dt, final String jns_plynan, final String tema,
                                    final String preacher_nm, final String worship_ldr, final String pemusik,
                                    final String usher, final String singers, final String kolektan,
                                    final String video, final String lcd, final String additional_svcr,
                                       final String id){
        // ========================== Volley Start ==============================
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = myCore.connectToDB("updateJadwalPelayanan");
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                        if(response.toString().contains("Success")){
                            Toast.makeText(getApplicationContext(),
                                    "Jadwal Pelayanan berhasil diupdate",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Anda sudah pernah membuat jadwal untuk tanggal dan jenis ibadah ini," +
                                            " silahkan mencoba kembali",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("service_dt", service_dt);
                jsonParams.put("service_type", jns_plynan);
                jsonParams.put("theme", tema);
                jsonParams.put("preacher_nm", preacher_nm);
                jsonParams.put("worship_ldr", worship_ldr);
                jsonParams.put("pemusik", pemusik);
                jsonParams.put("usher", usher);
                jsonParams.put("singers", singers);
                jsonParams.put("kolektan", kolektan);
                jsonParams.put("video", video);
                jsonParams.put("lcd", lcd);
                jsonParams.put("additional_svcr", additional_svcr);
                jsonParams.put("username", strUsername);
                jsonParams.put("cabang", strCabang);
                jsonParams.put("id", id);

                return jsonParams;
            }
        };


        queue.add(mStringRequest);

        // ========================== Volley End ===============================
    }
    //endregion

}
