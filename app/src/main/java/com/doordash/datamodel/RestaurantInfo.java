package com.doordash.datamodel;


import com.google.gson.annotations.SerializedName;

public class RestaurantInfo {

    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("cover_img_url")
    public String coverUrl;

    @SerializedName("description")
    public String description;

    @SerializedName("address")
    public Address address;

    public boolean isFavorite = false;

}
