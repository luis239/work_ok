package com.immagine.workok;

/**
 * Created by Alejandro on 09/06/2016.
 */
import org.afree.chart.AFreeChart;
import org.afree.graphics.geom.RectShape;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {

    private AFreeChart chart;
    private RectShape chartArea;
    private boolean drawCustomCanvas = false;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        chartArea = new RectShape();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chartArea.setWidth(w);
        chartArea.setHeight(h);
    }

    public void setDrawCustomCanvas(boolean drawCustomCanvas)
    {
        this.drawCustomCanvas = drawCustomCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!drawCustomCanvas) {
            super.onDraw(canvas);
        } else {
            this.chart.draw(canvas, chartArea);
        }
    }

    public void setChart(AFreeChart chart) {
        this.chart = chart;
    }

}
