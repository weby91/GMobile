package com.webster.gmobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.webster.gmobile.gmobile.R;

import com.webster.gmobile.util.myCore;

/**
 * Created by weby on 12/28/2015.
 */
public class JadwalPelayananActivity extends AppCompatActivity implements View.OnClickListener {

    //region Variable Initialization / Declaration
    private ProgressDialog progressDialog;
    public String[] arrservice_dt, arrservice_type, arrtheme, arrpreacher_nm, arradditional_svcr, arrid, arrcabang;
    public String username, password, role_cd, cabang;
    public int totalAdapter = 0;
    private FloatingActionButton widget_add_jadwal, widget_edit_jadwal;
    boolean doubleBackToExitPressedOnce = false;
    ListView lv;
    int arrtotal;
    List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_pelayanan);
        lv = (ListView) findViewById(R.id.lvjdwlplynan);
        //lv.setSelector(R.drawable.drawer_list_selector);
        widget_add_jadwal = (FloatingActionButton) findViewById(R.id.add_jadwal);
        //widget_edit_jadwal = (FloatingActionButton) findViewById(R.id.edit_jadwal);

        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
        role_cd = prefs.getString("role_cd", "");
        cabang = prefs.getString("cabang", "");

        if(role_cd.equals("1")) {
            widget_add_jadwal.setVisibility(View.VISIBLE);
            //widget_edit_jadwal.setVisibility(View.VISIBLE);
        }

        initList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();
                alertDialog(item);
                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });

        widget_add_jadwal.setOnClickListener(this);
        //widget_edit_jadwal.setOnClickListener(this);
    }
    //endregion

    //region onClick
    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()) {
                case R.id.add_jadwal:
                    Intent i = new Intent(getApplicationContext(), MaintainJadwalActivity.class);
                    startActivity(i);
                    break;
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    //endregion

    //region initList
    private void initList() {
        // We populate the planets

        // ========================== Volley Start ==============================
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = myCore.connectToDB("getJadwal");


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total;
                    String[] service_dt, service_type, theme, preacher_nm, additional_svcr, id, cabang;
                    Log.d("Response", response.toString());
                    JSONArray jArray = response.getJSONArray("count");
                    JSONObject msg = jArray.getJSONObject(0);

                    total = Integer.parseInt(msg.getString("total"));
                    arrtotal = total;
                    if(total > 0){

                        arrservice_dt = service_dt = new String[total];
                        arrservice_type = service_type = new String[total];
                        arrtheme = theme = new String[total];
                        arrpreacher_nm = preacher_nm = new String[total];
                        arradditional_svcr = additional_svcr = new String[total];
                        arrid = id = new String[total];
                        arrcabang = cabang = new String[total];
                        for(int i = 0;i < total; i++){
                            JSONObject newmsg = jArray.getJSONObject(i);
                            arrservice_dt[i] = service_dt[i] = newmsg.getString("service_dt");
                            arrservice_type[i] = service_type[i] = newmsg.getString("service_type");
                            arrtheme[i] = theme[i] = newmsg.getString("theme");
                            arrpreacher_nm[i] = preacher_nm[i] = newmsg.getString("preacher_nm");
                            arradditional_svcr[i] = additional_svcr[i] = newmsg.getString("additional_svcr");
                            arrid[i] = id[i] = newmsg.getString("id");
                            arrcabang[i] = cabang[i] = newmsg.getString("cabang");
                        }
                        //populateListView(total, service_dt, service_type, theme, preacher_nm, additional_svcr, id);
                        populateListView();
                        //getListItem(total, nm_dpn, nm_blkg, iduser);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: Data Pelayanan belum diinput. Harap hubungi admin.",
                            //"Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
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

    //region populateListView
    private void populateListView(){
        try{
            Calendar c = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            for(int i = 0;i < arrtotal; i++){
                date = df.parse(arrservice_dt[i].replace("/","-"));
                c.setTime(date);
                String day = String.format(c.getDisplayName(c.DAY_OF_WEEK, c.SHORT, Locale.ENGLISH),date);
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
                if(arrcabang[i].equals(cabang)) {
                    planetsList.add(createPlanet("planet", day + " - " + arrservice_dt[i] + " - " + arrservice_type[i] ));
                    totalAdapter++;
                }
            }
            SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
            lv.setAdapter(simpleAdpt);
            if(totalAdapter == 0)
                Toast.makeText(getApplicationContext(),
                        "Error: Data Pelayanan belum diinput. Harap hubungi admin.",
                        //"Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Log.e("Error", e.toString());
        }
    }
    //endregion

    //region alertDialog
    private void alertDialog(final String tanggal){
        new AlertDialog.Builder(this)
                .setTitle("Jadwal Pelayanan")
                .setMessage("Jadwal \"" + tanggal + "\" ")
                .setPositiveButton("Lihat Jadwal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String renewTanggal = "";
                        String renewServiceType = "";
                        String renewid = "";
                        Intent i = new Intent(getBaseContext(), DetailJadwalPelayananActivity.class);
                        if (tanggal.contains("-")) {
                            String[] splitTanggal = tanggal.split("-");
                            String[] splitID = tanggal.split("#");
                            renewTanggal = splitTanggal[1].trim() + "-" + splitTanggal[2].trim() + "-" + splitTanggal[3].trim();
                            renewServiceType = splitTanggal[4].trim();
                        }
                        if (!renewTanggal.equals("")) {
                            i.putExtra("service_dt", renewTanggal.trim());
                            i.putExtra("service_type", renewServiceType.trim());
                        }
                        // continue with confirm
                        /* progressDialog = new ProgressDialog(JadwalPelayananActivity.this);
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show(); */
                        startActivity(i);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    //endregion

    //region detailJadwalPelayanan
    private void detailJadwalPelayanan(String tanggal){
        new AlertDialog.Builder(this)
                .setTitle("Jadwal Pelayanan")
                .setMessage("Jadwal Tanggal \"" + tanggal + "\" ")
                .setPositiveButton("Lihat Jadwal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with confirm

                        progressDialog = new ProgressDialog(JadwalPelayananActivity.this);
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    //endregion

    //region HashMap
    private HashMap<String, String> createPlanet(String key, String name) {
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put(key, name);

        return planet;
    }
    //endregion

    //region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jadwal_pelayanan, menu);
        return true;
    }
    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent com.webster.gmobile.activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region When Button Back Pressed
    @Override
    public void onBackPressed() {
        Log.d("MainActivity", "onBackPressed Called");
        getSupportFragmentManager().popBackStack();
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
    //endregion

}


