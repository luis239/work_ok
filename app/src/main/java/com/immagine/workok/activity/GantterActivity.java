package com.immagine.workok.activity;

import java.util.ArrayList;
import java.util.List;

import org.afree.chart.AFreeChart;
import org.afree.data.category.CategoryDataset;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.immagine.workok.R;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class GantterActivity extends AppCompatActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantter);
    }


}




