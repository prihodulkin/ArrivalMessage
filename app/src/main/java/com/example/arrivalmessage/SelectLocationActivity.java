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
import android.app.SearchManager;



public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {



    private MapView mMapView;
    private double latitude;
    private double longitude;
    private Marker SelectedPlaceMarker;
    private String address;


    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (location!=null) {
               latitude=location.getLatitude();
               longitude=location.getLongitude(); }
            else{


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
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
         //настраиваем листенер
        LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SelectLocationActivity.this);
            builder.setTitle("Важное сообщение!")
                    .setMessage("Ошибка!")
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
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

        latitude=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        longitude=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        }
        catch (IOException e)
        {

        }



        LinearLayout search_layout=(LinearLayout)findViewById(R.id.search_layout);
        search_layout.bringToFront();
        ListView listView=(ListView)findViewById(R.id.list);
        listView.bringToFront();


        //
        SearchView searchView=(SearchView)findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               // showResults(s);
                try {

                   Geocoder geocoder=new Geocoder(getApplicationContext());
                    List<Address> addresses = geocoder.getFromLocationName(s, 10);
                    String[] addresses_string = new String[addresses.size()];
                    for(int i=0;i<addresses.size();i++)
                    {
                        addresses_string[i]=addresses.get(i).getAddressLine(0);
                    }
                    ArrayAdapter<String> a=new ArrayAdapter<String>(getApplicationContext(),R.layout.record,addresses_string);
                    ListView list=(ListView)findViewById(R.id.list);
                    list.setAdapter(a);
                }
                catch (Exception e)
                {

                }

                return true;
            }
        });
    }

    public void showResults(String query)
    {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            addresses = geocoder.getFromLocationName(query, 10);



// используем адаптер данных
            String[] adresses_string = new String[]{"dsd","ds","ffdfd"};

/*
             for(int i=0;i<addresses.size();i++)
            {
                if(addresses.get(i)!=null)
                adresses_string[i]=addresses.get(i).getAddressLine(0);
            }

*/


            ArrayAdapter<String> a=new ArrayAdapter<String>(this,R.layout.record,adresses_string);
            ListView list=(ListView)findViewById(R.id.list);
            list.setAdapter(a);
        }
        catch (IOException e)
        {

        }

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
        map.setMinZoomPreference(12);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(12)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address
        ));




        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SelectedPlaceMarker.remove();
                SelectedPlaceMarker =map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(address));
                latitude = latLng.latitude;
                longitude=latLng.latitude;
            }
        });
        try {
            map.setMyLocationEnabled(true);
        }
        catch (SecurityException s)
        {


        }


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


