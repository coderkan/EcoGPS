package ecogps.eg.com.ecogps;

import android.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class EcoGpsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static int CAMERA_RESULT = 103;
    private final static int READ_EXTERNAL_RESULT = 104;
    private final static int WRITE_EXTERNAL_RESULT = 105;
    private final static int ACCESS_NETWORK_RESULT = 106;
    private final static int ACCESS_NETWORK_STATE_RESULT = 107;
    private final static int ALL_PERMISSIONS_RESULT = 108;


    ArrayList<String> permissions =  new ArrayList<String>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gps_map);

        checkPermission();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void checkPermission(){
        permissions.clear();
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(android.Manifest.permission.INTERNET);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_WIFI_STATE);

        permissions = getPermissionRequested(permissions);

        requestPermissions(permissions,ALL_PERMISSIONS_RESULT);
    }

    public ArrayList<String> getPermissionRequested(ArrayList<String> perms){
        ArrayList<String> permissions = new ArrayList<String>();

        for (String perm : perms
                ) {
            if(!hasPermission(perm))
                permissions.add(perm);
        }

        return permissions;

    }

    private boolean hasPermission(String permission){
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermissions(ArrayList<String> permissions,int resultCode){
        if(permissions == null)
            return;
        if(permissions.size() > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions.toArray(new String[permissions.size()]),resultCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        ArrayList<String> rejectedPermissions = new ArrayList<String>();
        switch (requestCode)
        {
            case ALL_PERMISSIONS_RESULT:
                for (String perm: permissions){
                    if(!hasPermission(perm))
                        rejectedPermissions.add(perm);
                }

                if(rejectedPermissions.size() == 0){

                }

                requestPermissions(rejectedPermissions,ALL_PERMISSIONS_RESULT);

                break;

            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
