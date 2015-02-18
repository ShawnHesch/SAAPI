package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

import static com.parse.starter.R.id;

public class ParseStarterProjectActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void attempt_login(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    successful_login();

                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void successful_login() {
        //TODO: Get patient info here and send the object to the next intent, reduce lag in getting info

        String username = ((EditText) findViewById(R.id.username)).getText().toString();

        Intent myIntent = new Intent(this, HomePageActivity.class);
        myIntent.putExtra("username", username);

        startActivity(myIntent);
    }
}
