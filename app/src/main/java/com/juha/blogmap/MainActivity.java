package com.juha.blogmap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.juha.blogmap.databinding.ActivityMainBinding;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TMapView tmapview;
    ActivityMainBinding binding;
    LocationManager locationManager;
    String TMAP_API_KEY = "l7xx822771dc1c964fb7bc81be7f579a5e9c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        initInstances();
        eventListeners();
        findRestaurants();


    }

    @Override
    protected void onResume() {
        super.onResume();

        SearchBlog searchBlog = new SearchBlog("페어링룸");
        searchBlog.execute();

    }

    private void initInstances(){
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(TMAP_API_KEY);
        binding.layoutTmap.addView(tmapview);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }
    private void eventListeners(){
        binding.btnCurLoc.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnCurLoc:
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                    } catch (SecurityException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude();
            double lat = location.getLatitude();
            tmapview.setCenterPoint(lon, lat, true);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    public HashMap<String, String> findRestaurants() {
        TMapData tMapData = new TMapData();
        TMapPoint tMapPoint = tmapview.getCenterPoint();
        final HashMap<String, String> searchList = new LinkedHashMap<>();;
        tMapData.findAroundNamePOI(tMapPoint, "식음료", 1, 99, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    TMapPOIItem tMapPOIItem = arrayList.get(i);
                    if (!searchList.containsKey(tMapPOIItem.getPOIID())){
                        searchList.put(tMapPOIItem.getPOIID(), tMapPOIItem.getPOIName());
                    }
                    Log.d("식음료", "POI Name: " + tMapPOIItem.getPOIID() + "  " + tMapPOIItem.getPOIName());
                }
                Log.d("===============", "=============================================================");
                for (Map.Entry<String, String> entry: searchList.entrySet()){
                    Log.d("식음료", entry.getKey() + " " + entry.getValue());
                }
            }
        });
        return searchList;
    }

}