package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.FileInputStream;
import java.util.Date;

import static com.parse.starter.R.id;

public class ParseStarterProjectActivity extends Activity {

    EncryptedFileIO encryptor;
    Patient pat;
    boolean doneUpdate;
    private String pass;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        doneUpdate = false;

        //Determine if local file exits and if it has info
        encryptor = new EncryptedFileIO(this.getApplicationContext());
        if(encryptor.doesFileExists()){//local file exits, auto login
            quickLogin();
        }
    }

    public void attempt_login(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        try {
            ParseUser.logIn(username, password);
            //login succeeded
            successful_login(password);
        }catch(Exception e){
            //login failed
            Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void successful_login(String password) {

        String username = ((EditText) findViewById(R.id.username)).getText().toString();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subjects");
        query.whereEqualTo("username", username);

        try {
            ParseObject patty = query.getFirst();
            pat = new Patient(patty.getString("firstName") + " " + patty.getString("lastName"), patty.getString("gender"), patty.getDate("DOB"), patty.getString("username"), patty.getInt("ID"));
        }catch(Exception e){
            Log.d("StarterActivity", "Failed to get ParseObject from database");
        }
        //TODO: Get patient info here and send the object to the next intent, reduce lag in getting info
        boolean blah = encryptor.updateFile(this.getApplicationContext(), pat, password);

        Intent myIntent = new Intent(this, HomePageActivity.class);
        myIntent.putExtra("patient", pat.toString());

        startActivity(myIntent);
    }

    public void quickLogin(){
        FileInputStream filein;
        String name = "";
        try{
            filein = getApplicationContext().openFileInput("userData");
            byte[] nameBytes = new byte[filein.available()];
            filein.read(nameBytes);
            name = new String(nameBytes);
        }catch(Exception e){

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome back " + name + "!");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setInverseBackgroundForced(true);

        // Set up the buttons
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //entered password, decrypt local file and check password
                pass = input.getText().toString();
                //if loginsuccessful
                if(encryptor.validatePass(pass)){
                    Intent myIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                    myIntent.putExtra("patient", encryptor.getPatient().toString());

                    startActivity(myIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Not Me!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //not this user, begin as normal
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
