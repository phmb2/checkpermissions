package com.phmb2.checkpermission.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phmb2.checkpermission.R;

import java.util.ArrayList;

/**
 * Created by phmb2 on 15/08/17.
 */

public class PermissaoAdapter extends RecyclerView.Adapter<PermissaoAdapter.PermissaoViewHolder>
{
    private final Context context;
    private final LayoutInflater layoutInflater;
    ArrayList<String> permissions = new ArrayList<>();

    public PermissaoAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override public PermissaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new PermissaoViewHolder(layoutInflater.inflate(R.layout.permissao_lista_item, parent, false));
    }

    public void addAllAndNotify(ArrayList<String> permissions)
    {
        this.permissions.addAll(permissions);
        notifyDataSetChanged();
    }

    @Override public void onBindViewHolder(PermissaoViewHolder holder, int position)
    {
        holder.permissionId.setText(permissions.get(position));
    }

    @Override public int getItemCount()
    {
        return permissions.size();
    }

    static class PermissaoViewHolder extends RecyclerView.ViewHolder
    {
        TextView permissionId;

        public PermissaoViewHolder(View itemView)
        {
            super(itemView);
            permissionId = (TextView) itemView.findViewById(R.id.permission_id);
        }
    }
}
