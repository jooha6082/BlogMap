package com.juha.blogmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.juha.blogmap.databinding.ActivityMainBinding;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    private TMapView tmapview;
    ActivityMainBinding binding;
    LocationManager locationManager;
    SearchBlog searchBlog;
    HashMap<String, Integer> data = new HashMap<>();
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
    private void findRestaurants() {
        TMapData tMapData = new TMapData();
        TMapPoint tMapPoint = tmapview.getCenterPoint();
        final HashMap<String, TMapPOIItem> searchList = new LinkedHashMap<>();
        tMapData.findAroundNamePOI(tMapPoint, "식음료", 1, 99, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    TMapPOIItem tMapPOIItem = arrayList.get(i);
                    if (!searchList.containsKey(tMapPOIItem.getPOIID())){
                        searchList.put(tMapPOIItem.getPOIID(), tMapPOIItem);
                    }
//                    Log.d("식음료", "POI Name: " + tMapPOIItem.getPOIID() + "  " + tMapPOIItem.getPOIName());
                }
//                Log.d("===============", "=============================================================");
                for (Map.Entry<String, TMapPOIItem> entry: searchList.entrySet()){
                    Log.d("식음료", entry.getKey() + " " + entry.getValue());
                    searchBlog = new SearchBlog(entry.getValue());
                    searchBlog.execute();

                }
            }
        });
    }

    private class SearchBlog extends AsyncTask<Void, Void, Integer> {

        String TAG = "SearchBlogT";
        private TMapPOIItem query;

        public SearchBlog(TMapPOIItem place) {
            this.query = place;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int result = 0;
            try {
                String url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=" + query.getPOIName();

                Document doc = Jsoup.connect(url).get();
                Elements contents = doc.select("div.blog.section._blogBase._prs_blg > div > span");
                if (!contents.isEmpty()){
                    String[] texts = contents.text().split(" / ");
                    Log.i(TAG, texts[0] + " " + texts[1]);
                    String num = texts[1].replace("건", "");
                    result = Integer.parseInt(num.replace(",", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //returns the number of blogs in Naver
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.i(TAG, String.valueOf(integer));

//            TODO: put markers on the map
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

            tMapMarkerItem.setTMapPoint(query.getPOIPoint());
            tMapMarkerItem.setName(query.getPOIName());
            tMapMarkerItem.setVisible(TMapMarkerItem.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.round_room_black_24dp);
            tMapMarkerItem.setIcon(bitmap);
            tMapMarkerItem.setPosition(0.5f, 1.0f);

            tmapview.addMarkerItem(query.getPOIID(), tMapMarkerItem);

        }
    }

}