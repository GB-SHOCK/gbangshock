package com.github.pocmo.sensordashboard;

import android.content.Intent;
import android.net.Uri;

import com.github.pocmo.sensordashboard.shared.ClientPaths;
import com.github.pocmo.sensordashboard.shared.DataMapKeys;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageReceiverService extends WearableListenerService {
    private static final String TAG = "MessageReceiverService";

    private DeviceClient deviceClient;

    @Override
    public void onCreate() {
        super.onCreate();

        deviceClient = DeviceClient.getInstance(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals(ClientPaths.START_MEASUREMENT)) {
            startService(new Intent(this, SensorService.class));
        }

        if (messageEvent.getPath().equals(ClientPaths.STOP_MEASUREMENT)) {
            stopService(new Intent(this, SensorService.class));
        }
    }
}
