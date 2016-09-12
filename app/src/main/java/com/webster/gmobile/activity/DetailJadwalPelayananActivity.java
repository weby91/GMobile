package com.webster.gmobile.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Locale;

public class DetailJadwalPelayananActivity extends AppCompatActivity implements View.OnClickListener {

    public String[] arrservice_dt, arrservice_type, arrtheme, arrpreacher_nm, arrworship_ldr, arrpemusik,
                    arrusher, arrsingers, arrkolektan, arrvideo, arrlcd, arradditional_svcr, arrid;
    private FloatingActionButton  widget_edit_jadwal;
    public String username, password, role_cd, cabang;
    String service_dt = "";
    String service_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal_pelayanan);

        String srvc_id = "";
        Bundle extras = getIntent().getExtras();

        widget_edit_jadwal = (FloatingActionButton) findViewById(R.id.edit_jadwal);

        if (extras != null) {
            service_dt = extras.getString("service_dt");
            service_type = extras.getString("service_type");
        }

        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        role_cd = prefs.getString("role_cd", "");
        cabang = prefs.getString("cabang", "");

        if(role_cd.equals("1")) {
            widget_edit_jadwal.setVisibility(View.VISIBLE);
        }

        try {
            getDetailJadwalPelayanan(service_dt, service_type) ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        widget_edit_jadwal.setOnClickListener(this);
    }

    //region onClick
    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()) {
                case R.id.edit_jadwal:
                    Intent i = new Intent(getApplicationContext(), EditJadwalPelayananActivity.class);
                    i.putExtra("service_dt", service_dt.trim());
                    i.putExtra("service_type", service_type.trim());
                    startActivity(i);
                    break;
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    //endregion

    //region Get Detail Jadwal Pelayanan
    private void getDetailJadwalPelayanan(final String service_dt, final String service_type) throws UnsupportedEncodingException {

        // ========================== Volley Start ==============================
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = myCore.connectToDB("getDetailJadwal");
        url = url + "?service_dt=" + service_dt + "&service_type=" + URLEncoder.encode(service_type, "UTF-8")
            + "&cabang=" + URLEncoder.encode(cabang, "UTF-8");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total;
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


                            tvdetailtanggal[i]= new TextView(getBaseContext());
                            tvdetailtanggal[i] .setText(day + " - " + arrservice_dt[i]);
                            tvdetailtanggal[i] .setTextSize(30);
                            tvdetailtanggal[i] .setTextColor(Color.GRAY);

                            tvdetails[i]= new TextView(getBaseContext());
                            tvdetails[i] .setText("\n" + "Ibadah : " + arrservice_type[i]);
                            tvdetails[i] .append("\n" + "Tema : " + arrtheme[i]);
                            tvdetails[i] .append("\n" + "Pengkhotbah : " + arrpreacher_nm[i]);

                            /*16th January 2016 - Webster Tulai
                            Menampilkan field-field baru yang di tambahkan di database (worship_ldr, pemusik, usher, dll)*/
                            if(!arrworship_ldr[i].equals(""))
                                tvdetails[i] .append("\n" + "Worship Leader : " + arrworship_ldr[i]);
                            if(!arrpemusik[i].equals(""))
                                tvdetails[i] .append("\n" + "Pemusik : " + arrpemusik[i]);
                            if(!arrusher[i].equals(""))
                                tvdetails[i] .append("\n" + "Usher : " + arrusher[i]);
                            if(!arrsingers[i].equals(""))
                                tvdetails[i] .append("\n" + "Singers : " + arrsingers[i]);
                            if(!arrkolektan[i].equals(""))
                                tvdetails[i] .append("\n" + "Kolektan : " + arrkolektan[i]);
                            if(!arrvideo[i].equals(""))
                                tvdetails[i] .append("\n" + "Video : " + arrvideo[i]);
                            if(!arrlcd[i].equals(""))
                                tvdetails[i] .append("\n" + "LCD : " + arrlcd[i]);
                            if(!arradditional_svcr[i].equals(""))
                                tvdetails[i] .append("\n" + arradditional_svcr[i]);

                            tvdetails[i] .setTextSize(15);
                            tvdetails[i] .setTextColor(Color.BLACK);


                            //LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams((int) ViewGroup.LayoutParams.WRAP_CONTENT, (int)ViewGroup.LayoutParams.WRAP_CONTENT);
                            //params4.leftMargin = 200+(i+10);
                            //params4.topMargin = 280;
                            //tvservice_type[i] .setLayoutParams(params4);
                            ll.addView(tvdetailtanggal[i] );
                            ll2.addView(tvdetails[i]);
                            //ll.addView(tvservice_type[i] );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_jadwal_pelayanan, menu);
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
