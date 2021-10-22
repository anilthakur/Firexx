package com.example.fireex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fireex.model.User;
import com.example.fireex.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fire.db";
    public static final String FIRE_TABLE_NAME = "scannerlist";
    public static final String FIRE_COLUMN_ID = "id";
    public static final String FIRE_COLUMN_NAME = "name";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createFireDB(db);
        createUserDB(db);
        createFireExB(db);
        insertUserDetails(db,new User("sachin","math",3,"9742434424"));
        insertUserDetails(db,new User("basavraj","mole",3,"9742434424"));
        insertUserDetails(db,new User("santosh","ITC",3,"9742434424"));
        insertUserDetails(db,new User("anand","ITC",3,"9742434424"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS fire");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS fire_ex");
        onCreate(db);
    }
    private void createFireDB(SQLiteDatabase db) {
        db.execSQL("create table fire (id integer primary key,fire_point_num text,area text,building_name text,location text," +
                "fire_extinguisher_num text,fire_extinguisher_type text)");
    }

    private void createUserDB(SQLiteDatabase db) {
        db.execSQL("create table user (id integer primary key, first_name text,last_name text,role_id integer,ph_num text)");
    }
    private void createFireExB(SQLiteDatabase db) {

        db.execSQL("create table fire_ex (id integer primary key,fire_ex_num text,data2 text,data3 text,data4 text," +
                "data5 text,data6 text)");
    }
    public boolean insertFire(String firePointNum,String area,String buildingName,String location,String extinguisherNum,String fireExtinguisherType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fire_point_num", firePointNum);
        contentValues.put("area",area);
        contentValues.put("building_name",buildingName);
        contentValues.put("location",location);
        contentValues.put("fire_extinguisher_num",extinguisherNum);
        contentValues.put("fire_extinguisher_type",fireExtinguisherType);
        db.insertWithOnConflict("fire", firePointNum, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean insertFireExtinguisher(String fireExNum, String data2, String data3, String data4, String data5, String data6) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fire_ex_num", fireExNum);
        contentValues.put("data2",data2);
        contentValues.put("data3",data3);
        contentValues.put("data4",data4);
        contentValues.put("data5",data5);
        contentValues.put("data6",data6);
        db.insertWithOnConflict("fire_ex", fireExNum, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }
    private void insertUserDetails(SQLiteDatabase db,User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIRST_NAME, user.getFirstName());
        contentValues.put(Constant.LAST_NAME, user.getLastName());
        contentValues.put(Constant.ROLE_ID, user.getRoleId());
        contentValues.put(Constant.PH_NUM, user.getPhNum());
        db.insert("user", null, contentValues);

    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from fire where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FIRE_TABLE_NAME);
        return numRows;
    }

    public boolean updateFire(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.update("fire", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteFire(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("fire",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteFireEX(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("fire_ex",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public int deleteAllFireData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("fire",null,null);
        db.close();
        return i;
    }

    public int deleteAllFireExData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("fire_ex",null,null);
        db.close();
        return i;
    }

    public ArrayList<HashMap<String, String>> getAllFire() {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from fire", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            HashMap<String, String> list = new HashMap<>();
            list.put("id", res.getString(0));
            list.put("fire_point_num", res.getString(1));
            list.put("area", res.getString(2));
            list.put("building_name", res.getString(3));
            list.put("location", res.getString(4));
            list.put("fire_extinguisher_num", res.getString(5));
            list.put("fire_extinguisher_type", res.getString(6));
            res.moveToNext();
            array_list.add(list);
        }
        return array_list;
    }

    public ArrayList<HashMap<String, String>> getAllFireEx() {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from fire_ex", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            HashMap<String, String> list = new HashMap<>();
            list.put("id", res.getString(0));
            list.put("fire_ex_num", res.getString(1));
            list.put("data2", res.getString(2));
            list.put("data3", res.getString(3));
            list.put("data4", res.getString(4));
            list.put("data5", res.getString(5));
            list.put("data6", res.getString(6));
            res.moveToNext();
            array_list.add(list);
        }
        return array_list;
    }





    public User getUserByData(String firstName, String lastName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where first_name like '" + firstName + "' and last_name like '" + lastName +"'", null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),cursor.getString(4));
        return user;
    }


}