package com.example.josien.programmeerproject2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView items_listview;
    EditText input_station;
    private Train_AsyncTask obj;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";
    private static final String TAG_RITNUMMER = "Ritnummer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_station = (EditText) findViewById(R.id.editText);
        items_listview = (ListView) findViewById(R.id.listViewleavingtrains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        CheckIn.class);
                in.putExtra(TAG_EINDBESTEMMING, eindbestemming);
                in.putExtra(TAG_VERTREKTIJD, vertrektijd);
                in.putExtra(TAG_RITNUMMER, ritnummer);
                startActivity(in);

            }
        });
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
            Intent friendscheckin = new Intent(this, Friends_checkin.class);
            friendscheckin.putExtra("friendscheckin", 500);
            startActivity(friendscheckin);
        } else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, Instellingen.class);
            instellingen.putExtra("Instellingen", 500);
            startActivity(instellingen);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void get_data(View view) {
        String input = input_station.getText().toString();
        Train_AsyncTask asyncTask = new Train_AsyncTask(this);
        asyncTask.execute(input);
    }

    public void setData(ArrayList<TrainData> traindata) {
        TrainAdapter adapter = new TrainAdapter(this, traindata);
        items_listview.setAdapter(adapter);
    }

}
