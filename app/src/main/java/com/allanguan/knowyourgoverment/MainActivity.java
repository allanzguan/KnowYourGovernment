package com.allanguan.knowyourgoverment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements Serializable, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private final List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;

    private Map<String, String> states = new HashMap<>();

    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private LocationManager locationManager;
    private Criteria criteria;
    private String queryZip;
    private String queryCity;
    private String queryState;
    private TextView location;

    private Official o;

    private String choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.location);
        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            setLocation();
//            for (int i = 0; i < 10 ;i++){
//                officialList.add(new Official("title "+ Integer.toString(i), "name "+Integer.toString(i)) );
//            }
            officialAdapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
            }
        }


    }


    @SuppressLint("MissingPermission")
    private void setLocation() {

        if (!checkNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded without an internet connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            location.setText("No Data For Location");
            return;
        }


        String bestProvider = locationManager.getBestProvider(criteria, true);


        Geocoder geocoder = new Geocoder(this);
        Location currentLocation = null;
        if (bestProvider != null) {
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
        }
        if (currentLocation != null) {
            try {
                List<Address> adrs = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                queryZip = adrs.get(0).getPostalCode();
                queryCity = adrs.get(0).getLocality();
                queryState = adrs.get(0).getAdminArea();
                doSearch(queryZip);
//                location = findViewById(R.id.location);
//                location.setText(queryCity + ",  " + states.get(queryState) +" "+ queryZip);
//                Toast.makeText(this, queryCity + "  " + queryZip, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "setLocation: No Location");
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onClick(View view) {
        final int pos = recyclerView.getChildLayoutPosition(view);
        o = officialList.get(pos);
        Intent data = new Intent(this, OfficialActivity.class);
        data.putExtra("OBJ", o);

        if(!queryCity.isEmpty() && !queryState.isEmpty() && !queryZip.isEmpty()){
            data.putExtra("LOC", queryCity + ", " + queryState + " " + queryZip);
        }
        else if (!queryCity.isEmpty() && !queryState.isEmpty() && queryZip.isEmpty()){
            data.putExtra("LOC", queryCity + ", " + queryState);
        }
        else{
            data.putExtra("LOC", queryZip);
        }
        startActivity(data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.search:
                makeDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void makeDialog(){
        if (!checkNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded without an internet connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                choice = et.getText().toString().trim();

                doSearch(choice);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

//        builder.setMessage("Please enter a Stock symbol:");
        builder.setTitle("Enter a City, State or a Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doSearch(String s){
        OfficialDownloader officialDownloader = new OfficialDownloader(this, s);
        new Thread(officialDownloader).start();
    }

    public void addOfficialToList(List ol, String city, String state, String zip){
        queryState = state;
        queryCity = city;
        queryZip = zip;

        if(!city.isEmpty() && !state.isEmpty() && !zip.isEmpty()){
            location.setText(city + ", " + state + " " + zip);
        }
        else if (!city.isEmpty() && !state.isEmpty() && zip.isEmpty()){
            location.setText(city + ", " + state);
        }
        else{
            location.setText(zip);
        }

        officialList.clear();
        officialList.addAll(ol);
        officialAdapter.notifyDataSetChanged();
    }

    public void doNoLocation(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("No data for specified location");
        builder.setTitle("Location Not Found: " + symbol);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}