package com.webster.gmobile.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.util.JSONParser;
import com.webster.gmobile.util.myCore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MaintainJadwalActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText service_dt, additional_svcr, theme, preacher_nm, worship_ldr, pemusik, usher,
                    singers, kolektan, video, lcd;
    public String strServiceDt, strAdditionalSvcr, strJnsPlynan, strTheme, strPreacherNm, strUsername, strWorshipLdr,
                  strPemusik, strUsher, strSingers, strKolektan, strVideo, strLCD, strCabang;
    public StringBuilder sb_additional_svcr = new StringBuilder();
    public Spinner ddlJenisPelayanan;
    private ProgressDialog pDialog;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_jadwal);

        TextView Submit;
        Submit = (TextView) findViewById(R.id.submit);

        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        strUsername = prefs.getString("username", "");
        strCabang = prefs.getString("cabang", "");

        service_dt = (EditText)findViewById(R.id.service_dt);
        ddlJenisPelayanan = (Spinner)findViewById(R.id.ddlJenisPelayanan);
        theme = (EditText)findViewById(R.id.theme);
        preacher_nm = (EditText)findViewById(R.id.preacher_nm);
        additional_svcr = (EditText)findViewById(R.id.additional_svcr);
        worship_ldr = (EditText)findViewById(R.id.worship_ldr);
        pemusik = (EditText)findViewById(R.id.pemusik);
        usher = (EditText)findViewById(R.id.usher);
        singers = (EditText)findViewById(R.id.singers);
        kolektan = (EditText)findViewById(R.id.kolektan);
        video = (EditText)findViewById(R.id.video);
        lcd = (EditText)findViewById(R.id.lcd);

        Spinner dropdownJnsPlynan = (Spinner)findViewById(R.id.ddlJenisPelayanan);
        String[] itemsJnsPlynan = new String[]{"KU 1", "KU 2", "Remaja", "Pemuda", "Youth (Gabungan)",
                "Komisi Pasutri", "Komisi Wanita", "Komisi Usia Emas", "Persekutuan Doa", "MPD", "Natal", "Jumat Agung",
                "Paskah", "Tahun Baru", "KKR", "Retreat"};
        ArrayAdapter<String> adapterJnsPlynan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsJnsPlynan);
        dropdownJnsPlynan.setAdapter(adapterJnsPlynan);

        service_dt.setOnClickListener(this);
        Submit.setOnClickListener(this);
    }

    public void onClick(View v) {
        try {
            if (v.getId() == R.id.submit) {
                strServiceDt = service_dt.getText().toString().trim();
                strJnsPlynan = ddlJenisPelayanan.getSelectedItem().toString();
                strTheme = theme.getText().toString().trim();
                strPreacherNm = preacher_nm.getText().toString().trim();
                strWorshipLdr = worship_ldr.getText().toString().trim();
                strPemusik = pemusik.getText().toString().trim();
                strUsher = usher.getText().toString().trim();
                strSingers = singers.getText().toString().trim();
                strKolektan = kolektan.getText().toString().trim();
                strVideo = video.getText().toString().trim();
                strLCD = lcd.getText().toString().trim();
                strAdditionalSvcr = additional_svcr.getText().toString().trim();
                String strReturnMsg = myCore.validateServiceScheduleStrings(strServiceDt, strTheme, strPreacherNm, strWorshipLdr);
                if(strReturnMsg.equals("")){
                    setJadwalPelayanan(strServiceDt, strJnsPlynan, strTheme.trim(), strPreacherNm.trim(), strWorshipLdr.trim(),
                                    strPemusik.trim(), strUsher.trim(), strSingers.trim(), strKolektan.trim(),
                                    strVideo.trim(), strLCD.trim(), sb_additional_svcr.toString().trim());
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

    //region Set Jadwal Pelayanan
    private void setJadwalPelayanan(final String service_dt, final String jns_plynan, final String tema,
                                    final String preacher_nm, final String worship_ldr, final String pemusik,
                                    final String usher, final String singers, final String kolektan,
                                    final String video, final String lcd, final String additional_svcr){
        // For test only - Delete Later
        final String passenger_title[] = {"1"};
        final String passenger_first_name[] = {"Dadan"};
        final String passenger_last_name[] = {"Stromerb"};
        final String passenger_middle_name[] = {""};
        final String passenger_dob[] = {"22-11-1992"};
        final String passenger_meal[] = {"1"};
        final String passenger_room_type[] = {"19348"};
        final String passenger_visa_id[][] = {{"275"},{"1"}};
        final String passenger_passport_select[] = {"1"};
        final String passenger_passport_number[] = {"234324234"};
        final String passenger_poi[] = {"114"};
        final String passenger_passport_expirity[] = {"15-12-2021"};
        // End for test

        // ========================== Volley Start ==============================
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://lab.gositus.com/avia/mobile_api/gateway/?key=719afed92b614b95f203d852015e80c66e844d7f&method=booking"; // myCore.connectToDB("setJadwalPelayanan")
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                        if(response.toString().contains("Success")){
                            Toast.makeText(getApplicationContext(),
                                    "Jadwal Pelayanan berhasil ditambahkan",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MaintainJadwalActivity.this, MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Anda sudah pernah membuat jadwal untuk tanggal dan jenis ibadah ini," +
                                            " silahkan mencoba kembali",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MaintainJadwalActivity.this, MainActivity.class);
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
                // Real Work here, uncomment after finish
                /*jsonParams.put("service_dt", service_dt);
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
*/
                /*try {


                    JSONObject passenger_title = new JSONObject();
                    JSONObject passenger_first_name = new JSONObject();
                    JSONObject passenger_last_name = new JSONObject();
                    JSONObject passenger_middle_name = new JSONObject();
                    JSONObject passenger_dob = new JSONObject();
                    JSONObject passenger_meal = new JSONObject();
                    JSONObject passenger_room_type = new JSONObject();
                    JSONObject passenger_visa_id_2d = new JSONObject();
                    JSONObject passenger_visa_id_1d = new JSONObject();
                    JSONObject passenger_passport_select = new JSONObject();
                    JSONObject passenger_passport_number = new JSONObject();
                    JSONObject passenger_poi = new JSONObject();
                    JSONObject passenger_passport_expirity = new JSONObject();


                    passenger_title.put("1", "1");
                    passenger_first_name.put("1", "Dadan");
                    passenger_last_name.put("1", "Stromerb");
                    passenger_middle_name.put("1", "");
                    passenger_dob.put("1", "22-11-1992");
                    passenger_meal.put("1", "1");
                    passenger_room_type.put("1", "19348");
                    passenger_visa_id_2d.put("275", "1");
                    passenger_visa_id_1d.put("1", passenger_visa_id_2d);
                    passenger_passport_select.put("1", "1");
                    passenger_passport_number.put("1", "234324234");
                    passenger_poi.put("1", "114");
                    passenger_passport_expirity.put("1", "15-12-2021");
                    // TO DELETE
                    jsonParams.put("tpid", "3147");
                    jsonParams.put("contact_title", "1");
                    jsonParams.put("contact_first_name", "Dadan");
                    jsonParams.put("contact_last_name", "Stromerb");
                    jsonParams.put("contact_middle_name", "asd");
                    jsonParams.put("contact_email", "dadan@gositus.com");
                    jsonParams.put("contact_dob", "22-11-1992");
                    jsonParams.put("contact_phone", "081806037274");
                    jsonParams.put("contact_address", "jajat");
                    jsonParams.put("contact_postcode", "14120");
                    jsonParams.put("contact_country", "114");

                    JSONArray params_passenger_title = new JSONArray();
                    JSONArray params_passenger_first_name = new JSONArray();
                    JSONArray params_passenger_last_name = new JSONArray();
                    JSONArray params_passenger_middle_name = new JSONArray();
                    JSONArray params_passenger_dob = new JSONArray();
                    JSONArray params_passenger_meal = new JSONArray();
                    JSONArray params_passenger_room_type = new JSONArray();
                    JSONArray params_passenger_visa_id = new JSONArray();
                    JSONArray params_passenger_passport_select = new JSONArray();
                    JSONArray params_passenger_passport_number = new JSONArray();
                    JSONArray params_passenger_poi = new JSONArray();
                    JSONArray params_passenger_passport_expirity = new JSONArray();
                    JSONArray params_2d_params_passenger_visa_id = new JSONArray();
                    JSONArray arrArticulos = new JSONArray();

                    //params_passenger_title.put(arrHobbies);
                    *//*params_passenger_first_name.put(passenger_first_name[0]);
                    params_passenger_last_name.put(passenger_last_name[0]);
                    params_passenger_middle_name.put(passenger_middle_name[0]);
                    params_passenger_dob.put(passenger_dob[0]);
                    params_passenger_meal.put(passenger_meal[0]);
                    params_passenger_room_type.put(passenger_room_type[0]);
                    params_passenger_visa_id.put(passenger_visa_id[0][0]);
                    params_2d_params_passenger_visa_id.put(params_passenger_visa_id);
                    params_passenger_passport_select.put(passenger_passport_select[0]);
                    params_passenger_passport_number.put(passenger_passport_number[0]);
                    params_passenger_poi.put(passenger_poi[0]);
                    params_passenger_passport_expirity.put(passenger_passport_expirity[0]);*//*


                    jsonParams.put("passenger_title", passenger_title.toString());
                    jsonParams.put("passenger_first_name", passenger_first_name.toString());
                    jsonParams.put("passenger_last_name", passenger_last_name.toString());
                    jsonParams.put("passenger_middle_name", passenger_middle_name.toString());
                    jsonParams.put("passenger_dob", passenger_dob.toString());
                    jsonParams.put("passenger_meal", passenger_meal.toString());
                    jsonParams.put("passenger_room_type", passenger_room_type.toString());
                    jsonParams.put("passenger_visa_id", passenger_visa_id_1d.toString());
                    jsonParams.put("passenger_passport_select", passenger_passport_select.toString());
                    jsonParams.put("passenger_passport_number", passenger_passport_number.toString());
                    jsonParams.put("passenger_poi", passenger_poi.toString());
                    jsonParams.put("passenger_passport_expirity", passenger_passport_expirity.toString());

                    jsonParams.put("booking_notes", "test");
                    jsonParams.put("agree_termofservice", "on");

                    Log.d("Response : ", jsonParams.toString());
                    // END TO DELETE
                }catch (JSONException e) {
                    e.printStackTrace();
                }*/

                // ------------

                String json = "{\"tpid\":\"5\"}";

                HttpClient httpClient = new DefaultHttpClient();

                try {
                    HttpPost request = new HttpPost("http://lab.gositus.com/avia/mobile_api/gateway/?key=719afed92b614b95f203d852015e80c66e844d7f&method=booking    ");
                    StringEntity params =new StringEntity("message=" + json);
                    request.addHeader("content-type", "application/json");
                    request.setEntity(params);
                    HttpResponse response = httpClient.execute(request);

                    // handle response here...

                    System.out.println(org.apache.http.util.EntityUtils.toString(response.getEntity()));
                    org.apache.http.util.EntityUtils.consume(response.getEntity());
                } catch (Exception ex) {
                    // handle exception here
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
                // -----------
                return jsonParams;


            }
        };


        queue.add(mStringRequest);

        // ========================== Volley End ===============================
    }
    //endregion

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
            DatePickerDialog dialog = new DatePickerDialog(MaintainJadwalActivity.this, this, startYear, startMonth, startDay);
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
            service_dt.setText(strMonth + "/" + strDay + "/" + strYear);
            //showStartDateDialog();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintain_jadwal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
