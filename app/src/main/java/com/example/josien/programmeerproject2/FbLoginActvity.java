package com.example.josien.programmeerproject2;

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


public class FbLoginActvity extends AppCompatActivity {

    CallbackManager callbackManager;
    //String accessToken = "1040163589387502|iFO4sc4Fv2FChu5xrWL_RBbXjoU";

   // AccessToken accessToken;
    SocialService socialService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_login);
        App42API.initialize(getApplicationContext(),"ad9a5dcb7cd3013f200ba0f4b38528f6dd14401bb2afe526d11ff947c154d7a9","b92836c9f828c8e7cbf153b4510ecf8fc3ac49be1c696f1bc057cc3bb3663591");
        socialService = App42API.buildSocialService();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        getLoginDetails(loginButton);
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
                                Intent intent = new Intent(FbLoginActvity.this,MainActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

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