package com.phmb2.checkpermission.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.fragments.GraficoFragment;
import com.phmb2.checkpermission.fragments.GraficoPermissoesFragment;

/**
 * Created by phmb2 on 02/09/17.
 */

public class GraficoPermissoesActivity extends AppCompatActivity
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new GraficoPermissoesFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
