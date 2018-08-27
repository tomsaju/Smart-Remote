package ir.iot.smartremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String  tag_string_req = "string_req";
    private static final String TAG = "MainActivity";
    String BASE_URL = "http://192.168.225.236/";
    ArrayList<RemoteFunction> functionArrayList = new ArrayList<>();
    ArrayList<Channel> channels = new ArrayList<>();
    private HashMap<String, String> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemoteFunction power = new RemoteFunction("POWER","A1DEC13E",32,"NEC");
        RemoteFunction mute = new RemoteFunction("MUTE","A1DE59A6",32,"NEC");
        RemoteFunction home = new RemoteFunction("HOME","A1DE48B7",32,"NEC");

        functionArrayList.add(power);
        functionArrayList.add(mute);
        functionArrayList.add(home);

        Channel tv1 = new Channel(1,"Discovery Science","https://seeklogo.com/images/D/discovery-science-logo-804ACEFF86-seeklogo.com.jpg");
        Channel tv2 = new Channel(2,"Cartoon Network","https://i.pinimg.com/originals/c6/b6/f5/c6b6f52250f14be4bee0c4c658b1b028.jpg");
        Channel tv3 = new Channel(3,"Star Movies","https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/0021/1923/brand.gif?itok=xh9Oc6rW");

        channels.add(tv1);
        channels.add(tv2);
        channels.add(tv3);

        RecyclerView channelRecyclerview = findViewById(R.id.mainList);
        ChannelRecyclerAdapter adapter = new ChannelRecyclerAdapter(this,channels);
        channelRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        channelRecyclerview.addItemDecoration(itemDecoration);
        channelRecyclerview.setAdapter(adapter);
      //  MainListAdapter adapter = new MainListAdapter(this,functionArrayList);
      //  mainList.setAdapter(adapter);

    }

    void sendData(final String code, final String protocol, final int numberOfBits, int targetDevice){
        String url=BASE_URL;



        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Toast.makeText(MainActivity.this, "error onresponse", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }


        }


        ){@Override
        public Map<String, String> getParams(){
            mParams = new HashMap<String, String>();
            mParams.put("code", code);
            mParams.put("protocol", protocol);
            mParams.put("numbits", String.valueOf(numberOfBits));

            return mParams;
        }
        };;

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




}
