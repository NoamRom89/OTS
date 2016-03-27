package com.shenkar.android.ots.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.shenkar.android.ots.R;
import com.shenkar.android.ots.general.Toolbar;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class ManageTeamActivity extends Toolbar {
    private ListView listView;
    private List<String> names = new ArrayList<String>();
    private Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_your_team_activity);
            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            add_button = (Button)findViewById(R.id.add_team_members_button);
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ManageTeamActivity.this,CreateTeamActivity.class);
                    intent.putExtra("add",true);
                    startActivity(intent);
                }
            });
            // Define a new Adapter
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,names);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, com.parse.ParseException e) {
                    if (e == null) {
                        for (ParseUser user : objects) {
                            names.add(user.getUsername());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            // Assign adapter to ListView
            listView.setAdapter(adapter);
            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();

                }

            });

        }
}
