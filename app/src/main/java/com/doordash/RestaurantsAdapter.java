package com.doordash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordash.datamodel.RestaurantInfo;
import com.doordash.api.RestaurantResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.doordash.storage.StorageHelper;
import com.squareup.picasso.Picasso;

public class RestaurantsAdapter  extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder> {

    private static final String TAG = RestaurantsAdapter.class.getName();
    private final List<RestaurantInfo> mRestaurantList = new ArrayList<RestaurantInfo>();
    private Context mContext;

    public RestaurantsAdapter(Context ctx) {
        mContext = ctx;
    }

    public void setData(List<RestaurantInfo> restaurantInfoList) {
        mRestaurantList.clear();
        mRestaurantList.addAll(restaurantInfoList);
        notifyDataSetChanged();
    }

    public void updateData(RestaurantInfo changed) {
        int len = mRestaurantList.size();
        for (int i = 0; i < len; i++) {
            RestaurantInfo cur = mRestaurantList.get(i);
            if (cur.id == changed.id) {
                mRestaurantList.set(i, changed);
                notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public RestaurantsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.restaurant_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RestaurantInfo item = mRestaurantList.get(position);
        Picasso.with(mContext)
            .load(item.coverUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.mImageView);

        holder.setRestaurantInfo(item, mContext.getResources());
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    private static void toggleFavorite(Context ctx, RestaurantInfo info) {
        info.isFavorite = !info.isFavorite;
        new RestaurantResource().asyncUpdateInfo(ctx, info);
    }

    private void updateFavoriteUI(ImageView imageView, long id) {
        boolean isFavorite = StorageHelper.isFavorite(mContext, id);
        Drawable drawable;
        if (isFavorite) {
            drawable = mContext.getResources().getDrawable(R.drawable.favorite);
        } else {
            drawable = mContext.getResources().getDrawable(R.drawable.favorite_off);
        }
        imageView.setImageDrawable(drawable);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.restaurant_name)
        TextView mTextView;

        @BindView(R.id.res_image)
        ImageView mImageView;

        @BindView(R.id.restaurant_desc)
        TextView mDesc;

        @BindView(R.id.restaurant_addr)
        TextView mAddr;

        @BindView(R.id.favorite_icon)
        ImageView mFavoriteIcon;

        private RestaurantInfo mRestaurantInfo;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private MyViewHolder setRestaurantInfo(RestaurantInfo info, Resources resources) {
            mRestaurantInfo = info;
            mTextView.setText(info.name);
            mDesc.setText(info.description);
            mAddr.setText(info.address.printableAddress);
            mFavoriteIcon.setImageDrawable(
                resources.getDrawable(
                    info.isFavorite ? R.drawable.favorite : R.drawable.favorite_off));
            return this;
        }

        @OnClick(R.id.favorite_icon)
        public void onFavoriteClicked(View view) {
            toggleFavorite(view.getContext(), mRestaurantInfo);
        }
    }
}

