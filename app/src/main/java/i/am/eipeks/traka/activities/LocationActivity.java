package i.am.eipeks.traka.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import i.am.eipeks.traka.R;


public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private FirebaseAuth auth;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    private TextView currentLocation;
    private Button locationUpdates, getLocation, displayOnMap, go;
    private TextInputLayout textInputLayout;
    private EditText nameOfPlace;

    private boolean requestingLocationUpdates = false;
    private LocationRequest locationRequest;
    private GoogleApiClient apiClient;
    private Location lastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private double latitude, longitude;

    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String NAME_OF_PLACE = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        auth = FirebaseAuth.getInstance();
//        Toast.makeText(this, auth.getCurrentUser().getDisplayName().concat("\n").concat(auth.getCurrentUser().getEmail()), Toast.LENGTH_SHORT).show();

        currentLocation = (TextView) findViewById(R.id.location_coordinates);

        locationUpdates = (Button) findViewById(R.id.start_location_updates);
        getLocation = (Button) findViewById(R.id.get_current_location);
        displayOnMap = (Button) findViewById(R.id.see_on_map);
        go = (Button) findViewById(R.id.go);

        textInputLayout = (TextInputLayout) findViewById(R.id.name_of_place_input_layout);
        textInputLayout.setHint(getResources().getString(R.string.name_of_place));

        nameOfPlace = (EditText) findViewById(R.id.name_of_place);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLocation();
            }
        });

        locationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePeriodicLocation();
            }
        });

        displayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayout.setVisibility(View.VISIBLE);
                nameOfPlace.setVisibility(View.VISIBLE);
                go.setVisibility(View.VISIBLE);
            }});
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameOfPlace.getText())){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Oops. Field is empty!");
                } else {
                    String nameOfPlaceText = nameOfPlace.getText().toString();
                    Intent mapsActivityIntent = new Intent(LocationActivity.this, MapsActivity.class);
                    mapsActivityIntent.putExtra(LATITUDE, latitude);
                    mapsActivityIntent.putExtra(LONGITUDE, longitude);
                    mapsActivityIntent.putExtra(NAME_OF_PLACE, nameOfPlaceText);
                    startActivity(mapsActivityIntent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (apiClient != null){
            apiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
        if (apiClient != null){
            apiClient.disconnect();
        }
        super.onStop();
    }

    private void togglePeriodicLocation() {
        if (!requestingLocationUpdates){
            locationUpdates.setText(String.format("%s", "Stop location updates"));
            requestingLocationUpdates = true;
            startLocationUpdates();
        } else {
            locationUpdates.setText(String.format("%s", "Start location updates"));
            requestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            currentLocation.setText(String.format("%s: %s\n%s: %s", "Latitude", latitude, "Longitude", longitude));
        } else {
            currentLocation.setText(String.format("%s", "Couldn't get Location. \nMake sure location is enabled on the device."));
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private boolean checkPlayServices() {
        int requestCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (requestCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(requestCode)) {
                GooglePlayServicesUtil.getErrorDialog(requestCode, this, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toast.makeText(this, "Device not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (checkPlayServices()){
                buildGoogleApiClient();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
        if (requestingLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        displayLocation();
    }
}
