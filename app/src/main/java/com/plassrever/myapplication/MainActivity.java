package com.plassrever.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private EditText editCountOfV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btn_start);
        editCountOfV = findViewById(R.id.edit_count);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCountOfV.getText().toString().length() > 0) {
                    Intent intent = new Intent(MainActivity.this, CreateGraph.class);
                    intent.putExtra("countOfV", editCountOfV.getText().toString());
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(MainActivity.this, "Не может быть меньше 1 вершины!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
