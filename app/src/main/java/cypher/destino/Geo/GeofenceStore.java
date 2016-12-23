package cypher.destino;

public class GeofenceStore{}

/*
public class GeofenceStore implements ConnectionCallbacks,
        OnConnectionFailedListener, ResultCallback<Status>, LocationListener {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private ArrayList<Geofence> mGeofences;
    private GeofencingRequest mGeofencingRequest;
    private LocationRequest mLocationRequest;
  public GeofenceStore(Context context, ArrayList<Geofence> geofences) {

        mContext = context;
        mGeofences = new ArrayList<Geofence>(geofences);
        mPendingIntent = null;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(NotYetConfig.CameraUpdateInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(Status result) {
        if (result.isSuccess()) {
            Log.v(TAG, "Success!");
        } else if (result.hasResolution()) {
            // TODO Handle resolution
        } else if (result.isCanceled()) {
            Log.v(TAG, "Canceled");
        } else if (result.isInterrupted()) {
            Log.v(TAG, "Interrupted");
        } else {

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "Connection failed.");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We're connected, now we need to create a GeofencingRequest with the geofences we have stored.


        mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(mGeofences).build();

        mPendingIntent = createRequestPendingIntent();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // Submitting the request to monitor geofences.
        PendingResult<Status> pendingResult = LocationServices.GeofencingApi
                .addGeofences(mGoogleApiClient, mGeofencingRequest,
                        mPendingIntent);

        // Set the result callbacks listener to this class.
        pendingResult.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.v(TAG, "Connection suspended.");
    }

    private PendingIntent createRequestPendingIntent() {
        if (mPendingIntent == null) {
            Log.v(TAG, "Creating PendingIntent");
            Intent intent = new Intent(mContext, GeofenceIntentService.class);
            mPendingIntent = PendingIntent.getService(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return mPendingIntent;
    }

    @Override
    public void onLocationChanged(Location location) {
//print data
    }

*/