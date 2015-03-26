package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;


public class CalcDosageActivity extends Activity {
    Patient pat;
    String medName;
    boolean med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_dosage);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.pat = new Patient(myIntent.getStringExtra("patient"));
        this.medName = myIntent.getStringExtra("medName");

        TextView tv = (TextView)findViewById(R.id.drugname);
        if(medName == "rosuvastatin"){
            med = true;
            tv.setText("of Rosuvastatin");
        }else{
            med = false;
            tv.setText("of Atorvastatin");
        }

        calcDosage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc_dosage, menu);
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
    public void calcDosage(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GenotypeResearch");
        query.whereEqualTo("subjectID", pat.getID());

        try {
            List<ParseObject> research = query.find();
            List<ParseObject> rsNumbers = new LinkedList<ParseObject>();
            ParseObject rsNum;
            for(int i=0; i< research.size(); i++){
                query = ParseQuery.getQuery("GenotypeRSNumber");
                query.whereEqualTo("rsNumber", research.get(i).getString("rsNumber"));
                rsNum = query.getFirst();
                rsNumbers.add(rsNum);
            }
            //got all the research for specified patient and subsenquent rsnumbers

            for(int i=0; i<rsNumbers.size(); i++){
                if(rsNumbers.get(i).getString("gene") == "ABCG2"){

                }
            }


        }catch(Exception e){
            Log.d("PrescriptionsActivity", "Failed to get ParseObject from database");
        }

    }
}
