package com.inf311.pratica5;

import com.inf311.pratica5.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.inf311.pratica5.data.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private AppDatabase dbHelper;
    private AutoCompleteTextView acvLocal;
    private Spinner spinnerCategoria;
    private Button btnCheckin;

    private TextView tvLatitude;
    private TextView tvLongitude;

    private LocationManager locationManager;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CheckInLocais");

        acvLocal = findViewById(R.id.autoCompleteLocal);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnCheckin = findViewById(R.id.btnCheckin);

        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);

        dbHelper = new AppDatabase(this);
        dbHelper.abrir(this);
        carregarLocais();
        carregarCategorias();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    1
            );
        } else {
            startLocationUpdates();
        }

        btnCheckin.setOnClickListener(v -> salvarCheckin());
    }

    private void carregarLocais() {
        String[] colunas = {"Local"};
        Cursor c = dbHelper.buscar("Checkin", colunas, "", "Local ASC");

        List<String> listaLocais = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                listaLocais.add(c.getString(c.getColumnIndexOrThrow("Local")));
            } while (c.moveToNext());
        }
        c.close();

        ArrayAdapter<String> adapterLocal = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                listaLocais
        );
        acvLocal.setAdapter(adapterLocal);
        acvLocal.setThreshold(1);
    }

    private void carregarCategorias() {
        String[] colunas = {"nome"};
        Cursor c = dbHelper.buscar("Categoria", colunas, "", "idCategoria ASC");

        List<String> listaCategorias = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                listaCategorias.add(c.getString(c.getColumnIndexOrThrow("nome")));
            } while (c.moveToNext());
        }
        c.close();

        ArrayAdapter<String> adapterCat = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaCategorias
        );
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCat);
    }

    private void salvarCheckin() {
        String local = acvLocal.getText().toString().trim();
        if (local.isEmpty()) {
            Toast.makeText(this, "Informe o nome do local", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "Escolha uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lastLocation == null) {
            Toast.makeText(this, "Aguardando posição do dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        String whereLocal = "Local='" + local.replace("'", "''") + "'";

        Cursor c = dbHelper.buscar("Checkin", new String[]{"qtdVisitas"}, whereLocal, null);

        if (c.moveToFirst()) {
            int qtd = c.getInt(c.getColumnIndexOrThrow("qtdVisitas"));
            c.close();

            android.content.ContentValues cv = new android.content.ContentValues();
            cv.put("qtdVisitas", qtd + 1);

            dbHelper.atualizar("Checkin", cv, whereLocal);

            Toast.makeText(this, "Check-in atualizado em " + local, Toast.LENGTH_SHORT).show();
        } else {
            c.close();

            String nomeCat = spinnerCategoria.getSelectedItem().toString();

            Cursor cc = dbHelper.buscar(
                    "Categoria",
                    new String[]{"idCategoria"},
                    "nome='" + nomeCat.replace("'", "''") + "'", null
            );

            int catId = 0;
            if (cc.moveToFirst()) {
                catId = cc.getInt(cc.getColumnIndexOrThrow("idCategoria"));
            }
            cc.close();

            android.content.ContentValues cv = new android.content.ContentValues();

            cv.put("Local", local);
            cv.put("qtdVisitas", 1);
            cv.put("cat", catId);
            cv.put("latitude", String.valueOf(lastLocation.getLatitude()));
            cv.put("longitude", String.valueOf(lastLocation.getLongitude()));

            dbHelper.inserir("Checkin", cv);

            Toast.makeText(this, "Novo check-in em " + local, Toast.LENGTH_SHORT).show();

            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }

        carregarLocais();
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    10,
                    this
            );
        } catch (SecurityException e) {
            Log.e("Location", "Permissão negada", e);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation = location;

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        tvLatitude.setText(String.format(Locale.getDefault(), "Latitude: %.6f", lat));
        tvLongitude.setText(String.format(Locale.getDefault(), "Longitude: %.6f", lon));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mapa_checkin)
        {
            Intent it = new Intent(this, MapActivity.class);

            if (lastLocation != null) {
                it.putExtra("EXTRA_LATITUDE", lastLocation.getLatitude());
                it.putExtra("EXTRA_LONGITUDE", lastLocation.getLongitude());
            }
            startActivity(it);
            return true;
        } else if (item.getItemId() == R.id.gestao_checkin)
        {
            Intent it = new Intent(this, ManagementActivity.class);
            startActivity(it);
            return true;
        } else if (item.getItemId() == R.id.lugares_visitados)
        {
            Intent it = new Intent(this, ReportActivity.class);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        dbHelper.fechar();
    }
}
