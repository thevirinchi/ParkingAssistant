package com.z3t4z00k.parkingassistant;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Par1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout floor1 = findViewById(R.id.floor1);
        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent floor1 = new Intent(Par1.this, par1Floor1.class);
                startActivity(floor1);
            }
        });
    }
}
