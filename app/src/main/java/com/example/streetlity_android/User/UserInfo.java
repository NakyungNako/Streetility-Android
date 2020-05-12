package com.example.streetlity_android.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.example.streetlity_android.MapAPI;
import com.example.streetlity_android.MyApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streetlity_android.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Button btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent source = getIntent();

                Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                final MapAPI tour = retro.create(MapAPI.class);
                String token = ((MyApplication) UserInfo.this.getApplication()).getToken();
                Call<ResponseBody> call = tour.logout(token, source.getStringExtra("username"),
                        ((MyApplication) getApplication()).getDeviceToken());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                Log.e("", "onResponse: " + jsonObject );
                                ((MyApplication) UserInfo.this.getApplication()).setToken("");
                                ((MyApplication) UserInfo.this.getApplication()).setRefreshToken("");
                                ((MyApplication) UserInfo.this.getApplication()).setUsername("");

                                SharedPreferences s = getSharedPreferences("userPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = s.edit();
                                e.clear();
                                e.apply();

                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            Intent data = new Intent();
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        else {
                            try {
                                //JSONObject jsonObject = new JSONObject(response.body().string());
                                //JSONObject jsonObject1 = new JSONObject(response.errorBody().toString());

                                Log.e("", "onResponse: "  + response.code());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("", "onFailure: " + t.toString());
                    }
                });
            }
        });

        Button btnChangePass = findViewById(R.id.btn_change_password);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(UserInfo.this, ChangePassword.class);
                startActivityForResult(t,1);
            }
        });

        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(UserInfo.this, EditProfile.class);
                startActivityForResult(t,2);
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
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(UserInfo.this, "Password changed", Toast.LENGTH_LONG);
                toast.show();
            }
            if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(UserInfo.this, "Profile updated", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}
