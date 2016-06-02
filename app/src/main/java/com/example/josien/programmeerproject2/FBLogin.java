package com.example.josien.programmeerproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.FacebookSdk;

/**
 * Created by Josien on 2-6-2016.
 */
public class FBLogin extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void go_to_app(View view){
        Intent next_screen = new Intent(this, MainActivity.class);
        next_screen.putExtra("go_to_app", 500);
        startActivity(next_screen);
    }

}
