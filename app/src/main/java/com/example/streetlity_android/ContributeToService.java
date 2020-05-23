package com.example.streetlity_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContributeToService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contribute_to_service);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Button btnFuel = findViewById(R.id.btn_fuel);
        Button btnATM = findViewById(R.id.btn_atm);
        Button btnMaintenance = findViewById(R.id.btn_maintenance);
        Button btnWC = findViewById(R.id.btn_wc);
        Button btnConfirming = findViewById(R.id.btn_confirm_location);

        btnFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ContributeToService.this, SelectFromMap.class);
                t.putExtra("type", 1);
                startActivity(t);
            }
        });

        btnATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ContributeToService.this, AddAnATM.class);
                startActivityForResult(t,2);
            }
        });

        btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ContributeToService.this, AddAMaintenance.class);
                startActivityForResult(t, 1);
            }
        });

        btnWC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ContributeToService.this, SelectFromMap.class);
                t.putExtra("type", 2);
                startActivity(t);
            }
        });

        btnConfirming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ContributeToService.this, ConfirmLocations.class);
                startActivity(t);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(ContributeToService.this, R.string.added_successfully, Toast.LENGTH_LONG);
                toast.show();
            }

        } catch (Exception e) {
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
        }

    }
}
