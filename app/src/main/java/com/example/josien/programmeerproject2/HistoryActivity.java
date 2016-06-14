package com.example.josien.programmeerproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Josien on 14-6-2016.
 */
public class HistoryActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    ListView newList;
    DBHelper dbHelper;
    ArrayAdapter<History> listAdapter;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newList = (ListView) findViewById(R.id.historie_listview);
        dbHelper = new DBHelper(this, null, null, 1);

        setupHistoryListView();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            Intent friendscheckin = new Intent(this, FriendsActivity.class);
            friendscheckin.putExtra("friendscheckin", 500);
            startActivity(friendscheckin);
        } else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("instellingen", 500);
            startActivity(instellingen);
        } else if (id == R.id.nav_historie) {
            Toast.makeText(this, "Je bent al op deze pagina", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
   Add history into database

    * Combine the listview and database correctly
    */
    public void setupHistoryListView() {

        final ArrayList<History> historieArray = dbHelper.retrieveHistorie();

        // Make a new ArrayAdapter to handle the objects, and add them to the view
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, historieArray);

        // Apply the adapter on the ViewList
        newList.setAdapter(listAdapter);

        // Add a new on click listener
        newList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked item value
                History item = (History) newList.getItemAtPosition(position);
                // Delete item from database
                dbHelper.deleteItem(item.get_id());
                // Delete item from listView
                listAdapter.remove(item);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void onSuccess(LoginResult login_result) {
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                login_result.getAccessToken(),
                //AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Intent intent = new Intent(HistoryActivity.this, FriendsActivity.class);
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            intent.putExtra("jsondata", rawName.toString());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }
}


