package com.example.echolynk.Utils.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.echolynk.Model.Conversation_Item;
import com.example.echolynk.Model.MassageModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    

    public DBHelper(Context context){
        super(context,"echolynk.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table conversation(" +
                "conversation_id INTEGER primary key AUTOINCREMENT," +
                "date Date," +
                "start_time TIME," +
                "end_time TIME," +
                "title TEXT," +
                "last_massage TEXT )");

        db.execSQL("create table massages(" +
                "massage_id INTEGER primary key AUTOINCREMENT," +
                "massage TEXT," +
                "type INTEGER," +
                "conversation_id_fk INTEGER," + // Add the foreign key column
                "FOREIGN KEY (conversation_id_fk) REFERENCES conversation(conversation_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists massages");
        db.execSQL("drop Table if exists conversation");
    }


    public Boolean insertMassages(String massage,int type,int conversationId){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("massage",massage);
        contentValues.put("type",type);
        contentValues.put("conversation_id_fk", conversationId);
        long result = DB.insert("massages", null, contentValues);

        if (result==-1) {
            DB.close();
            return false;
        }else {
            DB.close();
            return true;
        }

    }

    public Boolean insertConversation(String date, String start_time, String end_time, String title, String lastMassage, List<MassageModel> massages){

        for (MassageModel temp:massages) {
            Log.d("Test Massage List", temp.getMassage()+" - "+temp.getType());
        }

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("title", title);
        contentValues.put("last_massage", lastMassage);
        long result=DB.insert("conversation",null,contentValues);

        if (result==-1){
            return false;
        }else {
            // get last conversationId -->
            Log.d("massage list", massages.toString());
            int conversationLastId=getConversationLastId();
            Log.d("conversation last id", conversationLastId+"");
            for (MassageModel temp : massages) {
                Boolean b = insertMassages(temp.getMassage(), temp.getType(),conversationLastId);
                if (b){
                   // Log.d("massage insertion", temp.getMassage()+" - "+temp.getType());
                }else {
                    return false;
                }
            }
            DB.close();
            return true;
        }

    }


    @SuppressLint("Range")
    public  ArrayList<Conversation_Item> getConversations(){
        ArrayList<Conversation_Item> conversationList=new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("select conversation_id,title,last_massage,date,start_time,end_time from conversation", null);
        while (cursor.moveToNext()){
            conversationList.add(new Conversation_Item(
                    cursor.getInt(cursor.getColumnIndex("conversation_id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("last_massage")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("start_time")),
                    cursor.getString(cursor.getColumnIndex("end_time"))
            ));
        }

        return conversationList;
    }

    @SuppressLint("Range")
     private int getConversationLastId(){
        int lastId=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select conversation_id from conversation ORDER BY conversation_id DESC LIMIT 1", null);
        if (cursor.moveToNext()){
            lastId=cursor.getInt(cursor.getColumnIndex("conversation_id"));
        }
        cursor.close();
        db.close();
        return lastId;
    }

    @SuppressLint("Range")
    public ArrayList<MassageModel> getMassageList(int conversationId){
        ArrayList<MassageModel> massageList=new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("select massage,type from massages where conversation_id_fk = ?",
                new String[]{String.valueOf(conversationId)});

        while (cursor.moveToNext()){
            String massage = cursor.getString(cursor.getColumnIndex("massage"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));

            massageList.add(new MassageModel(massage,type));
        }

        return massageList;
    }

    @SuppressLint("Range")
    public ArrayList<MassageModel> getAllMassages(){
        ArrayList<MassageModel> massageList=new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("select massage,type from massages", null);

        while (cursor.moveToNext()){
            massageList.add(new MassageModel(
                    cursor.getString(cursor.getColumnIndex("massage")),
                    cursor.getInt(cursor.getColumnIndex("type"))
            ));
        }

        return massageList;
    }
}
