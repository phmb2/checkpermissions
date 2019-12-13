package com.phmb2.checkpermission.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.adapters.PermissaoAdapter;
import com.phmb2.checkpermission.models.Aplicativo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by phmb2 on 15/08/17.
 */

public class DetalhesAplicativoFragment extends Fragment
{
    private View mRootView;
    private ImageView appIcon;
    private TextView packageNameTv;
    private TextView appName;
    private TextView appVersion;
    private TextView appUpdateDate;
    private RecyclerView permissionsList;
    private PermissaoAdapter permissaoAdapter;
    private TextView noPermissionLabel;
    //private Button openAppBtn;
    //private Button appDetailsBtn;
    private String mPackageName;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_detalhes_aplicativo, container, false);
        configureToolBar();
        mPackageName = getArguments().getString("nome_pacote");
        initView();
        //bindEvents();
        fetchAppDetails(mPackageName);
        return mRootView;
    }

    /*private void bindEvents()
    {
        openAppBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent openAppIntent = getActivity().getPackageManager().getLaunchIntentForPackage(mPackageName);
                startActivity(openAppIntent);
            }
        });

        appDetailsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mPackageName, null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }*/

    private void initView()
    {
        //openAppBtn = (Button) mRootView.findViewById(R.id.open_app);
        //appDetailsBtn = (Button) mRootView.findViewById(R.id.app_details);
        appIcon = (ImageView) mRootView.findViewById(R.id.app_picture);
        appName = (TextView) mRootView.findViewById(R.id.app_name);
        appVersion = (TextView) mRootView.findViewById(R.id.app_version);
        appUpdateDate = (TextView) mRootView.findViewById(R.id.app_update_date);
        noPermissionLabel = (TextView) mRootView.findViewById(R.id.detail_label);
        packageNameTv = (TextView) mRootView.findViewById(R.id.app_package);
        permissionsList = (RecyclerView) mRootView.findViewById(R.id.permission_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        permissionsList.setLayoutManager(layoutManager);
        permissionsList.addItemDecoration(divider);

        permissaoAdapter = new PermissaoAdapter(getActivity());
        permissionsList.setAdapter(permissaoAdapter);
    }

    private void fetchAppDetails (final String packageName)
    {
        Single<Aplicativo> appDetailsSingle = Single.fromCallable(new Callable<Aplicativo>()
        {
            @Override public Aplicativo call() throws Exception
            {

                Aplicativo aplicativo = new Aplicativo();

                PackageManager packageManager = getActivity().getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);

                aplicativo.nomeAplicativo = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                aplicativo.versaoAplicativo = packageInfo.versionName;
                aplicativo.dataAtualizacaoAplicativo = getDateTime(packageInfo.lastUpdateTime);
                aplicativo.iconeAplicativo = packageInfo.applicationInfo.loadIcon(packageManager);
                aplicativo.nomePacoteAplicativo = packageName;

                if (packageInfo.requestedPermissions != null)
                {
                    aplicativo.listaPermissoesAplicativo = new ArrayList<>(Arrays.asList(packageInfo.requestedPermissions));
                }

                return aplicativo;
            }
        });

        appDetailsSingle.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Aplicativo>() {

                    @Override public void onSubscribe(@NonNull Disposable d) {}

                    @Override public void onSuccess(@NonNull Aplicativo aplicativo)
                    {
                        appIcon.setImageDrawable(aplicativo.iconeAplicativo);
                        appName.setText(aplicativo.nomeAplicativo);
                        appVersion.setText(aplicativo.versaoAplicativo);
                        appUpdateDate.setText(aplicativo.dataAtualizacaoAplicativo);
                        packageNameTv.setText(aplicativo.nomePacoteAplicativo);

                        if (aplicativo.listaPermissoesAplicativo == null)
                        {
                            noPermissionLabel.setText(getString(R.string.no_permissions_required));
                        }
                        else
                        {
                            noPermissionLabel.setText(getString(R.string.permission_count, aplicativo.listaPermissoesAplicativo.size()));
                            permissaoAdapter.addAllAndNotify(aplicativo.listaPermissoesAplicativo);
                        }
                    }

                    @Override public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    //Obtem a data no formato personalizado
    private String getDateTime(long updateDate)
    {
        final Locale myLocale = new Locale("pt", "BR");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", myLocale);
        return sdf.format(new Date(updateDate));
    }

    //Configura a Toolbar
    private void configureToolBar()
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText(getString(R.string.title_app_details));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_keyboard_arrow_left);
    }
}


