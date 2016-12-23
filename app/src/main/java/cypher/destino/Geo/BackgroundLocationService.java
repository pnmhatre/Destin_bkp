package cypher.destino.Geo;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import cypher.destino.Adapters.NotYetConfig;
import cypher.destino.Storage.Helperss;
import cypher.destino.Storage.SqlObject;

/**
 * Created by karhack on 24/11/16.
 */
public class BackgroundLocationService extends Service implements ResultCallback<Status>, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private static final String TAG = "BackgroundLocationService";
    private GoogleApiClient googleApiClient;
    private PendingIntent mPendingIntent;
    private ArrayList<Geofence> arrayList;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
            Log.i(TAG, "Service GoogleClient Connected");
        } catch (Exception e) {
            Log.e(TAG, "OnCreate Err : " + e.getLocalizedMessage());
        }

    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(NotYetConfig.CameraUpdateInterval);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        arrayList = new ArrayList<>();
        Helperss helperss = new Helperss(this);
        ArrayList<SqlObject> dataArrayList = helperss.getAllRecords();
        for (int i = 0 ; i < dataArrayList.size() ; i++) {
            arrayList.add(new Geofence.Builder().setRequestId(dataArrayList.get(i).name).setCircularRegion(Double.valueOf(dataArrayList.get(i).latitude), Double.valueOf(dataArrayList.get(i).longitude), Integer.valueOf(dataArrayList.get(i).radius)).setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(3000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).build());
        Log.e(TAG, "Geofence "+ i + " added");
        }

        if (arrayList.size()<1){
            Log.e(TAG, "No geoData");
            return;
        }
            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder().addGeofences(arrayList).build();
            PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences(googleApiClient,geofencingRequest, createRequestPendingIntent());
            pendingResult.setResultCallback(this);
    }catch (Exception e){
            Log.e(TAG, "onConnected err : "+e.getLocalizedMessage());
        }}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
        Log.i(TAG, "Service GoogleClient Disconnected");
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.v(TAG, "Success!");
        } else if (status.hasResolution()) {
            // TODO Handle resolution
        } else if (status.isCanceled()) {
            Log.v(TAG, "Canceled");
        } else if (status.isInterrupted()) {
            Log.v(TAG, "Interrupted");
        }
    }
    public PendingIntent createRequestPendingIntent() {
        if (mPendingIntent == null) {
            Intent intent = new Intent(this, GeofenceIntentService.class);
            mPendingIntent = PendingIntent.getService(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mPendingIntent;
    }

}
