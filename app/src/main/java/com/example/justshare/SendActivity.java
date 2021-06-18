package com.example.justshare;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SendActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    WifiManager wifiManager;

    Button send, onOff, discover;
    ListView peerListView;
    TextView readMessage, connectionStatus;
    EditText writeMessage;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] devicenameArray;
    WifiP2pDevice[] deviceArray;

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                devicenameArray  = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];

                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()){
                    devicenameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
                ArrayAdapter arrayAdapter;
                arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, devicenameArray);
                peerListView.setAdapter(arrayAdapter);
            }

            if(peers.size() == 0){
                Toast.makeText(SendActivity.this, "NO Peers Found", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        peerListView = findViewById(R.id.peerListView);
        readMessage = findViewById(R.id.readMsg);
        writeMessage = findViewById(R.id.writeMsg);
        send = findViewById(R.id.sendButton);
        onOff = findViewById(R.id.onOff);
        discover = findViewById(R.id.discover);
        connectionStatus = findViewById(R.id.connectionStatus);



        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);


        exqListener();

        intentFilter = new IntentFilter();

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }

    private void exqListener() {
        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    onOff.setText("ON");
                } else {
                    wifiManager.setWifiEnabled(true);
                    onOff.setText("OFF");
                }
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoverPeers();
            }
        });



        peerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device = deviceArray[i];
                WifiP2pConfig  config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(SendActivity.this, "Connection to " + device.deviceName + " Successful", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i) {
                            Toast.makeText(SendActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    requestPermission();
                }


            }
        });

    }


    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress ownerAdress = wifiP2pInfo.groupOwnerAddress;

            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
                connectionStatus.setText("Host");
            }
            else {
                connectionStatus.setText("Client");
            }
        }
    };



    public void discoverPeers() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    connectionStatus.setText("Discovery Started");
                }

                @Override
                public void onFailure(int i) {
                    connectionStatus.setText("Can not discover");
                }
            });
        }
        else{
            requestPermission();
        }


    }

    private void requestPermission() {
        String[] prems = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, prems)){
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            EasyPermissions.requestPermissions(
                    this,
                    "Please grant",
                    1,
                    prems
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        discoverPeers();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Permissions are Required", Toast.LENGTH_SHORT).show();

        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "Give Permission", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
