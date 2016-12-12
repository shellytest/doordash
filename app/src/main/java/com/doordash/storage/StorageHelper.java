package com.doordash.storage;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.doordash.event.EventBus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageHelper {

    private static final String TAG = StorageHelper.class.getName();

    private static Set<Long> sFavorites;
    private static final String FAVORITE = "favorite";
    private static final String DELIM = ",";
    private static final Executor sExecutor = Executors.newSingleThreadExecutor();

    public static void updateFavorite(Context ctx, long id, boolean isFavorite) {
        Set<Long> favorites = getFavorites(ctx);
        if (isFavorite) {
            favorites.add(id);
        } else {
            favorites.remove(id);
        }
        saveString(ctx, FAVORITE, asString(favorites));
    }

    public static boolean isFavorite(Context ctx, long restaurantId) {
        return getFavorites(ctx).contains(restaurantId);
    }

    public synchronized static Set<Long> getFavorites(Context ctx) {
        if (sFavorites == null) {
            loadFavorites(ctx);
        }
        return sFavorites;
    }

    private static void saveString(Context ctx, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String asString(Set<Long> integerSet) {
        String ret = TextUtils.join(DELIM, integerSet);
        return ret;
    }

    private static void loadFavorites(Context ctx) {
        if (sFavorites == null) {
            sFavorites = new HashSet<Long>();
        } else {
            sFavorites.clear();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String strFavorites = prefs.getString(FAVORITE, "");
        sFavorites = asSet(strFavorites);
    }

    private static Set<Long> asSet(String s) {
        Set<Long> ret = new HashSet<Long>();
        if (TextUtils.isEmpty(s)) {
            return ret;
        }
        String[] items = s.split(DELIM);
        for (String item : items) {
            ret.add(Long.valueOf(item));
        }
        return ret;
    }

}
