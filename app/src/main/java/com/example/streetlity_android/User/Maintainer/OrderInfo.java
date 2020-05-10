package com.example.streetlity_android.User.Maintainer;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.streetlity_android.R;

public class OrderInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvPhone = findViewById(R.id.tv_phone);
        TextView tvAddress = findViewById(R.id.tv_address);
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvNote = findViewById(R.id.tv_note);

        Button btnGot = findViewById(R.id.btn_goto);
        Button btnAccept = findViewById(R.id.btn_accept);
        Button btnDecline = findViewById(R.id.btn_decline);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogDecline= new Dialog(OrderInfo.this);

                final LayoutInflater inflater = LayoutInflater.from(OrderInfo.this);

                final android.view.View dialogView = inflater.inflate(R.layout.dialog_broadcast, null);

                EditText edtReason = findViewById(R.id.edt_reason);

                Button btnConfirm = dialogView.findViewById(R.id.btn_decline);

                Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDecline.cancel();
                    }
                });

                dialogDecline.setContentView(dialogView);

                dialogDecline.show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }
}
