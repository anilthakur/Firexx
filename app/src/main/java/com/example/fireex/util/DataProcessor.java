package com.example.fireex.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fireex.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    private static Context context;

    public DataProcessor(Context context) {
        this.context = context;
    }

    public final static String PREFS_NAME = "appname_prefs";

    public boolean sharedPreferenceExist(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        if (!prefs.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static void setInt(String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public static void setStr(String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "DNF");
    }

    public static void setBool(String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public static void saveUser(String key, User value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static Object getUser(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public static void saveImage(ArrayList<String> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }
    public static ArrayList<String> getImage(String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static void clearImages(String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().remove(key).commit();

    }
}