package com.inf311.pratica3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inf311.pratica3.data.db.AppDatabase;

import android.Manifest;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private LatLng casaNatal, casaVicosa, departamento;

    private GoogleMap map;
    public LocationManager lm;
    public Criteria criteria;
    public String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        AppDatabase db = new AppDatabase(this);

        Cursor cNatal = db.buscar(
                "Location",
                new String[]{"latitude", "longitude" },
                "descricao = 'Casa Natal'",
                ""
        );
        double latNatal = 0, lngNatal = 0;
        if (cNatal.moveToFirst()) {
            latNatal = cNatal.getDouble(cNatal.getColumnIndexOrThrow("latitude"));
            lngNatal = cNatal.getDouble(cNatal.getColumnIndexOrThrow("longitude"));
        }
        cNatal.close();

        Cursor cVicosa = db.buscar(
                "Location",
                new String[]{"latitude", "longitude" },
                "descricao = 'Casa em Viçosa'",
                ""
        );
        double latVicosa = 0, lngVicosa = 0;
        if (cVicosa.moveToFirst()) {
            latVicosa = cVicosa.getDouble(cVicosa.getColumnIndexOrThrow("latitude"));
            lngVicosa = cVicosa.getDouble(cVicosa.getColumnIndexOrThrow("longitude"));
        }
        cVicosa.close();

        Cursor cDepto = db.buscar(
                "Location",
                new String[]{"latitude", "longitude" },
                "descricao = 'Departamento'",
                ""
        );
        double latDepto = 0, lngDepto = 0;
        if (cDepto.moveToFirst()) {
            latDepto = cDepto.getDouble(cDepto.getColumnIndexOrThrow("latitude"));
            lngDepto = cDepto.getDouble(cDepto.getColumnIndexOrThrow("longitude"));
        }
        cDepto.close();
        db.fechar();

        casaNatal = new LatLng(latNatal, lngNatal);
        casaVicosa = new LatLng(latVicosa, lngVicosa);
        departamento = new LatLng(latDepto, lngDepto);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if (hasGPS) {
            Log.i("GPS", "Usando GPS");
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        } else {
            Log.i("GPS", "Usando Wi-fi ou dados");
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void onDestroy() {
        lm.removeUpdates(this);
        Log.w("Provedor", "Provedor" + provider + "desativado");
        super.onDestroy();
    }

    public void onClick_casaNatal(View v) {
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(casaNatal, 15);
            map.animateCamera(update);
        }
    }

    public void onClick_casaVicosa(View v) {
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(casaVicosa, 15);
            map.animateCamera(update);
        }
    }

    public void onClick_meuDepartamento(View v) {
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(departamento, 15);
            map.animateCamera(update);
        }
    }

    public void onClick_minhaLocalizacao(View v) {
        Log.i("MapActivity", "botão Minha Localização pressionado");

        provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            Toast.makeText(this, "Nenhum provedor disponível", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de localização falta", Toast.LENGTH_SHORT).show();
            return;
        }

        Location last = lm.getLastKnownLocation(provider);
        if (last != null) {
            onLocationChanged(last);
        } else {
            lm.requestSingleUpdate(provider, this, getMainLooper());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.addMarker(new MarkerOptions().position(casaNatal).title("Casa Natal"));
        map.addMarker(new MarkerOptions().position(casaVicosa).title("Casa em Viçosa"));
        map.addMarker(new MarkerOptions().position(departamento).title("Departamento"));

        Intent it = getIntent();
        int location = it.getIntExtra("location", 0);

        switch (location) {
            case 0:
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(casaNatal, 15));
                break;
            case 1:
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(casaVicosa, 15));
                break;
            case 2:
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(departamento, 15));
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (map != null) {
            LatLng meuLocal = new LatLng(location.getLatitude(), location.getLongitude());

            map.addMarker(new MarkerOptions()
                    .position(meuLocal)
                    .title("Minha localização atual")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(meuLocal, 15f));

            Location casaVicosaLoc = new Location("pontoFixo");
            casaVicosaLoc.setLatitude(casaVicosa.latitude);
            casaVicosaLoc.setLongitude(casaVicosa.longitude);

            float distancia = location.distanceTo(casaVicosaLoc);

            String texto = String.format(
                    "Você está a %.2f metros da casa em Viçosa",
                    distancia
            );
            Toast.makeText(this, texto, Toast.LENGTH_LONG).show();

            lm.removeUpdates(this);
        }
    }
}