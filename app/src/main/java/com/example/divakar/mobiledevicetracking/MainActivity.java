package com.example.divakar.mobiledevicetracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String DeviceModel, DeviceName, DeviceVersion;
//Change here
//Enter your database URl here to send the data to database
    private static final String REGISTER_URL = "ENTER YOUR DATABASE URL HERE";

    public static final String KEY_MODEL = "model";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_VERSION = "version";
    public static final String KEY_LOCATION = "location";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextLocation;

    private Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GPSTracker mGPS = new GPSTracker(this);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
//Edit Text set to not editable
        editTextUsername.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextLocation.setEnabled(false);
        //buttonRegister.setEnabled(false);
        //buttonRegister.setVisibility(View.VISIBLE);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        DeviceModel = android.os.Build.MODEL;
        DeviceName = android.os.Build.MANUFACTURER;
        DeviceVersion = android.os.Build.VERSION.RELEASE;

        editTextUsername.setText(DeviceModel);
        editTextPassword.setText(DeviceName);
        editTextEmail.setText(DeviceVersion);

        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            editTextLocation.setText("Lat"+"   "+mGPS.getLatitude() +" "+"Lon"+"   "+mGPS.getLongitude());
        }else{
            editTextLocation.setText("Unabletofind");
            System.out.println("Unable");
        }



        buttonRegister.setOnClickListener(this);
//Set without click function by using time(thread)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                //registerUser();
                                buttonRegister.performClick();


                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//Here is JSON VOLLEY
    private void registerUser() {
        final String model = editTextUsername.getText().toString().trim();
        final String device = editTextPassword.getText().toString().trim();
        final String version = editTextEmail.getText().toString().trim();
        final String location = editTextLocation.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_MODEL, model);
                params.put(KEY_DEVICE, device);
                params.put(KEY_VERSION, version);
                params.put(KEY_LOCATION, location);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonRegister){

            //Button Invisiable
            buttonRegister.setVisibility(View.INVISIBLE);
            registerUser();


        }
    }

}

