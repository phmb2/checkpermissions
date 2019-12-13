package com.phmb2.checkpermission.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.fragments.PermissaoFragment;

public class MainActivity extends AppCompatActivity
{
    //private static final String SELECTED_ITEM = "arg_selected_item";

    //private BottomNavigationView mBottomNavigationView;
    //private int mSelectedItem;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PermissaoFragment()).commit();
        }

        /*mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;

        if (savedInstanceState != null)
        {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNavigationView.getMenu().findItem(mSelectedItem);
        }
        else
        {
            selectedItem = mBottomNavigationView.getMenu().getItem(0);
        }

        selectFragment(selectedItem);*/
    }

    /*private void selectFragment(MenuItem item)
    {
        Fragment frag = null;

        //inicializa o fragment correspondente
        switch (item.getItemId())
        {
            case R.id.navigation_permission_groups:
                frag = new PermissaoFragment();

                break;

            case R.id.navigation_installed_apps:
                frag = new AplicativosFragment();
                break;
        }

        //modifica o item selecionado
        mSelectedItem = item.getItemId();

        //desmarca os outros itens
        for (int i = 0; i < mBottomNavigationView.getMenu().size(); i++)
        {
            MenuItem menuItem = mBottomNavigationView.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        if (frag != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }*/

    /*@Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }*/

    /*@Override
    public void onBackPressed()
    {
        MenuItem homeItem = mBottomNavigationView.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId())
        {
            //select home item
            selectFragment(homeItem);
        }
        else
        {
            super.onBackPressed();
        }
    }*/

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
