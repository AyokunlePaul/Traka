package i.am.eipeks.traka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private double latitude, longitude;
    private String locationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_maps);

        latitude = getIntent().getDoubleExtra(LocationActivity.LATITUDE, 0.0);
        longitude = getIntent().getDoubleExtra(LocationActivity.LONGITUDE, 0.0);
        locationName = getIntent().getStringExtra(LocationActivity.NAME_OF_PLACE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng NACOSS_OAU = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(NACOSS_OAU).title(locationName));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setTrafficEnabled(true);
//        map.
        map.moveCamera(CameraUpdateFactory.newLatLng(NACOSS_OAU));
    }
}
