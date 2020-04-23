//package com.example.streetlity_android;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.widget.ListView;
//
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//
//        String[] Permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//        if (!hasPermissions(this, Permissions)) {
//            ActivityCompat.requestPermissions(this, Permissions, 4);
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            this.finish();
//            return;
//        }
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override

//

//
//    public void addATMMarkerToList(float lat, float lon, String type){
//        LatLng pos = new LatLng(lat,lon);
//        MarkerOptions option = new MarkerOptions();
//        option.title(type);
//        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.atm_icon));
//        option.position(pos);
//        mMarkers.add(option);
//    }
//
//    public void addMaintenanceMarkerToList(float lat, float lon, String name){
//        LatLng pos = new LatLng(lat,lon);
//        MarkerOptions option = new MarkerOptions();
//        option.title(name);
//        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.fix_icon));
//        option.position(pos);
//        mMarkers.add(option);
//    }
//
//    public void addWCMarkerToList(float lat, float lon){
//        LatLng pos = new LatLng(lat,lon);
//        MarkerOptions option = new MarkerOptions();
//        option.title("WC");
//        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.wc_icon));
//        option.position(pos);
//        mMarkers.add(option);
//    }
//

//
//    public void callATM(double lat, double lon){
//        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.207.83/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        final MapAPI tour = retro.create(MapAPI.class);
//        //Call<ResponseBody> call = tour.getATMInRange((float)lat,(float)lon,(float)0.1);
//        Call<ResponseBody> call = tour.getAllATM();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.code() == 200) {
//                    final JSONObject jsonObject;
//                    JSONArray jsonArray;
//                    try {
//                        jsonObject = new JSONObject(response.body().string());
//                        Log.e("", "onResponse: " + jsonObject.toString());
//                        jsonArray = jsonObject.getJSONArray("Fuels");
//
//                        for (int i = 0; i< jsonArray.length();i++){
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            Log.e("", "onResponse: " + jsonObject1.toString());
//                            addATMMarkerToList((float)jsonObject1.getDouble("Lat"),
//                                    (float)jsonObject1.getDouble("Lon"),"atm type here");
//                        }
//
//                        for (int i = 0; i < mMarkers.size(); i++){
//                            Log.e("", mMarkers.get(i).getTitle());
//                            mMap.addMarker(mMarkers.get(i));
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("", "onFailure: " + t.toString());
//            }
//        });
//    }
//
//    public void callMaintenance(double lat, double lon){
//
//    }
//
//    public void callWC(double lat, double lon){
//        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.207.83/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        final MapAPI tour = retro.create(MapAPI.class);
//        //Call<ResponseBody> call = tour.getWCInRange((float)lat,(float)lon,(float)0.1);
//        Call<ResponseBody> call = tour.getAllWC();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.code() == 200) {
//                    final JSONObject jsonObject;
//                    JSONArray jsonArray;
//                    try {
//                        jsonObject = new JSONObject(response.body().string());
//                        Log.e("", "onResponse: " + jsonObject.toString());
//                        jsonArray = jsonObject.getJSONArray("Fuels");
//
//                        for (int i = 0; i< jsonArray.length();i++){
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            Log.e("", "onResponse: " + jsonObject1.toString());
//                            addWCMarkerToList((float)jsonObject1.getDouble("Lat"),
//                                    (float)jsonObject1.getDouble("Lon"));
//                        }
//
//                        for (int i = 0; i < mMarkers.size(); i++){
//                            Log.e("", mMarkers.get(i).getTitle());
//                            mMap.addMarker(mMarkers.get(i));
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("", "onFailure: " + t.toString());
//            }
//        });
//    }
//
//    public void onLocationChanged(Location location) {
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
//        mMap.animateCamera(cameraUpdate);
//    }
//
//
//}

package com.example.streetlity_android.MapFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.streetlity_android.MapAPI;
import com.example.streetlity_android.R;
import com.example.streetlity_android.Review;
import com.example.streetlity_android.ReviewAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 * Use the {@link #newInstance} factory method to
 * create an instance of this fragment.
 */
public class FuelFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    Marker currentPosition;

    ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FuelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new_trip instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new_trip instance of fragment SuggestTour.
     */
    // TODO: Rename and change types and number of parameters
    public static FuelFragment newInstance(String param1, String param2) {
        FuelFragment fragment = new FuelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
              //  .findFragmentById(R.id.map);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

            String[] Permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(getActivity(), Permissions)) {
            ActivityCompat.requestPermissions(getActivity(), Permissions, 4);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            getActivity().finish();
            return;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

        public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        //ArrayList<MarkerOptions> markList = new ArrayList<MarkerOptions>();

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        double latitude = 0;
        double longitude = 0;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationManager
                    .GPS_PROVIDER);
            if(location == null){
                Log.e("", "onMapReady: MULL");
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("", "onMapReady: " + latitude+" , " + longitude );
        }

//        if (type == 1) {
        callFuel(latitude,longitude);
//        }
//        else if (type == 2) {
//            callATM(latitude,longitude);
//        }
//        else if (type == 3) {
//            //callFuel(latitude,longitude);
//        }
//        else if (type == 4) {
//            callWC(latitude,longitude);
//        }

        // Add a marker in Sydney and move the camera
//        MarkerOptions option = new MarkerOptions();
//        MarkerOptions option2 = new MarkerOptions();
//        MarkerOptions option3 = new MarkerOptions();
//
//        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
//        option.title("Marker in Sydney");
//        option.position(sydney);
//        markList.add(option);
//
//        sydney = new LatLng(-34, 161);
//        option2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
//        option2.title("Marker in awaswa");
//        option2.position(sydney);
//        markList.add(option2);
//
//        sydney = new LatLng(-54, 161);
//        option3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
//        option3.title("Marker in awaswa");
//        option3.position(sydney);
//        markList.add(option3);
//
//        for (int i = 0; i < markList.size(); i++) {
//            mMap.addMarker(markList.get(i));
//            Log.e("abc", "aa" );
//        }

        MarkerOptions curPositionMark = new MarkerOptions();
        curPositionMark.position(new LatLng(latitude,longitude));
        curPositionMark.title("You are here");

        currentPosition = mMap.addMarker(curPositionMark);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if(!marker.equals(currentPosition)) {

            final LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

            final android.view.View dialogView = inflater.inflate(R.layout.dialog_point_info, null);

            ListView lv = dialogView.findViewById(R.id.lv_review);

            ArrayList<Review> items = new ArrayList<Review>();

            items.add(new Review("nhut", "i donek know kaahfeeefffffffeijkla jkl ja klj akljfklajj kajkljw klj lkaj eklwaj elkjwa kljela ej l", (float)2.5));

            final ReviewAdapter adapter = new ReviewAdapter(getActivity(), R.layout.review_item, items);

            lv.setAdapter(adapter);

            Log.e("", "onMarkerClick: mapclick");
            marker.showInfoWindow();

            BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            dialog.show();

        }

        marker.showInfoWindow();

        return true;
    }

        public void addFuelMarkerToList(float lat, float lon){
        LatLng pos = new LatLng(lat,lon);
        MarkerOptions option = new MarkerOptions();
        option.title("Fuel");
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
        option.position(pos);
        mMarkers.add(option);
    }

        public void callFuel(double lat, double lon){
        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.207.83/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.getFuelInRange((float)lat, (float)lon,2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    final JSONObject jsonObject;
                    JSONArray jsonArray;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e("", "onResponse: " + jsonObject.toString());
                        if(jsonObject.getJSONArray("Fuels").toString() != "null") {
                            jsonArray = jsonObject.getJSONArray("Fuels");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Log.e("", "onResponse: " + jsonObject1.toString());
                                addFuelMarkerToList((float) jsonObject1.getDouble("Lat"),
                                        (float) jsonObject1.getDouble("Lon"));
                            }

                            for (int i = 0; i < mMarkers.size(); i++) {
                                Log.e("", mMarkers.get(i).getTitle());
                                mMap.addMarker(mMarkers.get(i));
                            }
                        }
                    } catch (Exception e){
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

