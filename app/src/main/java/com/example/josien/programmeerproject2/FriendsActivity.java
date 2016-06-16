package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.shephertz.app42.paas.sdk.android.App42API;

import org.json.JSONArray;


import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendscheckin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        App42API.initialize(getApplicationContext(),"ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9","b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Intent intent = getIntent();
        //String jsondata = intent.getStringExtra("jsondata");

        //JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            JSONArray friendslist = new JSONArray(prefs.getString("key", "[]"));
            for (int i = 0; i < friendslist.length(); i++) {
                Log.d("JSonarray", "onCreate() returned: " + friendslist);
                friends.add(friendslist.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listView);
        assert listView != null;
        listView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_checkin) {
            Intent checkin = new Intent(this, MainActivity.class);
            checkin.putExtra("Checkin", 500);
            startActivity(checkin);
        } else if (id == R.id.nav_friendscheckin) {
            Toast.makeText(this, "Je bent al op deze pagina", Toast.LENGTH_SHORT).show();
        }  else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("instellingen", 500);
            startActivity(instellingen);
        }   else if (id == R.id.nav_historie) {
            Intent historie = new Intent(this, HistoryActivity.class);
            historie.putExtra("historie", 500);
            startActivity(historie);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
