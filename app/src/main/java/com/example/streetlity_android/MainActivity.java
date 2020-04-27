package com.example.streetlity_android;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streetlity_android.User.Login;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnFind = findViewById(R.id.btn_find);
        Button btnContribute = findViewById(R.id.btn_contribute);

        ImageView imgUser = findViewById(R.id.img_user_avatar);
        imgUser.setClipToOutline(true);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapNavigationHolder.class));
            }
        });

        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this, Login.class);
                startActivityForResult(t, 1);
                //startActivity(new Intent(MainActivity.this, ContributeToService.class));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Button btnContribute = findViewById(R.id.btn_contribute);

                btnContribute.setText(getString(R.string.contribute));
                btnContribute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ContributeToService.class));
                    }
                });

                TextView tvUsername = findViewById(R.id.tv_username);
                tvUsername.setText(data.getStringExtra("username"));

                LinearLayout lo = findViewById(R.id.layout_user);
                lo.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}
