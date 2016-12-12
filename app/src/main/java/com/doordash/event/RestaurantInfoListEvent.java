package com.doordash.event;

import com.doordash.datamodel.RestaurantInfo;

import org.json.JSONObject;

import java.util.List;


public class RestaurantInfoListEvent {

    private List<RestaurantInfo> mData;
    public RestaurantInfoListEvent(List<RestaurantInfo> data) {
        mData = data;
    }

    public List<RestaurantInfo> getData() {
        return mData;
    }

}
