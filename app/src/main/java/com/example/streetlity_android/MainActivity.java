package com.example.streetlity_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.streetlity_android.User.Maintainer.Works;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("" ,"getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        Log.e("", token);
//                    }
//                });

        ImageButton btnFind = findViewById(R.id.btn_find);
        Button btnContribute = findViewById(R.id.btn_contribute);

        ImageView imgUser = findViewById(R.id.img_user_avatar);
        imgUser.setClipToOutline(true);

        SharedPreferences s = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        if (s.contains("token")){

            if(s.getInt("userType",0) == 7) {
                Button btnWork = findViewById(R.id.btn_works);
                btnWork.setVisibility(View.VISIBLE);
                btnWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, Works.class));
                    }
                });
            }

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
            btnContribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent t = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(t, 1);
                    //startActivity(new Intent(MainActivity.this, ContributeToService.class));
                }
            });
        }

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapNavigationHolder.class));
            }
        });

                FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Meo", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("Meo", token);
                    }
                });
//
//        String token = FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
        Log.println(Log.INFO, "", "hi mom");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Button btnContribute = findViewById(R.id.btn_contribute);

                if(((MyApplication) this.getApplication()).getUserType() == 7) {
                    Button btnWork = findViewById(R.id.btn_works);
                    btnWork.setVisibility(View.VISIBLE);
                    btnWork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, Works.class));
                        }
                    });
                }

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
                        startActivityForResult(new Intent(MainActivity.this, Login.class),1);
                    }
                });

                LinearLayout lo = findViewById(R.id.layout_user);
                lo.setVisibility(View.GONE);

                Button btnWork = findViewById(R.id.btn_works);
                btnWork.setVisibility(View.GONE);

                Toast toast = Toast.makeText(MainActivity.this, R.string.logged_out, Toast.LENGTH_LONG);
                toast.show();
            }

        } catch (Exception e) {
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
        }

    }
}
