package io.github.djunicode.salespersontracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationResult;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class LocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE="io.github.djunicode.salespersontracker.UPDATE_LOCATION";
    WebSocket webSocket;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null)
        {
            final String action=intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result=LocationResult.extractResult(intent);
                if(result!=null)
                {
                    Location location=result.getLastLocation();
                    String location_string=new StringBuilder(""+location.getLatitude()).append("/").append(location.getLongitude()).toString();
                    //TODO:send coordinates to server
                    instantiateWebSocket();
                    webSocket.send(location_string);
                    Toast.makeText(context,location_string,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void instantiateWebSocket()
    {
        OkHttpClient client = new OkHttpClient();

        //TODO: get url from the backend team
        Request request = new Request.Builder().url("").build();
        SocketListener listener = new SocketListener(MainActivity.getInstance());
        webSocket = client.newWebSocket(request, listener);
    }

    public class SocketListener extends WebSocketListener
    {
        public MainActivity activity;

        public SocketListener(MainActivity mainActivity)
        {
            this.activity = mainActivity;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Connection established!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);


        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }
    }
}
