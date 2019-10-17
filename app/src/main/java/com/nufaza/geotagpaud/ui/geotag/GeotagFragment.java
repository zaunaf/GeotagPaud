package com.nufaza.geotagpaud.ui.geotag;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.snackbar.Snackbar;
import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.Geotag_Table;
import com.nufaza.geotagpaud.util.PermissionUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import static java.text.DateFormat.getTimeInstance;


public class GeotagFragment extends Fragment
    implements
        LocationListener,
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback
{

    private GeotagFragment me;
    private GeotagViewModel geotagViewModel;
    private View root;
    private MainActivity mainActivity;
    private LocationManager locationManager;
    private Location currentLocation;
    private int loadingCount;
    private static final long MIN_TIME = 500;
    private static final float MIN_DISTANCE = 0;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        me = this;
        geotagViewModel = ViewModelProviders.of(this).get(GeotagViewModel.class);
        root = inflater.inflate(R.layout.fragment_geotag, container, false);
        mainActivity = (MainActivity) getActivity();

//        final TextView textView = root.findViewById(R.id.text_share);
//        geotagViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(this);

        saveButton = root.findViewById(R.id.save_loc);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sekolahId = mainActivity.getPreference(MainActivity.SPKEY_SEKOLAH_ID);
                String penggunaId = mainActivity.getPreference(MainActivity.SPKEY_PENGGUNA_ID);

                if (sekolahId.equals("")) {
                    Snackbar.make(root, "Mohon login dulu.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                final Geotag geotag = SQLite.select()
                        .from(Geotag.class)
                        .where(Geotag_Table.sekolah_id.eq(UUID.fromString(sekolahId)))
                        .and(Geotag_Table.status_geotag_id.greaterThanOrEq(2))
                        .querySingle();


                String pernahStr = "";
                String pilihStr = "Mohon pilih jenis geotag ini.";

                if (geotag != null) {
                    pernahStr = "Geotagging sudah pernah dilakukan. ";
                }

                final Typeface faceMed = ResourcesCompat.getFont(mainActivity, R.font.quicksand_semibold);
                final Typeface face = ResourcesCompat.getFont(mainActivity, R.font.quicksand_regular);

                final MaterialDialog loginDialog = new MaterialDialog.Builder(mainActivity)
                        .title("Menyimpan Lokasi")
                        .content(pernahStr + pilihStr)
                        .positiveText("Baru/Koreksi")
                        .neutralText("Pindah Posisi")
                        .negativeText("Batal")
                        .typeface(faceMed, face)
                        .autoDismiss(true)
                        .show();

                loginDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        me.saveGeotag(geotag,2);
                        loginDialog.dismiss();
                    }
                });

                loginDialog.getActionButton(DialogAction.NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        me.saveGeotag(geotag,3);
                        loginDialog.dismiss();
                    }
                });

                // } else {
                //     Geotag geotagNew = new Geotag();
                //     geotagNew.setGeotagId(UUID.randomUUID());
                //     me.saveGeotag(geotagNew);
                // }
            }
        });
        return root;
    }

    public void saveGeotag(Geotag geotag, Integer status){

        if (geotag == null) {
            geotag = new Geotag();
            geotag.setGeotagId(UUID.randomUUID());
        }

        String sekolahId = mainActivity.getPreference(MainActivity.SPKEY_SEKOLAH_ID);
        String penggunaId = mainActivity.getPreference(MainActivity.SPKEY_PENGGUNA_ID);

        geotag.setSekolahId(UUID.fromString(sekolahId));
        geotag.setPenggunaId(UUID.fromString(penggunaId));
        geotag.setSekolahLink(sekolahId);
        geotag.setSekolahLink(penggunaId);
        geotag.setLintang(String.valueOf(currentLocation.getLatitude()));
        geotag.setBujur(String.valueOf(currentLocation.getLongitude()));
        geotag.setStatusGeotagId(status);
        geotag.setStatusData(1);
        geotag.setTglPengambilan(new Date());
        geotag.save();

        Snackbar.make(root, "Data tersimpan.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        // First Position
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Location button etc
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

//         CameraPosition bandung = CameraPosition.builder()
//                 .target(new LatLng(-6.225657, 106.801943))
//                 .target(new LatLng(-2.496966, 119.316534))
//                 .zoom(4)
//                 .bearing(0)
//                 .tilt(0)
//                 .build();
//
//         mMap.moveCamera(CameraUpdateFactory.newCameraPosition(bandung));

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(mainActivity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            // Create LocationManager
            locationManager = (LocationManager) mainActivity.getSystemService(MainActivity.LOCATION_SERVICE);

            // Set initial location
            try {
                Location location = null;
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    CameraPosition cameraPosition = null;
                    if (location != null) {
                        cameraPosition = CameraPosition.builder()
                                //.target(new LatLng(-6.225657, 106.801943))
                                //.target(new LatLng(-2.496966, 119.316534))
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(14)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        locationManager.requestLocationUpdates(getLocationProvider(), MIN_TIME, 0, this);
                    }
                }
                //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            } catch (Exception e) {
                Snackbar.make(root, "Mohon nyalakan GPS anda.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    }

    private String getLocationProvider() {
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return locationManager.getBestProvider(criteria, true);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // Toast.makeText(mainActivity, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Toast.makeText(mainActivity, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
         // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }


    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        String now = DateFormat.getTimeInstance().format(new Date());
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        String acc = String.valueOf(location.getAccuracy());
        String prv = location.getProvider();
        System.out.println(String.format("[%s] %s - Position (%s, %s) accuracy %s meters", now, prv, lat, lon, acc));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(cameraUpdate);
        currentLocation = location;

        // locationManager.removeUpdates(this);
        Float accuracy = location.getAccuracy();
        Integer accuracyMeters = Math.round(accuracy);

        String msg = null;
        String loader = "";

        for(int counter=0; counter < (loadingCount % 3); counter++)
            loader += ".";


        if (accuracyMeters <= 6) {
            msg = "Simpan Posisi (akurasi: " +  accuracyMeters.toString() + "m)";
            saveButton.setText(msg);
            saveButton.setEnabled(true);
            // saveButton.style
            locationManager.removeUpdates(this);
        } else {
            msg = "Menerima Sinyal GPS (akurasi: " +  accuracyMeters.toString() + "m)";
            saveButton.setText(msg);
        }

        loadingCount++;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
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
}