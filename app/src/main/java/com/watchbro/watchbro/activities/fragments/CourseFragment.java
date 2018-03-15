package com.watchbro.watchbro.activities.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.watchbro.watchbro.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment du suivi de l'utilisateur
 */
public class CourseFragment extends Fragment {

    LineChart lchart;
    private List<Integer> yData;
    private List<Integer> xData;
    private final String[] hours = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00"};
    IAxisValueFormatter formatterHours;


    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course, container, false);
        lchart = view.findViewById(R.id.lchart);
        formatterHours = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return hours[(int) value];
            }
        };

        // GRAPHE BPM

        // Jeu de données
        yData = new ArrayList<>();
        xData = new ArrayList<>();
        yData.add(0);
        xData.add(57);
        yData.add(1);
        xData.add(58);
        yData.add(2);
        xData.add(57);
        yData.add(3);
        xData.add(56);
        yData.add(4);
        xData.add(57);
        yData.add(5);
        xData.add(59);
        yData.add(6);
        xData.add(58);
        yData.add(7);
        xData.add(62);
        yData.add(8);
        xData.add(65);
        yData.add(9);
        xData.add(70);
        yData.add(10);
        xData.add(69);
        yData.add(11);
        xData.add(75);
        yData.add(12);
        xData.add(72);
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            entries.add(new Entry((float) yData.get(i), (float) xData.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Votre BPM moyen par heure");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        LineData lineData = new LineData(dataSet);

        // Axis Right
        YAxis axisRight = lchart.getAxisRight();
        axisRight.setEnabled(false);
        // Axis Left
        YAxis axisLeft = lchart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setAxisMaximum(200);
        // XAxis
        XAxis xAxis = lchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatterHours);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // Limite
        LimitLine ll = new LimitLine(140f, "BPM Maximum");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(2f);
        ll.setTextColor(Color.RED);
        ll.setTextSize(12f);
        lchart.getAxisLeft().addLimitLine(ll);

        lchart.setData(lineData);
        lchart.getDescription().setEnabled(false);
        lchart.getLegend().setEnabled(false);
        lchart.notifyDataSetChanged();
        lchart.invalidate(); // refresh


        // Inflate the layout for this fragment
        return view;
    }
}
