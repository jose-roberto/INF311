package com.inf311.pratica3.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppDatabase {
    protected SQLiteDatabase db;
    private final String NOME_BANCO = "pratica3.bd";
    private final String[] SCRIPT_DATABASE_CREATE = new String[]{
            "CREATE TABLE IF NOT EXISTS Location(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "descricao TEXT NOT NULL," +
                    "latitude REAL NOT NULL," +
                    "longitude REAL NOT NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Logs(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idLocation INTEGER NOT NULL," +
                    "msg TEXT NOT NULL," +
                    "timestamp TEXT NOT NULL," +
                    "FOREIGN KEY(idLocation) REFERENCES Location(id)" +
                    ");",
            "INSERT INTO Location(descricao, latitude, longitude) VALUES" +
                    "('Casa Natal', -20.662764795703676, -43.78107042226744)," +
                    "('Casa em Viçosa', -20.756752239645305, -42.881357360157615)," +
                    "('Departamento', -20.764776767125422, -42.868349297544);"
    };

    public AppDatabase(Context ctx) {
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Location'", null);
        if (c.getCount() == 0) {
            for (String sql : SCRIPT_DATABASE_CREATE) {
                db.execSQL(sql);
            }
            Log.i("Database", "Tabelas criadas.");
        }
        c.close();

        Log.i("Database", "Abriu conexão com o banco.");
    }

    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);
        Log.i("Database", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);
        Log.i("Database", "Atualizou [" + count + "] registros");
        return count;
    }

    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);
        Log.i("Database", "Deletou [" + count + "] registros");
        return count;
    }

    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;
        if (!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        Log.i("Database", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    public void abrir(Context ctx) {
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        Log.i("Database", "Abriu conexão com o banco.");
    }

    public void fechar() {
        if (db != null) {
            db.close();
            Log.i("Database", "Fechou conexão com o Banco.");
        }
    }
}
