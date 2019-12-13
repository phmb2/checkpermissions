package com.phmb2.checkpermission.fragments;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.activities.GraficoPermissoesActivity;
import com.phmb2.checkpermission.adapters.AplicativosAdapter;
import com.phmb2.checkpermission.models.Aplicativo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by phmb2 on 14/08/17.
 */

public class AplicativosFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener
{
    List<String> mAllValues;

    private View mRootView;
    private ArrayList<String> packages;
    private RecyclerView mAppListView;
    private AplicativosAdapter aplicativosAdapter;
    private List<Aplicativo> appDetailList;
    private ProgressBar progressBar;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_aplicativos_lista, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.app_list_progress_bar);
        mAppListView = (RecyclerView) mRootView.findViewById(R.id.app_list);
        mAppListView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        aplicativosAdapter = new AplicativosAdapter(getActivity());
        mAppListView.setAdapter(aplicativosAdapter);

        if (getArguments() != null)
        {
            packages = getArguments().getStringArrayList("packages");
        }

        mAllValues = new ArrayList<>();
        configureToolBar();
        getAppDetails();
        return mRootView;
    }

    private void getAppDetails()
    {
        Single<List<Aplicativo>> listSingle = Single.fromCallable(new Callable<List<Aplicativo>>()
        {
            @Override public List<Aplicativo> call() throws Exception
            {
                if (appDetailList == null)
                {
                    appDetailList = new ArrayList<>();

                    if (packages == null)
                    {
                        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        PackageManager packageManager = getActivity().getPackageManager();
                        List<ResolveInfo> applicationInfos = packageManager.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
                        for (ResolveInfo resolveInfo : applicationInfos)
                        {
                            Aplicativo aplicativo = fetchDetail(resolveInfo.activityInfo.packageName);
                            appDetailList.add(aplicativo);
                        }
                    }
                    else
                    {
                        for (String packageName : packages)
                        {
                            Aplicativo aplicativo = fetchDetail(packageName);
                            appDetailList.add(aplicativo);
                        }
                    }
                }

                return appDetailList;
            }
        });

        listSingle.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Aplicativo>>() {

                    @Override public void onSubscribe(@NonNull Disposable d)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        mAppListView.setVisibility(View.GONE);
                    }

                    @Override public void onSuccess(@NonNull List<Aplicativo> appDetails)
                    {
                        progressBar.setVisibility(View.GONE);
                        mAppListView.setVisibility(View.VISIBLE);
                        aplicativosAdapter.addAllAndNotify(appDetails);
                    }

                    @Override public void onError(@NonNull Throwable e) {}
                });
    }

    private Aplicativo fetchDetail(String packageName)
    {
        PackageManager packageManager = getActivity().getPackageManager();
        Aplicativo detalhes_app = new Aplicativo();

        try
        {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            PackageInfo pInfo = packageManager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            detalhes_app.nomeAplicativo = applicationInfo.loadLabel(packageManager).toString();
            mAllValues.add(detalhes_app.nomeAplicativo);
            detalhes_app.iconeAplicativo = packageManager.getApplicationIcon(packageName);
            detalhes_app.nomePacoteAplicativo = packageName;
            detalhes_app.versaoAplicativo = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return detalhes_app;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search_apps_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:

                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(this);
                searchView.setQueryHint(getString(R.string.title_search));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if (newText == null || newText.trim().isEmpty())
        {
            resetSearch();
            return false;
        }

        List<String> filteredValues = new ArrayList<String>(mAllValues);
        for (String value : mAllValues) {
            if (!value.toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }

        aplicativosAdapter = new AplicativosAdapter(getActivity());
        mAppListView.setAdapter(aplicativosAdapter);
        //mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, filteredValues);
        //setListAdapter(mAdapter);

        return false;
    }

    public void resetSearch()
    {
        aplicativosAdapter = new AplicativosAdapter(getActivity());
        mAppListView.setAdapter(aplicativosAdapter);
        //mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mAllValues);
        //setListAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    //Configura a Toolbar
    private void configureToolBar()
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (packages == null)
        {
            titleTextView.setText(getString(R.string.title_installed_apps));
        }
        else
        {
            titleTextView.setText(getString(R.string.title_apps_list));
        }

        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_keyboard_arrow_left);
    }

}
