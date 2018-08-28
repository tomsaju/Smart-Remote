package ir.iot.smartremote;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import ir.iot.smartremote.fragment.RemoteLayoutFragment;

public class MainActivity extends AppCompatActivity implements ChannelRecyclerAdapter.IRecyclerViewListener, RemoteLayoutFragment.onRemotePanelInteractionListener {
    String tag_string_req = "string_req";
    private static final String TAG = "MainActivity";
    String BASE_URL = "http://192.168.225.236/";
    ArrayList<RemoteFunction> functionArrayList = new ArrayList<>();
    ArrayList<Channel> channels = new ArrayList<>();
    private HashMap<String, String> mParams;
    FirebaseDatabase database;
    DatabaseReference channelListRef;
    RecyclerView channelRecyclerview;
    HashMap<Integer, String> numberCodesMap;
    HashMap<String, String> remoteFunctionsMap;
    BottomSheetBehavior sheetBehavior;
    LinearLayout bottomSheet;
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheet = findViewById(R.id.bottom_sheet);


        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);


        channelListRef = database.getReference("Channels");
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


        remoteFunctionsMap = new HashMap<>();
        remoteFunctionsMap.put("CHANNELS", "0xA1DE49B6");
        remoteFunctionsMap.put("GARDEN", "0xA1DE08F7");
        remoteFunctionsMap.put("ON DEMAND", "0xA1DEF10E");
        remoteFunctionsMap.put("SHOPPING", "0xA1DE6B94");
        remoteFunctionsMap.put("CHANNEL+", "0xA1DE738C");
        remoteFunctionsMap.put("CHANNEL-", "0xA1DEF30C");
        remoteFunctionsMap.put("VOLUME+", "0xA1DE53AC");
        remoteFunctionsMap.put("VOLUME-", "0xA1DE33CC");
        remoteFunctionsMap.put("RED", "0xA1DE718E");
        remoteFunctionsMap.put("GREEN", "0xA1DE39C6");
        remoteFunctionsMap.put("YELLOW", "0xA1DEA956");
        remoteFunctionsMap.put("BLUE", "0xA1DE817E");
        remoteFunctionsMap.put("OK", "0xA1DEFB04");
        remoteFunctionsMap.put("BACK", "0xA1DE9966");
        remoteFunctionsMap.put("INFO", "0xA1DEA15E");


        channelRecyclerview = findViewById(R.id.mainList);


    }

    void sendData(final String code, final String protocol, final int numberOfBits, int targetDevice) {
        String url = BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error.", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onResume() {
        super.onResume();
        // Read from the database
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
                Log.d(TAG, "onDataChange: Printing from firebase fetch");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Channel channel = snapshot.getValue(Channel.class);
                    channels.add(channel);
                    System.out.println(channel.getTitle());
                }
                ChannelRecyclerAdapter adapter = new ChannelRecyclerAdapter(getBaseContext(), channels, MainActivity.this);
                channelRecyclerview.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getBaseContext(), R.dimen.item_offset);
                channelRecyclerview.addItemDecoration(itemDecoration);
                channelRecyclerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onChannelSelected(View view) {
        int itemPosition = channelRecyclerview.getChildLayoutPosition(view);
        Channel selectedChannel = channels.get(itemPosition);
        Toast.makeText(this, selectedChannel.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

        int n = selectedChannel.getIndex();
        while (n > 0) {
            int d = n / 10;
            int k = n - d * 10;
            n = d;
            System.out.println(k);
            sendData(numberCodesMap.get(k), "NEC", 32, 0);
        }


    }


    public void onRemotePanelClicked(View view) {
        String btnText = ((Button) view).getText().toString();
        sendData(remoteFunctionsMap.get(btnText.toUpperCase()), "NEC", 32, 0);
    }

    @Override
    public void onRemoteFunctionCLicked(RemoteFunction remoteFunction) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_mic:
                Toast.makeText(getApplicationContext(), "Voice Search", Toast.LENGTH_LONG).show();
                displaySpeechRecognizer();
                return true;
            case R.id.menu_power:
                Toast.makeText(getApplicationContext(), "Switch On off", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_search:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
                return true;
            case R.id.quick_access_menu:
                showQuickMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showQuickMenu() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show();
            getChannelDetail(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getChannelDetail(String spokenText) {
        for (Channel channel : channels) {
            if (channel.getTitle().equalsIgnoreCase(spokenText)) {
                gotoChannel(channel);
            }
        }
    }

    private void gotoChannel(Channel channel) {
        Channel selectedChannel = channel;
        Toast.makeText(this, selectedChannel.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

        int n = selectedChannel.getIndex();
        while (n > 0) {
            int d = n / 10;
            int k = n - d * 10;
            n = d;
            System.out.println(k);
            sendData(numberCodesMap.get(k), "NEC", 32, 0);
        }
    }

}
