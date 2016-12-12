package com.doordash.event;

import android.os.Looper;
import android.os.Handler;


import com.squareup.otto.Bus;

public class EventBus extends Bus {

    private static EventBus sBus = new EventBus();

    @Override
    public void post(final Object object) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(object);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    public void run() {
                        EventBus.super.post(object);
                    }
                });
        }
    }

    public static EventBus getInstance() {
        return sBus;
    }


}
