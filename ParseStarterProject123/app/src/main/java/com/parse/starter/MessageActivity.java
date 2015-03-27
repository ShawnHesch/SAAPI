package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import java.util.HashMap;


public class MessageActivity extends Activity {

    private Patient pat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.pat = new Patient(myIntent.getStringExtra("patient"));

        showMessages();

    }

    public void showMessages() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereEqualTo("userID", pat.getUserName());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    ArrayList<Boolean> saved = new ArrayList<Boolean>();

                    ExpandableListView expListView = (ExpandableListView) findViewById(R.id.messageListView);

                    List<String> listDataHeader = new ArrayList<String>();
                    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

                    //sort by date
                    Collections.sort(messageList, new Comparator<ParseObject>() {
                        public int compare(ParseObject o1, ParseObject o2) {
                            return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                        }
                    });

                    //put into screen
                    for (int i = 0; i < messageList.size(); i++) {
                        ParseObject messageObj = messageList.get(i);
                        String subject = messageObj.getString("subject");
                        String message = messageObj.getString("message");
                        saved.add(i, messageObj.getBoolean("read"));

                        listDataHeader.add(subject);
                        List<String> messageChild = new ArrayList<String>();
                        messageChild.add(message);
                        listDataChild.put(listDataHeader.get(i), messageChild);
                    }

                    ExpandableListAdapter listAdapter = new ExpandableListAdapter(MessageActivity.this, listDataHeader, listDataChild);
                    listAdapter.saved = saved;
                    expListView.setAdapter(listAdapter);

                    giveChildOnClickListener(expListView, listAdapter);
                    giveGroupOnClickListener(expListView, listAdapter);

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

    //used to delete messages
    private void createAndShowAlertDialog(final String subject, final String message, final int g, final int c, final ExpandableListAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Delete This Message?" +  "\n\n" + subject)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MessageActivity.this, subject + " Deleted.",
                                Toast.LENGTH_SHORT).show();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                        query.whereEqualTo("userID", pat.getUserName());
                        query.whereEqualTo("subject", subject);
                        query.whereEqualTo("message", message);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> messageList, ParseException e) {
                                if (e == null) {
                                    messageList.get(0).deleteInBackground();
                                    showMessages();
                                    adapter.notifyDataSetChanged();

                                } else {
                                    Log.d("message", "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)						//Do nothing on no
                .show();
    }

    private void giveChildOnClickListener(ExpandableListView list, final ExpandableListAdapter adapter) {

        list.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Object group =  adapter.getGroup(groupPosition);
                final String text = adapter.getChild(groupPosition, childPosition).toString();
                createAndShowAlertDialog(adapter.getGroup(groupPosition).toString(), text, groupPosition, childPosition, adapter);
                return false;
            }
        });
    }

    private void giveGroupOnClickListener(ExpandableListView list, final ExpandableListAdapter adapter) {

        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                parent.smoothScrollToPosition(groupPosition);

                if (adapter.saved.get(groupPosition) == Boolean.FALSE) {

                    adapter.saved.set(groupPosition, Boolean.TRUE);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                    query.whereEqualTo("userID", pat.getUserName().toString());
                    query.whereEqualTo("subject", adapter.getGroup(groupPosition).toString());
                    query.whereEqualTo("message", adapter.getChild(groupPosition, 0).toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> messageList, ParseException e) {
                            if (e == null) {
                                messageList.get(0).put("read", true);
                                messageList.get(0).saveInBackground();
                            } else {
                                Log.d("message", "Error: " + e.getMessage());
                            }
                        }
                    });

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, HomePageActivity.class);
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
            startActivity(new Intent(this, ParseStarterProjectActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
