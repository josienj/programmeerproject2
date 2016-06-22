package com.example.josien.programmeerproject2;

/*
*  Josien Jansen
*  11162295
*  Programmeerproject
*  06-2016
*  Universiteit van Amsterdam
*/

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;

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
* This Activity is the very first screen of the app: every user has to log in with their Facebook-
* account. By logging in, the Facebookfriends of the user who are also using this app will be
* stored and showed in the FriendsActivity. Once the user has logged-in, this screen will be skipped.
 */


public class FbLoginActvity extends AppCompatActivity {

    CallbackManager callbackManager;
    SocialService socialService;
    String id;
    String friendsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_login);

        // Initialize App42 for storage of every Facebooklogin
        App42API.initialize(getApplicationContext(), "ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9", "b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        socialService = App42API.buildSocialService();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        // Check whether the user has already logged-in, then move to next Activity.
        try {
            String accessToken = AccessToken.getCurrentAccessToken().getToken();
            if (accessToken != null) {
                Intent checkin = new Intent(this, MainActivity.class);
                checkin.putExtra("Checkin", 500);
                gotodatabase();
                assert loginButton != null;
                loginButton.setReadPermissions("user_friends");
                getLoginDetails(loginButton);
                // User may not be able to go back to this screen without first logging out.
                finish();
                startActivity(checkin);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        assert loginButton != null;
        loginButton.setReadPermissions("user_friends");
        getLoginDetails(loginButton);
    }

    /*
    * This method stores the Facebookusers into the online Database from App42. It is stored by
    * Facebook_id, profile picture and other public information.
     */
    public void gotodatabase(){
        String userName = Profile.getCurrentProfile().getId();
        String accessToken = AccessToken.getCurrentAccessToken().getToken();

        socialService.linkUserFacebookAccount(userName, accessToken, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Social social  = (Social)response;
                String jsonResponse = social.toString();
                getfriends();
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });
    }

    /*
    * This method get the Friends of the Facebookusers who are also using this app.
     */
    public void getfriends(){
        String accessToken = AccessToken.getCurrentAccessToken().getToken();
        socialService.getFacebookFriendsFromAccessToken(accessToken, new App42CallBack() {
                    public void onSuccess(Object response) {
                        Social social = (Social) response;

                        System.out.println("accessToken is " + social.getFacebookAccessToken());
                        JSONArray jArray = new JSONArray();
                        for (int i = 0; i < social.getFriendList().size(); i++) {
                            System.out.println("Installed is : " + social.getFriendList().get(i).getInstalled());
                            System.out.println("Id is : " + social.getFriendList().get(i).getId());
                            System.out.println("Picture is :" + social.getFriendList().get(i).getPicture());
                            System.out.println("Name is : " + social.getFriendList().get(i).getName());

                            id = social.getFriendList().get(i).getName();

                            SharedPreferences settings = getSharedPreferences("SETTINGS KEY", 0);
                            SharedPreferences.Editor editor = settings.edit();

                            jArray.put(id);

                            editor.putString("jArray", jArray.toString());
                            editor.apply();
                        }

                        /*
                        for (int j = 0; j <social.getFriendList().size(); j++) {
                            fbid[j] = social.getFriendList().get(j).getId();

                            Log.d("Fbid", "onSuccess() returned: " + fbid[j]);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putInt("array_size", fbid.length);
                            for(int i=0;i<fbid.length; i++)
                                editor.putString("array_" + i, fbid[i]);
                            editor.apply();

                        }
                        */

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

            // Callback registration.
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {

                // Get result of Facebooklogin and get the friends of the Facebookuser.
                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        login_result.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Intent intent = new Intent(FbLoginActvity.this,MainActivity.class);
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    // Save the friends also using the app in SharedPreferences, so
                                    // you can access it in every Activity.
                                    SharedPreferences prefs = PreferenceManager
                                            .getDefaultSharedPreferences(getApplicationContext());
                                    rawName.put(1);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("key", rawName.toString());
                                    System.out.println(rawName.toString());
                                    editor.apply();

                                    gotodatabase();
                                    finish();
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                ).executeAsync();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*
    * Initialize the FacebookSDK so data can be accessed.
     */

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
