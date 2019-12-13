package com.phmb2.checkpermission.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phmb2.checkpermission.R;
import com.phmb2.checkpermission.activities.GraficoPermissoesActivity;

import java.util.ArrayList;

/**
 * Created by phmb2 on 05/09/17.
 */

public class GraficoFragment extends Fragment
{
    private View mRootView;
    PieChart pieChart;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_grafico, container, false);
        configureToolBar();
        initView();

        return mRootView;
    }

    public void initView()
    {
        pieChart = (PieChart) mRootView.findViewById(R.id.chart);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(2000);

    }

    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("NO PERMISSIONS");
        PieEntryLabels.add("MSC");
        PieEntryLabels.add("MSN");
    }

    /*private ArrayList<PieDataSet> getDataSet()
    {
        ArrayList<PieDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet = new ArrayList<>();

        for(int i = 1; i <= 3; i++)
        {
            float x = i * 10 % 3 + 3;
            valueSet.add(new BarEntry(x, i));
        }

        PieDataSet pieDataSet = new PieDataSet(valueSet, getString(R.string.title_permission_groups));
        //barDataSet.setColor(Color.rgb(140, 155, 0));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(pieDataSet);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();

        for(int i = 1; i <= 3; i++)
        {
            xAxis.add("GP" + i);
        }
        return xAxis;
    }*/

    //Configura a Toolbar
    private void configureToolBar()
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleTextView.setText("lala"+getString(R.string.title_permission_groups_chart));
        ActionBar supportActionBar = ((GraficoPermissoesActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_keyboard_arrow_left);
    }

}
