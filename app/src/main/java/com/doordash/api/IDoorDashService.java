package com.doordash.api;

import com.doordash.datamodel.RestaurantInfo;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;


public interface IDoorDashService {

    // 'https://api.doordash.com/v2/restaurant/?lat=37.422740&lng=-122.139956
    @GET("restaurant/")
    Call<List<RestaurantInfo>> getRestaurant(@Query("lat") float lat, @Query("lng") float lng);

}
