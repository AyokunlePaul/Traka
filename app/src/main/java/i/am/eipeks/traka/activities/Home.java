package i.am.eipeks.traka.activities;

import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import i.am.eipeks.traka.R;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationManager;
    private LocationProvider provider;
    private Button getLocation;
//    private StringBuilder stringProviders = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        List<String> providers = locationManager.getProviders(true);
//
//        for (String provider: providers){
//            stringProviders.append(provider.toUpperCase().concat("\n"));
//        }
//
//        Toast.makeText(this, stringProviders.toString(), Toast.LENGTH_SHORT).show();

        getLocation = (Button) findViewById(R.id.get_location);
        getLocation.setText(String.format("%s", "Get Provider"));
//        getLocation.setOnClickListener(this);
        LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        String information = String.format("%s: %s", "Power Requirement", gpsProvider.getPowerRequirement())
                + "\n" + String.format("%s: %s","Requires Satellite", gpsProvider.requiresSatellite())
                + "\n" + String.format("%s: %s", "Requires Cell", gpsProvider.requiresCell())
                + "\n" + String.format("%s: %s", "Accuracy", gpsProvider.getAccuracy())
                + "\n" + String.format("%s: %s", "Requires Network", gpsProvider.requiresNetwork())
                + "\n" + String.format("%s: %s", "Altitude Support", gpsProvider.supportsAltitude())
                + "\n" + String.format("%s: %s", "Bearing Support", gpsProvider.supportsBearing())
                + "\n" + String.format("%s: %s", "Bearing Support", gpsProvider.supportsSpeed());
        Toast.makeText(this, information, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.get_location:
                Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCriteria.setCostAllowed(true);
                locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
                locationCriteria.setAltitudeRequired(false);
                locationCriteria.setBearingRequired(false);
                locationCriteria.setSpeedRequired(false);
                locationCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                locationCriteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
                locationCriteria.setSpeedAccuracy(Criteria.ACCURACY_LOW);
                locationCriteria.setBearingAccuracy(Criteria.ACCURACY_LOW);

                String bestProvider = locationManager.getBestProvider(locationCriteria, false);

                Toast.makeText(this, bestProvider, Toast.LENGTH_SHORT).show();

                getLocation.setText(String.format("%s", "Get Current LocationActivity"));
                break;
        }
    }
}
