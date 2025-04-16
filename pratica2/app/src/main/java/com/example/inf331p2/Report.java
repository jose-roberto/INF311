package com.example.inf331p2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Report extends AppCompatActivity {

    private String getClassName() {
        return getClass().getName();
    }

    private double calculateIMC(double height, double weight) {
        return Math.round(weight / (height * height));
    }

    private String evaluateIMC(double imc) {
        String classification = "None";

        if (imc < 18.5) {
            classification = "Abaixo do Peso";
        } else if (imc < 24.9) {
            classification = "Saudável";
        } else if (imc < 29.9) {
            classification = "Sobrepeso";
        } else if (imc < 34.9) {
            classification = "Obesidade Grau I";
        } else if (imc < 39.9) {
            classification = "Obesidade Grau II";
        } else {
            classification = "Obesidade Grau III";
        }

        return classification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Ciclo de Vida", getClassName() + " - onCreate()");
        setContentView(R.layout.activity_report);

        Intent it = getIntent();
        Bundle params = it.getExtras();

        String name = params.getString("name");
        int age = params.getInt("age");
        double height = params.getDouble("height");
        double weight = params.getDouble("weight");

        double imc = calculateIMC(height, weight);

        String classification = evaluateIMC(imc);

        TextView show_name = findViewById(R.id.show_name);
        TextView show_age = findViewById(R.id.show_age);
        TextView show_height = findViewById(R.id.show_height);
        TextView show_weight = findViewById(R.id.show_weight);
        TextView show_imc = findViewById(R.id.show_imc);
        TextView show_classificaiton = findViewById(R.id.show_classification);

        show_name.setText(name);
        show_age.setText(String.valueOf(age));
        show_height.setText(String.format("%sm", height));
        show_weight.setText(String.format("%skg", weight));
        show_imc.setText(String.format("%skg/m²", imc));
        show_classificaiton.setText(classification);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Ciclo de Vida", getClassName() + " - onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Ciclo de Vida", getClassName() + " - onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Ciclo de Vida", getClassName() + " - onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Ciclo de Vida", getClassName() + " - onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Ciclo de Vida", getClassName() + " - onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Ciclo de Vida", getClassName() + " - onDestroy()");
    }
    public void button_action(View view) {
        finish();
    }
}