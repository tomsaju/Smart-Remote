package ir.iot.smartremote;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.iot.smartremote.fragment.AllChannelAdapter;

public class AllChannelsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,AllChannelAdapter.allChannelListClickListener {
    private static final String TAG = "AllChannelsActivity";
    String tag_string_req = "string_req";
    FirebaseDatabase database;
    DatabaseReference channelListRef;
    ArrayList<Channel> channels;
    ListView channelListView;
    Spinner spinner;
    private HashMap<String, String> mParams;
    SearchView searchView;
     ArrayAdapter<String> dataAdapter;
    AllChannelAdapter adapter;
    HashMap<Integer, String> numberCodesMap;
    String BASE_URL = "http://192.168.225.236/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_channels);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        channelListView = findViewById(R.id.all_channel_list);
        database = FirebaseDatabase.getInstance();

        channelListRef = database.getReference("Channels");


        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Spinner element
         spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("All channels");
        categories.add("Malayalam");
        categories.add("News");
        categories.add("Sports");
        categories.add("Movies");
        categories.add("Music");
        categories.add("Entertainment");
        categories.add("Infotainment");
        categories.add("Regional");
        categories.add("Kids");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        numberCodesMap = new HashMap<>();
        numberCodesMap.put(0, "0xA1DE23DC");
        numberCodesMap.put(1, "0xA1DEA35C");
        numberCodesMap.put(2, "0xA1DE639C");
        numberCodesMap.put(3, "0xA1DEE31C");
        numberCodesMap.put(4, "0xA1DE916E");
        numberCodesMap.put(5, "0xA1DE51AE");
        numberCodesMap.put(6, "0xA1DED12E");
        numberCodesMap.put(7, "0xA1DE31CE");
        numberCodesMap.put(8, "0xA1DEC936");
        numberCodesMap.put(9, "0xA1DE29D6");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_list, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // do something when text changes
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
            if(adapter!=null) {
                adapter.getFilter().filter(item);
            }

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.menu_search:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        channelListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (channels != null) {
                    channels.clear();
                } else {
                    channels = new ArrayList<>();
                }


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Channel channel = snapshot.getValue(Channel.class);
                    channels.add(channel);
                    System.out.println(channel.getTitle());
                }
                 adapter = new AllChannelAdapter(channels,getBaseContext(), AllChannelsActivity.this);
                channelListView.setAdapter(adapter);
                spinner.setSelection(0);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


    }

    @Override
    public void onCLicked(Channel channel) {


        int n = channel.getIndex();
        while (n > 0) {
            int d = n / 10;
            int k = n - d * 10;
            n = d;
            System.out.println(k);
            sendData(numberCodesMap.get(k), "NEC", 32, 0);
        }
    }

    void sendData(final String code, final String protocol, final int numberOfBits, int targetDevice) {
        String url = BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Toast.makeText(AllChannelsActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(AllChannelsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
            }


        }


        ) {
            @Override
            public Map<String, String> getParams() {
                mParams = new HashMap<String, String>();
                mParams.put("code", code);
                mParams.put("protocol", protocol);
                mParams.put("numbits", String.valueOf(numberOfBits));
                Log.d(TAG, "getParams: code is " + code);
                return mParams;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
