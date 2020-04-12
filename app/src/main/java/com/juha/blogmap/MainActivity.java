package com.juha.blogmap;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TmapAuthentication;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private static String TMAP_API_KEY = "l7xx822771dc1c964fb7bc81be7f579a5e9c";
    private TMapView tmapview;
    private Context context;
    private boolean trackingMode;

    private TMapGpsManager tMapGpsManager;
    private static int markerId;

    private ArrayList<TMapPoint> tMapPoints = new ArrayList<>();
    private ArrayList<String> arrayMarkerId = new ArrayList<>();
    private ArrayList<MapPoint> mapPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(TMAP_API_KEY);
        RelativeLayout relativeLayout = findViewById(R.id.relativelayout_tmap);
        relativeLayout.addView(tmapview);

        context = this;
        showMarkerPoint();

        tmapview.setCompassMode(true);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tMapGpsManager = new TMapGpsManager(MainActivity.this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
        }
        tMapGpsManager.OpenGps();

        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);

        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                Toast.makeText(MainActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onLocationChange(Location location) {
        if(trackingMode){
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void showMarkerPoint(){
        for(int i=0; i<mapPoints.size(); i++){
            TMapPoint tMapPoint = new TMapPoint(mapPoints.get(i).getLatitude(), mapPoints.get(i).getLongitude());
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            tMapMarkerItem.setTMapPoint(tMapPoint);
            tMapMarkerItem.setName(mapPoints.get(i).getName());
            tMapMarkerItem.setVisible(tMapMarkerItem.VISIBLE);
            tMapMarkerItem.setIcon(bitmap);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            tMapMarkerItem.setCalloutTitle(mapPoints.get(i).getName());
            tMapMarkerItem.setCalloutSubTitle("서울");
            tMapMarkerItem.setCanShowCallout(true);
            tMapMarkerItem.setAutoCalloutVisible(true);

            Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);

            tMapMarkerItem.setCalloutRightButtonImage(bitmap1);

            String strId = String.format("pmarker%d", markerId++);

            tmapview.addMarkerItem(strId, tMapMarkerItem);
            arrayMarkerId.add(strId);
        }
    }
}