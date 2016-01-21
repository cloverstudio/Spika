package com.clover_studio.spikachatmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.robospice.NetworkUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationActivity extends BaseActivity implements OnMapReadyCallback{

    GoogleMap googleMap;
    private LatLng activeLatLng = null;
    private boolean toShowLocation = false;

    /**
     * start activity for choose location
     * @param context
     */
    public static void startLocationActivity(Context context){
        Intent intent = new Intent(context, LocationActivity.class);
        ((Activity)context).startActivityForResult(intent, Const.RequestCode.LOCATION_CHOOSE);
    }

    /**
     * start activity for show location
     * @param context
     * @param lat latitude of location
     * @param lng longitude of location
     */
    public static void startShowLocationActivity(Context context, double lat, double lng){
        Intent intent = new Intent(context, LocationActivity.class);
        LatLng latLng = new LatLng(lat, lng);
        intent.putExtra(Const.Extras.LATLNG, latLng);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setToolbar(R.id.tToolbar, R.layout.custom_camera_preview_toolbar);
        setMenuLikeBack();
        setToolbarTitle(getString(R.string.location));

        findViewById(R.id.okButton).setOnClickListener(onOkClickedListener);
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra(Const.Extras.LATLNG)){
            toShowLocation = true;
            activeLatLng = getIntent().getParcelableExtra(Const.Extras.LATLNG);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();

                FrameLayout layoutForMap = (FrameLayout) findViewById(R.id.frameForMap);
                getSupportFragmentManager().beginTransaction().add(layoutForMap.getId(), mapFragment, "TAG").commit();

                mapFragment.getMapAsync(LocationActivity.this);
            }
        }, 600);

    }

    private View.OnClickListener onOkClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(activeLatLng == null){
                NotifyDialog.startInfo(getActivity(), getString(R.string.location_error_title), getString(R.string.location_error_select));
                return;
            }

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            String addressAll = getString(R.string.location);

            try {
                addresses = geocoder.getFromLocation(activeLatLng.latitude, activeLatLng.longitude, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                addressAll = "";
                addressAll = addToStringIfNotEmpty(addressAll, address);
                addressAll = addToStringIfNotEmpty(addressAll, city);
                addressAll = addToStringIfNotEmpty(addressAll, state);
                addressAll = addToStringIfNotEmpty(addressAll, country);

            } catch (IOException e) {
                e.printStackTrace();
            }

            sendMessage(addressAll, activeLatLng);

        }
    };

    private String addToStringIfNotEmpty(String data, String toAdd){
        if(!TextUtils.isEmpty(toAdd)){
            if(TextUtils.isEmpty(data)){
                data = toAdd;
            }else{
                data = data + ", " + toAdd;
            }
        }
        return data;
    }

    private void sendMessage(String address, LatLng latLng) {
        Intent intentData = new Intent();
        intentData.putExtra(Const.Extras.ADDRESS, address);
        intentData.putExtra(Const.Extras.LATLNG, latLng);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap = googleMap;
        if(toShowLocation){
            gotoOtherLocation();
            findViewById(R.id.buttonsAll).setVisibility(View.GONE);
        }else{
            gotoMyLocation();
            googleMap.setOnMapClickListener(onMapClickListener);
        }
    }

    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng));
            activeLatLng = latLng;
        }
    };

    /**
     * animate google map to my location and add marker to my location
     */
    private void gotoMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if(myLocation != null){
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            activeLatLng = latLng;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }

    }

    /**
     * animate google map to given location and add marker
     */
    private void gotoOtherLocation() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(activeLatLng, 15));
        googleMap.addMarker(new MarkerOptions().position(activeLatLng));
    }

}
