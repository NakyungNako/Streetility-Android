//package com.example.streetlity_android;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//
//public class MapNavigationHolder extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_service_selection);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
//
//        Button btnFuel = findViewById(R.id.btn_fuel);
//        Button btnATM = findViewById(R.id.btn_atm);
//        Button btnMaintenance = findViewById(R.id.btn_maintenance);
//        Button btnWC = findViewById(R.id.btn_wc);
//
//        btnFuel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(MapNavigationHolder.this, MapsActivity.class);
//                t.putExtra("type", 1);
//                startActivity(t);
//            }
//        });
//
//        btnATM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(MapNavigationHolder.this, BankSelection.class);
//                startActivity(t);
//            }
//        });
//
//        btnMaintenance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(MapNavigationHolder.this, MapsActivity.class);
//                t.putExtra("type", 3);
//                startActivity(t);
//            }
//        });
//
//        btnWC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(MapNavigationHolder.this, MapsActivity.class);
//                t.putExtra("type", 4);
//                startActivity(t);
//            }
//        });
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        this.finish();
//
//        return true;
//    }
//}

package com.example.streetlity_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.example.streetlity_android.MapFragment.ATMFragment;
import com.example.streetlity_android.MapFragment.FuelFragment;
import com.example.streetlity_android.MapFragment.MaintenanceFragment;
import com.example.streetlity_android.MapFragment.WCFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MapNavigationHolder extends AppCompatActivity implements FuelFragment.OnFragmentInteractionListener,
        ATMFragment.OnFragmentInteractionListener, MaintenanceFragment.OnFragmentInteractionListener,
        WCFragment.OnFragmentInteractionListener {

    TextView toolbar_tittle;
    Fragment fragment;
    boolean firstLoad;

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation);

        firstLoad = true;

        fragment = new FuelFragment();
        loadFragment(fragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_atm:
                    fragment = new ATMFragment();
                    break;
                case R.id.navigation_fuel:
                    fragment = new FuelFragment();
                    break;
                case R.id.navigation_maintenance:
                    fragment = new MaintenanceFragment();
                    break;
                case R.id.navigation_wc:
                    fragment = new WCFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };
    private boolean loadFragment(Fragment fragment) {
        // load fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void detachFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}

