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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;

import org.json.JSONArray;
import org.json.JSONException;


/*
This Activity handles a part of the Login of Facebook.
 */


public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    //String accessToken = "1040163589387502|iFO4sc4Fv2FChu5xrWL_RBbXjoU";

    // AccessToken accessToken;
    SocialService socialService;


    // Declare variables.
    public static final int INDEX_SIMPLE_LOGIN = 0;

    private static final String STATE_SELECTED_FRAGMENT_INDEX = "selected_fragment_index";
    public static final String FRAGMENT_TAG = "fragment_tag";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_login);
        App42API.initialize(getApplicationContext(),"ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9","b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        socialService = App42API.buildSocialService();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        assert loginButton != null;
        loginButton.setReadPermissions("user_friends");
        getLoginDetails(loginButton);
        facebookPost();
    }

    public void gotodatabase(){
        String userName = Profile.getCurrentProfile().getId();
        String accessToken = AccessToken.getCurrentAccessToken().getToken();
        Log.d("accestoken", "gotodatabase() returned: " + accessToken);
        Log.d("username", "gotodatabase() returned: " + userName);

        socialService.linkUserFacebookAccount(userName, accessToken, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Social social  = (Social)response;
                System.out.println("userName is " + social.getUserName());
                System.out.println("fb Access Token is " + social.getFacebookAccessToken());
                String jsonResponse = social.toString();
                Log.d("jsonResponse", "onSuccess() returned: " + jsonResponse);
                getfriends();
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });
    }

    public void getfriends(){
        String accessToken = AccessToken.getCurrentAccessToken().getToken();
        socialService.getFacebookFriendsFromAccessToken(accessToken, new App42CallBack() {
            public void onSuccess(Object response) {
                Social social = (Social) response;
                System.out.println("accessToken is " + social.getFacebookAccessToken());
                for (int i = 0; i < social.getFriendList().size(); i++) {
                    System.out.println("Installed is : " + social.getFriendList().get(i).getInstalled());
                    System.out.println("Id is : " + social.getFriendList().get(i).getId());
                    System.out.println("Picture is :" + social.getFriendList().get(i).getPicture());
                    System.out.println("Name is : " + social.getFriendList().get(i).getName());
                }
            }

            public void onException(Exception ex) {
                System.out.println("Exception Message" + ex.getMessage());
            }
        });
    }



    /*
     * Register a callback function with LoginButton to respond to the login result.
     */
    protected void getLoginDetails(LoginButton login_button){

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {

                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        login_result.getAccessToken(),

                        //AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    SharedPreferences prefs = PreferenceManager
                                            .getDefaultSharedPreferences(getApplicationContext());
                                    rawName.put(1);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("key", rawName.toString());
                                    System.out.println(rawName.toString());
                                    editor.apply();
                                    gotodatabase();
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                ).executeAsync();
                Log.d("Graphrequest", "onSuccess() returned: " + graphRequestAsyncTask);

                Log.d("ONsucces", "onSuccess() returned: " + login_result);
            }
            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_simple_login) {
            toggleFragment(INDEX_SIMPLE_LOGIN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFragment(int index) {
        Fragment fragment = mFragmentManager.findFragmentByTag(FRAGMENT_TAG);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (index){
            case INDEX_SIMPLE_LOGIN:
                transaction.replace(android.R.id.content, new LoginButtonFacebook(),FRAGMENT_TAG);
                break;
        }
        transaction.commit();
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    // not used yet
    private void facebookPost() {
        //check login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Log.d("Niet ingelogd", ">>>" + "Signed Out");
        } else {
            Log.d("Ingelogd", ">>>" + "Signed In");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Intent checkin = new Intent(this, MainActivity.class);
            checkin.putExtra("Checkin", 500);
            startActivity(checkin);
        }
    }

    // after succesfully logged in: go to MainActivity.
    public void next_screen2(View view){
        Intent next_screen = new Intent(this, MainActivity.class);
        next_screen.putExtra("next_screen", 500);
        startActivity(next_screen);
    }

}
