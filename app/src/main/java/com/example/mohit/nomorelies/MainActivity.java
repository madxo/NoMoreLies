package com.example.mohit.nomorelies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.TintContextWrapper;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button TeacherBtn;
    private Button ParentBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     TeacherBtn=(Button)findViewById(R.id.Teacher);
        ParentBtn=(Button)findViewById(R.id.Parent);

        TeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TIntent = new Intent(MainActivity.this,TeacherLogin.class);
                startActivity(TIntent);
            }
        });

        ParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PIntent = new Intent(MainActivity.this,ParentLogin.class);
                startActivity(PIntent);
            }
        });
    }

    }

