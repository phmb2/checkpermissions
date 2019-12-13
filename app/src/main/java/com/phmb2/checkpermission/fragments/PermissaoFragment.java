package com.phmb2.checkpermission.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.activities.GraficoPermissoesActivity;
import com.phmb2.checkpermission.adapters.GrupoPermissoesAdapter;
import com.phmb2.checkpermission.models.GrupoPermissoes;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by phmb2 on 14/08/17.
 */

public class PermissaoFragment extends Fragment
{
    private View mRootView;
    private GrupoPermissoesAdapter permissionGroupListAdapter;
    private RecyclerView permissionsList;
    private ArrayList<GrupoPermissoes> permissionList;
    private ProgressBar progressBar;

    public PermissaoFragment()
    {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_permissao_lista, container, false);

        configureToolBar();

        progressBar = (ProgressBar) mRootView.findViewById(R.id.permission_list_progress_bar);
        permissionsList = (RecyclerView) mRootView.findViewById(R.id.permission_list);
        permissionGroupListAdapter = new GrupoPermissoesAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());

        permissionsList.setLayoutManager(layoutManager);
        permissionsList.addItemDecoration(divider);
        permissionsList.setAdapter(permissionGroupListAdapter);

        if (permissionList == null)
        {
            makeRx();
        }
        else
        {
            permissionGroupListAdapter.addAll(permissionList);
        }

        return mRootView;
    }

    private void makeRx()
    {
        Single<ArrayList<GrupoPermissoes>> permissions = Single.fromCallable(new Callable<ArrayList<GrupoPermissoes>>()
        {
            @Override public ArrayList<GrupoPermissoes> call() throws Exception
            {
                TreeMap<String, GrupoPermissoes> groups = fetchPermissionsList();
                return new ArrayList<>(groups.values());
            }
        });

        permissions.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<GrupoPermissoes>>()
                {
                    @Override public void onSubscribe(@NonNull Disposable d)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        permissionsList.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(@NonNull ArrayList<GrupoPermissoes> groupDetailsList)
                    {
                        Log.i("Single subscriber test ", groupDetailsList.size() + " ");
                        progressBar.setVisibility(View.GONE);
                        permissionsList.setVisibility(View.VISIBLE);
                        permissionList = groupDetailsList;
                        permissionGroupListAdapter.addAll(groupDetailsList);
                    }

                    @Override public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private TreeMap<String, GrupoPermissoes> fetchPermissionsList()
    {
        PackageManager packageManager = getActivity().getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> applicationInfos = packageManager.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);

        TreeMap<String, GrupoPermissoes> permissionGroupDetailsMap = new TreeMap<>();
        addMiscCategory(permissionGroupDetailsMap);
        addNoPermissionCategory(permissionGroupDetailsMap);

        for (ResolveInfo applicationInfo : applicationInfos)
        {
            try
            {
                PackageInfo packageInfo = packageManager.getPackageInfo(applicationInfo.activityInfo.packageName, PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null)
                {
                    for (String permission : requestedPermissions)
                    {
                        PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);

                        if (permissionInfo.group == null)
                        {
                            GrupoPermissoes grupoPermissoes = permissionGroupDetailsMap.get("z_MISC");

                            if (grupoPermissoes.pacotesApp.add(packageInfo.packageName))
                            {
                                grupoPermissoes.quantidadeApps = grupoPermissoes.quantidadeApps + 1;
                            }

                            continue;
                        }
                        if (permissionGroupDetailsMap.containsKey(permissionInfo.group))
                        {
                            GrupoPermissoes grupoPermissoes = permissionGroupDetailsMap.get(permissionInfo.group);
                            if (grupoPermissoes.pacotesApp.add(packageInfo.packageName))
                            {
                                grupoPermissoes.quantidadeApps = grupoPermissoes.quantidadeApps + 1;
                            }
                        }
                        else
                        {
                            GrupoPermissoes grupoPermissoes = new GrupoPermissoes();
                            grupoPermissoes.nomeGrupoPermissoes = permissionInfo.group;
                            if (permissionInfo.loadDescription(packageManager) == null)
                            {
                                grupoPermissoes.descricaoGrupoPermissoes = "No desc";
                            }
                            else
                            {
                                grupoPermissoes.descricaoGrupoPermissoes = permissionInfo.loadDescription(packageManager).toString();
                            }

                            if (grupoPermissoes.pacotesApp.add(packageInfo.packageName))
                            {
                                grupoPermissoes.quantidadeApps = 1;
                            }

                            permissionGroupDetailsMap.put(permissionInfo.group, grupoPermissoes);
                        }
                    }
                }
                else
                {
                    GrupoPermissoes detalhesGrupo = permissionGroupDetailsMap.get("z_NO_PERMISSION");
                    if (detalhesGrupo.pacotesApp.add(packageInfo.packageName))
                    {
                        detalhesGrupo.quantidadeApps = detalhesGrupo.quantidadeApps + 1;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Log.i("Map size ", permissionGroupDetailsMap.size() + " ");
        return permissionGroupDetailsMap;
    }

    //Configura a Toolbar
    private void configureToolBar()
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText(getString(R.string.title_permission_groups));
        toolbar.setTitle("");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_keyboard_arrow_left);
    }

    private void addNoPermissionCategory(TreeMap<String, GrupoPermissoes> permissionGroupDetailsMap)
    {
        GrupoPermissoes miscPermissionGroup = new GrupoPermissoes();
        miscPermissionGroup.descricaoGrupoPermissoes = "App não precisa de nenhuma permissão";
        miscPermissionGroup.nomeGrupoPermissoes = "NO PERMISSIONS REQUIRED";
        miscPermissionGroup.quantidadeApps = 0;
        permissionGroupDetailsMap.put("z_NO_PERMISSION", miscPermissionGroup);
    }

    private void addMiscCategory(TreeMap<String, GrupoPermissoes> permissionGroupDetailsMap)
    {
        GrupoPermissoes miscPermissionGroup = new GrupoPermissoes();
        miscPermissionGroup.descricaoGrupoPermissoes = "Permissões personalizadas/diversas";
        miscPermissionGroup.nomeGrupoPermissoes = "MISC PERMISSION";
        miscPermissionGroup.quantidadeApps = 0;
        permissionGroupDetailsMap.put("z_MISC", miscPermissionGroup);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lista_permissao_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.listApps:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AplicativosFragment())
                        .addToBackStack("CheckPermissions apps")
                        .commit();
                return true;

            /*case R.id.chart:

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new GraficoPermissoesFragment())
                        .addToBackStack("CheckPermissions chart")
                        .commit();

                //Intent intent = new Intent(getActivity(), GraficoPermissoesActivity.class);
                //startActivity(intent);

                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
