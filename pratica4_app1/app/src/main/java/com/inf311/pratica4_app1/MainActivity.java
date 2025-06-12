package com.inf311.pratica4_app1;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor_light, sensor_proximity;

    double light_value = 0, proximity_value = 0;
    int lightAction = 0, proximityAction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensor_light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensor_light != null && sensor_proximity != null) {
            sensorManager.registerListener(this, sensor_light, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensor_proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        Intent intent = getIntent();

        if (intent != null) {
            controlarSensores(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensor_light != null && sensor_proximity != null) {
            sensorManager.registerListener(this, sensor_light, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, sensor_proximity, SensorManager.SENSOR_DELAY_GAME);

            Log.i("SENSOR_INICIOU", sensor_light.getName() + " e " + sensor_proximity.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);

        Log.i("SENSOR_PAROU", "Sensor parou");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            light_value = event.values[0];
            Log.i("SENSOR_VALOR", "NOME: " + event.sensor.getName() + " VALOR: " + light_value);
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximity_value = event.values[0];
            Log.i("SENSOR_VALOR", "NOME: " + event.sensor.getName() + " VALOR: " + proximity_value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void classificarLeituras(View v) {
        Intent intent = new Intent("CLASSIFICAR");

        Bundle bundle = new Bundle();

        bundle.putDouble("light", light_value);
        bundle.putDouble("proximity", proximity_value);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void controlarSensores(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            lightAction = extras.getInt("lightAction");
            proximityAction = extras.getInt("proximityAction");
        }

        Switch lightSwitch = findViewById(R.id.lanterna);
        Switch proximitySwitch = findViewById(R.id.vibracao);

        LanternaHelper lanternaHelper = new LanternaHelper(this);
        MotorHelper motorHelper = new MotorHelper(this);

        if (lightAction == 1) {
            lanternaHelper.ligar();
            lightSwitch.setChecked(true);
        } else {
            lanternaHelper.desligar();
            lightSwitch.setChecked(false);
        }

        if (proximityAction == 1) {
            motorHelper.iniciarVibracao();
            proximitySwitch.setChecked(true);
        } else {
            motorHelper.pararVibracao();
            proximitySwitch.setChecked(false);
        }
    }
}