package com.phmb2.checkpermission.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.fragments.AplicativosFragment;
import com.phmb2.checkpermission.models.GrupoPermissoes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phmb2 on 14/08/17.
 */

public class GrupoPermissoesAdapter extends RecyclerView.Adapter<GrupoPermissoesAdapter.PermissaoViewHolder>
{
    private final Context context;
    private final ArrayList<GrupoPermissoes> list = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public GrupoPermissoesAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override public PermissaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = layoutInflater.inflate(R.layout.permissao_grupo_lista_item, parent, false);
        return new PermissaoViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final PermissaoViewHolder holder, int position)
    {
        String[] permissionSplit = list.get(position).nomeGrupoPermissoes.split("\\.");
        String permissionHeader = "";

        if (permissionSplit.length > 0)
        {
            permissionHeader = permissionSplit[permissionSplit.length - 1].replace("_", " ");
        }

        holder.permissionName.setText(permissionHeader);

        holder.permissionDes.setText(list.get(position).descricaoGrupoPermissoes);
        holder.appsCount.setText(String.valueOf(list.get(position).quantidadeApps));
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("packages", new ArrayList<>(list.get(holder.getAdapterPosition()).pacotesApp));
                AplicativosFragment listaAppsFragment = new AplicativosFragment();
                listaAppsFragment.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, listaAppsFragment)
                        .addToBackStack("Permission Details")
                        .commit();
            }
        });
    }

    public void addAll(List<GrupoPermissoes> strings)
    {
        list.addAll(strings);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    static class PermissaoViewHolder extends RecyclerView.ViewHolder
    {
        TextView permissionName;
        TextView permissionDes;
        TextView appsCount;

        public PermissaoViewHolder(View itemView)
        {
            super(itemView);
            permissionName = (TextView) itemView.findViewById(R.id.permission_group_name);
            permissionDes = (TextView) itemView.findViewById(R.id.permission_group_description);
            appsCount = (TextView) itemView.findViewById(R.id.permission_group_app_count);
        }
    }

}
