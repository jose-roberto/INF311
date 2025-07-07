package com.inf311.pratica5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inf311.pratica5.data.db.AppDatabase;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private AppDatabase dbHelper;
    private double userLat, userLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100
            );
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHelper = new AppDatabase(this);
        dbHelper.abrir(this);

        userLat = getIntent().getDoubleExtra("EXTRA_LATITUDE", 0);
        userLon = getIntent().getDoubleExtra("EXTRA_LONGITUDE", 0);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng center = new LatLng(userLat, userLon);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 16));

        Cursor c = dbHelper.buscar(
                "Checkin",
                new String[]{"Local","qtdVisitas","cat","latitude","longitude"},
                "", null
        );
        if (c.moveToFirst()) {
            do {
                String local = c.getString(c.getColumnIndexOrThrow("Local"));
                int qtd = c.getInt(c.getColumnIndexOrThrow("qtdVisitas"));
                int catId = c.getInt(c.getColumnIndexOrThrow("cat"));
                double lat = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("latitude")));
                double lon = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("longitude")));

                String catName = "";
                Cursor cc = dbHelper.buscar(
                        "Categoria",
                        new String[]{"nome"},
                        "idCategoria=" + catId,
                        null
                );
                if (cc.moveToFirst()) {
                    catName = cc.getString(cc.getColumnIndexOrThrow("nome"));
                }
                cc.close();

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title(local)
                        .snippet(catName + " (" + qtd + ")")
                );
            } while (c.moveToNext());
        }
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_back) {
            finish();
            return true;
        }
        if (id == R.id.menu_gestao) {
            startActivity(new Intent(this, MainActivity.class)
                    .setAction("GESTAO_CHECKIN"));
            return true;
        }
        if (id == R.id.menu_relatorio) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        }
        if (id == R.id.map_normal) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }
        if (id == R.id.map_hybrid) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.fechar();
    }
}