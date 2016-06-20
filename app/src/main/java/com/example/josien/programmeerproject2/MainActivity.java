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

import java.util.List;

/*
*  MainActivity handles the first screen of the app after the Facebook Login. In this screen, the
*  user can search for a station he/she is leaving from. After hitting the search-button, all
*  leaving trains for at least the upcoming hour will be shown. The user can click on a single item
*  and then go to the next screen.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declare variables.
    ListView items_listview;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private static final String TAG_VERTREKTIJD = "Vertrektijd";
    private static final String TAG_RITNUMMER = "Ritnummer";
    AutoCompleteTextView autoCompleteTextView;
    String[] stations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the right variables to the right xml-ids.
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        items_listview = (ListView) findViewById(R.id.listViewleavingtrains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Handles the autoCompleteTextView.
        stations = getResources().getStringArray(R.array.stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stations);
        autoCompleteTextView.setAdapter(adapter);

        // Handles the Navigation bar.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        ListView lv = getListView();


        // Handles the next Activity when clicking on a single item.
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

                // Starting the CheckInActivity and get the right values.
                Intent in = new Intent(getApplicationContext(),
                        CheckInActivity.class);
                in.putExtra(TAG_EINDBESTEMMING, eindbestemming);
                in.putExtra(TAG_VERTREKTIJD, vertrektijd);
                in.putExtra(TAG_RITNUMMER, ritnummer);

                startActivity(in);


            }
        });

    }

    /*
    Get the right ListView by calling it and combine it with the XML-ListView.
     */
    private ListView getListView() {
        return items_listview;
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



    /*
    Handles the Navigation bar, so the user can go to different Activities.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_checkin) {
            Toast.makeText(this, R.string.zelfdepagina, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_friendscheckin) {
            Intent friendscheckin = new Intent(this, FriendsActivity.class);
            friendscheckin.putExtra("friendscheckin", 500);
            startActivity(friendscheckin);
        } else if (id == R.id.nav_instellingen) {
            Intent instellingen = new Intent(this, SettingsActivity.class);
            instellingen.putExtra("SettingsActivity", 500);
            startActivity(instellingen);
        } else if (id == R.id.nav_historie) {
            Intent historie = new Intent(this, HistoryActivity.class);
            historie.putExtra("historie", 500);
            startActivity(historie);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    When clicking on the search button, get_data will do his work. It set the first word of the
    station behind the Http-request, and execute it to TrainAsyncTask.
     */
    public void get_data(View view) {
        TextView richting = (TextView) findViewById(R.id.richting);
        assert richting != null;
        richting.setVisibility(View.VISIBLE);
        TextView vertrektijd = (TextView) findViewById(R.id.vertrektijd);
        assert vertrektijd != null;
        vertrektijd.setVisibility(View.VISIBLE);

        TrainAsyncTask apiHandler = new TrainAsyncTask(MainActivity.this);

        // Make a string of the Station.
        String station = autoCompleteTextView.getText().toString();
        // Split the string and get only the first word (the abbreviation of the station).
        String arr[] = station.split(" ", 2);
        String input = arr[0];

        apiHandler.execute(input);

    }

    /*
    The method setData combines the ListView and Data correctly by using an Adapter.
     */
    public void setData(List<TrainData> traindata) {
        TrainAdapter adapter = new TrainAdapter(this, -1, traindata);
        items_listview.setAdapter(adapter);

    }
}
