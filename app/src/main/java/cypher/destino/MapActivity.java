package cypher.destino;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cypher.destino.Adapters.NotYetConfig;
import cypher.destino.Geo.BackgroundLocationService;
import cypher.destino.Storage.Helperss;
import cypher.destino.Storage.SqlObject;

/**
 * Created by karhack on 30/11/16.
 */
public class MapActivity extends FragmentActivity implements ResultCallback<Status>,
        LocationListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "MapActivity";
    private static final int GPS_PERMISSION_CODE = 101;
    private GoogleApiClient googleApiClient;
    private GoogleMap mgoogleMap;
    private FrameLayout frameLayout;
    private cypher.destino.GeofenceStore geofenceStore;
    private ArrayList<Geofence> mGeofencesArray = new ArrayList<>();
    private Circle circle;
    private Marker marker;
    private Marker markerHold;
    public static SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private PendingIntent mPendingIntent;
    private boolean isFirstZoom = true;
    FloatingActionButton fabMapAct;
    private static BottomSheetDialog bottomSheetDialog;
    private ImageView imgAdd;
    private EditText bmName;
    private LinearLayout footerLayout;
    private TextView tvDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        try {

            sharedPrefs = getSharedPreferences(NotYetConfig.sharedPrefs, MODE_PRIVATE);
            editor = sharedPrefs.edit();
            frameLayout = (FrameLayout) findViewById(R.id.container_frame);
            if (gpsAllowed()) {
                mainProcess();
            } else {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION_CODE);
            }
        } catch (Exception e) {
            Log.e(TAG, "OnCreate Err : " + e.getLocalizedMessage());
        }
    }

    public boolean gpsAllowed() {
        int result = ContextCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        if (gpsAllowed()) {
            googleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(NotYetConfig.CameraUpdateInterval);
            locationRequest.setFastestInterval(NotYetConfig.CameraUpdateInterval_Fastest);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MapActivity.this);
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException " + e.getLocalizedMessage());
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

            if (googleMap != null) {
                setCircles(googleMap);
                this.mgoogleMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setBuildingsEnabled(true);
                googleMap.setPadding(0, 200, 200, 0);
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if (googleMap != null) {
                            googleMap.clear();
                            markerHold = googleMap.addMarker(new MarkerOptions().position(latLng));
                            setFence(latLng.latitude, latLng.longitude, true);
                        }
                    }
                });
            }


        } catch (Exception e) {
            Log.e(TAG, "OnMapReady Err: " + e.getLocalizedMessage());
        }
    }


    String radius = null;
    String action = null;
    public void setFence(final Double lat, final Double lon, boolean show) {
        try {
            final String[] strings = getAddress(lat, lon);
            if (strings[0] == null) {
                strings[0] = "Unknown place";
            }

            try {
                final View view1 = View.inflate(MapActivity.this, R.layout.bottomsheet_add_geo, null);
                bmName = (EditText) view1.findViewById(R.id.edtext_title);
                tvDistance = (TextView) view1.findViewById(R.id.tv_bm_distance);
                final TextView bmAddress = (TextView) view1.findViewById(R.id.tv_bm_address);
                footerLayout = (LinearLayout) view1.findViewById(R.id.footer_layout);
                Button btnAdd = (Button) view1.findViewById(R.id.btn_bm_add);
                imgAdd = (ImageView) view1.findViewById(R.id.fabAdd);

                Location location = new Location("a1");
                location.setLatitude(lat);
                location.setLongitude(lon);

                Location location2 = new Location("a2");
                location2.setLatitude(Double.valueOf(sharedPrefs.getString(NotYetConfig.sharedLat,null)));
                location2.setLongitude(Double.valueOf(sharedPrefs.getString(NotYetConfig.sharedLon,null)));

                Float distance = location.distanceTo(location2)/1000;
                tvDistance.setText(String.valueOf(distance).substring(0,5)+"km away");


                final AppCompatRadioButton checkBoxNotify = (AppCompatRadioButton) view1.findViewById(R.id.checkbox_notify);
                final AppCompatRadioButton checkBoxChangeProfile = (AppCompatRadioButton) view1.findViewById(R.id.checkbox_change_profile);


                final ImageButton btnWalk = (ImageButton) view1.findViewById(R.id.btnWalk);
                final ImageButton btnCar = (ImageButton) view1.findViewById(R.id.btnCar);
                final ImageButton btnTrain = (ImageButton) view1.findViewById(R.id.btnTrain);

                btnWalk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radius = NotYetConfig.radiusWalk;
                        btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one_pressed));
                        btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                        btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));
                    }
                });
                btnCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radius = NotYetConfig.radiusCar;
                        btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                        btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two_pressed));
                        btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));
                    }
                });
                btnTrain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radius = NotYetConfig.radiusTrain;
                        btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                        btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                        btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three_pressed));
                    }
                });


                bottomSheetDialog = new BottomSheetDialog(MapActivity.this);
                bottomSheetDialog.setContentView(view1);

                if (show) {
                    bottomSheetDialog.show();
                    bmName.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                    imgAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view1.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                            bmName.setVisibility(View.VISIBLE);
                            footerLayout.setVisibility(View.VISIBLE);
                            imgAdd.setVisibility(View.GONE);
                        }
                    });

                    bmAddress.setText(strings[0] + ", " + strings[1]);
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                if (bmName.getText().toString().length()<3){
                                    Toast.makeText(MapActivity.this, "Please enter a unique title for this location", Toast.LENGTH_SHORT).show();
                                    bmName.setError("Enter a unique title");
                                    return;
                                }

                                if (radius == null) {
                                    Toast.makeText(MapActivity.this, "Please select the mode of travelling", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (checkBoxNotify.isChecked()) {
                                    action = NotYetConfig.actionNotify;
                                } else if (checkBoxChangeProfile.isChecked()) {
                                    action = NotYetConfig.actionChangeProfile;
                                }

                                if (action == null){
                                    Toast.makeText(MapActivity.this, "Please select an action to trigger after entering the geofenced area", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                SqlObject sqlObject = new SqlObject();

                                sqlObject.name = bmName.getText().toString();
                                sqlObject.address = bmAddress.getText().toString();
                                sqlObject.latitude = String.valueOf(lat).substring(0, 9);
                                sqlObject.longitude = String.valueOf(lon).substring(0, 9);
                                sqlObject.radius = String.valueOf(radius);
                                sqlObject.latitudeCurrent = sharedPrefs.getString(NotYetConfig.sharedLat, null);
                                sqlObject.longitudeCurrent = sharedPrefs.getString(NotYetConfig.sharedLon, null);
                                sqlObject.timestamp = String.valueOf(System.currentTimeMillis()/1000);
                                sqlObject.status = "ON";
                                sqlObject.action = action;

                                Helperss helperss = new Helperss(MapActivity.this);
                                helperss.addRow(sqlObject);

                                Toast.makeText(MapActivity.this, "GeoFence Added", Toast.LENGTH_SHORT).show();
                                if (mgoogleMap != null) {
                                    mgoogleMap.clear();
                                    setCircles(mgoogleMap);
                                }
                                bottomSheetDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (Exception e) {
                Log.e(TAG, "OnClick Err : " + e.getLocalizedMessage());
                e.printStackTrace();
            }


        } catch (Exception e) {
            Log.e(TAG, "Adding circle err : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == GPS_PERMISSION_CODE) {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar snackbar = Snackbar.make(frameLayout, "Permission Granted", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    mainProcess();
                    googleApiClient.connect();

                } else {
                    finish();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "OnPermission Results Err : " + e.getLocalizedMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            editor.putString(NotYetConfig.sharedLat, String.valueOf(location.getLatitude()));
            editor.putString(NotYetConfig.sharedLon, String.valueOf(location.getLongitude()));
            editor.commit();
            if (isFirstZoom) {
                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                isFirstZoom = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        } else {

        }
    }

    /*public PendingIntent createRequestPendingIntent() {
        if (mPendingIntent == null) {
            Log.v(TAG, "Creating PendingIntent");
            Intent intent = new Intent(MapActivity.this, GeofenceIntentService.class);
            mPendingIntent = PendingIntent.getService(MapActivity.this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mPendingIntent;
    }
    */
    public void mainProcess() {

        startService(new Intent(MapActivity.this, BackgroundLocationService.class));

        fabMapAct = (FloatingActionButton) findViewById(R.id.fabMapAct);
        fabMapAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LatLng latLng = new LatLng(Double.valueOf(sharedPrefs.getString(NotYetConfig.sharedLat,null)),Double.valueOf(sharedPrefs.getString(NotYetConfig.sharedLon,null)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,14);
                    mgoogleMap.animateCamera(cameraUpdate);

                } catch (Exception e) {
                    Log.e(TAG, "stopService " + e.getLocalizedMessage());
                }
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        EditText editText = (EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        editText.setTextColor(Color.WHITE);
        ImageView imgSearch = (ImageView) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button);
        imgSearch.setImageResource(R.mipmap.ic_search);

        editText.setHint("Search address or hold map");
        editText.setPadding(0,0,0,0);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setHintTextColor(Color.WHITE);
        editText.setTextSize(14f);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14);
                mgoogleMap.animateCamera(cameraUpdate);
                marker = mgoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).draggable(true));
                setFence(place.getLatLng().latitude, place.getLatLng().longitude, true);
                mgoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        setFence(place.getLatLng().latitude, place.getLatLng().longitude, false);
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        setFence(place.getLatLng().latitude, place.getLatLng().longitude, false);
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        setFence(marker.getPosition().latitude, marker.getPosition().longitude, true);
                    }
                });
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String[] getAddress(Double lati, Double longi) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lati, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String stateCountry = addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName(); // Only if available else return NULL

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addresses.get(0).getFeatureName() + " isFeatureName,,\n").append(addresses.get(0).getLocality() + " isLocality,,\n")
                .append(addresses.get(0).getPremises() + " isPremises,,\n")
                .append(addresses.get(0).getSubAdminArea() + " isSubAdminArea,,\n")
                .append(addresses.get(0).getSubThoroughfare() + " isSubThroughFare,,\n")
                .append(addresses.get(0).getSubLocality() + " isSubLocality,,\n")
                .append(addresses.get(0).getUrl() + " isUrl,,\n");
        String[] strings = {address, stateCountry};
        return strings;
    }

    public void setCircles(GoogleMap googleMap) {
        try {
            Helperss helperss = new Helperss(MapActivity.this);
            ArrayList<SqlObject> arrayList = helperss.getAllRecords();
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).status.equalsIgnoreCase("ON")) {
                    LatLng latLng = new LatLng(Double.valueOf(arrayList.get(i).latitude), Double.valueOf(arrayList.get(i).longitude));
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(latLng);
                    circleOptions.radius(Double.valueOf(arrayList.get(i).radius));
                    circleOptions.strokeWidth(5);
                    circleOptions.strokeColor(R.color.colorAccent);
                    googleMap.addCircle(circleOptions);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "setCirclessetCircles err : " + e.getLocalizedMessage());
        }
    }

}
