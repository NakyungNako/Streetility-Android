package com.example.streetlity_android.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        Button btnSignup = findViewById(R.id.btn_to_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                Intent t = new Intent(Login.this, SignUp.class);
                startActivityForResult(t, 1);
            }
        });

        Button btnForgot = findViewById(R.id.btn_to_forgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                Intent t = new Intent(Login.this, ForgotPassword.class);
                startActivityForResult(t, 2);
            }
        });

        Button btnSignupMaintainer = findViewById(R.id.btn_to_signup_as_maintainer);
        btnSignupMaintainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                Intent t = new Intent(Login.this, SignupAsMaintainer.class);
                startActivityForResult(t, 3);
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
        Call<ResponseBody> call = tour.login(username, password, ((MyApplication) getApplication()).getDeviceToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject);
                        if(jsonObject.getBoolean("Status")) {

                            //((MyApplication) Login.this.getApplication()).setToken(jsonObject.getString("AccessToken"));
                            ((MyApplication) Login.this.getApplication()).setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTQ0NDUzMzIsImlkIjoibXJYWVpaemFidiJ9.0Hd4SpIELulSuTxGAeuCPl_A33X-KoPUpRmgK4dTphk");
                            ((MyApplication) Login.this.getApplication()).setRefreshToken(jsonObject.getString("RefreshToken"));
                            ((MyApplication) Login.this.getApplication()).setUsername(username);

                            jsonObject = jsonObject.getJSONObject("User");
                            if (jsonObject.getInt("Role") != -1) {
                                ((MyApplication) Login.this.getApplication()).setUserType(jsonObject.getInt("Role"));


                                SharedPreferences s = getSharedPreferences("userPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = s.edit();
                                e.clear();
                                e.commit();
                                e.putString("username", username);
                                e.putString("token", ((MyApplication) Login.this.getApplication()).getToken());
                                e.putString("refreshToken", ((MyApplication) Login.this.getApplication()).getRefreshToken());
                                e.putInt("userType", jsonObject.getInt("Role"));
                                e.commit();

                                Call<ResponseBody> call2 = tour.addDevice("1.0.0", ((MyApplication) Login.this.getApplication()).getToken(),
                                        username);
                                call2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 200) {
                                            try {
                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                if (jsonObject1.getBoolean("Status")) {
                                                    Log.e("", "onResponse: " + jsonObject1.toString());
                                                } else {
                                                    Log.e("", "onResponse: " + jsonObject1.toString());
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("", "onResponse: " + response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });

                                Intent data = new Intent();
                                setResult(RESULT_OK, data);
                                finish();
                            } else {
                                Toast toast = Toast.makeText(Login.this, "User is waiting for approval", Toast.LENGTH_LONG);
                                TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                                tv.setTextColor(Color.RED);

                                toast.show();
                            }
                        }
                        else{
                            Toast toast = Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    try {
                        Log.e("", "onResponse: " + response.code());
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
            if (requestCode == 3 && resultCode == RESULT_OK && null != data) {
                Toast toast = Toast.makeText(Login.this, "Sign up successfully, please wait for verification", Toast.LENGTH_LONG);
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

