package com.doordash.api;

import android.content.Context;
import android.util.Log;

import com.doordash.api.RestaurantResource;
import com.doordash.datamodel.RestaurantInfo;
import com.doordash.event.EventBus;
import com.doordash.event.RestaurantInfoChangeEvent;
import com.doordash.event.RestaurantInfoListEvent;
import com.doordash.storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantResource {

    private static String TAG = RestaurantResource.class.getName();
    private static final Executor sExecutor = Executors.newSingleThreadExecutor();

    public void asyncGetData(final Context ctx, final float lat, final float lng) {

        sExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    EventBus.getInstance().post(new RestaurantInfoListEvent(getData(ctx, lat, lng)));
                }
            });
    }

    public void asyncUpdateInfo(final Context ctx, final RestaurantInfo info) {
        sExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    EventBus.getInstance().post(new RestaurantInfoChangeEvent(updateInfo(ctx, info)));
                }
            });
    }

    private RestaurantInfo updateInfo(Context ctx, RestaurantInfo info) {
        StorageHelper.updateFavorite(ctx, info.id, info.isFavorite);
        return info;
    }

    private List<RestaurantInfo> getData(final Context ctx, final float lat, final float lng) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.doordash.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        IDoorDashService service = retrofit.create(IDoorDashService.class);
        try {
            List<RestaurantInfo> data = service.getRestaurant(lat, lng).execute().body();
            for (RestaurantInfo info: data) {
                info.isFavorite = StorageHelper.isFavorite(ctx, info.id);
            }
            return data;
        } catch (IOException any) {
            // Don't worry about errors for now.
            Log.w(TAG, "Ignoring for now", any);
            return new ArrayList<RestaurantInfo>();
        }
    }
}
