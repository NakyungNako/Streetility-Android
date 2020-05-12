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

import com.example.streetlity_android.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpAsCommon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText edtUsername = findViewById(R.id.edt_username);
        final EditText edtPassword = findViewById(R.id.edt_password);
        final EditText edtCFPassword = findViewById(R.id.edt_cfpassword);
        final EditText edtMail = findViewById(R.id.edt_email);
        final EditText edtPhone = findViewById(R.id.edt_phone);
        final EditText edtAddress = findViewById(R.id.edt_address);

        Button btnSighUp = findViewById(R.id.btn_signup);
        btnSighUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                if(checkFields(edtUsername.getText().toString(), edtPassword.getText().toString(),
                        edtCFPassword.getText().toString(),edtMail.getText().toString(),edtPhone.getText().toString()
                ,edtAddress.getText().toString())){

                    Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    final MapAPI tour = retro.create(MapAPI.class);

                    Call<ResponseBody> call = tour.signupCommon(edtUsername.getText().toString(), edtPassword.getText().toString(),
                            edtMail.getText().toString(),edtPhone.getText().toString(),edtAddress.getText().toString());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    Log.e("", "onResponse: " + jsonObject.toString());
                                    if (jsonObject.getBoolean("Status")) {
                                        Intent data = new Intent();
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }
                                    else{
                                        Toast toast = Toast.makeText(SignUpAsCommon.this, jsonObject.getString("Message"), Toast.LENGTH_LONG);
                                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                                        tv.setTextColor(Color.RED);

                                        toast.show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Log.e("", "onResponse: " + response.code() );
                                    Toast toast = Toast.makeText(SignUpAsCommon.this, "Username or Email existed", Toast.LENGTH_LONG);
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

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }

    public boolean checkFields(String username, String pass, String cfPass, String email, String phoneNumber, String address){
        if(username.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Username is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(pass.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Password is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(cfPass.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Confirm password is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(email.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Email is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(!pass.equals(cfPass)){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Password mismatch", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(phoneNumber.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Phone number is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(address.equals("")){
            Toast toast = Toast.makeText(SignUpAsCommon.this, "Address is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }

        return true;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}