package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class HomePageActivity extends Activity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getActionBar().setDisplayHomeAsUpEnabled(false);

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
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {//logout here
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void openMessages(View view) {

        Intent myIntent = new Intent(this, MessageActivity.class);
        myIntent.putExtra("patient", patient.toString());

        finish();
        startActivity(myIntent);
    }

    public void openPrescriptions(View view){

        Intent myIntent = new Intent(this, PrescriptionsActivity.class);
        myIntent.putExtra("patient", patient.toString());

        finish();
        startActivity(myIntent);

    }
}
