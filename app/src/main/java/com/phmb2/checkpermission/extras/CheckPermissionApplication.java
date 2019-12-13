package com.phmb2.checkpermission.extras;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.phmb2.checkpermission.bd.Database;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by phmb2 on 15/09/17.
 */

public class CheckPermissionApplication extends Application
{
    private Database db;
    public BlockingQueue<Pair<String,Integer>> fila;

    private boolean fimLoadingBD = false;

    private boolean fimLoadingInApp = false;

    private synchronized void terminar(int n)
    {
        switch (n)
        {
            case 0:
                if(fimLoadingBD) return;
                fimLoadingBD = true;
                break;

            case 1:
                if(fimLoadingInApp) return;
                fimLoadingInApp = true;
                break;
        }
        if(fimLoadingInApp && fimLoadingBD)
        {
            try
            {
                if(fila != null)
                    fila.put(new Pair<String, Integer>("Iniciando app...", 100));

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    /*@Override
    public void onCreate()
    {
        super.onCreate();

        Database.getDatabase(this).getReadableDatabase().close();

        fila = new ArrayBlockingQueue<Pair<String,Integer>>(150);
        Database.init(this, fila, new Runnable()
        {
            @Override
            public void run()
            {
                CheckPermissionApplication.this.terminar(0);
            }
        });
    }

    public Database getDatabase()
    {
        if(db == null)
            db = Database.getInstance(this);

        return db;
    }*/
}
