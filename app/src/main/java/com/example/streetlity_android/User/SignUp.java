package com.example.streetlity_android.User;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.streetlity_android.MapAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streetlity_android.MapFragment.MaintenanceObject;
import com.example.streetlity_android.MyApplication;
import com.example.streetlity_android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class SignUp extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    ArrayList<Marker> mMarkers = new ArrayList<>();
    ArrayList<MaintenanceObject> items = new ArrayList<>();
    ArrayList<File> arrImg = new ArrayList<>();

    boolean hasImg = false;

    private GoogleMap mMap;
    String username = "";
    String pass = "";
    String cfPass = "";
    String mail = "";
    String phone = "";
    String address = "";
    int type = -1;
    int id = -1;
    String mName = "";
    double mLat = -500;
    double mLon = -500;
    String mNote= "";
    String mAddress = "";

    int exist=-1;

    boolean changed = false;

    private ViewPager mPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<Integer> layouts;
    private Button btnPrevious, btnNext;

    int step = 0;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private MyViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        mPager = findViewById(R.id.view_pager);
        mPager.setOffscreenPageLimit(7);

        layouts = new ArrayList<>();
        layouts.add(R.layout.vp_signup_select_user_type);
        layouts.add(R.layout.vp_signup_main_info);
        layouts.add(R.layout.vp_signup_additional_info);

        myViewPagerAdapter = new MyViewPagerAdapter();
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

                // checking for last page
                // if last page home screen will be launched
                if(step == 0){
                    RadioButton rdoCommon = mPager.findViewById(R.id.rdo_common);
                    RadioButton rdoOwner = mPager.findViewById(R.id.rdo_owner);
                    if(rdoCommon.isChecked()){
                        type = 0;
                        if(layouts.contains(R.layout.vp_signup_store_exist)) {
                            layouts.remove(3);
                            myViewPagerAdapter.notifyDataSetChanged();

                            changed = true;
                        }
                        if(!layouts.contains(R.layout.vp_signup_success)) {
                            layouts.add(R.layout.vp_signup_success);
                            myViewPagerAdapter.notifyDataSetChanged();
                        }
                        isPass = true;
                    }
                    else if(rdoOwner.isChecked()){
                        type = 1;
                        if(layouts.contains(R.layout.vp_signup_success)) {
                            layouts.remove(3);
                            myViewPagerAdapter.notifyDataSetChanged();
                            changed = true;
                        }
                        if(!layouts.contains(R.layout.vp_signup_store_exist)) {
                            layouts.add(R.layout.vp_signup_store_exist);
                            myViewPagerAdapter.notifyDataSetChanged();
                        }
                        isPass = true;
                    }
                    else if(!rdoCommon.isChecked() && !rdoOwner.isChecked()){
                        Toast toast = Toast.makeText(SignUp.this, R.string.please_select_type, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }

                }

                if(step ==1){
                    EditText edtUsername = mPager.findViewById(R.id.edt_username);
                    EditText edtEmail = mPager.findViewById(R.id.edt_email);
                    EditText edtPassword = mPager.findViewById(R.id.edt_password);
                    EditText edtCFPassword = mPager.findViewById(R.id.edt_cfpassword);

                    if(edtUsername.getText().toString().equals("")){
                        Toast toast = Toast.makeText(SignUp.this, R.string.empty_user_name, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }else if(edtUsername.getText().length() < 6){
                        Toast toast = Toast.makeText(SignUp.this, R.string.username_short, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }else if(edtEmail.getText().toString().equals("")){
                        Toast toast = Toast.makeText(SignUp.this, R.string.empty_user_mail, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }
                    else if(edtPassword.getText().toString().equals("")){
                        Toast toast = Toast.makeText(SignUp.this, R.string.empty_pass, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    } else if(edtPassword.getText().toString().length() < 6){
                        Toast toast = Toast.makeText(SignUp.this, R.string.pass_short, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }
                    else if(edtCFPassword.getText().toString().equals("")){
                        Toast toast = Toast.makeText(SignUp.this, R.string.empty_cf_pass, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    } else if(!edtCFPassword.getText().toString().equals(edtPassword.getText().toString())){
                        Toast toast = Toast.makeText(SignUp.this, R.string.password_mismatch, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();

                    } else{
                        username = edtUsername.getText().toString();
                        mail = edtEmail.getText().toString();
                        pass = edtPassword.getText().toString();
                        cfPass = edtCFPassword.getText().toString();

                        isPass = true;
                    }
                }

                if(step == 2){
                    EditText edtAddress = mPager.findViewById(R.id.edt_address);
                    EditText edtPhone = mPager.findViewById(R.id.edt_phone);

                    address = edtAddress.getText().toString();
                    phone = edtPhone.getText().toString();

                    isPass = true;
                }

                if(step == 2 && type == 0){
                    Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    final MapAPI tour = retro.create(MapAPI.class);

                    Call<ResponseBody> call = tour.signUpCommon(username,pass,mail,address,phone);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    Log.e("", "onResponse: " + jsonObject.toString());
                                    if (jsonObject.getBoolean("Status")) {

                                        btnNext.setText(R.string.finish);

                                        int current = getItem(+1);
                                        if (current < layouts.size()) {
                                            // move to next screen
                                            mPager.setCurrentItem(current);
                                            step++;
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(SignUp.this, jsonObject.getString("Message"), Toast.LENGTH_LONG);
                                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                                        tv.setTextColor(Color.RED);

                                        toast.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Log.e("", "onResponse: " + response.code());
                                    Toast toast = Toast.makeText(SignUp.this, "Username or Email existed", Toast.LENGTH_LONG);
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


                if(step == 3 && type ==0){
                    Intent t = getIntent();
                    int from = t.getIntExtra("from", 1);
                    if(from == 1){
                        Intent t2 = new Intent(SignUp.this, Login.class);
                        t2.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(t2);

                        finish();
                    }
                    else if (from == 2){
                        finish();
                    }
                }else if (step == 3 && type == 1) {
                    RadioButton rdoYes = mPager.findViewById(R.id.rdo_yes);
                    RadioButton rdoNo = mPager.findViewById(R.id.rdo_no);
                    if(!rdoYes.isChecked() && !rdoNo.isChecked()){
                        Toast toast = Toast.makeText(SignUp.this, R.string.please_select_option, Toast.LENGTH_LONG);
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setTextColor(Color.RED);

                        toast.show();
                    }
                    else if(rdoNo.isChecked()){
                        exist = 0;
                        if(layouts.contains(R.layout.vp_signup_store_select) &&
                                layouts.contains(R.layout.vp_signup_success)) {
                            layouts.remove(5);
                            layouts.remove(4);
                            myViewPagerAdapter.notifyDataSetChanged();

                            changed = true;
                        }
                        if(!layouts.contains(R.layout.vp_signup_store_location) &&
                                !layouts.contains(R.layout.vp_signup_store_info) &&
                                !layouts.contains(R.layout.vp_signup_success)) {
                            layouts.add(R.layout.vp_signup_store_info);
                            layouts.add(R.layout.vp_signup_store_location);
                            layouts.add(R.layout.vp_signup_success);
                            myViewPagerAdapter.notifyDataSetChanged();
                        }
                        isPass = true;
                    }
                    else if(rdoYes.isChecked()){
                        exist = 1;
                        if(layouts.contains(R.layout.vp_signup_store_location) &&
                                layouts.contains(R.layout.vp_signup_store_info) &&
                                layouts.contains(R.layout.vp_signup_success)) {
                            layouts.remove(6);
                            layouts.remove(5);
                            layouts.remove(4);
                            myViewPagerAdapter.notifyDataSetChanged();

                            changed = true;
                        }
                        if(!layouts.contains(R.layout.vp_signup_store_select) &&
                                !layouts.contains(R.layout.vp_signup_success)) {
                            layouts.add(R.layout.vp_signup_store_select);
                            layouts.add(R.layout.vp_signup_success);
                            myViewPagerAdapter.notifyDataSetChanged();
                        }
                        isPass = true;
                    }
                }

                if(exist == 0){
                    if (step == 5){
                        EditText edtStoreAddress = mPager.findViewById(R.id.edt_store_address);
                        if(edtStoreAddress.getText().toString().equals("")){
                            Toast toast = Toast.makeText(SignUp.this, R.string.empty_address, Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        }
                        else if(mLat == -500 || mLon == -500){
                            Toast toast = Toast.makeText(SignUp.this, R.string.please_select_location, Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        }
                        else{
                            mAddress = edtStoreAddress.getText().toString();
                            Log.e("", "onClick: " + mAddress );
                            isPass = true;
                        }
                    }
                    if (step == 4){
                        EditText edtName = mPager.findViewById(R.id.edt_store_name);
                        EditText edtNote = mPager.findViewById(R.id.edt_store_note);
                        if(edtName.getText().toString().equals("")){
                            Toast toast = Toast.makeText(SignUp.this, R.string.empty_name, Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        } else{
                            addMaintenance();
                        }
                    }
                    if (step == 6){
                        Intent t = getIntent();
                        int from = t.getIntExtra("from", 1);
                        if(from == 1){
                            Intent t2 = new Intent(SignUp.this, Login.class);
                            t2.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(t2);

                            finish();
                        }
                        else if (from == 2){
                            finish();
                        }
                    }
                }else if (exist == 1){
                    if (step == 4){
                        if(id == -1){
                            Toast toast = Toast.makeText(SignUp.this, R.string.please_select_store, Toast.LENGTH_LONG);
                            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                            tv.setTextColor(Color.RED);

                            toast.show();
                        }else{
                            signUpMaintain();
                            isPass = true;
                        }
                    }
                    if (step == 5){
                        Intent t = getIntent();
                        int from = t.getIntExtra("from", 1);
                        if(from == 1){
                            Intent t2 = new Intent(SignUp.this, Login.class);
                            t2.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(t2);

                            finish();
                        }
                        else if (from == 2){
                            finish();
                        }
                    }
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


//        final EditText edtUsername = findViewById(R.id.edt_username);
//        final EditText edtPassword = findViewById(R.id.edt_password);
//        final EditText edtCFPassword = findViewById(R.id.edt_cfpassword);
//        final EditText edtMail = findViewById(R.id.edt_email);
//        final EditText edtPhone = findViewById(R.id.edt_phone);
//        final EditText edtAddress = findViewById(R.id.edt_address);

//        Button btnSighUp = findViewById(R.id.btn_signup);
//        btnSighUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getCurrentFocus() != null) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
//
//



    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener mPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 0 || position == layouts.size()-1) {
                btnPrevious.setVisibility(View.GONE);

                if((step == 2 && type == 1)){
                    btnPrevious.setVisibility(View.VISIBLE);
                }

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

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts.get(position), container, false);

            if(layouts.get(position) == R.layout.vp_signup_store_location){
                EditText edtAddress = view.findViewById(R.id.edt_store_address);

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

            if(layouts.get(position) == R.layout.vp_signup_store_select ||
                    layouts.get(position) == R.layout.vp_signup_store_location){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(SignUp.this);
            }

            if(layouts.get(position) == R.layout.vp_signup_success){
                if(type == 1){
                    TextView tvSuccess = view.findViewById(R.id.tv_success);
                    tvSuccess.setText(R.string.signup_success_owner);
                }
            }

            if(layouts.get(position) == R.layout.vp_signup_store_info){
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

        @Override
        public int getItemPosition(Object object)
        {
            if (changed) {
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }
    }

    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

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

        if(exist == 1) {
            getLocations((float) latitude, (float) longitude);
        } else if (exist == 0){
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    MarkerOptions opt = new MarkerOptions().position(latLng).title("Here");

                    mMap.addMarker(opt);

                    mLat = latLng.latitude;
                    mLon = latLng.longitude;
                }
            });
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );
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
        if(exist == 1) {
            for (int i = 0; i < mMarkers.size(); i++) {
                if (marker.equals(mMarkers.get(i))) {
                    id = items.get(i).getId();
                    EditText edtStore = mPager.findViewById(R.id.edt_store);
                    edtStore.setText(items.get(i).getName());
                    break;
                }
            }
        }
        return true;
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
                            Toast toast = Toast.makeText(SignUp.this, "Address not found", Toast.LENGTH_LONG);
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

                            LatLng location = new LatLng(mLat,mLon);

                            MarkerOptions opt = new MarkerOptions().position(location).title("Here");

                            EditText edtAddress = mPager.findViewById(R.id.edt_address);

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

    public void signUpMaintain(){
        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.232.218/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);

        Call<ResponseBody> call = tour.signUpMaintainer(username, pass, mail,phone,address, id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject;
                if (response.code() == 200) {
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: "+ jsonObject.toString() + response.code());
                        if(jsonObject.getBoolean("Status")) {
                            btnNext.setText(R.string.finish);

                            int current = getItem(+1);
                            if (current < layouts.size()) {
                                // move to next screen
                                mPager.setCurrentItem(current);
                                step++;
                            }
                        }else{
                            Toast toast = Toast.makeText(SignUp.this, jsonObject.getString("Message"), Toast.LENGTH_LONG);
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
                        Toast toast = Toast.makeText(SignUp.this, "Username or Email existed", Toast.LENGTH_LONG);
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

    public void addMaintenance(){
        Retrofit retro = new Retrofit.Builder().baseUrl(((MyApplication) this.getApplication()).getServiceURL())
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);

        String token = ((MyApplication) this.getApplication()).getToken();

        Call<ResponseBody> call = tour.addMaintenance("1.0.0",token,(float)mLat,(float)mLon, mAddress, mName, mNote);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    final JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());

                        if(jsonObject.getBoolean("Status")) {
                            id = 0;
                            signUpMaintain();
                        }else{
                            Toast toast = Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_LONG);
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