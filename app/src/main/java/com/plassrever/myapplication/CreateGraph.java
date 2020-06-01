package com.plassrever.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class CreateGraph extends AppCompatActivity {

    private int countOfV;
    private ArrayList<String> strlistOfV;
    private StringBuilder sbResult;
    private int[][] matrix, matrixCopy, P;
    private int pointA, pointB, road;

    private CheckBox orientedCheck;
    private EditText roadEdit;
    private TextView resultText, resultMatrixText;
    private ListView resultList;
    private Button showGraphButton;

    private ArrayList<Integer> selectedPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_graph);

        Intent intent = getIntent();
        countOfV = Integer.parseInt(intent.getStringExtra("countOfV"));
        strlistOfV = new ArrayList<>();
        matrix = new int[countOfV][countOfV];
        P = new int[countOfV][countOfV];

        for (int i = 0; i < countOfV; i++){
            for (int j = 0; j < countOfV; j++){
                matrix[i][j] = 32000;
                if (i == j) matrix[i][j] = 0;
                P[i][j] = 32000;
            }
        }

        for (int i = 0; i < countOfV; i++)
            strlistOfV.add(String.valueOf(i+1));

        Spinner spinnerPointA = findViewById(R.id.point_a);
        Spinner spinnerPointB = findViewById(R.id.point_b);
        roadEdit = findViewById(R.id.road_edittext);
        resultText = findViewById(R.id.result_text);
        orientedCheck = findViewById(R.id.oriented_check);
        resultMatrixText = findViewById(R.id.matrix_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strlistOfV);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPointA.setAdapter(adapter);
        spinnerPointB.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListenerA = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pointA = Integer.parseInt((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        AdapterView.OnItemSelectedListener itemSelectedListenerB = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pointB = Integer.parseInt((String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerPointA.setOnItemSelectedListener(itemSelectedListenerA);
        spinnerPointB.setOnItemSelectedListener(itemSelectedListenerB);

        Button addRoadButton = findViewById(R.id.add_road_button);
        addRoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roadEdit.getText().toString().length() != 0){
                    resultMatrixText.setVisibility(View.VISIBLE);
                    road = Integer.parseInt(roadEdit.getText().toString());
                    matrix[pointA-1][pointB-1] = road;
                    if (!orientedCheck.isChecked())
                        matrix[pointB-1][pointA-1] = road;

                    StringBuilder sbM = new StringBuilder("");
                    for (int i = 0; i < countOfV; i++){
                        for (int j = 0; j < countOfV; j++){
                            if (i != j){
                                strlistOfV.add((i+1) + " ---> " + (j+1));
                                if (matrix[i][j] != 32000)
                                    sbM.append(matrix[i][j] + " ");
                                else sbM.append("N ");
                            }
                            else sbM.append("0 ");
                        }
                        sbM.append("\n");
                    }
                    resultMatrixText.setText(sbM.toString());

                    Toast.makeText(CreateGraph.this,
                            "A: " + pointA + "\n" + "B: " + pointB + "\n" + "Стоимость: " + road,
                                    Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(CreateGraph.this, "Введите расстояние!", Toast.LENGTH_SHORT).show();
            }
        });

        showGraphButton = findViewById(R.id.create_graph_button);
        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGraph.this, GraphView.class);
                intent.putExtra("matrixSize", matrixCopy.length);
                intent.putExtra("selectedPoints", selectedPoints);
                int i = 0;
                for (int[] n : matrixCopy)
                    intent.putExtra("matrix" + String.valueOf(i++), n);

                startActivity(intent);
            }
        });

        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                matrixCopy = new int[countOfV][countOfV];

                for (int i = 0; i < countOfV; i++){
                    for (int j = 0; j < countOfV; j++){
                        matrixCopy[i][j] = matrix[i][j];
                    }
                }

                strlistOfV.removeAll(strlistOfV);
                showGraphButton.setVisibility(View.VISIBLE);

                StringBuilder sbMatrix = new StringBuilder("");

                for (int k = 0; k < countOfV; k++) {
                    for (int i = 0; i < countOfV; i++) {
                        for (int j = 0; j < countOfV; j++) {
                            if ((matrix[i][k] + matrix[k][j]) < matrix[i][j]){
                                matrix[i][j] = matrix[i][k] + matrix[k][j];
                                P[i][j] = k;
                            }
                        }

                        if (k != i){
                            strlistOfV.add((k+1) + " ---> " + (i+1));
                            if (matrix[k][i] != 32000)
                                sbMatrix.append(matrix[k][i] + " ");
                            else sbMatrix.append("N");
                        }
                        else sbMatrix.append("0 ");
                    }
                    sbMatrix.append("\n");
                }

                resultMatrixText.setText(sbMatrix.toString());

                resultList = findViewById(R.id.result_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateGraph.this, android.R.layout.simple_list_item_1, strlistOfV);
                resultList.setAdapter(adapter);

                resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String parsStr =  (String)parent.getItemAtPosition(position);
                        pointA = Integer.parseInt(parsStr.split(" ")[0]);
                        pointB = Integer.parseInt(parsStr.split(" ")[2]);

                        selectedPoints.removeAll(selectedPoints);

                        sbResult = new StringBuilder("");
                        sbResult.append("Результат: ");
                        sbResult.append(pointA + " --> ");
                        selectedPoints.add(pointA);
                        path(pointA-1, pointB-1);
                        sbResult.append(pointB);
                        selectedPoints.add(pointB);

                        if (matrix[pointA-1][pointB-1] != 32000)
                            sbResult.append("\nМинимальное расстояние: " + String.valueOf(matrix[pointA-1][pointB-1]));
                        else
                            sbResult.append("\nИз точки " + pointA + " в точку " + pointB + " невозможно попасть.");

                        resultText.setText(sbResult.toString());

                        Toast.makeText(CreateGraph.this, parsStr, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void path(int i, int j){
        int k = P[i][j];
        if (k == 32000) return;
        path(i, k);
        sbResult.append(" " + (int)(k+1) + " --> ");
        selectedPoints.add(k+1);
        path(k, j);
    }
}
