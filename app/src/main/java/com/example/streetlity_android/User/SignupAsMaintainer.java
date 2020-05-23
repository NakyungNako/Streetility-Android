package com.example.streetlity_android.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.streetlity_android.MapAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streetlity_android.MapFragment.MaintenanceObject;
import com.example.streetlity_android.R;
import com.example.streetlity_android.WorkaroundMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupAsMaintainer extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    ArrayList<Marker> mMarkers = new ArrayList<>();
    ArrayList<MaintenanceObject> items = new ArrayList<>();

    int id = -1;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_as_maintainer);
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnSighUp = findViewById(R.id.btn_signup);
        btnSighUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                if(checkFields(edtUsername.getText().toString(), edtPassword.getText().toString(), edtCFPassword.getText().toString(),
                        edtMail.getText().toString(),edtPhone.getText().toString(),edtAddress.getText().toString())){

                    Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    final MapAPI tour = retro.create(MapAPI.class);

                    Call<ResponseBody> call = tour.signUpMaintainer(edtUsername.getText().toString(), edtPassword.getText().toString(),
                            edtMail.getText().toString(), edtPhone.getText().toString(), edtAddress.getText().toString(), id);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            JSONObject jsonObject;
                            if (response.code() == 200) {
                                try {
                                    jsonObject = new JSONObject(response.body().string());
                                    Log.e("", "onResponse: "+ jsonObject.toString() + response.code());
                                    if(jsonObject.getBoolean("Status")) {
                                        Intent data = new Intent();
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }else{
                                        Toast toast = Toast.makeText(SignupAsMaintainer.this, jsonObject.getString("Message"), Toast.LENGTH_LONG);
                                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                                        tv.setTextColor(Color.RED);

                                        toast.show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Log.e("", "onResponse: "+  response.code());
                                    Toast toast = Toast.makeText(SignupAsMaintainer.this, "Username or Email existed", Toast.LENGTH_LONG);
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

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public boolean checkFields(String username, String pass, String cfPass, String email, String phoneNumber, String address){
        if(username.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Username is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(pass.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Password is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(cfPass.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Confirm password is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(email.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Email is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(!pass.equals(cfPass)){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Password mismatch", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(phoneNumber.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Phone number is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }
        else if(address.equals("")){
            Toast toast = Toast.makeText(SignupAsMaintainer.this, "Address is empty", Toast.LENGTH_LONG);
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setTextColor(Color.RED);

            toast.show();
            return false;
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        final ScrollView mScrollView = findViewById(R.id.scrollview); //parent scrollview in xml, give your scrollview id value
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        double latitude = 0;
        double longitude = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationManager
                    .NETWORK_PROVIDER);
            if(location == null){
                Log.e("", "onMapReady: MULL");
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("", "onMapReady: " + latitude+" , " + longitude );
        }

        getLocations((float)latitude,(float)longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );
    }

    public void addMaintenanceMarkerToList(float lat, float lon, String name){
        LatLng pos = new LatLng(lat,lon);
        MarkerOptions option = new MarkerOptions();
        option.title(name);
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_maintenance));
        option.position(pos);
        Marker marker = mMap.addMarker(option);
        mMarkers.add(marker);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();

        for (int i = 0; i<mMarkers.size();i++){
            if(marker.equals(mMarkers.get(i))){
                id = items.get(i).getId();
                EditText edtStore = findViewById(R.id.edt_store);
                edtStore.setText(items.get(i).getName());
                EditText edtAddress = findViewById(R.id.edt_address);
                edtAddress.setText(items.get(i).getAddress());
                break;
            }
        }

        return true;
    }

    public void getLocations(float lat, float lon){
        mMap.clear();
        mMarkers.removeAll(mMarkers);
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
                    JSONArray jsonArray;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());
                        if(jsonObject.getBoolean("Status")) {
                            jsonArray = jsonObject.getJSONArray("Maintenances");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Log.e("", "onResponse: " + jsonObject1.toString());
                                addMaintenanceMarkerToList((float) jsonObject1.getDouble("Lat"),
                                        (float) jsonObject1.getDouble("Lon"), jsonObject1.getString("Name"));

                                MaintenanceObject object = new MaintenanceObject(jsonObject1.getInt("Id"),
                                        jsonObject1.getString("Name"), (float) jsonObject1.getDouble("Lat"),
                                        (float) jsonObject1.getDouble("Lon"), jsonObject1.getString("Note"),
                                        jsonObject1.getString("Address"));

                                items.add(object);
                            }
                        }
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
}
