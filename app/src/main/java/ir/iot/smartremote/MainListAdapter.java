package ir.iot.smartremote;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dreamz on 25-08-2018.
 */

public class MainListAdapter extends BaseAdapter {
    String  tag_string_req = "string_req";
    private static final String TAG = "listadapter";
    String BASE_URL = "http://192.168.225.236/";
    ArrayList<RemoteFunction> functionArrayList = new ArrayList<>();
    private HashMap<String, String> mParams;


    Context context;
    ArrayList<RemoteFunction> funList;

    public MainListAdapter(Context context, ArrayList<RemoteFunction> funList) {
        this.context = context;
        this.funList = funList;
    }

    @Override
    public int getCount() {
        return funList.size();
    }

    @Override
    public Object getItem(int position) {
        return funList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myItem = inflater.inflate(R.layout.main_list_item_layout,parent,false);
        TextView title = myItem.findViewById(R.id.main_list_item_title);
        title.setText(funList.get(position).getTitle());

        myItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item with code "+funList.get(position).getCode(), Toast.LENGTH_SHORT).show();
                sendData(funList.get(position).code,funList.get(position).protocol,funList.get(position).numberOfBits,2);
            }
        });


        return myItem;
    }

    void sendData(final String code, final String protocol, final int numberOfBits, int targetDevice){
        String url=BASE_URL;



        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Toast.makeText(context, "error onresponse", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
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
