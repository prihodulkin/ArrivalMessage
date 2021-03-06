package com.example.arrivalmessage;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.core.app.ActivityCompat;
import com.example.arrivalmessage.VK_Module.VKUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;
import java.lang.Object;


public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private double latitude=MainActivity.defLatitude;
    private double longitude=MainActivity.defLongitude;
    private String location;
    private Marker SelectedPlaceMarker;
    private GoogleMap gmap;


    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.curData.longitude!=null)
            longitude=MainActivity.curData.longitude;
        if(MainActivity.curData.latitude!=null)
            latitude=MainActivity.curData.latitude;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Bundle arguments = getIntent().getExtras();
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
                SelectedPlaceMarker =gmap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                final Geocoder geocoder = new Geocoder(SelectLocationActivity.this, new Locale("ru", "RU"));

                Address address;
                try {
                    address = geocoder.getFromLocation(latitude, longitude, 1).get(0);

                    final String street = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                    final String house = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";
                    final String city = address.getLocality() != null ? address.getLocality() : "";

                    MainActivity.curData.location = street + " " + house + ", " + city;
                    if(MainActivity.reserveData!=null&&SavedNotificationsActivity.texts!=null&&SavedNotificationsActivity.texts.containsKey( MainActivity.curData))
                        SavedNotificationsActivity.texts.get(MainActivity.curData).setText(street + " " + house + ", " + city);

                    if (MainActivity.curData.location.equals(" , ")) {
                        MainActivity.curData.location = "Unknown location";
                        if(MainActivity.reserveData!=null&&SavedNotificationsActivity.texts!=null&&SavedNotificationsActivity.texts.containsKey( MainActivity.curData))
                            SavedNotificationsActivity.texts.get(MainActivity.curData).setText(street + " " + house + ", " + city);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
                        MainActivity.curData.longitude=longitude;
                        MainActivity.curData.latitude=latitude;
                        SelectLocationActivity.super.finish();
                    }
                }
        );
        Button next_btn = findViewById(R.id.next_btn);

        final Context context = this;
        next_btn.bringToFront();
        next_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Geocoder geocoder = new Geocoder(context, new Locale("ru", "RU"));

                        Address address;
                        try {
                            address = geocoder.getFromLocation(latitude, longitude, 1).get(0);

                            final String street = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                            final String house = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";
                            final String city = address.getLocality() != null ? address.getLocality() : "";

                            location = street + " " + house + ", " + city;

                            if (location.equals(" , ")) {
                                location = "Unknown location";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MainActivity.curData.location=location;
                        MainActivity.curData.latitude=latitude;
                        MainActivity.curData.longitude=longitude;
                        Intent intent = new Intent(SelectLocationActivity.this, ChooseTextActivity.class);
                        FinishManager.addActivity(SelectLocationActivity.this);
                        startActivity(intent);
                    }
                }


        );


    }

    @Override
    public void onBackPressed() {
        MainActivity.curData.longitude=longitude;
        MainActivity.curData.latitude=latitude;
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();


    }

    @Override
    public void onMapReady(final GoogleMap map) {
        gmap=map;
        SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.marker)));
         BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.marker);

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        gmap.setMyLocationEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SelectedPlaceMarker.remove();
                SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                latitude = latLng.latitude;
                longitude=latLng.longitude;

                final Geocoder geocoder = new Geocoder(SelectLocationActivity.this, new Locale("ru", "RU"));

                Address address;
                try {
                    address = geocoder.getFromLocation(latitude, longitude, 1).get(0);

                    final String street = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                    final String house = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";
                    final String city = address.getLocality() != null ? address.getLocality() : "";



                    MainActivity.curData.location = street + " " + house + ", " + city;
                    if(MainActivity.reserveData!=null&&SavedNotificationsActivity.texts!=null&&SavedNotificationsActivity.texts.containsKey( MainActivity.curData))
                        SavedNotificationsActivity.texts.get(MainActivity.curData).setText(street + " " + house + ", " + city);

                    if (MainActivity.curData.location.equals(" , ")) {
                        MainActivity.curData.location = "Unknown location";
                        if(MainActivity.reserveData!=null&&SavedNotificationsActivity.texts!=null&&SavedNotificationsActivity.texts.containsKey( MainActivity.curData))
                            SavedNotificationsActivity.texts.get(MainActivity.curData).setText(street + " " + house + ", " + city);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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


