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
        getActionBar().setDisplayHomeAsUpEnabled(false);

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
    public void onBackPressed() {
        Intent myIntent = new Intent(this, PrescriptionsActivity.class);
        myIntent.putExtra("patient", pat.toString());

        finish();
        startActivity(myIntent);
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
            String x9a34 = "", x9b421 = "", x8a388 = "", x8b521 = "";
            for(int i=0; i<rsNumbers.size(); i++) {
                for (int j = 0; j < research.size(); j++) {
                    if (rsNumbers.get(i).getString("gene").equals("ABCG2")) {
                        if (rsNumbers.get(i).getString("rsNumber").equals(research.get(j).getString("rsNumber"))
                                && research.get(j).getString("rsNumber").equals("rs2231137")) {
                            x9a34 = research.get(j).getString("genotype");
                        } else if (rsNumbers.get(i).getString("rsNumber").equals(research.get(j).getString("rsNumber"))) {
                            x9b421 = research.get(j).getString("genotype");
                        }
                    } else if (rsNumbers.get(i).getString("gene").equals("SLCO1B1")) {
                        if (rsNumbers.get(i).getString("rsNumber").equals(research.get(j).getString("rsNumber"))
                                && research.get(j).getString("rsNumber").equals("rs2306283")) {
                            x8a388 = research.get(j).getString("genotype");
                        } else if (rsNumbers.get(i).getString("rsNumber").equals(research.get(j).getString("rsNumber"))) {
                            x8b521 = research.get(j).getString("genotype");
                        }
                    }
                }
            }
            int dose = 0;
            //now for long set of ifs
            if(x8b521.equals("T/T")){
                if(!med) {
                    if (x8a388.equals("A/A")) {
                        if (pat.getAge() <= 45) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    } else if (x8a388.equals("A/G")) {
                        if (pat.getAge() <= 60) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    } else if (x8a388.equals("G/G")) {
                        dose = 80;
                    }
                }else {
                    if (x9b421.equals("C/C")) {
                        if (pat.getAge() <= 70) {
                            dose = 40;
                        } else {
                            dose = 20;
                        }
                    } else if (x9b421.equals("C/A")) {
                        if (pat.getAge() <= 35) {
                            dose = 40;
                        } else {
                            dose = 20;
                        }
                    } else if (x9b421.equals("A/A")) {
                        dose = 20;
                    }
                }
            }else if(x8b521.equals("T/C")){
                if(!med) {
                    if (x8a388.equals("A/A")) {
                        if (pat.getAge() <= 70) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    } else if (x8a388.equals("A/G")) {
                        if (pat.getAge() <= 40) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    } else if (x8a388.equals("G/G")) {
                        if (pat.getAge() <= 55) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    }
                }else {
                    if (x9b421.equals("C/C")) {
                        if (pat.getAge() <= 45) {
                            dose = 40;
                        } else {
                            dose = 20;
                        }
                    } else if (x9b421.equals("C/A")) {
                        dose = 20;
                    } else if (x9b421.equals("A/A")) {
                        if (pat.getAge() <= 55) {
                            dose = 20;
                        } else {
                            dose = 10;
                        }
                    }
                }
            }else if(x8b521.equals("C/C")){
                if(!med) {
                    if (x8a388.equals("A/A")) {
                        if (pat.getAge() <= 55) {
                            dose = 40;
                        } else {
                            dose = 20;
                        }
                    } else if (x8a388.equals("A/G")) {
                        if (pat.getAge() <= 70) {
                            dose = 40;
                        } else {
                            dose = 20;
                        }
                    } else if (x8a388.equals("G/G")) {
                        if (pat.getAge() <= 40) {
                            dose = 80;
                        } else {
                            dose = 40;
                        }
                    }
                }else {
                    if (x9b421.equals("C/C")) {
                        dose = 20;
                    } else if (x9b421.equals("C/A")) {
                        if (pat.getAge() <= 60) {
                            dose = 20;
                        } else {
                            dose = 10;
                        }
                    } else if (x9b421.equals("A/A")) {
                        if (pat.getAge() <= 65) {
                            dose = 10;
                        } else {
                            dose = 5;
                        }
                    }
                }
            }


            TextView tv = (TextView)findViewById(R.id.dosagevalue);
            String text = "";
            if(med){
                text = "5 - " + dose + " mg";
            }else {
                text = "10 - " + dose + " mg";
            }
            tv.setText(text);

        }catch(Exception e){
            Log.d("PrescriptionsActivity", "Failed to get ParseObject from database");
        }

    }
}
