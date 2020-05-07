package com.example.streetlity_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.streetlity_android.User.UserInfo;

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

        SharedPreferences s = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        if (s.contains("token")){

            ((MyApplication) this.getApplication()).setToken(s.getString("token",""));
            ((MyApplication) this.getApplication()).setRefreshToken(s.getString("refreshToken",""));
            ((MyApplication) this.getApplication()).setToken(s.getString("username",""));

            btnContribute = findViewById(R.id.btn_contribute);

            btnContribute.setText(getString(R.string.contribute));
            btnContribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ContributeToService.class));
                }
            });

            final TextView tvUsername = findViewById(R.id.tv_username);
            tvUsername.setText(s.getString("username", ""));

            LinearLayout lo = findViewById(R.id.layout_user);
            lo.setVisibility(View.VISIBLE);

            lo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent t = new Intent(MainActivity.this, UserInfo.class);
                    t.putExtra("username", tvUsername.getText().toString());
                    startActivityForResult(t, 2);
                }
            });
        }else {

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

                final TextView tvUsername = findViewById(R.id.tv_username);
                tvUsername.setText(data.getStringExtra("username"));

                LinearLayout lo = findViewById(R.id.layout_user);
                lo.setVisibility(View.VISIBLE);

                lo.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent t = new Intent(MainActivity.this, UserInfo.class);
                        t.putExtra("username", tvUsername.getText().toString());
                        startActivityForResult(t, 2);
                    }
                });
            }else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                Button btnContribute = findViewById(R.id.btn_contribute);

                btnContribute.setText(getString(R.string.main_login));
                btnContribute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                });

                LinearLayout lo = findViewById(R.id.layout_user);
                lo.setVisibility(View.GONE);

                Toast toast = Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}
