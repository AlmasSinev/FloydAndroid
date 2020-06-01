package com.plassrever.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class GraphView extends AppCompatActivity {

    public int[][] matrix;

    public int[][] m2 = {{0, 8, 5},
            {3, 0, 32000},
            {32000, 2, 0}};

    public int[][] matrix3 = {
            {0, 700, 200, 32000, 32000, 32000},
            {700, 0, 300, 200, 32000, 400},
            {200, 300, 0, 700, 600, 32000},
            {32000, 200, 700, 0, 300, 100},
            {32000, 32000, 600, 300, 0, 500},
            {32000, 400, 32000, 100, 500, 0}
    };

    int[][] matrix1 = {
            {0, 1, 2, 32000, 32000, 32000, 32000, 32000},
            {32000, 0, 1, 5, 2, 32000, 32000, 32000},
            {32000, 32000, 0, 2, 1, 4, 32000, 32000},
            {32000, 32000, 32000, 0, 3, 6, 8, 32000},
            {32000, 32000, 32000, 32000, 0, 3, 7, 32000},
            {32000, 32000, 32000, 32000, 32000, 0, 5, 2},
            {32000, 32000, 32000, 32000, 32000, 32000, 0, 6},
            {32000, 32000, 32000, 32000, 32000, 32000, 32000, 0}
    };

    int[][] matrix2 = {
            {0, 5, 1, 3, 32000, 32000, 32000},
            {32000, 0, 32000, 7, 1, 6, 32000},
            {32000, 2, 0, 6, 7, 32000, 32000},
            {32000, 32000, 7, 0, 32000, 4, 6},
            {32000, 32000, 32000, 3, 0, 5, 9},
            {32000, 7, 32000, 32000, 32000, 0, 2},
            {32000, 32000, 32000, 32000, 32000, 32000, 0}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));

        Intent intent = getIntent();
        int matrixSize = intent.getIntExtra("matrixSize", 0);
        matrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++){
            matrix[i] = intent.getIntArrayExtra("matrix" + String.valueOf(i));
        }
    }

    class DrawView extends View{

        Paint paintCircle, paintText, paintTextDistance, paintLine, paintLineSelect, paintCircleSelect;
        ArrayList<Point> points = new ArrayList<>();

        public DrawView(Context context) {
            super(context);
            paintCircle = new Paint();
            paintText = new Paint();
            paintTextDistance = new Paint();
            paintLine = new Paint();
            paintLineSelect = new Paint();
            paintCircleSelect = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawARGB(80, 255, 255, 255);

            paintCircle.setColor(getResources().getColor(R.color.colorCircle));
            paintCircle.setStrokeWidth(10);

            paintText.setColor(Color.WHITE);
            paintText.setStrokeWidth(40);
            paintText.setTextSize(60);

            paintTextDistance.setColor(Color.BLACK);
            paintTextDistance.setStrokeWidth(40);
            paintTextDistance.setTextSize(50);

            paintLine.setColor(getResources().getColor(R.color.colorLine));
            paintLine.setStrokeWidth(10);

            int x = 50;
            for (int i =0; i < matrix.length; i++){
                x += 200;
                int y = 450;

                Point point = new Point(x, y, i+1);
                for (int j =0; j < matrix.length; j++){
                    if (i != j ){
                        if (matrix[i][j] != 32000){
                            point.starts += 1;
                            point.NEIGHBOURHOOD.add(j + 1);
                        }
                    }
                }
                points.add(point);
            }

            int distY = 175;
            boolean up = false;

            for (Point point : points){
                for (int n : point.NEIGHBOURHOOD){
                    if (!points.get(n-1).isDrawed){
                        points.get(n-1).pY = up ? points.get(n-1).pY+distY : points.get(n-1).pY-distY;

                        if(n == 2)
                            points.get(n-1).pY -= 175;
                        else if (n == 1)
                            points.get(n-1).pY = 350;

                        distY = up ? distY+60 : distY;
                        up = !up;
                        points.get(n-1).isDrawed = true;
                    }
                }
            }

            for (int i =0; i < matrix.length; i++){
                for (int j =0; j < matrix.length; j++){
                    if (i != j) {
                        if (matrix[i][j] != 32000) {
                            canvas.drawLine(points.get(i).pX, points.get(i).pY, points.get(j).pX,points.get(j).pY, paintLineSelect );
                            canvas.drawText(String.valueOf(matrix[i][j]), (points.get(i).pX + points.get(j).pX)/2, (points.get(i).pY + points.get(j).pY)/2, paintTextDistance);
                            canvas.drawCircle(points.get(i).pX, points.get(i).pY, 80, paintCircle);
                            canvas.drawText(String.valueOf(points.get(i).pointName), points.get(i).pX - 15, points.get(i).pY + 10, paintText);
                            canvas.drawCircle(points.get(j).pX, points.get(j).pY, 80, paintCircle);
                            canvas.drawText(String.valueOf(points.get(j).pointName), points.get(j).pX - 15, points.get(j).pY + 10, paintText);
                        }
                    }
                }
                canvas.drawCircle(points.get(i).pX, points.get(i).pY, 80, paintCircle);
                canvas.drawText(String.valueOf(points.get(i).pointName), points.get(i).pX, points.get(i).pY, paintText);
            }
        }
    }

    class Point {
        int pX, pY;
        public int starts = 0;
        public int ends = 0;
        public int pointName;
        public boolean isDrawed = false;
        public ArrayList<Integer> NEIGHBOURHOOD = new ArrayList<>();

        public Point(int x, int y, int name){
            pX = x;
            pY = y;
            pointName = name;
        }

    }
}
