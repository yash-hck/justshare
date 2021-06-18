package com.example.justshare;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    SendActivity sendActivity;


    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, SendActivity sendActivity) {
        super();
        this.channel = channel;
        this.manager = manager;
        this.sendActivity = sendActivity;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context, "Wifi On", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Wifi Off", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (manager != null){
                manager.requestPeers(channel,sendActivity.peerListListener);
            }
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if(manager == null)return;

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo != null && networkInfo.isConnected()) {
                manager.requestConnectionInfo(channel,sendActivity.connectionInfoListener);
            }
            else {
                sendActivity.connectionStatus.setText("Device Disconnected");
            }
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }

    }
}
