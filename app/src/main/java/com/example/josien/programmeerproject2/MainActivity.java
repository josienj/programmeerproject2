package com.example.josien.programmeerproject2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView items_listview;
    MainActivity activity;
    private TrainAdapter adapter;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";
    private static final String TAG_RITNUMMER = "Ritnummer";
    AutoCompleteTextView autoCompleteTextView;
    String[] stations;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        items_listview = (ListView) findViewById(R.id.listViewleavingtrains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stations = getResources().getStringArray(R.array.stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations);
        autoCompleteTextView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView lv = getListView();


        // Handles the more information when clicking on a single item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Getting values from selected ListItem
                String eindbestemming = ((TextView) view.findViewById(R.id.eindbestemming))
                        .getText().toString();
                String vertrektijd = ((TextView) view.findViewById(R.id.vertrektijd))
                        .getText().toString();
                String ritnummer = ((TextView) view.findViewById(R.id.ritnummer))
                        .getText().toString();

                // Starting the MoreInformationActivity
                Intent in = new Intent(getApplicationContext(),
                        CheckInActivity.class);
                in.putExtra(TAG_EINDBESTEMMING, eindbestemming);
                in.putExtra(TAG_VERTREKTIJD, vertrektijd);
                in.putExtra(TAG_RITNUMMER, ritnummer);

                startActivity(in);


            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private ListView getListView() {
        return items_listview;
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
            Toast.makeText(this, "Je bent al op deze pagina", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_friendscheckin) {
            Intent friendscheckin = new Intent(this, FriendsActivity.class);
            friendscheckin.putExtra("friendscheckin", 500);
            startActivity(friendscheckin);
        } else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("SettingsActivity", 500);
            startActivity(instellingen);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void get_data(View view) {
        TrainAsyncTask apiHandler = new TrainAsyncTask(MainActivity.this);

        String station = autoCompleteTextView.getText().toString();
        String arr[] = station.split(" ", 2);
        String input = arr[0];

        apiHandler.execute(input);

    }

    public void setData(List<TrainData> traindata) {
        TrainAdapter adapter = new TrainAdapter(this, -1, traindata);
        items_listview.setAdapter(adapter);

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
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.josien.programmeerproject2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.josien.programmeerproject2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
