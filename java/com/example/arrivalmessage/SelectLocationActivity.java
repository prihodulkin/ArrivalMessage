package com.example.arrivalmessage;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.maps.model.Marker;
import java.util.*;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;


public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private double latitude=47.216724;
    private double longitude=39.628510;
    private Marker SelectedPlaceMarker;
    private GoogleMap gmap;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        addListenerOnButton();
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},1
                );


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SelectLocationActivity.this);
            builder.setTitle("Важное сообщение!")
                    .setMessage("Необходим доступ к местоположению!")
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return;

        }



        if(!Places.isInitialized())
        {
            Places.initialize(this,getString(R.string.api_key));
        }
        Places.createClient(this);
        setAutocompleteSupportFragment();

    }



    private  void setAutocompleteSupportFragment()
    {
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                SelectedPlaceMarker.remove();
                latitude =place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;
                SelectedPlaceMarker =gmap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16f));


            }

            @Override
            public void onError(Status status) {
                Log.i( "PlaceApi","An error occurred: " + status.getStatusCode () );
            }
        });
    }




    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectLocationActivity.this, SelectUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
        ImageButton next_btn = findViewById(R.id.next_btn);
        next_btn.bringToFront();
        next_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectLocationActivity.this, ChooseTextActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }


        );


    }





    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();


    }

    @Override
    public void onMapReady(final GoogleMap map) {
        gmap=map;
        SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        gmap.setMyLocationEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SelectedPlaceMarker.remove();
                SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));
                latitude = latLng.latitude;
                longitude=latLng.longitude;
            }
        });

    }

















    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}


