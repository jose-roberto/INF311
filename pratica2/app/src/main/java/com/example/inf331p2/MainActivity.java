package com.example.inf331p2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String getClassName() {
        return getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Ciclo de Vida", getClassName() + " - onCreate()");

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Ciclo de Vida", getClassName() + " - onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EditText name_edt = findViewById(R.id.name_edt);
        EditText age_edt = findViewById(R.id.age_edt);
        EditText height_edt = findViewById(R.id.height_edt);
        EditText weight_edt = findViewById(R.id.weight_edt);

        name_edt.setText("");
        age_edt.setText("");
        height_edt.setText("");
        weight_edt.setText("");

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
        EditText name_edt = findViewById(R.id.name_edt);
        EditText age_edt = findViewById(R.id.age_edt);
        EditText height_edt = findViewById(R.id.height_edt);
        EditText weight_edt = findViewById(R.id.weight_edt);

        if (name_edt.getText().toString().isEmpty() || age_edt.getText().toString().isEmpty() || height_edt.getText().toString().isEmpty() || weight_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {
            try {
                String name = name_edt.getText().toString();
                int age = Integer.parseInt(age_edt.getText().toString());
                double height = Double.parseDouble(height_edt.getText().toString());
                double weight = Double.parseDouble(weight_edt.getText().toString());

                Bundle params = new Bundle();

                params.putString("name", name);
                params.putInt("age", age);
                params.putDouble("height", height);
                params.putDouble("weight", weight);

                Intent it = new Intent(getBaseContext(), Report.class);

                it.putExtras(params);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(it);
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao converter dados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}