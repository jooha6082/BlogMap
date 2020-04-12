package com.juha.blogmap;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {

    private TMapView tmapview;

    String TMAP_API_KEY = "l7xx822771dc1c964fb7bc81be7f579a5e9c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(TMAP_API_KEY);
        RelativeLayout relativeLayout = findViewById(R.id.relativelayout_tmap);
        relativeLayout.addView(tmapview);


    }
}