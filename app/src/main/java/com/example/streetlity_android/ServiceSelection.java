package com.example.streetlity_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ServiceSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Button btnFuel = findViewById(R.id.btn_fuel);
        Button btnATM = findViewById(R.id.btn_atm);
        Button btnMaintenance = findViewById(R.id.btn_maintenance);
        Button btnWC = findViewById(R.id.btn_wc);

        btnFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ServiceSelection.this, MapsActivity.class);
                t.putExtra("type", 1);
                startActivity(t);
            }
        });

        btnATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ServiceSelection.this, MapsActivity.class);
                t.putExtra("type", 2);
                startActivity(t);
            }
        });

        btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ServiceSelection.this, MapsActivity.class);
                t.putExtra("type", 3);
                startActivity(t);
            }
        });

        btnWC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ServiceSelection.this, MapsActivity.class);
                t.putExtra("type", 4);
                startActivity(t);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }
}
