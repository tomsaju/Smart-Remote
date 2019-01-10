package ir.iot.smartremote;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ir.iot.smartremote.fragment.RemoteLayoutFragment;

public class MainActivity extends AppCompatActivity implements ChannelRecyclerAdapter.IRecyclerViewListener, RemoteLayoutFragment.onRemotePanelInteractionListener {
    String tag_string_req = "string_req";
    private static final String TAG = "MainActivity";
    String BASE_URL = "http://192.168.1.15/";
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
    private RecyclerView mainList;
    static boolean busy = false;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds


        channelRecyclerview = findViewById(R.id.mainList);
        channelRecyclerview.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getBaseContext(), R.dimen.item_offset);
        channelRecyclerview.addItemDecoration(itemDecoration);
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


        /*channelListRef = database.getReference("Channels");
        numberCodesMap = new HashMap<>();
        numberCodesMap.put(0, "A1DE23DC");
        numberCodesMap.put(1, "A1DEA35C");
        numberCodesMap.put(2, "A1DE639C");
        numberCodesMap.put(3, "A1DEE31C");
        numberCodesMap.put(4, "A1DE916E");
        numberCodesMap.put(5, "A1DE51AE");
        numberCodesMap.put(6, "A1DED12E");
        numberCodesMap.put(7, "A1DE31CE");
        numberCodesMap.put(8, "A1DEC936");
        numberCodesMap.put(9, "A1DE29D6");*/

        channelListRef = database.getReference("Channels");


        numberCodesMap = new HashMap<>();
        numberCodesMap.put(0, "2715689948");
        numberCodesMap.put(1, "2715722588");
        numberCodesMap.put(2, "2715706268");
        numberCodesMap.put(3, "2715738908");
        numberCodesMap.put(4, "2715717998");
        numberCodesMap.put(5, "2715701678");
        numberCodesMap.put(6, "2715734318");
        numberCodesMap.put(7, "2715693518");
        numberCodesMap.put(8, "2715732278");
        numberCodesMap.put(9, "2715691478");


        remoteFunctionsMap = new HashMap<>();
        remoteFunctionsMap.put("CHANNELS", "2715699638");
        remoteFunctionsMap.put("GARDEN", "2715683063");
        remoteFunctionsMap.put("ON DEMAND", "2715742478");
        remoteFunctionsMap.put("SHOPPING", "2715708308");
        remoteFunctionsMap.put("CH+", "2715710348");
        remoteFunctionsMap.put("CH -", "2715742988");
        remoteFunctionsMap.put("VOL+", "2715702188");
        remoteFunctionsMap.put("VOL-", "2715694028");
        remoteFunctionsMap.put("RED", "2715709838");
        remoteFunctionsMap.put("GREEN", "2715695558");
        remoteFunctionsMap.put("YELLOW", "2715724118");
        remoteFunctionsMap.put("BLUE", "2715713918");
        remoteFunctionsMap.put("ENTER", "2715745028");
        remoteFunctionsMap.put("BACK", "2715720038");
        remoteFunctionsMap.put("INFO", "2715722078");
        remoteFunctionsMap.put("UP", "2715685868");
        remoteFunctionsMap.put("DOWN", "2715683318");
        remoteFunctionsMap.put("RIGHT", "2715715958");
        remoteFunctionsMap.put("LEFT", "2715697598");






    }

    void sendData(final String code, final String protocol, final int numberOfBits, int targetDevice) {



        String url = BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
             //   Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Connection Error.", Toast.LENGTH_SHORT).show();
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

        if(busy){
            return;
        }
        vibrateforClick();
        int itemPosition = channelRecyclerview.getChildLayoutPosition(view);
        Channel selectedChannel = channels.get(itemPosition);
        Toast.makeText(this, selectedChannel.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

        int n = selectedChannel.getIndex();

        String channelNumber = String.valueOf(n);
        for (int i = 0; i < channelNumber.length(); i++) {
            char sDigit = channelNumber.charAt(i);
            int sendDigit = Integer.parseInt(String.valueOf(sDigit));
            sendData(numberCodesMap.get(sendDigit), "NEC", 32, 0);
        }

        busy = true;
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        busy = false;
                    }
                });
            }
        }, 1000);


    }


    public void onRemotePanelClicked(View view) {
        vibrateforClick();
        String btnText = ((Button) view).getText().toString();
        sendData(remoteFunctionsMap.get(btnText.toUpperCase()), "NEC", 32, 0);
    }

    public void onRemotePanelDigitClicked(View view) {
        vibrateforClick();
        String btnText = ((Button) view).getText().toString();
        int numb = Integer.parseInt(btnText);
        sendData(numberCodesMap.get(numb), "NEC", 32, 0);
    }
    @Override
    public void onRemoteFunctionCLicked(RemoteFunction remoteFunction) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_mic:
                // Toast.makeText(getApplicationContext(), "Voice Search", Toast.LENGTH_LONG).show();
                displaySpeechRecognizer();
                return true;
            case R.id.menu_power:
               // Toast.makeText(getApplicationContext(), "Switch On off", Toast.LENGTH_LONG).show();
                sendData("6156","RC5",13,0);
                return true;
            case R.id.menu_search:
                showFullRemote();
                return true;
            case R.id.quick_access_menu:
                showAllChannels();
                return true;

            case R.id.setup_remote:
                setupRemote();
                return true;

            case R.id.add_channel:
                AddChannelPage();
                return true;
            case R.id.reconnect:
                //reconnect();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void AddChannel() {
        //show dialog to add channel
    }

    private void setupRemote() {
        startActivity(new Intent(this, SetupRemoteActivity.class));
    }

    private void AddChannelPage() {
        startActivity(new Intent(MainActivity.this,NewChannelActivity.class));


    }

    private void showAllChannels() {
        startActivity(new Intent(MainActivity.this, AllChannelsActivity.class));
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

    private void showFullRemote(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,new RemoteLayoutFragment()).commit();
    }

    private void hideFullRemote(){
        FragmentManager manager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        Fragment MyFraglastFrag=manager.findFragmentById(android.R.id.content);
        if(MyFraglastFrag!=null&&MyFraglastFrag.isVisible()) {
            fragmentTransaction.remove(MyFraglastFrag);
            fragmentTransaction.commit();
        }else{
            super.onBackPressed();
        }

    }



    @Override
    public void onBackPressed() {
        hideFullRemote();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something
            sendData("2715694028", "NEC", 32, 0);
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            sendData("2715702188", "NEC", 32, 0);
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return true;


    }


    public void vibrateforClick(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(20);
        }
    }

    public void reconnect(){
         String   s_dns1 ;
         String   s_dns2;
         String   s_gateway;
         String   s_ipAddress;
         String   s_leaseDuration;
         String   s_netmask;
         String   s_serverAddress;
        TextView info;
        DhcpInfo d;
        WifiManager wifii;

        wifii = (WifiManager) getBaseContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();

        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);

        Log.d(TAG, "reconnect: "+s_dns1);
        Log.d(TAG, "reconnect: "+s_dns2);
        Log.d(TAG, "reconnect: "+s_gateway);
        Log.d(TAG, "reconnect: "+s_ipAddress);
        Log.d(TAG, "reconnect: "+s_leaseDuration);
        Log.d(TAG, "reconnect: "+s_netmask);
        Log.d(TAG, "reconnect: "+s_serverAddress);


        new InspectNetwork().execute(d);

    }

    public String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    class InspectNetwork extends AsyncTask<DhcpInfo,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(DhcpInfo... dhcpInfos) {
            Log.d(TAG, "doInBackground: worked");
            String connections = "";
            InetAddress host;
            DhcpInfo d = dhcpInfos[0];
            try
            {
                host = InetAddress.getByName(intToIp(d.dns1));
                byte[] ip = host.getAddress();

                for(int i = 1; i <= 254; i++)
                {
                    ip[3] = (byte) i;
                    InetAddress address = InetAddress.getByAddress(ip);
                    if(address.isReachable(100))
                    {
                        System.out.println(address + " machine is turned on and can be pinged");
                        connections+= address+"\n";
                    }
                    else if(!address.getHostAddress().equals(address.getHostName()))
                    {
                        System.out.println(address + " machine is known in a DNS lookup");
                    }

                }
            }
            catch(UnknownHostException e1)
            {
                e1.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            System.out.println(connections);

            return null;
        }
    }

}
