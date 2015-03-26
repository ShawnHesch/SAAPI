package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.EncryptedPrivateKeyInfo;


public class HomePageActivity extends Activity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.patient = new Patient(myIntent.getStringExtra("patient"));

        //adding in personal info to page
        TextView name_TextView = (TextView) findViewById(R.id.hello_textView);
        TextView age_TextView = (TextView) findViewById(R.id.age_textView);
        ImageView gender_image = (ImageView) findViewById(R.id.picture_imageView);

        name_TextView.setText("Hello, " + patient.getName());
        age_TextView.setText("" + patient.getAge());
        if (patient.getGender().equals("male"))
            gender_image.setBackgroundResource(R.drawable.male_icon);
        else
            gender_image.setBackgroundResource(R.drawable.female_icon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void openMessages(View view) {

        Intent myIntent = new Intent(this, MessageActivity.class);
        myIntent.putExtra("patient", patient.toString());
        startActivity(myIntent);
    }

    public void openPrescriptions(View view){

        Intent myIntent = new Intent(this, PrescriptionsActivity.class);
        myIntent.putExtra("patient", patient.toString());
        startActivity(myIntent);

    }
}
