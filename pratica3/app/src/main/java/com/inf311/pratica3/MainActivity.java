package com.inf311.pratica3;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.inf311.pratica3.data.db.AppDatabase;

import java.time.Instant;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] menu = new String [] {"Minha casa na cidade natal", "Minha casa em Viçosa", "Meu departamento", "Relatório", "Fechar aplicação"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrayAdapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent it_map = new Intent(getBaseContext(), MapActivity.class);
        it_map.putExtra("location", position);
        String item = l.getItemAtPosition(position).toString();

        switch(position) {
            case 0:
                Toast.makeText(getBaseContext(), item + " selecionado", Toast.LENGTH_SHORT).show();
                startActivity(it_map);
                break;
            case 1:
                Toast.makeText(getBaseContext(), item + " selecionado", Toast.LENGTH_SHORT).show();
                startActivity(it_map);
                break;
            case 2:
                Toast.makeText(getBaseContext(), item + " selecionado", Toast.LENGTH_SHORT).show();
                startActivity(it_map);
                break;
            case 3:
                Intent it_rep = new Intent(getBaseContext(), ReportActivity.class);
                Toast.makeText(getBaseContext(), item + " selecionado", Toast.LENGTH_SHORT).show();
                startActivity(it_rep);
                break;
            case 4:
                finish();
                break;
            default:
                break;
        }

        if (position < 3) {
            Instant timestamp = Instant.now();

            ContentValues cv = new ContentValues();
            cv.put("idLocation", position + 1);
            cv.put("msg", item);
            cv.put("timestamp", "" + timestamp);

            AppDatabase db = new AppDatabase(this);
            long novoId = db.inserir("Logs", cv);
            db.fechar();
        }

//        if (novoId == -1) {
//            Toast.makeText(getBaseContext(), "Erro ao inserir log", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getBaseContext(), "Log inserido com sucesso", Toast.LENGTH_SHORT).show();
//        }
    }
}