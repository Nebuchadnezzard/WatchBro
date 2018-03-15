package com.watchbro.watchbro.activities.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.watchbro.watchbro.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment de l'activité de l'utilisateur
 */
public class ActivityFragment extends Fragment implements SensorEventListener {

    private BarChart bchart;
    private List<Integer> yData;
    private List<Integer> xData;
    private final String[] days = {"J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10", "J11"};
    private IAxisValueFormatter formatterDays;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView mStepsSinceReboot;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        bchart = (BarChart) view.findViewById(R.id.bchart);

        formatterDays = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return days[(int) value];
            }
        };

        xData = new ArrayList<>();
        yData = new ArrayList<>();
        yData.add(0);
        yData.add(1);
        yData.add(2);
        yData.add(3);
        yData.add(4);
        yData.add(5);
        yData.add(6);
        yData.add(7);
        yData.add(8);
        yData.add(9);
        yData.add(10);
        xData.add(900);
        xData.add(750);
        xData.add(11590);
        xData.add(15000);
        xData.add(14520);
        xData.add(12069);
        xData.add(13679);
        xData.add(867);
        xData.add(7620);
        xData.add(11687);
        xData.add(15632);


        List<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < yData.size(); i++) {
            entries.add(new BarEntry((float) yData.get(i), (float) xData.get(i)));
        }
        BarDataSet dataS = new BarDataSet(entries, "Votre nombre de pas par jour");
        BarData lData = new BarData(dataS);

        // Limite
        LimitLine ll = new LimitLine(10000f, "Nombre de pas idéal");
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(2f);
        ll.setTextColor(Color.GREEN);
        ll.setTextSize(12f);
        bchart.getAxisLeft().addLimitLine(ll);
        bchart.setData(lData);
        bchart.notifyDataSetChanged();
        bchart.invalidate(); // refresh

        mStepsSinceReboot =  view.findViewById(R.id.textSteps);

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
        {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        return view;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mStepsSinceReboot.setText(String.valueOf(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
