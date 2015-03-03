package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.HashMap;


public class MessageActivity extends Activity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.username = myIntent.getStringExtra("username");

        showMessages();

    }

    public void showMessages() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereEqualTo("userID", this.username);
        //Toast.makeText(getApplicationContext(), "Hello " + username, Toast.LENGTH_SHORT).show();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    //Toast.makeText(getApplicationContext(), "Hello " + username, Toast.LENGTH_SHORT).show();
                    Log.d("message", "Retrieved " + messageList.size() + " scores");

                    ExpandableListView expListView = (ExpandableListView) findViewById(R.id.messageListView);
                    List<String> listDataHeader = new ArrayList<String>();
                    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

                    /*ListView messageView = (ListView) findViewById(R.id.messageListView);
                    ArrayList<String> arrayList = new ArrayList<String>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
                    messageView.setAdapter(adapter);*/

                    for (int i = 0; i < messageList.size(); i++) {
                        ParseObject messageObj = messageList.get(i);
                        String subject = messageObj.getString("subject");
                        String message = messageObj.getString("message");

                        listDataHeader.add(subject);
                        List<String> messageChild = new ArrayList<String>();
                        messageChild.add(message);
                        listDataChild.put(listDataHeader.get(0), messageChild);

                        //arrayList.add(message);
                        //adapter.notifyDataSetChanged();
                    }

                    ExpandableListAdapter listAdapter = new ExpandableListAdapter(MessageActivity.this, listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);


                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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
