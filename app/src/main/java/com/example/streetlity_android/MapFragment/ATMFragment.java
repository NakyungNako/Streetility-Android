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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

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
 * {@link ATMFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ATMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ATMFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;

    double latitude = 0;
    double longitude = 0;

    Marker currentPosition;

    ArrayList<ATMObject> items = new ArrayList<>();

    ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FuelFragment.OnFragmentInteractionListener mListener;

    public ATMFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_atm, container, false);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);

        ImageButton imgConfirmRange = rootView.findViewById(R.id.img_btn_confirm_range);
        final SeekBar sbRange = rootView.findViewById((R.id.sb_range));

        imgConfirmRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callATM(latitude,longitude,(float)sbRange.getProgress());
                Log.e("", "onClick: " +  sbRange.getProgress());
            }
        });

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
        if (context instanceof FuelFragment.OnFragmentInteractionListener) {
            mListener = (FuelFragment.OnFragmentInteractionListener) context;

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            mapFragment.getMapAsync(this);
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


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationManager
                    .NETWORK_PROVIDER);
            if(location == null){
                Log.e("", "onMapReady: MULL");
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("", "onMapReady: " + latitude+" , " + longitude );
        }

        callATM(latitude,longitude, 1);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        if(!marker.equals(currentPosition)) {

            final LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

            final android.view.View dialogView = inflater.inflate(R.layout.dialog_simple_goto, null);

            Button btnGo = dialogView.findViewById(R.id.btn_go);

            TextView tvNote = dialogView.findViewById(R.id.tv_note);

            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            dialog.show();
        }

        marker.showInfoWindow();

        return true;
    }

        public void addATMMarkerToList(float lat, float lon, String type){
        LatLng pos = new LatLng(lat,lon);
        MarkerOptions option = new MarkerOptions();
        option.title(type);
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm));
        option.position(pos);
        mMarkers.add(option);
    }

        public void callATM(double lat, double lon, float range){
            mMap.clear();
            mMarkers.removeAll(mMarkers);
        Retrofit retro = new Retrofit.Builder().baseUrl("http://35.240.207.83/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        final MapAPI tour = retro.create(MapAPI.class);
        Call<ResponseBody> call = tour.getATMInRange("1.0.0",(float)lat,(float)lon,(float)range + 1);
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
                        jsonArray = jsonObject.getJSONArray("Atms");

                        for (int i = 0; i< jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Log.e("", "onResponse: " + jsonObject1.toString());
                            addATMMarkerToList((float)jsonObject1.getDouble("Lat"),
                                    (float)jsonObject1.getDouble("Lon"),"atm type here");
                        }

                        for (int i = 0; i < mMarkers.size(); i++){
                            Log.e("", mMarkers.get(i).getTitle());
                            mMap.addMarker(mMarkers.get(i));
                        }

                        MarkerOptions curPositionMark = new MarkerOptions();
                        curPositionMark.position(new LatLng(latitude,longitude));
                        curPositionMark.title("You are here");

                        currentPosition = mMap.addMarker(curPositionMark);


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
