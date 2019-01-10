package ir.iot.smartremote;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tom.saju on 1/9/2019.
 */

public class PrefrenceManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static PrefrenceManager instance;
    String BASE_URL = "baseUrl";

    public PrefrenceManager(Context context){
        this.context=context;
        try {
            sharedPreferences = context.getSharedPreferences("prefs", 0);
            editor = sharedPreferences.edit();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static PrefrenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefrenceManager(context);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences(){
        return this.sharedPreferences;
    }


    public void setBaseUrl(String baseUrl){
        editor.putString(BASE_URL, baseUrl);
        editor.commit();
    }

    public String getBaseUrl(){
        return sharedPreferences.getString(BASE_URL,"");
    }


    public void setCode(String label,String code){
        editor.putString(label, code);
        editor.commit();
    }

    public String getCode(String label){
        return sharedPreferences.getString(label,"");
    }
}
