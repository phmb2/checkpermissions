package com.phmb2.checkpermission.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.phmb2.checkpermission.bd.tables.AplicativoTable;
import com.phmb2.checkpermission.models.Aplicativo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by phmb2 on 11/09/17.
 */

public class Database
{
    private static final String DATABASE_NAME = "database.checkpermission"; //nome do arquivo database
    private static final int DATABASE_VERSION = 1; //número da versao do database

    private AplicativoTable aplicativoTable;

    private static Context context;
    private  static Database instance;
    public static Database db;

    /*private static BlockingQueue<Pair<String, Integer>> fila = null;

    public static Database getDatabase(Context context)
    {
        if(context != null)
        {
            Database.context = context;
            if (instance == null) {
                instance = new Database(context);
                instance.getReadableDatabase().close();
            }
        }
        return instance;
    }

    public static Database init(Context context, BlockingQueue<Pair<String, Integer>> fi, final Runnable fim)
    {
        if (db == null) {
            db = new Database(context);
        }
        fila = fi;
        new Thread() {
            @Override
            public void run()
            {
                SQLiteDatabase dbW = db.getReadableDatabase();
                dbW.close();
                if (fim != null) fim.run();
            }
        }.start();
        return db;
    }

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this;

        //Inicialização das tabelas do BD
        this.aplicativoTable = new AplicativoTable();

        this.context = context;
    }

    public static Database getInstance(Context context) {

        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(aplicativoTable.getCreateCommandString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private static ContentValues contentValuesFromAplicativo(Aplicativo aplicativo)
    {
        ContentValues args = new ContentValues();
        args.put(AplicativoTable.ID_COL, aplicativo.getID());
        args.put(AplicativoTable.NOME_COL, aplicativo.getNomeAplicativo());
        args.put(AplicativoTable.ICONE_COL, String.valueOf(aplicativo.getIconeAplicativo()));
        args.put(AplicativoTable.NOME_PACOTE_COL, aplicativo.getNomePacoteAplicativo());
        args.put(AplicativoTable.VERSAO_COL, aplicativo.getVersaoAplicativo());
        args.put(AplicativoTable.DATA_ATUALIZACAO_COL, aplicativo.getDataAtualizacaoAplicativo());

        return args;
    }

    private static Aplicativo AplicativoFromCursor(Cursor cursor)
    {
        return new Aplicativo(cursor.getInt(AplicativoTable.ID_COL_INDEX),
                cursor.getString(AplicativoTable.NOME_COL_INDEX),
                cursor.getString(AplicativoTable.NOME_PACOTE_COL_INDEX),
                cursor.getString(AplicativoTable.VERSAO_COL_INDEX),
                cursor.getString(AplicativoTable.DATA_ATUALIZACAO_COL_INDEX)
        );
    }

    public boolean saveApp(Aplicativo app)
    {
        boolean retorno;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = contentValuesFromAplicativo(app);
        String consulta = "select * from " + AplicativoTable.TABLE_NAME + " WHERE " + AplicativoTable.ID_COL + " = " + app.getID();
        Cursor cursor = db.rawQuery(consulta,null);

        if(cursor.moveToFirst())
        {
            cursor.close();
            retorno = db.update(AplicativoTable.TABLE_NAME, args, AplicativoTable.ID_COL + "=" + app.getID(), null) > 0;
        }
        else
        {
            cursor.close();
            retorno = db.insert(AplicativoTable.TABLE_NAME, null, args) > 0;
        }

        db.close();
        return retorno;
    }

    public Aplicativo findAppByID(int id)
    {
        Aplicativo app = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT A.* from " + AplicativoTable.TABLE_NAME + " A where A." + AplicativoTable.ID_COL + " == " + id + " ;", null);

        if (cursor.moveToFirst())
        {
            app = AplicativoFromCursor(cursor);
        }

        cursor.close();
        db.close();

        return app;
    }

    public List<Aplicativo> listApp()
    {
        List<Aplicativo> aplicativos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT A.* from " + AplicativoTable.TABLE_NAME + " A;", null);

        if (cursor.moveToFirst())
        {
            do {
                aplicativos.add(AplicativoFromCursor(cursor));

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return aplicativos;

    }*/

}
