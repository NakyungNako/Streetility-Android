package com.example.streetlity_android.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.streetlity_android.MapAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streetlity_android.MyApplication;
import com.example.streetlity_android.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText edtUser = findViewById(R.id.edt_username);
        final EditText edtPass = findViewById(R.id.edt_password);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUser.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(Login.this, "Empty Username", Toast.LENGTH_LONG);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    tv.setTextColor(Color.RED);

                    toast.show();
                } else if (edtPass.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(Login.this, "Empty Password", Toast.LENGTH_LONG);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    tv.setTextColor(Color.RED);

                    toast.show();
                } else {
                    login(edtUser.getText().toString(),edtPass.getText().toString());
                }
            }
        });

        Button btnSignup = findViewById(R.id.btn_to_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Login.this, SignUp.class);
                startActivityForResult(t, 1);
            }
        });

        Button btnForgot = findViewById(R.id.btn_to_forgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Login.this, ForgotPassword.class);
                startActivityForResult(t, 2);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }

    public void login(final String username, String password){
        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject);
                        ((MyApplication) Login.this.getApplication()).setToken(jsonObject.getString("AccessToken"));
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Intent data = new Intent();
                    data.putExtra("username", username);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    try {
                        Toast toast = Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();

                    } catch (Exception e) {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(Login.this, "Sign up successfully", Toast.LENGTH_LONG);
                toast.show();
            }
            if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(Login.this, "Password resetted", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}

