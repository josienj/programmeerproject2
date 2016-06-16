package com.example.josien.programmeerproject2;

/*
Josien Jansen
11162295
Programmeerproject
Universiteit van Amsterdam
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    ListView items_listview;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";
    private static final String TAG_RITNUMMER = "Ritnummer";
    List<TrainData> traindata;
    DBHelper DBHelper;
    ArrayAdapter<History> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        DBHelper = new DBHelper(this, null, null, 1);

        // getting intent data
        Intent in = getIntent();
        String eindbestemming = in.getStringExtra(TAG_EINDBESTEMMING);
        String vertrektijd = in.getStringExtra(TAG_VERTREKTIJD);
        String ritnummer = in.getStringExtra(TAG_RITNUMMER);


        TextView eindbestemming_view = (TextView) findViewById(R.id.eindbestemming);
        TextView vertrektijd_view = (TextView) findViewById(R.id.vertrektijd);
        TextView ritnummer_view = (TextView) findViewById(R.id.ritnummer);

        assert eindbestemming_view != null;
        eindbestemming_view.setText(eindbestemming);
        assert vertrektijd_view != null;
        vertrektijd_view.setText(vertrektijd);
        assert ritnummer_view != null;
        ritnummer_view.setText(ritnummer);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        }  else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("instellingen", 500);
            startActivity(instellingen);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*
   Add history into database
     */
    public void addHistory(View view) throws JSONException {
        Intent in = getIntent();
        String eindbestemming = in.getStringExtra(TAG_EINDBESTEMMING);
        String vertrektijd = in.getStringExtra(TAG_VERTREKTIJD);
        String ritnummer = in.getStringExtra(TAG_RITNUMMER);

        String History = eindbestemming+vertrektijd;
        DBHelper.addHistory(eindbestemming, vertrektijd);
        Log.d("LINK", "addHistory() returned: " + History);
        Log.d("DBHelper", "addHistory() returned: " + DBHelper);
        //listAdapter.add(history);

        Log.d("History", "addHistory() returned: " + History);

        String dbName = "test";
        String collectionName = "ritnummer";
        Log.d("Ritnummer", "addHistory() returned: " + ritnummer);
        String employeeJSON = "{\"ritnummer\":\"";
        String okee ="\"}";
        String ok = employeeJSON + ritnummer + okee;
        Log.d("JSONok", "addHistory() returned: " + ok);

/* Below snippet will save JSON object in App42 Cloud */
        StorageService storageService = App42API.buildStorageService();
        storageService.insertJSONDocument(dbName,collectionName,ok,new App42CallBack() {
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

        Toast.makeText(this, "Je bent ingecheckt in deze trein!", Toast.LENGTH_SHORT).show();
        Intent friends = new Intent(this, FriendsActivity.class);
        friends.putExtra("friends", 500);
        startActivity(friends);
    }


    private ListView getListView() {
        return items_listview;
    }

}
