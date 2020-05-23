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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
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

public class UserInfo extends AppCompatActivity {

    boolean edtState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        EditText edtAddress = findViewById(R.id.edt_address);
        EditText edtPhone = findViewById(R.id.edt_phone);

        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtState){
                    edtState = true;
                    fabEdit.setImageResource(R.drawable.checkmark_black);

                    edtAddress.setFocusable(true);
                    edtAddress.setFocusableInTouchMode(true);

                    edtPhone.setFocusable(true);
                    edtPhone.setFocusableInTouchMode(true);

                    edtAddress.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);

                    edtAddress.setLongClickable(true);
                    edtPhone.setLongClickable(true);

                    edtAddress.requestFocus();

                    InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else {
                    edtState = false;
                    fabEdit.setImageResource(R.drawable.edit);

                    edtAddress.setFocusable(false);
                    edtAddress.setFocusableInTouchMode(false);

                    edtPhone.setFocusable(false);
                    edtAddress.setFocusableInTouchMode(false);

                    edtAddress.setInputType(InputType.TYPE_NULL);
                    edtPhone.setInputType(InputType.TYPE_NULL);

                    edtAddress.setLongClickable(false);
                    edtPhone.setLongClickable(false);

                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        });

    }
}
