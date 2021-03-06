package com.example.streetlity_android;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.streetlity_android.User.SignUp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

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

public class AddAMaintenance extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<File> arrImg = new ArrayList<>();
    boolean hasImg = false;

    private GoogleMap mMap;

    boolean firstClick = false;

    double mLat = -500;
    double mLon = -500;

    String mName = "";
    String mNote= "";
    String mAddress = "";

    private ViewPager mPager;
    private AddAMaintenance.MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<Integer> layouts;
    private Button btnPrevious, btnNext;

    EditText edtAddress;

    int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_maintenance);

        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        mPager = findViewById(R.id.view_pager);
        mPager.setOffscreenPageLimit(3);

        layouts = new ArrayList<>();
        layouts.add(R.layout.vp_maintenance_store_info);
        layouts.add(R.layout.vp_maintenance_store_location);
        layouts.add(R.layout.vp_maintenance_success);

        myViewPagerAdapter = new AddAMaintenance.MyViewPagerAdapter();
        mPager.setAdapter(myViewPagerAdapter);
        mPager.addOnPageChangeListener(mPagerPageChangeListener);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current < layouts.size()) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                    step--;
                } else {

                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPass = false;

                if(step == 0){
                    EditText edtName = mPager.findViewById(R.id.edt_store_name);
                    if(edtName.getText().toString() == ("")){
                        Toast toast = Toast.makeText(AddAMaintenance.this,  R.string.empty_name, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }
                    else{
                        mName = edtName.getText().toString();
                        EditText edtNote = findViewById(R.id.edt_store_note);
                        mNote = edtNote.toString();
                        isPass = true;
                    }
                }
                else if(step == 1){
                    edtAddress = findViewById(R.id.edt_store_address);
                    if(edtAddress.getText().toString().equals("")){
                        Toast toast = Toast.makeText(AddAMaintenance.this,  R.string.empty_address, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    } else if(mLon == -500 || mLat == -500){
                        Toast toast = Toast.makeText(AddAMaintenance.this,  R.string.please_select_location, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    } else{
                        mAddress = edtAddress.getText().toString();
                        addMaintenance();
                    }

                }
                else if(step == 2){
                    finish();
                }


                if(isPass){
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    int current = getItem(+1);
                    if (current < layouts.size()) {
                        // move to next screen
                        mPager.setCurrentItem(current);
                        step++;
                    } else {

                    }
                }
            }


        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions opt = new MarkerOptions().position(latLng).title("Here");

                mMap.clear();

                mMap.addMarker(opt);



                mLat = latLng.latitude;
                mLon = latLng.longitude;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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
                            Toast toast = Toast.makeText(AddAMaintenance.this, R.string.address_not_found, Toast.LENGTH_LONG);
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

                            mLat = jsonLatLng.getDouble("lat");
                            mLon = jsonLatLng.getDouble("lng");

                            LatLng location = new LatLng(mLat, mLon);

                            MarkerOptions opt = new MarkerOptions().position(location).title("Here");

                            edtAddress.setText(jsonObject1.getString("formatted_address"));

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

    public void addMaintenance(){
        Retrofit retro = new Retrofit.Builder().baseUrl(((MyApplication) this.getApplication()).getServiceURL())
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);

        String token = ((MyApplication) this.getApplication()).getToken();

        Call<ResponseBody> call = tour.addMaintenance("1.0.0",token,(float) mLat,(float) mLon, mAddress, mName, mNote);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    final JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());

                        if(jsonObject.getBoolean("Status")) {
                            btnNext.setText(R.string.finish);

                            int current = getItem(+1);
                            if (current < layouts.size()) {
                                // move to next screen
                                mPager.setCurrentItem(current);
                                step++;
                            }
                        }else{
                            Toast toast = Toast.makeText(AddAMaintenance.this, R.string.something_wrong, Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
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

    ViewPager.OnPageChangeListener mPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 0 || position == layouts.size()-1) {
                btnPrevious.setVisibility(View.GONE);
            } else {
                btnPrevious.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts.get(position), container, false);

            if(layouts.get(position) == R.layout.vp_maintenance_store_location){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(AddAMaintenance.this);

                edtAddress = view.findViewById(R.id.edt_store_address);

                ImageButton imgBtnSearch = view.findViewById(R.id.img_btn_search_address);
                imgBtnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getCurrentFocus() != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                        callGeocoding(edtAddress.getText().toString());
                    }
                });
            }

            if(layouts.get(position) == R.layout.vp_maintenance_store_info){
                EditText edtImg = view.findViewById(R.id.edt_select_img);
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
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
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

                    EditText edtSelectImg = mPager.findViewById(R.id.edt_select_img);
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

                        EditText edtSelectImg = mPager.findViewById(R.id.edt_select_img);
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
}
