package com.metalexplorer;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserBookmarks {

    private static final String PREFS_NAME = "UserPrefs";

    public static void saveLongList(Context context, String userId, List<Long> newLongList) {
        List<Long> existingLongList = getLongList(context, userId);

        // If the existing list is null (i.e., no previous entries), initialize it with an empty list
        if (existingLongList == null) {
            existingLongList = new ArrayList<>();
        }

        // Append new entries to the existing list
        existingLongList.addAll(newLongList);

        // Save the updated list to SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(getUserPrefsName(userId), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(existingLongList);
        editor.putString("longList", json);
        editor.apply();
    }

    // Method to retrieve the list of longs for the specified user
    public static List<Long> getLongList(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences(getUserPrefsName(userId), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("longList", null);
        Type type = new TypeToken<ArrayList<Long>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Method to generate a unique SharedPreferences name for the specified user
    private static String getUserPrefsName(String userId) {
        return PREFS_NAME + "_" + userId;
    }
}
