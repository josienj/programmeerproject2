package com.example.josien.programmeerproject2;

/*
*  Josien Jansen
*  11162295
*  Programmeerproject
*  06-2016
*  Universiteit van Amsterdam
*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Objects;

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
    String dbName = "test";
    String collectionName = "ritnummer";
    String key;
    String value;
    private static final String TAG_RITNUMMER = "Ritnummer";
    ArrayList<String> checkInList;
    String userName;
    int listnumber;
    String ownRitnummer;
    private static FriendsActivity parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendscheckin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userName = Profile.getCurrentProfile().getId();

        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");

        View mMessengerButton = findViewById(R.id.messenger_send_button);

        Intent intent = getIntent();
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
        ArrayList<String> friends = new ArrayList<>();

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

    /*
    *  This method doesn't work correctly, but it is assumed that this method will parse the data
    *  from App42 correctly.
     */


    public void parsedata(){
        String dbName = "test";
        String collectionName = "ritnummer";
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        StorageService storageService = App42API.buildStorageService();
        storageService.findAllDocuments(dbName, collectionName, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Storage  storage  = (Storage )response;
                System.out.println("dbName is " + storage.getDbName());
                System.out.println("collection Name is " + storage.getCollectionName());
                ArrayList<String> checkInList = new ArrayList<>();
                ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                for(int i=0;i<jsonDocList.size();i++)
                {
                    System.out.println("objectId is " + jsonDocList.get(i).getDocId());
                    System.out.println("CreatedAt is " + jsonDocList.get(i).getCreatedAt());
                    System.out.println("UpdatedAtis " + jsonDocList.get(i).getUpdatedAt());
                    System.out.println("Jsondoc is " + jsonDocList.get(i).getJsonDoc());
                    String jsondoc = jsonDocList.get(i).getJsonDoc();
                    checkInList.add(jsondoc);

                }
                Log.d("checkInlist", "onSuccess() returned: " + checkInList.toString());

                for(int j=0; j<checkInList.size();j++){
                    String checkIn = checkInList.get(j);
                    Log.d("checkin", "onSuccess() returned: " + checkIn);
                    try {
                        JSONObject object = new JSONObject(checkIn);
                        String fbId = object.getString("facebookid");
                        Log.d("fbid", "onSuccess() returned: " + fbId);

                        if (fbId.equalsIgnoreCase(userName)){
                            ownRitnummer = object.getString("ritnummer");
                            int listnumber = j;
                            Log.d("eigenritnummer", "onSuccess() returned: " + ownRitnummer);
                            JSONArray ownFriends = object.getJSONArray("fbfriends");
                            Log.d("jsonarray own friends", "onSuccess() returned: " + ownFriends);

                            ArrayList<String> ownFriendsList = new ArrayList<>();
                            for (int k=0; k<ownFriends.length(); k++) {
                                if (k == listnumber) {
                                    Log.d("listnumber", "onSuccess() returned: " + listnumber);
                                    continue;
                                }
                                ownFriendsList.add(ownFriends.getString(k));
                            }
                            Log.d("eigenfriendslijst", "onSuccess() returned: " + ownFriendsList);

                            for (int k=0; k<ownFriends.length(); k++) {
                                String friend = ownFriends.getString(k);
                                Log.d("friend", "onSuccess() returned: " + friend);

                                for(int l=0; l<checkInList.size();l++) {

                                    String friendCheckIn = checkInList.get(l);

                                    try {
                                        JSONObject friendObject = new JSONObject(friendCheckIn);
                                        String friendId = friendObject.getString("facebookid");
                                        Log.d("FriendID", "onSuccess() returned: " + friendId);

                                        if (ownFriendsList != null) {
                                            if (ownFriendsList.contains(friendId)) {
                                                String friendRitnummer =friendObject.getString("ritnummer");
                                                Log.d("friendritnummer", "onSuccess() returned: " + friendRitnummer);

                                                if (friendRitnummer.equalsIgnoreCase(ownRitnummer)){
                                                    Toast.makeText(FriendsActivity.this, "Awesome", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
    public void toast(){
        Toast.makeText(FriendsActivity.this, "This is amazing", Toast.LENGTH_SHORT).show();
    }


}
