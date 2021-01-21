package uk.ac.ncl.team2.qrquest;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private final LatLng[] pickedLoc = new LatLng[1];
    private Button save;
    private Button cancel;

    public static final String LAT_CODE = "uk.ac.ncl.team2.qrquest.Lat";
    public static final String LNG_CODE = "uk.ac.ncl.team2.qrquest.Lng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        /* Getting a reference to the google map */
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* zooming the map to Newcastle University */
        LatLng NClUniversity = new LatLng(54.979114,-1.615023);
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(NClUniversity, 16);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);

        /* detecting any user marker that point a location on the map */
        /*final LatLng[] pickedLoc = new LatLng[1]; */   // the default solution by eclipse
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                /* Creating a marker and associate it with given latitude Longitude  coordinates */
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                    // Use default InfoWindow frame
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    // Defines the contents of the InfoWindow
                    @Override
                    public View getInfoContents(Marker arg0) {

                        /* Getting view from the layout file 'info_window_layout' */
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        LatLng latLng = arg0.getPosition();
                        TextView title = (TextView) v.findViewById(R.id.title);
                        TextView snippet = (TextView) v.findViewById(R.id.snippet);

                        // Extracting an address of latLng
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses;
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (addresses != null) {
                                Address returnedAddress = addresses.get(0);
                                StringBuilder strReturnedAddress = new StringBuilder("Address: ");
                                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                                }
                                title.setText(strReturnedAddress.toString());  // need correction
                                snippet.setText(strReturnedAddress.toString());
                            } else {
                                snippet.setText("No Address returned!");
                            }
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        return v;
                    }
                });

                /* Setting the title for the marker */
                mMap.clear(); // Clears the previously touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); // Animating to the touched position
                mMap.addMarker(marker); // Placing a marker on the touched position
                pickedLoc[0] = latLng;

                // Display maker information
                TextView text = (TextView) findViewById(R.id.locationLatlng);
                text.setText("Latitude:" + latLng.latitude + "  " + "Longitude:" + latLng.longitude);
            }});


        save = (Button)findViewById(R.id.save);
        save.setText("Save");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(LAT_CODE, pickedLoc[0].latitude);
                intent.putExtra(LNG_CODE, pickedLoc[0].longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setText("Cancel");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
