package com.inf311.pratica4_app2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    double light = 0, proximity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if (extras != null) {
            light = extras.getDouble("light");
            proximity = extras.getDouble("proximity");

            Toast.makeText(this, "Light: " + light + " - Proximity: " + proximity, Toast.LENGTH_LONG).show();
        }
    }

    public void devolverClassificacoes(View V) {
        int lightAction = 0, proximityAction = 0;

        if (light > 0 && light < 20) {
            lightAction = 1;
        }

        if (proximity > 3) {
            proximityAction = 1;
        }

        Intent intent = new Intent("SENSORES");

        Bundle extras = new Bundle();

        extras.putInt("lightAction", lightAction);
        extras.putInt("proximityAction", proximityAction);

        intent.putExtras(extras);

        startActivity(intent);

    }
}