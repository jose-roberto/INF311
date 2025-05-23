package com.inf311.pratica3;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.inf311.pratica3.data.db.AppDatabase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Relatório de Logs");

        AppDatabase db = new AppDatabase(this);

        Cursor cLogs = db.buscar(
                "Logs",
                new String[]{"msg", "timestamp"},
                "",
                ""
        );

        List<String> listaLogs = new ArrayList<>();

        if (cLogs.moveToFirst()) {
            do {
                String msg = cLogs.getString(cLogs.getColumnIndexOrThrow("msg"));
                String timestamp = cLogs.getString(cLogs.getColumnIndexOrThrow("timestamp"));
                listaLogs.add(msg + " — " + timestamp);
            } while (cLogs.moveToNext());
        }
        cLogs.close();
        db.fechar();

        String[] logs = listaLogs.toArray(new String[0]);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logs);
        setListAdapter(arrayAdapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = l.getItemAtPosition(position).toString();

        AppDatabase db = new AppDatabase(this);

        String msg = item.substring(0, item.indexOf(" — "));
        String timestamp = item.substring(item.indexOf(" — ") + 3);

        Cursor cDepto = db.buscar(
                "Location lc INNER JOIN Logs lg ON lc.id = lg.idLocation",
                new String[]{"lc.latitude", "lc.longitude" },
                "lg.msg = '" + item + "' AND lg.timestamp = '" + timestamp + "'",
                ""
        );
        double latDepto = 0, lngDepto = 0;
        if (cDepto.moveToFirst()) {
            latDepto = cDepto.getDouble(cDepto.getColumnIndexOrThrow("latitude"));
            lngDepto = cDepto.getDouble(cDepto.getColumnIndexOrThrow("longitude"));
        }
        cDepto.close();
        db.fechar();

        Toast.makeText(this, "Latitude: " + latDepto + " Longitude: " + lngDepto, Toast.LENGTH_LONG).show();

    }
}