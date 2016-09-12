package com.webster.gmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.webster.gmobile.fragment.UserConfirmationFragment;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import com.webster.gmobile.fragment.FriendsFragment;
import com.webster.gmobile.fragment.JadwalPelayananFragment;
import com.webster.gmobile.fragment.MessagesFragment;
import com.webster.gmobile.gmobile.R;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
            JadwalPelayananFragment.OnFragmentInteractionListener, UserConfirmationFragment.OnFragmentInteractionListener{

    //region MainActivity
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    boolean doubleBackToExitPressedOnce = false;
    public String username, password, role_cd, cabang;
    SharedPreferences prefs;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //region Save to shared Preferences
    private void saveToSharedPreferences(String isExit){
        prefs = this.getSharedPreferences(
                "utilities", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("isExit", isExit);
        prefsEditor.commit();

    }
    //endregion

    //region Jadwal Pelayanan onFragmentInteraction
    public void onFragmentInteraction(Uri uri, String str){
        Intent i = new Intent(MainActivity.this, JadwalPelayananActivity.class);
        startActivity(i);
    }
    //endregion

    //region User Confirmation onFragmentInteraction
    public void onFragmentInteraction(Uri uri, int a){
        Intent i = new Intent(MainActivity.this, UserConfirmationActivity.class);
        startActivity(i);
    }
    //endregion

    //region Get Shared Preferences
    private void getSharedPreferences(){
        prefs = this.getSharedPreferences("utilities", 0);
    }
    //endregion

    //region Clear Shared Preferences
    private void clearSharedPreferences(String pref_name){
        prefs = this.getSharedPreferences(pref_name, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        saveToSharedPreferences("Yes");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
        role_cd = prefs.getString("role_cd", "");
        cabang = prefs.getString("cabang", "");

        /*
        //region Images Scroll View
        LinearLayout ll_0 = (LinearLayout)findViewById(R.id.main_ll_child);
        ScrollView sv_0 = (ScrollView)findViewById(R.id.main_scroll_view);
        ImageView iv_0 = new ImageView(this);
        ImageView iv_1 = new ImageView(this);
        ImageView iv_2 = new ImageView(this);
        iv_0.setImageResource(R.drawable.christ_was_born);
        iv_1.setImageResource(R.drawable.born_lived_die);
        iv_2.setImageResource(R.drawable.john_3_16);

        iv_0.setAdjustViewBounds(true);
        iv_1.setAdjustViewBounds(true);
        iv_2.setAdjustViewBounds(true);

        FrameLayout.LayoutParams sv_0_lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        Resources r = iv_0.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                r.getDisplayMetrics()
        );

        //sv_0_lp.setMargins(0,0,0,convertPixelsToDp(200,this));

        iv_0.setBackgroundResource(0);
        iv_1.setBackgroundResource(0);
        iv_0.setPadding(0,0,0,px);
        iv_1.setPadding(0,0,0,px);
        iv_2.setPadding(0,0,0,px);
        //ll_0.setBackgroundResource(0);
        //sv_0.setBackgroundResource(0);

        ll_0.setLayoutParams(sv_0_lp);
        ll_0.requestLayout();
        iv_0.setLayoutParams(sv_0_lp);
        iv_1.setLayoutParams(sv_0_lp);
        iv_2.setLayoutParams(sv_0_lp);

        ll_0.addView(iv_0);
        ll_0.addView(iv_1);
        ll_0.addView(iv_2);

        sv_0_lp.topMargin = px;
        sv_0_lp.rightMargin = px;
        sv_0_lp.leftMargin = px;

        //setContentView(ll_0);
        //endregion
*/
        //region Create Layout
        /*ScrollView sv_0 = new ScrollView(this);
        RelativeLayout rl_0 = new RelativeLayout(this);
        LinearLayout ll_0 = new LinearLayout(this);
        LinearLayout ll_1 = new LinearLayout(this);
        ImageView iv_0 = new ImageView(this);

        RelativeLayout.LayoutParams rl_0_lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        ll_0.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ll_0_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ll_1_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        setContentView(ll_0, ll_0_lp);
        setContentView(rl_0, rl_0_lp);
        setContentView(ll_1, ll_1_lp);

        rl_0.addView(ll_0);*/

        //endregion

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        getSharedPreferences();
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

        if(id == R.id.action_search){
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = null;
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new JadwalPelayananFragment();
                title = getString(R.string.title_jadwal_pelayanan);
                break;
            case 2:
                fragment = new UserConfirmationFragment();
                title = getString(R.string.title_confirm_user);
                break;
            case 3:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case 4:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
        else
            getSupportActionBar().setTitle("Home");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app com.webster.gmobile.activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.webster.gmobile.activity/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app com.webster.gmobile.activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.webster.gmobile.activity/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //region When Button Back Pressed
    @Override
    public void onBackPressed() {
        Log.d("com.webster.gmobile.activity.MainActivity", "onBackPressed Called");

        SharedPreferences prefs = this.getSharedPreferences("utilities", 0);
        String isExit = prefs.getString("isExit" ,"");
        if(isExit.equals("Yes")){
            if (doubleBackToExitPressedOnce) {
                clearSharedPreferences("utilities");
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }else{
            getSupportFragmentManager().popBackStack();
            //Intent i = new Intent(this, MainActivity.class);
            //startActivity(i);
            }
    }
    //endregion
//endregion

}
