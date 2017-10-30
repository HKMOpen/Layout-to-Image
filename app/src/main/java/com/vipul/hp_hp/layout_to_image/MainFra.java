package com.vipul.hp_hp.layout_to_image;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pang on 10/19/17.
 */

public class MainFra extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framea);
        getSupportFragmentManager().beginTransaction().replace(R.id.actual_frame, new TicScan(), "fef").commit();
    }


    public static void init_start(AppCompatActivity app) {
        Intent intent = new Intent(app, MainFra.class);
        app.startActivityForResult(intent, 2901);
    }
}
