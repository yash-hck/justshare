package com.example.justshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoSend(View view) {
        startActivity(new Intent(MainActivity.this, selectMediaActivity.class));
    }

    public void gotorecive(View view) {
    }
}
