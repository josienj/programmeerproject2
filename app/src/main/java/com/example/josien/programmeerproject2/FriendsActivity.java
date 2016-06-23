package com.example.josien.programmeerproject2;

/*
*  Josien Jansen
*  11162295
*  Programmeerproject
*  06-2016
*  Universiteit van Amsterdam
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
* The FriendsActivity shows the Facebookfriends of the user who are also using the app.
* It also gives the user the possibility to send an image and text to a Facebookfriend when they
* are in the same train by Facebook Messenger.
 */

public class FriendsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declare variables.
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    private boolean mPicking;
    String userName;
    String ownRitnummer;
    String friendId;
    String friend="";
    String eindbestemming;
    Boolean zelfdetrein = false;
    Boolean checkin = false;
    Boolean checkout;
    SharedPreferences bool;
    SharedPreferences pref;
    SharedPreferences check;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendscheckin);

        // Call this method because that method checks whether the boolean for parsing is true of false.
        parseforcheckbox();


        // Get the boolean of the check-in, so you know the checkbox must be checked or not.
        try {
            boolean checkin = pref.getBoolean("check", false);

            checkbox = (CheckBox) findViewById(R.id.checkBox);
            // If checkin = false, checkbox must not be checked.
            if (!checkin) {
                assert checkbox != null;
                checkbox.setChecked(false);
            }
            // If checkin = true, checkbox must be checked.
            if (checkin) {
                assert checkbox != null;
                checkbox.setChecked(true);
            }
        } catch (Exception e){
            // When nothing is found, there is no checkin so checkbox must not be checked.
            checkbox.setChecked(false);
        }

        // Set toolbar for navigationbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        eindbestemming = intent.getStringExtra(TAG_EINDBESTEMMING);
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            MessengerThreadParams mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

        }

        // Set onClickListener on the Messenger Button.
        View mMessengerButton = findViewById(R.id.messenger_send_button);
        assert mMessengerButton != null;
        mMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }
        });

        // Initialize App42 to parse data later in this Activity.
        App42API.initialize(getApplicationContext(),"ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9","b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");

        // Handles Navigation Bar.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // Get friends of Facebookuser in an ArrayList.
        final ArrayList<String> friends = new ArrayList<>();

        // Get friends out of SharedPreferences.
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            JSONArray friendslist = new JSONArray(prefs.getString("key", "[]"));
            for (int i = 0; i < friendslist.length(); i++) {
                friends.add(friendslist.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // simple textview for list item
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, friends);
        ListView listView = (ListView) findViewById(R.id.listView_friends);
        assert listView != null;
        listView.setAdapter(adapter);

        parsedata();
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
            Toast.makeText(this, R.string.zelfdepagina, Toast.LENGTH_SHORT).show();
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
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    * This method handles the onClick on the messenger button. It will send the image of people
    * sitting in the same train to Facebook Messenger. The user only has to select the right user
    * and can also add some personal text before sending it.
     */

    private void onMessengerButtonClicked() {

        // Get the right Uri + drawable to send to a Facebook friend.
        Uri uri =
                Uri.parse("android.resource://com.example.josien.programmeerproject2/" + R.drawable.zelfdetrein);

        // Create the parameters for what want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                        .setMetaData("{ \"image\" : \"zelfdetrein\" }")
                        .build();

        if (mPicking) {
            // If launched from Messenger, call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.
            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
        } else {
            // Otherwise, launch directly
            MessengerUtils.shareToMessenger(
                    this,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }

    }

    /*
    * When the user clicked on the 'Check for your Social Train Journey'-button, this method will
    * start. It get the Name of the Facebook user, create the SharedPref bool and initialize App42
    * Within this method, two other methods will be called: parsedata, whereby the real check of
    * facebookfriends in the same train will take place, and check, whereby it get the right boolean
    * and show the right message.
     */
    public void check_friends(View view){
        // Get username of the Facebookuser, so you know who is doing what.
        userName = Profile.getCurrentProfile().getName();

        // Get SharedPref bool.
        bool = getSharedPreferences("Boolean", 0);
        bool.edit().putBoolean("key",zelfdetrein).apply();

        // Initialize App42, so data can be stored and parsed from it.
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");

        // Call the other methods to do their work.
        parsedata();
        check();
    }

    /*
    *  This method will check whether there is data from the user in the App42 database, when there
    *  is data found, set checkbox Checked. Otherwise, the user is not checked in a train, so the
    *  checkbox will be Unchecked.
     */
    public void parseforcheckbox(){
        // Declare how the data is stored in App42 so they know what you are looking for.
        String dbName = "test";
        String collectionName = "ritnummer";
        String key = "facebookid";

        // Get SharedPrefs for pref.
        pref = getSharedPreferences("Boolean", 0);

        // Initialize App42 and check whether there is data of the user in the database
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        StorageService storageService = App42API.buildStorageService();
        storageService.findDocumentByKeyValue(dbName, collectionName, key, userName, new App42CallBack() {
            // onSuccess means there is data found.
            public void onSuccess(Object response)
            {
                Storage  storage  = (Storage )response;

                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                for(int i=0;i<jsonDocList.size();i++)
                {
                    // When there is data, the user is checked in so boolean is true.
                    pref = getSharedPreferences("Boolean", 0);
                    checkin = true;
                    pref.edit().putBoolean("check",checkin).apply();
                }

            }
            public void onException(Exception ex)
            {
                // In this case, there is no data found so the boolean is false.
                pref = getSharedPreferences("Boolean", 0);
                checkin = false;
                pref.edit().putBoolean("check",checkin).apply();
            }
        });
    }

    /*
    * This method parses the data from App42, whereby the combination of Facebookfriends and the
    * check-in of train can be made.
     */
    public void parsedata(){
        // Declare names of data in the online database so you can look for it.
        String dbName = "test";
        String collectionName = "ritnummer";
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        StorageService storageService = App42API.buildStorageService();

        storageService.findAllDocuments(dbName, collectionName, new App42CallBack() {
            // There is data found.
            public void onSuccess(Object response)
            {
                // Store it in an ArrayList.
                Storage  storage  = (Storage )response;
                ArrayList<String> checkInList = new ArrayList<>();
                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                // For as long as there is data.
                for(int i=0;i<jsonDocList.size();i++)
                {
                    // Check every item.
                    String jsondoc = jsonDocList.get(i).getJsonDoc();
                    checkInList.add(jsondoc);
                }

                // For as long as the list is.
                for(int j=0; j<checkInList.size();j++){
                    String checkIn = checkInList.get(j);
                    try {
                        // Get the Name of the Facebookuser who created the check-in.
                        JSONObject object = new JSONObject(checkIn);
                        String fbId = object.getString("facebookid");

                        // If the Facebookuser of the check-in is the same as the current user.
                        if (fbId.equalsIgnoreCase(userName)){
                            // Get 'ritnummer' out of the data.
                            ownRitnummer = object.getString("ritnummer");
                            JSONArray ownFriends = object.getJSONArray("fbfriends");

                            // As long as the list of your own Facebookfriends.
                            ArrayList<String> ownFriendsList = new ArrayList<>();
                            for (int k=0; k<ownFriends.length(); k++) {
                                //Add ownFriends to the list.
                                ownFriendsList.add(ownFriends.getString(k));
                            }

                            for(int l=0; l<checkInList.size();l++) {
                                // Get the other Facebooknames out of database.
                                try {
                                    JSONObject friendObject = new JSONObject(checkInList.get(l));
                                    friendId = friendObject.getString("facebookid");

                                    // If your friendlist contains the name of the other Facebookusers, it means you are FBfriends.
                                    if (ownFriendsList.contains(friendId)) {
                                        String friendRitnummer = friendObject.getString("ritnummer");

                                        // Check if the 'ritnummer' is the same.
                                        if (friendRitnummer.equalsIgnoreCase(ownRitnummer)) {
                                            // When it is the same, they are in the same train.
                                            bool = getSharedPreferences("Boolean", 0);
                                            // Set boolean true and save it in SharedPreferences.
                                            zelfdetrein = true;
                                            bool.edit().putBoolean("key",zelfdetrein).apply();
                                            // Get the name of the friend who is in the same train.
                                            friend = friend.concat(" ").concat(friendId);
                                        }
                                    }
                                    }
                                 catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });
    }

    /*
    * This method checks the boolean of people in the same train.
     */

    public void check() {
        // Get status of the boolean out of SharedPreferences.
        boolean zelfdetrein = bool.getBoolean("key", false);
        // If boolean is true, show the following AlertDialog.
        if (zelfdetrein) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.gezellig_reizen + friend + "!")
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            friend = "";
            AlertDialog alert = builder.create();
            alert.show();
        }
        // If boolean is false, show the following AlertDialog.
        if (!zelfdetrein){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.saai_reizen)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /*
    * This method handles the status of the Checkbox.
     */
    public void checkbox_checkin(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox:
                // When you want to check the Checkbox, user has to check-in a train
                if (checked){
                    Toast.makeText(FriendsActivity.this, R.string.eerst_inchecken, Toast.LENGTH_SHORT).show();
                    // Store check-in as false in SharedPref, because user is not checked in a train.
                    pref = getSharedPreferences("Boolean", 0);
                    checkin = false;
                    pref.edit().putBoolean("check",checkin).apply();
                    checkbox.setChecked(false);
                    // Help the user and go to MainActivity to let the user check-in a train.
                    Intent checkin = new Intent(this, MainActivity.class);
                    checkin.putExtra("Checkin", 500);
                    startActivity(checkin);
                }
                else{
                    // Checkbox is checked, so ask the user they want to check-out of the train.
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FriendsActivity.this);
                    builder
                            .setMessage(R.string.uitchecken)
                            .setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                                @Override
                                // When the user say yes, delete all the data the user has in the database.
                                public void onClick(DialogInterface dialog, int id) {
                                    String dbName = "test";
                                    String collectionName = "ritnummer";
                                    String key = "facebookid";
                                    String value = Profile.getCurrentProfile().getName();
                                    App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
                                    StorageService storageService = App42API.buildStorageService();
                                    storageService.deleteDocumentsByKeyValue(dbName, collectionName, key, value, new App42CallBack() {
                                        public void onSuccess(Object response)
                                        {
                                            // When deleting the data is succesfull, set checkin on false,
                                            // and checkout on true and store it in SharedPreferences.
                                            startActivity(getIntent());
                                            pref = getSharedPreferences("Boolean", 0);
                                            checkin = false;
                                            pref.edit().putBoolean("check",checkin).apply();
                                            checkbox.setChecked(false);

                                            check = getSharedPreferences("Boolz", 0);
                                            checkout = true;
                                            check.edit().putBoolean("checkout",checkout).apply();
                                        }
                                        public void onException(Exception ex)
                                        {
                                            System.out.println("Exception Message"+ex.getMessage());
                                        }
                                    });
                                }

                            })

                            // Nothing is done when "No" is pressed.
                            .setNegativeButton(R.string.nee, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    pref = getSharedPreferences("Boolean", 0);
                                    checkin = true;
                                    pref.edit().putBoolean("check",checkin).apply();
                                    checkbox.setChecked(true);
                                }
                            })
                            .show();
                }
                }
    }

    /*
    * This method handles the refresh of the Activity when clicking on the button. Sometimes it is
    * necessary to get the most up-to-date data.
     */
    public void refresh_activity(View view){
        finish();
        startActivity(getIntent());
    }
    }