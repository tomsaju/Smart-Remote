package ir.iot.smartremote;

import android.os.Bundle;
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
        numberCodesMap.put(0, "zero");
        numberCodesMap.put(1, "one");
        numberCodesMap.put(2, "two");
        numberCodesMap.put(3, "thrjtu");
        numberCodesMap.put(4, "0xffojtu");
        numberCodesMap.put(5, "fibejtu");
        numberCodesMap.put(6, "0sixgjtu");
        numberCodesMap.put(7, "0fdgjtu");
        numberCodesMap.put(8, "0sghgjtu");
        numberCodesMap.put(9, "0sgsdfhgjtu");


        remoteFunctionsMap = new HashMap<>();
        remoteFunctionsMap.put("1", "0xffteyt");
        remoteFunctionsMap.put("2", "0xffteyt");
        remoteFunctionsMap.put("3", "0xffteyt");
        remoteFunctionsMap.put("4", "0xffteyt");
        remoteFunctionsMap.put("5", "0xffteyt");
        remoteFunctionsMap.put("6", "0xffteyt");
        remoteFunctionsMap.put("7", "0xffteyt");
        remoteFunctionsMap.put("8", "0xffteyt");
        remoteFunctionsMap.put("9", "0xffteyt");
        remoteFunctionsMap.put("0", "0xffteyt");
        remoteFunctionsMap.put("Menu", "0xffteyt");
        remoteFunctionsMap.put("Mute", "0xffteyt");
        remoteFunctionsMap.put("Enter", "0xffteyt");
        remoteFunctionsMap.put("Up", "0xffteyt");
        remoteFunctionsMap.put("Back", "0xffteyt");


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
        sendData(remoteFunctionsMap.get(btnText), "NEC", 32, 0);
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
        switch (id){
            case R.id.menu_mic:
                Toast.makeText(getApplicationContext(),"Voice Search",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_power:
                Toast.makeText(getApplicationContext(),"Switch On off",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_search:
                Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
