package ir.iot.smartremote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by tom.saju on 1/9/2019.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static String DB_NAME = "Remote_DB";
    public static int DB_VERSION = 1;
    SQLiteDatabase database;
    //TABLE_NAME
    private final String TABLE_CHANNELS = "Channels_Table";

    //COLUMN NAMES
    private final String CHANNEL_ID = "Channel_ID";
    private final String CHANNEL_NAME = "Channel_Name";
    private final String CHANNEL_NUMBER = "Channel_Number";
    private final String CHANNEL_IMAGE_URL = "Channel_Image_Url";
    private final String CHANNEL_CATEGORY = "Channel_Category";
    private final String CHANNEL_LANGUAGE = "Channel_Language";

    //TABLE_CREATE_STATEMENTS
    private final String CREATE_TABLE_CHANNEL_STATEMENT = "CREATE TABLE " + TABLE_CHANNELS + "(" +
            CHANNEL_ID + " INTEGER," +
            CHANNEL_NAME + " TEXT," +
            CHANNEL_NUMBER + " INTEGER," +
            CHANNEL_CATEGORY + " TEXT," +
            CHANNEL_LANGUAGE + " TEXT," +
            CHANNEL_IMAGE_URL + " TEXT )";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHANNEL_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertChannel(Channel channel){
        if(database==null){
            database = this.getWritableDatabase();
        }

        ContentValues cv = new ContentValues();
        cv.put(CHANNEL_ID,channel.getIndex());
        cv.put(CHANNEL_NAME,channel.getTitle());
        cv.put(CHANNEL_NUMBER,channel.getIndex());
        cv.put(CHANNEL_CATEGORY,channel.getCategory());
        cv.put(CHANNEL_LANGUAGE,channel.getLanguage());
        cv.put(CHANNEL_IMAGE_URL,channel.getImageUrl());

        database.insert(TABLE_CHANNELS,null,cv);
    }

    public ArrayList<Channel> getAllChannelsList(){
        if(database==null){
            database = this.getWritableDatabase();
        }
        ArrayList<Channel> allChannelList = new ArrayList<>();
        Cursor cursor;
        cursor = database.query(TABLE_CHANNELS,null,null,null,null,null,null);
        if(cursor.getCount()>0){
            if(cursor.moveToNext()){
                Channel channel = new Channel();
                channel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_NAME)));
                channel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_CATEGORY)));
                channel.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_IMAGE_URL)));
                channel.setIndex(cursor.getInt(cursor.getColumnIndexOrThrow(CHANNEL_NUMBER)));
                channel.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_LANGUAGE)));
                allChannelList.add(channel);
            }
        }
        return allChannelList;
    }

}
