package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class PrescriptionsActivity extends Activity {

    Patient pat;
    String expMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.pat = new Patient(myIntent.getStringExtra("patient"));

        showPrescriptions();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prescriptions, menu);
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

    public void showPrescriptions(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Medication");
        query.whereEqualTo("subjectID", pat.getID());

        try {
            List<ParseObject> medications = query.find();
            TableLayout table = (TableLayout)findViewById(R.id.table);
            LayoutInflater inflater = getLayoutInflater();

            String name, dosage, frequency, method;
            //loop through list
            for(int i=0; i < medications.size(); i++){
                View row = inflater.inflate(R.layout.row, table, false);
                TableRow tRow = (TableRow)row.findViewById(R.id.row);
                TextView nameText = (TextView)tRow.findViewById(R.id.name);
                TextView dosageText = (TextView)tRow.findViewById(R.id.dosage);
                TextView frequencyText = (TextView)tRow.findViewById(R.id.frequency);
                TextView methodText = (TextView)tRow.findViewById(R.id.method);

                name = medications.get(i).getString("drug");
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                dosage = medications.get(i).getInt("dosage") + " " + medications.get(i).getString("dosageUnit");
                if(medications.get(i).getString("frequency") == "other"){
                    frequency = medications.get(i).getString("prn");
                }else{
                    frequency = medications.get(i).getString("frequency");
                }
                method = medications.get(i).getString("route");

                nameText.setText(name);
                dosageText.setText(dosage);
                frequencyText.setText(frequency);
                methodText.setText(method);

                nameText.setBackgroundResource(R.drawable.tablecellbacking);
                dosageText.setBackgroundResource(R.drawable.tablecellbacking);
                frequencyText.setBackgroundResource(R.drawable.tablecellbacking);
                methodText.setBackgroundResource(R.drawable.tablecellbacking);

                table.addView(tRow);

                if(name.contains("atorvastatin")){
                    expMed = "atorvastatin";
                }else if(name.contains("rosuvastatin")){
                    expMed = "rosuvastatin";
                }
            }

        }catch(Exception e){
            Log.d("PrescriptionsActivity", "Failed to get ParseObject from database");
        }

    }

    public void openCalcDosage(){

        Intent myIntent = new Intent(this, PrescriptionsActivity.class);
        myIntent.putExtra("patient", pat.toString());
        myIntent.putExtra("medName", expMed);
        startActivity(myIntent);

    }
}
