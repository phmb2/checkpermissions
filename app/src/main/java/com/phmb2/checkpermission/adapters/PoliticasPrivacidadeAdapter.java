package com.phmb2.checkpermission.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.extras.ListViewOnClickListenerHack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phmb2 on 10/11/17.
 */

public class PoliticasPrivacidadeAdapter extends ArrayAdapter<String[]> {

    private List<String[]> privacyPoliciesList = new ArrayList<String[]>();
    //private ListViewOnClickListenerHack listViewOnClickListenerHack;

    static class PrivacyPoliciesViewHolder
    {
        TextView politica;
        TextView metodo;
    }

    public PoliticasPrivacidadeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add(String[] object)
    {
        privacyPoliciesList.add(object);
        super.add(object);
    }

    @Override
    public int getCount()
    {
        return this.privacyPoliciesList.size();
    }

    @Override
    public String[] getItem(int position)
    {
        return this.privacyPoliciesList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        PrivacyPoliciesViewHolder viewHolder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.politicas_privacidade_item, parent, false);
            viewHolder = new PrivacyPoliciesViewHolder();
            viewHolder.politica = (TextView) row.findViewById(R.id.politica);
            viewHolder.metodo = (TextView) row.findViewById(R.id.metodo);
            row.setTag(viewHolder);

        }
        else {
            viewHolder = (PrivacyPoliciesViewHolder) row.getTag();
        }

        String[] values = getItem(position);
        viewHolder.politica.setText(values[0]);
        viewHolder.metodo.setText(values[1]);
        return row;
    }
}
