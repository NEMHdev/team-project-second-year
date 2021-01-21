package uk.ac.ncl.team2.qrquest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

// done with a help of a tutorial

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Getting google map fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gps_map_fragment);
        mapFragment.getMapAsync(this);

        Button scanCodeBtn = (Button)findViewById(R.id.scanCode);
        scanCodeBtn.setText("Scan Code");
        scanCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // QR code
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setText("Cancel");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
                finish();
            }
        });

    }

    /**
     * this method checks the version of SDK tools the application use and request permission check based on that
     * if permissions are granted, it builds a google api client object to start user navigation (GPS)
     * @param googleMap a GoogleMap object representing loaded map in this activity
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* checking if the SDK tools version has got at least Jelly Bean MR2 SDK tools
         (the minimum SDK tools for a platform supported by this application)
         (permissions are granted at installation time, they are always true) */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            /* if the SDK tools version is Marshmallow or above, check run time permissions */
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //Toast.makeText(this, "permission check", Toast.LENGTH_LONG).show(); // for testing
                checkLocationPermission();
            }
            if (mGoogleApiClient == null)
                buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        /* Zoom the map to Newcastle University if google play services was not initialized */
        // this require testing on an android platform which is lower than black beans mr2
        if(mGoogleApiClient == null){
            LatLng NClUniversity = new LatLng(54.979114,-1.615023);
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(NClUniversity, 16);
            mMap.moveCamera(cameraPosition);
            mMap.animateCamera(cameraPosition);
            Toast.makeText(this, "Failed to establish connection", Toast.LENGTH_LONG).show(); // for testing
        }
    }

    /**
     * this method request a connection to Google Location Service by creating a client object
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * this method is suppose to perform some action whenever the connection to location service is suspended
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Suspended connection to Location Service ", Toast.LENGTH_LONG).show();
        // yet to be implemented in future updates
    }

    /**
     * this method is suppose to perform some action whenever the connection to location service has failed
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect Location Service ", Toast.LENGTH_LONG).show();
        // yet to be implemented in future updates
    }

    /**
     * this method update google map's camera based on the given location
     * @param location user current location
     */
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    /**
     * this method checks whether ACCESS_FINE_LOCATION permission has been granted and make a permission request
     * if it was not granted
     * @return a boolean value indicating whether ACCESS_FINE_LOCATION permission has been granted  or not
     */
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, QuestActivity.PERMISSION_ARRAY,
                        QuestActivity.LOCATION_PERMISSION_REQUEST_CODE);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * this callback method analyze requested permission results and perform actions based on that. If it was granted, it creates
     * a google api client, if it was null, and allows map to set user location
     * @param requestCode an integer value representing permission request code
     * @param permissions a list of string object permissions
     * @param grantResults an integer list indicating whether each permission of permission list has been granted or not
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       if(requestCode == QuestActivity.LOCATION_PERMISSION_REQUEST_CODE) {

           // check if the request is accepted and has required permissions
           if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && ContextCompat
                   .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               if (mGoogleApiClient == null)
                   buildGoogleApiClient();
               mMap.setMyLocationEnabled(true);

           } else {
               // Permission denied, Disable the functionality that depends on this permission.
               Toast.makeText(this, "Unfortunately, without location permission you can not use the map", Toast.LENGTH_LONG).show();
           }
           return;
       }
    }

    /**
     * this method stops location update service
     */
    private void stopLocationService(){
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
