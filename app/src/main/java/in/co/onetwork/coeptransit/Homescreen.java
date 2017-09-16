package in.co.onetwork.coeptransit;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import in.co.onetwork.coeptransit.POJO.Example;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Homescreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    Double latitude,longitude;
    LatLng origin;
    LatLng dest;
    ArrayList<LatLng> MarkerPoints;
    TextView ShowDistanceDuration;
    Polyline line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gps=new GPSTracker(this);
        ShowDistanceDuration=(TextView)findViewById(R.id.textid);
    }
//Abhi says no

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (gps.canGetLocation()){
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
            Toast.makeText(this, "Location:"+latitude+" "+longitude, Toast.LENGTH_SHORT).show();
            origin =new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(origin).title("my"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
        }else{
            gps.showSettingsAlert();
        }
    }

    public void locate(View v) {
        if (gps.canGetLocation()){
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
            Toast.makeText(this, "Location:"+latitude+" "+longitude, Toast.LENGTH_SHORT).show();
            origin =new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(origin).title("my"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
        }else{
            gps.showSettingsAlert();
        }
    }
    public void drive(View v){
        dest =new LatLng(18.5294,73.8566);
        build_retrofit_and_get_response("driving");
    }
    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, type);
        Toast.makeText(Homescreen.this, "in method", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        Toast.makeText(Homescreen.this, "this", Toast.LENGTH_SHORT).show();
                        ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(20)
                                .color(Color.RED)
                                .geodesic(true)
                        );
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(Homescreen.this, " "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
