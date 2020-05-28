package com.example.streetlity_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BroadcastActivity extends AppCompatActivity {

    float currLat;
    float currLon;

    boolean isOther = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        currLat = getIntent().getFloatExtra("currLat", -500);
        currLon = getIntent().getFloatExtra("currLon", -500);

        final EditText edtName = findViewById(R.id.edt_name);
        final EditText edtPhone = findViewById(R.id.edt_phone);
        final Spinner spnReason = findViewById(R.id.spn_reason);
        final EditText edtReason = findViewById(R.id.edt_reason);
        final EditText edtNote = findViewById(R.id.edt_note);

        ArrayList<String> arrReason = new ArrayList<>();

        arrReason.add(getString(R.string.select_reason_spinner));
        SharedPreferences s = getSharedPreferences("broadcastReason", Context.MODE_PRIVATE);
        if(s.contains("size")){
            for(int i =1; i< s.getInt("size", 0); i++){
                arrReason.add(s.getString("reason" + i, ""));
            }
        }
        arrReason.add(getString(R.string.other));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            R.layout.spinner_item_broadcast, arrReason);

        spnReason.setAdapter(adapter);

        LinearLayout layoutOther = findViewById(R.id.layout_other_reason);

        ImageButton imgAdd = findViewById(R.id.img_add);
        ImageButton imgRemove = findViewById(R.id.img_remove);

        spnReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnReason.getSelectedItem().toString().equals(getString(R.string.other))) {
                    isOther = true;
                    layoutOther.setVisibility(View.VISIBLE);
                }
                else{
                    isOther = false;
                    layoutOther.setVisibility(View.GONE);
                }

                if(spnReason.getSelectedItemPosition() == 0 || spnReason.getSelectedItemPosition() == arrReason.size() - 1){
                    imgRemove.setVisibility(View.GONE);
                }else{
                    imgRemove.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnConfirm = findViewById(R.id.btn_broadcast);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if(isOther){
                    if(edtReason.getText().toString().equals("")){
                        Toast toast = Toast.makeText(BroadcastActivity.this, R.string.please_select_reason, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }else{
                        sendBroadcast( edtReason.getText().toString(), edtName.getText().toString(),
                                edtPhone.getText().toString(),edtNote.getText().toString(), currLat, currLon);
                    }
                }else {
                    if (spnReason.getSelectedItemPosition() == 0) {
                        Toast toast = Toast.makeText(BroadcastActivity.this, R.string.please_select_reason, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    } else {
                        sendBroadcast(spnReason.getSelectedItem().toString(), edtName.getText().toString(),
                                edtPhone.getText().toString(), edtNote.getText().toString(), currLat, currLon);
                    }
                }
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtReason.getText().equals("")) {
                    arrReason.set(arrReason.size() -1, edtReason.getText().toString());
                    arrReason.add(getString(R.string.other));
                    imgRemove.setVisibility(View.VISIBLE);
                    layoutOther.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    SharedPreferences s = getSharedPreferences("broadcastReason", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = s.edit();
                    e.clear();
                    e.apply();
                    e.putInt("size", arrReason.size()-2);
                    for(int i=1; i<arrReason.size() - 1; i++){
                        e.putString("reason"+i, arrReason.get(i));
                    }
                }
                else {
                    Toast toast = Toast.makeText(BroadcastActivity.this, R.string.empty_reason, Toast.LENGTH_LONG);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    tv.setTextColor(Color.RED);

                    toast.show();
                }
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrReason.remove(spnReason.getSelectedItemPosition());
                adapter.notifyDataSetChanged();
                if(spnReason.getSelectedItemPosition() == 0 || spnReason.getSelectedItemPosition() == arrReason.size() - 1){
                    imgRemove.setVisibility(View.GONE);
                }
                if( spnReason.getSelectedItemPosition() == arrReason.size() - 1){
                    layoutOther.setVisibility(View.VISIBLE);
                }
                SharedPreferences s = getSharedPreferences("broadcastReason", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.clear();
                e.apply();
                e.putInt("size", arrReason.size()-2);
                for(int i=1; i<arrReason.size() - 1; i++){
                    e.putString("reason"+i, arrReason.get(i));
                }
            }
        });
    }


    public void sendBroadcast(final String reason, final String name, final String phone, final String note, double lat, double lon){
        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.207.83/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.getMaintenanceInRange("1.0.0",(float)lat,(float)lon,(float)15);
        //Call<ResponseBody> call = tour.getAllATM();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    final JSONObject jsonObject;
                    final JSONArray jsonArray;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());
                        jsonArray = jsonObject.getJSONArray("Maintenances");

                        final ArrayList<Integer> idList = new ArrayList<>();

                        for (int i = 0; i< jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            idList.add(jsonObject1.getInt("Id"));
                        }

                        int[] id = new int[idList.size()];

                        for(int i =0; i< idList.size();i++){
                            id[i] = idList.get(i);
                        }

                        Call<ResponseBody> call2 = tour.broadcast("1.0.0", reason, name, phone, note, id, "", "");
                        call2.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                JSONObject jsonObject;
                                if(response.code() == 200) {
                                    Log.e("", "onResponse: " + response.raw().request());
                                    try{
                                        jsonObject = new JSONObject(response.body().string());
                                        Log.e("", "onResponse: " + jsonObject.toString());
                                        if (jsonObject.getBoolean("Status")){
                                            Intent data = new Intent();
                                            data.putExtra("numStore", idList.size());
                                            setResult(RESULT_OK, data);
                                            finish();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    try{ ;
                                        Log.e("", "onResponse: " + response.code());
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


                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        Log.e(", ",response.errorBody().toString() + response.code());
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

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();

        return true;
    }
}
