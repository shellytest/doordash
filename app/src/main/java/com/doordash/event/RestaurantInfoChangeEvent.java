package com.doordash.event;

import com.doordash.datamodel.RestaurantInfo;

import org.json.JSONObject;

import java.util.List;


public class RestaurantInfoChangeEvent {

    private RestaurantInfo mData;
    public RestaurantInfoChangeEvent(RestaurantInfo data) {
        mData = data;
    }

    public RestaurantInfo getData() {
        return mData;
    }

}
