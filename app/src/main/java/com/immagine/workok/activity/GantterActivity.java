package com.immagine.workok.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.CategoryAxis;
import org.afree.chart.axis.CategoryLabelPositions;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.data.time.SimpleTimePeriod;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.immagine.workok.R;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.Task;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class GantterActivity extends AppCompatActivity {

    Button b_generar_gantt;
    Button b_guardar_gantt;
    int project_id;
    CategoryDataset dataset;
    //TaskSeries tasks;
    AFreeChart chart;
    WebView gantt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);
        gantt = (WebView)findViewById(R.id.gantt);
        gantt.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        gantt.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            gantt.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            gantt.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        Intent intent = getIntent();
        project_id = intent.getIntExtra("project_id",0);

                try{

                    gantt.loadUrl("http://www.jexsantofagasta.cl/workok/wograph.php?action=1&project_id="+project_id);


                }catch(Exception e){

                    e.printStackTrace();

                }

        gantt.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gantter_menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.export_action:
                getBitmapFromWebView();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }


    public Bitmap getBitmapFromWebView() {
        gantt.setDrawingCacheEnabled(true);
        gantt.buildDrawingCache(true);
        File root = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        File myDir = new File(root, "Work OK");
        myDir.mkdirs();
        String fname = "diagram.png";
        File file = new File (myDir, fname);
        Bitmap bitmap = Bitmap.createBitmap(gantt.getDrawingCache());
        try {
            // fos = openFileOutput("test.png", Context.MODE_PRIVATE);
            FileOutputStream fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (Exception e) {
            System.out.println("-----error--" + e);
        }
        return bitmap;
    }

}




