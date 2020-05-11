package com.example.streetlity_android.User.Maintainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.streetlity_android.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<NormalOrderObject> itemNormal = new ArrayList<>();
    ArrayList<EmergencyOrderObject> itemEmer = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public MyOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrders newInstance(String param1, String param2) {
        MyOrders fragment = new MyOrders();
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
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);

        TextView tvNoOrderNorm =  rootView.findViewById(R.id.tv_normal_no_order);
        TextView tvNoOrderEmer = rootView.findViewById(R.id.tv_emer_no_order);

        ListView lvNormal = rootView.findViewById(R.id.lv_normal_order);
        ListView lvEmergency = rootView.findViewById(R.id.lv_emergency);

        NormalOrderApdater normalOrderAdapter = new NormalOrderApdater(getActivity(), R.layout.lv_item_order,
                itemNormal);

        lvNormal.setAdapter(normalOrderAdapter);

        EmergencyOrderApdater emerOrderAdapter = new EmergencyOrderApdater(getActivity(), R.layout.lv_item_order,
                itemEmer);

        lvEmergency.setAdapter(emerOrderAdapter);

        lvNormal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lvEmergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        if(lvNormal.getAdapter().getCount()==0){
            tvNoOrderNorm.setVisibility(View.VISIBLE);
        }

        if(lvEmergency.getAdapter().getCount()==0){
            tvNoOrderEmer.setVisibility(View.VISIBLE);
        }

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
}
