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
import android.util.Log;
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
    private boolean mPicking;
    String userName;
    String ownRitnummer;
    String friendId;
    Boolean zelfdetrein = false;
    SharedPreferences bool;
    String friend="";
    private static final String TAG_EINDBESTEMMING = "Eindbestemming";
    String eindbestemming;
    Boolean checkin = false;
    CheckBox checkbox;
    SharedPreferences pref;
    SharedPreferences check;
    Boolean checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendscheckin);
        parseforcheckbox();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            boolean checkin = pref.getBoolean("check", false);

            checkbox = (CheckBox) findViewById(R.id.checkBox);
            if (!checkin) {
                assert checkbox != null;
                checkbox.setChecked(false);
            }
            if (checkin) {
                assert checkbox != null;
                checkbox.setChecked(true);
            }
        } catch (Exception e){
            checkbox.setChecked(false);
        }
        setSupportActionBar(toolbar);
        userName = Profile.getCurrentProfile().getName();

        View mMessengerButton = findViewById(R.id.messenger_send_button);

        Intent intent = getIntent();
        eindbestemming = intent.getStringExtra(TAG_EINDBESTEMMING);
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            MessengerThreadParams mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

        }

        // Set onClickListener on the Messenger Button.
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
            Log.d("shared prefs", "onCreate() returned: " + friendslist);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // simple textview for list item
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, friends);
        ListView listView = (ListView) findViewById(R.id.listView);
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

    public void check_friends(View view){
        userName = Profile.getCurrentProfile().getName();

        bool = getSharedPreferences("Boolean", 0);
        bool.edit().putBoolean("key",zelfdetrein).apply();
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");

        parsedata();
        check();
    }

    /*
    *  This method doesn't work correctly, but it is assumed that this method will parse the data
    *  from App42 correctly.
     */
    public void parseforcheckbox(){
        String dbName = "test";
        String collectionName = "ritnummer";
        String key = "facebookid";
        String value = Profile.getCurrentProfile().getName();

        pref = getSharedPreferences("Boolean", 0);
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        StorageService storageService = App42API.buildStorageService();
        storageService.findDocumentByKeyValue(dbName, collectionName, key, value, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Storage  storage  = (Storage )response;

                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                for(int i=0;i<jsonDocList.size();i++)
                {
                    pref = getSharedPreferences("Boolean", 0);
                    checkin = true;
                    pref.edit().putBoolean("check",checkin).apply();
                    Log.d("NIET EMPTY", "onSuccess: ik ben hier blijkaar");
                }

            }
            public void onException(Exception ex)
            {
                pref = getSharedPreferences("Boolean", 0);
                checkin = false;
                pref.edit().putBoolean("check",checkin).apply();
                Log.d("EMPTY", "onSuccess: ik ben hier");
            }
        });
    }


    public void parsedata(){

        Log.d("start of parse", "start of parse");
        String dbName = "test";
        String collectionName = "ritnummer";
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        StorageService storageService = App42API.buildStorageService();

        storageService.findAllDocuments(dbName, collectionName, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Storage  storage  = (Storage )response;
                ArrayList<String> checkInList = new ArrayList<>();
                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                for(int i=0;i<jsonDocList.size();i++)
                {
                    String jsondoc = jsonDocList.get(i).getJsonDoc();
                    checkInList.add(jsondoc);
                    Log.d("jsondoclist", "onSuccess() returned: " + jsonDocList);

                }

                for(int j=0; j<checkInList.size();j++){
                    String checkIn = checkInList.get(j);
                    try {
                        JSONObject object = new JSONObject(checkIn);
                        String fbId = object.getString("facebookid");
                        Log.d("fbid", "onSuccess() returned: " + fbId);

                        if (fbId.equalsIgnoreCase(userName)){
                            ownRitnummer = object.getString("ritnummer");
                            JSONArray ownFriends = object.getJSONArray("fbfriends");
                            Log.d("ownfriends", "onSuccess() returned: " + ownFriends);

                            ArrayList<String> ownFriendsList = new ArrayList<>();
                            for (int k=0; k<ownFriends.length(); k++) {
                                ownFriendsList.add(ownFriends.getString(k));
                            }


                            for(int l=0; l<checkInList.size();l++) {

                                try {
                                    JSONObject friendObject = new JSONObject(checkInList.get(l));
                                    friendId = friendObject.getString("facebookid");
                                    Log.d("friendid", "onSuccess() returned: " + friendId);
                                    Log.d("ownfriendlist", "onSuccess() returned: " + ownFriendsList);


                                    if (ownFriendsList.contains(friendId)) {
                                        String friendRitnummer = friendObject.getString("ritnummer");
                                        Log.d("friendritnummer", "onSuccess() returned: " + friendRitnummer);

                                        if (friendRitnummer.equalsIgnoreCase(ownRitnummer)) {
                                            Log.d("fbfriend", "onSuccess() returned: " + friend);
                                            Log.d("ikbenhier", "onSuccess() returned: " + zelfdetrein);
                                            bool = getSharedPreferences("Boolean", 0);
                                            zelfdetrein = true;
                                            bool.edit().putBoolean("key",zelfdetrein).apply();
                                            Log.d("bool", "onSuccess() returned: " + bool);
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

    public void check() {
        boolean zelfdetrein = bool.getBoolean("key", false);
        Log.d("Zelfdetrein", "check() returned: " + zelfdetrein);
        if (zelfdetrein) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Gezellig, je kunt samen reizen met " + friend + "!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            friend = "";
            AlertDialog alert = builder.create();
            alert.show();
        }
        if (!zelfdetrein){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Helaas, geen van je vrienden zit in dezelfde trein. Succes met een saaie treinreis!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        }

    public void checkbox_checkin(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked){
                    Toast.makeText(FriendsActivity.this, "Je moet eerst inchecken in een trein", Toast.LENGTH_SHORT).show();
                    pref = getSharedPreferences("Boolean", 0);
                    checkin = false;
                    pref.edit().putBoolean("check",checkin).apply();
                    checkbox.setChecked(false);
                    Intent checkin = new Intent(this, MainActivity.class);
                    checkin.putExtra("Checkin", 500);
                    startActivity(checkin);
                }
                else{
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FriendsActivity.this);
                    builder
                            .setMessage("Ben je uit de trein gestapt? Als je op 'Ja' klikt word je definitief uitgecheckt")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @Override
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
                                            startActivity(getIntent());
                                            pref = getSharedPreferences("Boolean", 0);
                                            checkin = false;
                                            pref.edit().putBoolean("check",checkin).apply();
                                            checkbox.setChecked(false);

                                            check = getSharedPreferences("Boolz", 0);
                                            checkout = true;
                                            check.edit().putBoolean("checkout",checkout).apply();
                                            Log.d("Boolcheckout", "onSuccess() returned: " + checkout);

                                        }
                                        public void onException(Exception ex)
                                        {
                                            System.out.println("Exception Message"+ex.getMessage());
                                        }
                                    });
                                }

                            })

                            // Nothing is done when "No" is pressed.
                            .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
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

    public void refresh_activity(View view){
        finish();
        startActivity(getIntent());
    }
    }