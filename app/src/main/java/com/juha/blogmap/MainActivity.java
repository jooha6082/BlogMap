package com.juha.blogmap;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TMapView tmapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(BuildConfig.TMAP_API_KEY);
        RelativeLayout relativeLayout = findViewById(R.id.relativelayout_tmap);
        relativeLayout.addView(tmapview);


    }
}