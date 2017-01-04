package ecogps.eg.com.ecogps;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EcoGpsMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int CAMERA_RESULT = 103;
    private final static int READ_EXTERNAL_RESULT = 104;
    private final static int WRITE_EXTERNAL_RESULT = 105;
    private final static int ACCESS_NETWORK_RESULT = 106;
    private final static int ACCESS_NETWORK_STATE_RESULT = 107;
    private final static int ALL_PERMISSIONS_RESULT = 108;
    private Location mLastLocation;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;

    ArrayList<String> permissions = new ArrayList<String>();

    private GoogleMap mMap;


    LatLng start, waypoint, end;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gps_map);

        checkPermission();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            createLocationRequest();
        }

    }

    public void checkPermission() {
        permissions.clear();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);

        permissions = getPermissionRequested(permissions);

        requestPermissions(permissions, ALL_PERMISSIONS_RESULT);
    }

    public ArrayList<String> getPermissionRequested(ArrayList<String> perms) {
        ArrayList<String> permissions = new ArrayList<String>();

        for (String perm : perms
                ) {
            if (!hasPermission(perm))
                permissions.add(perm);
        }

        return permissions;

    }

    private boolean hasPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermissions(ArrayList<String> permissions, int resultCode) {
        if (permissions == null)
            return;
        if (permissions.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), resultCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> rejectedPermissions = new ArrayList<String>();
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissions) {
                    if (!hasPermission(perm))
                        rejectedPermissions.add(perm);
                }

                if (rejectedPermissions.size() == 0) {

                }

                requestPermissions(rejectedPermissions, ALL_PERMISSIONS_RESULT);

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showToast("Need Permission");
            return;
        }
        mMap.setMyLocationEnabled(true);


//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        LatLng sydney = new LatLng(-37.35, -122.0); //
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//
//        //18.015365, -77.499382
//
//        Routing routing = new Routing.Builder()
//                .travelMode(Routing.TravelMode.WALKING)
//                .withListener(this)
//                .waypoints(start, waypoint, end)
//                .key(getString(R.string.google_maps_key))
//                .build();
//        routing.execute();

//
//        // Instantiates a new Polyline object and adds points to define a rectangle
//        PolylineOptions rectOptions = new PolylineOptions()
//                .add(new LatLng(-34, 151.0))
//                .add(new LatLng(-34.40, 151.0))
//                .add(new LatLng(-34.40, 151.3))
//                .add(new LatLng(-34, 151.3));
//
////                .add(new LatLng(37.35, -122.0))
////                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
////                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
////                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
////                .add(new LatLng(37.35, -122.0)); // Closes the polyline.
//
//        // Get back the mutable Polyline
//        Polyline polyline = mMap.addPolyline(rectOptions);
    }


    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(client,
                        builder.build());


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("TAG", "Location Changed: " + location.getLatitude() + " :: " + location.getLongitude());
        showToast("Location Changed: " + location.getLatitude() + " :: " + location.getLongitude());

        updateUI(location);

//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void updateUI(Location location) {
        showToast("UpdateUI : " + location.getLatitude() + " : : " + location.getLongitude());
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EcoGpsMap Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (client.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast("Need Permission StartLocationUpdates");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (client != null)
            client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (client != null)
            client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("TAG", "onConnected...");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showToast("Need FusedLocationApi Permissions");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

        if(mLastLocation!=null){
            showToast("LastLocation is : " + String.valueOf(mLastLocation.getLatitude()) + " :: " + String.valueOf(mLastLocation.getLongitude()));
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Turkey"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("TAG","onConnectionSuspended...");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("TAG","onConnectionFailed...");
    }
}
