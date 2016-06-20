package com.example.josien.programmeerproject2;

/*
*  Josien Jansen
*  11162295
*  Programmeerproject
*  06-2016
*  Universiteit van Amsterdam
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONException;

import java.util.ArrayList;

/*
* This Activity handles the real Check-In of the user. The data will come from the previous
* activity: MainActivity, and by clicking on the button, the data will be stored in the SQLite-
* Database and the ritnummer will also be stored online.
 */

public class CheckInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declare variables.
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";
    private static final String TAG_RITNUMMER = "Ritnummer";
    DBHelper DBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        DBHelper = new DBHelper(this, null, null, 1);

        // Getting intent data from MainActivity.
        Intent in = getIntent();
        String eindbestemming = in.getStringExtra(TAG_EINDBESTEMMING);
        String vertrektijd = in.getStringExtra(TAG_VERTREKTIJD);
        String ritnummer = in.getStringExtra(TAG_RITNUMMER);

        // Combine the code with xml.
        TextView eindbestemming_view = (TextView) findViewById(R.id.eindbestemming);
        TextView vertrektijd_view = (TextView) findViewById(R.id.vertrektijd);
        TextView ritnummer_view = (TextView) findViewById(R.id.ritnummer);

        // Set the data into the right TextView.
        assert eindbestemming_view != null;
        eindbestemming_view.setText(eindbestemming);
        assert vertrektijd_view != null;
        vertrektijd_view.setText(vertrektijd);
        assert ritnummer_view != null;
        ritnummer_view.setText(ritnummer);

        // Handles the navigation bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
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
        }  else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("instellingen", 500);
            startActivity(instellingen);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    * When the 'Check-In' -button is clicked, addHistory will add the data into the SQLiteDatabase
    * and shows it in History. It also stores the ritnummers into the online App42 Database.
    */

    public void addHistory(View view) throws JSONException {

        // Get the right data from the previous Activity.
        Intent in = getIntent();
        String eindbestemming = in.getStringExtra(TAG_EINDBESTEMMING);
        String vertrektijd = in.getStringExtra(TAG_VERTREKTIJD);
        String ritnummer = in.getStringExtra(TAG_RITNUMMER);

        // Send the ritnummer to FriendsActivity too.
        Intent intent = new Intent(getApplicationContext(),
                FriendsActivity.class);

        intent.putExtra(TAG_RITNUMMER, ritnummer);

        DBHelper.addHistory(eindbestemming, vertrektijd);

        // Store data in online App42 Database.
        String dbName = "test";
        String collectionName = "ritnummer";

        // Store the ritnummer as JSON.
        String userName = Profile.getCurrentProfile().getId();
        Log.d("facebookid", "addHistory() returned: " + userName);
        String ritnummers = "{\"ritnummer\":\"";
        String to_json ="\"";
        String FacebookId =",\"facebookid\":\"";
        String realfacebookId = "\"}";
        String total = ritnummers + ritnummer + to_json + FacebookId + userName + realfacebookId;
        Log.d("JSONok", "addHistory() returned: " + total);

        // Below snippet will save JSON object in App42 Cloud
        StorageService storageService = App42API.buildStorageService();
        storageService.insertJSONDocument(dbName,collectionName,total,new App42CallBack() {
            public void onSuccess(Object response)
            {
                Storage  storage  = (Storage )response;
                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                for(int i=0;i<jsonDocList.size();i++)
                {
                    System.out.println("objectId is " + jsonDocList.get(i).getDocId());
                    //Above line will return object id of saved JSON object
                    System.out.println("CreatedAt is " + jsonDocList.get(i).getCreatedAt());
                    System.out.println("UpdatedAtis " + jsonDocList.get(i).getUpdatedAt());
                    System.out.println("Jsondoc is " + jsonDocList.get(i).getJsonDoc());
                }
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });

        // Let the user know that the Check-In is succesfull.
        Toast.makeText(this, R.string.ingecheckt, Toast.LENGTH_SHORT).show();

        // Go to next activity.
        Intent friends = new Intent(this, FriendsActivity.class);
        friends.putExtra("friends", 500);
        startActivity(friends);
        // User may not go back to this Activity, so the Activity has to be finished.
        finish();

    }
}
