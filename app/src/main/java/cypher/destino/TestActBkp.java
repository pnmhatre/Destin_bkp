package cypher.destino;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import cypher.destino.Adapters.NotYetConfig;


/**
 * Created by karhack on 2/12/16.
 */
public class TestActBkp extends AppCompatActivity implements LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ResultCallback<Status>,
        OnMapReadyCallback{

    private static final String TAG = "DashBoard";
    private GoogleApiClient googleApiClient;
    public static GoogleMap mgoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {


            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_main);
            viewPager.setAdapter(new mViewpagerAdapter(getSupportFragmentManager()));


             googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "onCreate err : " + e.getLocalizedMessage());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient!=null){
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {

            googleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient!=null){
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(NotYetConfig.CameraUpdateInterval);
            locationRequest.setFastestInterval(NotYetConfig.CameraUpdateInterval_Fastest);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, TestActBkp.this);
        }catch (SecurityException e){
            Log.e(TAG, "SecurityException "+e.getLocalizedMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
                this.mgoogleMap = googleMap;
            if (googleMap!=null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.setPadding(0, 200, 200, 0);
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                    }
                });
            }


        } catch (Exception e) {
            Log.e(TAG, "OnMapReady Err: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

    }

    @Override
    public void onResult(@NonNull Status status) {

    }


    public class mViewpagerAdapter extends FragmentStatePagerAdapter {
        public mViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new FragmentCard();
                        break;
                    case 1:
                        fragment = new FragmentCard();
                        break;
                    case 2:
                        fragment = new FragmentCard();
                        break;
                    case 3:
                        fragment = new FragmentCard();
                        break;
                }
                return fragment;
            } catch (Exception e) {
                Log.e(TAG, "getItem Err : " + e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        public float getPageWidth(int position) {
            return .6f;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
