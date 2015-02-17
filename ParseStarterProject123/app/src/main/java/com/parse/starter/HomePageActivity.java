package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class HomePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent myIntent = getIntent(); // gets the previously created intent
        String username = myIntent.getStringExtra("username");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subjects");
        query.whereEqualTo("username", username);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    Log.d("score", "Retrieved the object.");

                    //getting first name
                    String name = object.getString("firstName");

                    //getting age
                    Date birth = object.getDate("DOB");
                    int age = getAge(birth);

                    //getting gender
                    String gender = object.getString("gender");

                    //adding in personal info to page
                    TextView name_TextView = (TextView) findViewById(R.id.hello_textView);
                    TextView age_TextView = (TextView) findViewById(R.id.age_textView);
                    ImageView gender_image = (ImageView) findViewById(R.id.picture_imageView);

                    name_TextView.setText("Hello, " + name);
                    age_TextView.setText("" + age);
                    if (gender.equals("male"))
                        gender_image.setBackgroundResource(R.drawable.male_icon);
                    else
                        gender_image.setBackgroundResource(R.drawable.female_icon);
                }
            }
        });
    }

    int getAge(Date born) {
        int year = born.getYear() + 1900;
        int month = born.getMonth() + 1;
        int day = born.getDay();

        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int current_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int current_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Toast.makeText(getApplicationContext(), year + " " + month + " " + day, Toast.LENGTH_SHORT).show();

        if (current_month < month)
            return current_year - year - 1;
        else if (current_month == month) {
            if (current_day < day)
                return current_year - year - 1;
            else
                return current_year - year;
        } else
            return current_year - year;
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
}
