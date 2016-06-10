package com.immagine.workok.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.immagine.workok.Constants;
import com.immagine.workok.GraphView;
import com.immagine.workok.HttpHandler;
import com.immagine.workok.R;


public class GantterActivity extends AppCompatActivity {

    Button b_generar_gantt;
    Button b_guardar_gantt;
    GraphView grafico_gantt;
    String project_id = "7";
    HttpHandler httpHandler = new HttpHandler();
    CategoryDataset dataset;
    AFreeChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantter);

        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

        b_generar_gantt = (Button)findViewById(R.id.graph);
        b_guardar_gantt = (Button)findViewById(R.id.save);
        grafico_gantt = (GraphView)findViewById(R.id.grafico_gantt);

        //grafico_gantt.destroyDrawingCache();
        grafico_gantt.setDrawingCacheEnabled(true);


        b_generar_gantt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                try{



                    String result = httpHandler.Recibir_Proyecto(project_id);

                    JSONObject data = new JSONObject(result);

                    JSONArray tasks_list_array = new JSONArray(data.get("data").toString());

                    dataset = createDataset(tasks_list_array);
                    chart = createChart(dataset);
                    grafico_gantt.setChart(chart);
                    grafico_gantt.setDrawCustomCanvas(true);
                    grafico_gantt.invalidate();
                    b_guardar_gantt.setVisibility(View.VISIBLE);

					/*if (tasks_list_array.length() == 0) {

						Toast.makeText(getBaseContext(), "El proyecto no tiene tareas", Toast.LENGTH_LONG).show();


					} else {


						//ArrayList<Evento> eventos_arraylist = new ArrayList<Evento>();

						for (int i = 0; i < tasks_list_array.length(); i++) {

							JSONObject task = (JSONObject)tasks_list_array.get(i);


							//eventos_arraylist.add(new Evento(Integer.parseInt(evento_json.get("evento_id").toString()), evento_json.get("evento_titulo").toString(), evento_json.get("evento_fecha").toString(), evento_json.get("evento_hora").toString(), evento_json.get("evento_categoria").toString(), evento_json.get("evento_descripcion").toString(), evento_json.get("evento_ciudad").toString(), evento_json.get("evento_empresa").toString(), evento_json.get("evento_foto1").toString(), evento_json.get("evento_foto2").toString(), evento_json.get("evento_foto3").toString()));





						}





					}*/
                }catch(Exception e){

                    e.printStackTrace();

                }



            }
        });

        b_guardar_gantt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try{

                    savebitmap(project_id, grafico_gantt.getDrawingCache());

                }catch(Exception e){


                    e.printStackTrace();

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private static AFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        AFreeChart chart = ChartFactory.createBarChart(
                "proyecto 7",       // chart title
                "Tareas",               // domain axis label
                "Porcentaje",                  // range axis label
                dataset,                  // data
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
        GradientColor gp1 = new GradientColor(Color.GREEN, Color.rgb(0, 64, 0));
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

    private static CategoryDataset createDataset(JSONArray tasks_list_array) {


        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String percentage = "Porcentaje";


        try {

            for (int i = 0; i < tasks_list_array.length(); i++) {


                JSONObject task = (JSONObject)tasks_list_array.get(i);



                //eventos_arraylist.add(new Evento(Integer.parseInt(evento_json.get("evento_id").toString()), evento_json.get("evento_titulo").toString(), evento_json.get("evento_fecha").toString(), evento_json.get("evento_hora").toString(), evento_json.get("evento_categoria").toString(), evento_json.get("evento_descripcion").toString(), evento_json.get("evento_ciudad").toString(), evento_json.get("evento_empresa").toString(), evento_json.get("evento_foto1").toString(), evento_json.get("evento_foto2").toString(), evento_json.get("evento_foto3").toString()));
                // row keys...


                // column keys...
                dataset.addValue(task.getInt("percentage"), percentage, task.getString("title") + "\n" +  task.getString("date_start") + "/" + task.getString("date_end") +  "\n" + task.getString("description") +  "\n" + task.getString("status_id") +  "\n" + task.getString("user_id"));




            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        return dataset;
    }


    private void savebitmap(String filename, Bitmap gantt) {

        File root = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))+"/test/","gantt");
        File myDir = new File(root, "Gantt");
        myDir.mkdirs();
        String fname = filename +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            gantt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }






    }


}

