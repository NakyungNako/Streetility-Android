package com.example.streetlity_android.User.Maintainer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.streetlity_android.R;

public class Works extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        TextView tvNoOrderNorm =  findViewById(R.id.tv_normal_no_order);
        TextView tvNoOrderEmer = findViewById(R.id.tv_emer_no_order);

        ListView lvNormal = findViewById(R.id.lv_normal_order);
        ListView lvEmergency = findViewById(R.id.lv_emergency);

        lvNormal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lvEmergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

//        if(lvNormal.getAdapter().getCount()==0){
//            tvNoOrderEmer.setVisibility(View.VISIBLE);
//        }
//
//        if(lvEmergency.getAdapter().getCount()==0){
//            tvNoOrderEmer.setVisibility(View.VISIBLE);
//        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }
}
