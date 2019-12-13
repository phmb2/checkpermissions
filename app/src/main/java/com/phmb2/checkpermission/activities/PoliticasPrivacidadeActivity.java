package com.phmb2.checkpermission.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.adapters.PoliticasPrivacidadeAdapter;
import com.phmb2.checkpermission.models.CSVReader;

import java.io.InputStream;
import java.util.List;

/**
 * Created by phmb2 on 31/10/17.
 */

public class PoliticasPrivacidadeActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private PoliticasPrivacidadeAdapter politicasPrivacidadeAdapter;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicas_privacidade);

        listView = (ListView) findViewById(R.id.list_view);
        politicasPrivacidadeAdapter = new PoliticasPrivacidadeAdapter(getApplicationContext(), R.layout.politicas_privacidade_item);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(politicasPrivacidadeAdapter);
        listView.onRestoreInstanceState(state);

        InputStream inputStream = getResources().openRawResource(R.raw.privacy_policies);
        CSVReader csv = new CSVReader(inputStream);

        List<String[]> listaPrivacidade = csv.read();

        for(String [] dados : listaPrivacidade) {
            politicasPrivacidadeAdapter.add(dados);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("lala", String.valueOf(i));
                Log.d("lala", String.valueOf(l));
            }
        });

    }

    public void visualizarGruposPermissao()
    {
        Intent intent = new Intent(PoliticasPrivacidadeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.lista_grupos_permissao_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.gruposPermissao)
        {
            visualizarGruposPermissao();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
