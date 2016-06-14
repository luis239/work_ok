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

   /* private static AFreeChart createChart(TaskSeries dataset) {

        // create the chart...
        AFreeChart chart = ChartFactory.createBarChart(
                "proyecto 7",       // chart title
                "Tareas",               // domain axis label
                "Porcentaje",                  // range axis label
                (CategoryDataset) dataset,                  // data
                PlotOrientation.HORIZONTAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
        GradientColor gp1 = new GradientColor(Color.WHITE, Color.rgb(0, 0, 0));
        GradientColor gp2 = new GradientColor(Color.RED, Color.rgb(64, 0, 0));
        renderer.setSeriesPaintType(0, gp0);
        renderer.setSeriesPaintType(1, gp1);
        renderer.setSeriesPaintType(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;
    }

    private static TaskSeries createDataset(JSONArray tasks_list_array) {


        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        XYSeriesCollection datasets;
        //dataset.addValue(new SimpleTimePeriod(null, null), rowKey, columnKey)
        //String serie1 = "Planeado";
        //String serie2 = "Ejecutado";
        TaskSeries serie1 = new TaskSeries("planificado");


        try {

            for (int i = 0; i < tasks_list_array.length(); i++) {


                JSONObject task = (JSONObject)tasks_list_array.get(i);




                //eventos_arraylist.add(new Evento(Integer.parseInt(evento_json.get("evento_id").toString()), evento_json.get("evento_titulo").toString(), evento_json.get("evento_fecha").toString(), evento_json.get("evento_hora").toString(), evento_json.get("evento_categoria").toString(), evento_json.get("evento_descripcion").toString(), evento_json.get("evento_ciudad").toString(), evento_json.get("evento_empresa").toString(), evento_json.get("evento_foto1").toString(), evento_json.get("evento_foto2").toString(), evento_json.get("evento_foto3").toString()));
                // row keys...


                // column keys...
                //dataset.addValue(task.getInt("percentage"), serie1, task.getString("title") + "\n" +  task.getString("date_start") + "/" + task.getString("date_end") +  "\n" + task.getString("description") +  "\n" + task.getString("status_id") +  "\n" + task.getString("user_id"));
                //dataset.addValue(task.getInt("date_start"), serie1, task.getString("title"));
                //dataset.a
                serie1.add(new Task(task.getString("title"), new SimpleTimePeriod(makeDate(10, Calendar.JUNE, 2007), makeDate(15, Calendar.JUNE, 2007))));

                //task.getString("date_start") + "/" + task.getString("date_end")
                //task.getInt("percentage")
                //new SimpleTimePeriod(makeDate(10, Calendar.JUNE, 2007), makeDate(15, Calendar.JUNE, 2007))




            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return serie1;
    }


    private void savebitmap(String filename, Bitmap gantt) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = filename +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            gantt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }






    }

    private static Date makeDate(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
*/

}




