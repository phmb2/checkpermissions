package com.phmb2.checkpermission.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phmb2.checkpermission.R;

import java.util.ArrayList;

/**
 * Created by phmb2 on 04/09/17.
 */

public class GraficoPermissoesFragment extends Fragment
{
    private View mRootView;
    BarChart chart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_grafico_permissoes, container, false);
        configureToolBar();
        initView();

        return mRootView;
    }

    public void initView()
    {
        chart = (BarChart) mRootView.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription(getString(R.string.title_permission_groups));
        //chart.animateXY(2000, 2000);
        chart.setPinchZoom(true);
        chart.invalidate();
    }

    private ArrayList<BarDataSet> getDataSet()
    {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet = new ArrayList<>();

        for(int i = 1; i <= 12; i++)
        {
            float x = i * 10 % 12 + 3;
            valueSet.add(new BarEntry(x, i));
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, getString(R.string.title_permission_groups));
        //barDataSet.setColor(Color.rgb(140, 155, 0));
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i = 1; i <= 12; i++)
        {
            xAxis.add("GP" + i);
        }
        return xAxis;
    }

    //Configura a Toolbar
    private void configureToolBar()
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText(getString(R.string.title_permission_groups_chart));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_keyboard_arrow_left);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grafico_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.piechart:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new GraficoFragment())
                        .addToBackStack("PieChart")
                        .commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
