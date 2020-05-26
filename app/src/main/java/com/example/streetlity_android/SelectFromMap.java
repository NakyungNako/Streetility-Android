package com.example.streetlity_android;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectFromMap extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<File> arrImg = new ArrayList<>();
    boolean hasImg = false;

    private GoogleMap mMap;

    boolean firstClick = false;

    double latToAdd=-500;
    double lonToAdd=-500;

    EditText edtAddress;

    String mNote, mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_map);

        Intent t = getIntent();
        final int type = t.getIntExtra("type", -1);

        TextView tvTittle = findViewById(R.id.tv_title);

        if (type == 1){
            tvTittle.setText(getString(R.string.add_fuel));
        }
        else if (type == 2){
            tvTittle.setText(getString(R.string.add_wc));
        }

        EditText edtNote = findViewById(R.id.edt_note);
        edtAddress = findViewById(R.id.edt_store_address);

        Button confirm = findViewById(R.id.btn_confirm_adding);

        EditText edtImg = findViewById(R.id.edt_select_img);
        edtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
                if (edtAddress.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(SelectFromMap.this, R.string.empty_address, Toast.LENGTH_LONG);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    tv.setTextColor(Color.RED);

                    toast.show();
                }else if(latToAdd == -500 || lonToAdd == -500){
                    Toast toast = Toast.makeText(SelectFromMap.this, R.string.please_select_location, Toast.LENGTH_LONG);
                    TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                    tv.setTextColor(Color.RED);

                    toast.show();
                } else {

                    mNote = edtNote.getText().toString();
                    mAddress = edtAddress.getText().toString();
                    if (type == 1) {
                        addFuel();
                    }
                    if (type == 2) {
                        addWC();
                    }
                }
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton imgSearch = findViewById(R.id.img_btn_search_address);


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGeocoding(edtAddress.getText().toString());
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //ArrayList<MarkerOptions> markList = new ArrayList<MarkerOptions>();

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions opt = new MarkerOptions().position(latLng).title("Here");
                if (firstClick == true) {
                    mMap.clear();
                }

                mMap.addMarker(opt);

                if (firstClick == false) {
                    firstClick = true;
                    Button confirm = findViewById(R.id.btn_confirm_adding);
                    confirm.setVisibility(View.VISIBLE);
                }

                latToAdd = latLng.latitude;
                lonToAdd = latLng.longitude;
            }
        });
    }

    public void addFuel(){
        Retrofit retro = new Retrofit.Builder().baseUrl(((MyApplication) this.getApplication()).getServiceURL())
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.addFuel("1.0.0",((MyApplication) this.getApplication()).getToken(),
                (float)latToAdd,(float)lonToAdd, mAddress, mNote);
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
                            Intent data = new Intent();
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {

                        Log.e("", "onResponse: " + response.errorBody().toString());
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

    public void addWC(){
        Retrofit retro = new Retrofit.Builder().baseUrl(((MyApplication) this.getApplication()).getServiceURL())
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);

        String token = ((MyApplication) this.getApplication()).getToken();

        Call<ResponseBody> call = tour.addWC("1.0.0",token,(float)latToAdd,(float)lonToAdd, mAddress, mNote);
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
                            Intent data = new Intent();
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        Log.e(", ",response.errorBody().toString());

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

    public void callGeocoding(String address){
        Retrofit retro = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.geocode(address, "AIzaSyB56CeF7ccQ9ZeMn0O4QkwlAQVX7K97-Ss");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final JSONObject jsonObject;
                if(response.code() == 0 || response.code() == 200) {

                    JSONArray jsonArray;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());

                        if(jsonObject.getString("status").equals("ZERO_RESULTS")){
                            Toast toast = Toast.makeText(SelectFromMap.this, "Address not found", Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        }
                        else{
                            mMap.clear();

                            jsonArray = jsonObject.getJSONArray("results");

                            JSONObject jsonObject1;
                            jsonObject1 = jsonArray.getJSONObject(0);

                            JSONObject jsonObjectGeomertry = jsonObject1.getJSONObject("geometry");
                            JSONObject jsonLatLng = jsonObjectGeomertry.getJSONObject("location");

                            latToAdd = jsonLatLng.getDouble("lat");
                            lonToAdd = jsonLatLng.getDouble("lng");

                            LatLng location = new LatLng(latToAdd,lonToAdd);

                            MarkerOptions opt = new MarkerOptions().position(location).title("Here");

                            edtAddress.setText(jsonObject1.getString("formatted_address"));

                            if(firstClick == false){
                                firstClick =true;
                                Button confirm = findViewById(R.id.btn_confirm_adding);
                                confirm.setVisibility(View.VISIBLE);
                            }

                            mMap.addMarker(opt);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                            mMap.animateCamera( CameraUpdateFactory.zoomTo( 18.0f ));
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        Log.e(", ",response.errorBody().toString() + response.code());
                        Log.e("", "onResponse: " + response.errorBody());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();
                    File file = new File(mImageUri.getPath());

                    arrImg.add(file);

                    Log.e("", "onActivityResult: " + arrImg.size() );

                    EditText edtSelectImg = findViewById(R.id.edt_select_img);
                    String temp = getString(R.string.selected);
                    temp = temp + " 1 " +getString(R.string.images);
                    edtSelectImg.setHint(temp);
                    hasImg = true;
                } else{
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            File file = new File(uri.getPath());

                            arrImg.add(file);
                        }

                        Log.e("", "onActivityResult: " + arrImg.size() );

                        EditText edtSelectImg = findViewById(R.id.edt_select_img);
                        String temp = getString(R.string.selected);
                        temp = temp + " " +arrImg.size()+ " " +getString(R.string.images);
                        edtSelectImg.setHint(temp);
                        hasImg = true;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
        }

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
