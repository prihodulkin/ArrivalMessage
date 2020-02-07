package com.example.arrivalmessage;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import android.location.Geocoder;
import android.location.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import java.io.IOException;
import java.util.*;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {



    private MapView mMapView;
    private double latitude;
    private double longitude;
    private  LatLng latLng;
    private Marker SelectedPlaceMarker;
    private String address;
    private LocationManager manager;
    private GoogleMap gmap;
    private LocationManager locationManager;
    public static boolean geolocationEnabled = false;


    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            ActivityCompat.requestPermissions(SelectLocationActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},1
            );
        }


    };




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

        manager=(LocationManager) getSystemService(this.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

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
                // TODO: Get info about the selected place.

                SelectedPlaceMarker.remove();
                latitude =place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;
                SelectedPlaceMarker =gmap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address));
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, listener);
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                listener);
        if(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null) {
            latitude = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            longitude = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
//            SelectedPlaceMarker.remove();
//            SelectedPlaceMarker =gmap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address));
        }

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        gmap=map;
        SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address
        ));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16f));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SelectedPlaceMarker.remove();
                SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(address));
                latitude = latLng.latitude;
                longitude=latLng.longitude;
                //
                // gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16f));
            }
        });

    }




    private boolean checkLocationServiceEnabled() {
        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        try {
            geolocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return buildAlertMessageNoLocationService(geolocationEnabled);
    }

    /**
     *  Показываем диалог и переводим пользователя к настройкам геолокации
     */
    private boolean buildAlertMessageNoLocationService(boolean network_enabled) {
        String msg = !network_enabled ? getResources().getString(R.string.msg_switch_network) : null;

        if (msg != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setMessage(msg)
                    .setPositiveButton("Включить", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return false;
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


