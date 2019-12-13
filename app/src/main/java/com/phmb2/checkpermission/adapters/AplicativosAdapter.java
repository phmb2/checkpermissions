package com.phmb2.checkpermission.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.fragments.DetalhesAplicativoFragment;
import com.phmb2.checkpermission.models.Aplicativo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phmb2 on 14/08/17.
 */

public class AplicativosAdapter extends RecyclerView.Adapter<AplicativosAdapter.AplicativoViewHolder>
{
    private final Context context;
    private final LayoutInflater layoutInflater;
    ArrayList<Aplicativo> item = new ArrayList<>();

    public AplicativosAdapter(Context context)
    {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AplicativosAdapter.AplicativoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new AplicativoViewHolder(layoutInflater.inflate(R.layout.aplicativos_item, parent, false));

    }

    @Override
    public void onBindViewHolder(AplicativosAdapter.AplicativoViewHolder holder, int position)
    {
        final Aplicativo detalhesApp = item.get(position);

        holder.appIcon.setImageDrawable(detalhesApp.iconeAplicativo);

        holder.appName.setText(detalhesApp.nomeAplicativo);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("nome_pacote", detalhesApp.nomePacoteAplicativo);

                DetalhesAplicativoFragment detalhesAplicativoFragment = new DetalhesAplicativoFragment();
                detalhesAplicativoFragment.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, detalhesAplicativoFragment)
                        .addToBackStack("detalhes_app")
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return item.size();
    }

    public void addAllAndNotify(List<Aplicativo> detalhesApp)
    {
        item.addAll(detalhesApp);
        notifyDataSetChanged();
    }

    class AplicativoViewHolder extends RecyclerView.ViewHolder
    {
        ImageView appIcon;
        TextView appName;

        public AplicativoViewHolder(View itemView)
        {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon_iv);
            appName = (TextView) itemView.findViewById(R.id.app_name_tv);
        }
    }
}
