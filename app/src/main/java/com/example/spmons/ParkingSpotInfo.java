package com.example.spmons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;


public class ParkingSpotInfo extends AppCompatActivity  {


    int check = 0;
    String date = "";
    String d;
    OkHttpClient okHttpClient;
    Request request;
    ImageButton Sensor1,Sensor2,Sensor3,Sensor4,Sensor5,Sensor6,Sensor7,Sensor8,Sensor9,Sensor10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_spot_info);

        StateListDrawable states = new StateListDrawable();
        Sensor1 = (ImageButton) findViewById(R.id.imageButton11);
        Sensor2 = (ImageButton) findViewById(R.id.imageButton12);

        Sensor3 = (ImageButton) findViewById(R.id.imageButton21);
        Sensor4 = (ImageButton) findViewById(R.id.imageButton22);
        Sensor5 = (ImageButton) findViewById(R.id.imageButton31);
        Sensor6 = (ImageButton) findViewById(R.id.imageButton32);
        Sensor7 = (ImageButton) findViewById(R.id.imageButton41);
        Sensor8 = (ImageButton) findViewById(R.id.imageButton42);
        Sensor9 = (ImageButton) findViewById(R.id.imageButton51);
        Sensor10 = (ImageButton) findViewById(R.id.imageButton52);



        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        new CountDownTimer(100000, 9000) {

            public void onTick(long millisUntilFinished) {

                readparkingsensordata();


            }


            public void onFinish() {
                //mTextField.setText("done!");
            }
        }.start();

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        //myUpdateOperation();
                        mSwipeRefreshLayout.setRefreshing(false);
                        new CountDownTimer(100000, 9000) {

                            public void onTick(long millisUntilFinished) {

                                readparkingsensordata();


                            }


                            public void onFinish() {
                                //mTextField.setText("done!");
                            }
                        }.start();
                    }


                }
        );



    }
    private void readparkingsensordata() {

        okHttpClient = new OkHttpClient();

       // Log.d("D", "Date:" + date);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.42.49:5000/api/v1/resources/latestRecord").newBuilder();
        //urlBuilder.addQueryParameter("date", date);
        String url = urlBuilder.build().toString();
        Log.d("urlcreate", url);
        // building a request
        request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .header("Content-type", "application.json").build();
        //textLoad.setText("Finished");


        okHttpClient.newCall(request).enqueue(new Callback()  {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ParkingSpotInfo.this, "server down", Toast.LENGTH_SHORT).show();
                        //textViewResult.setText("error connecting to the server");
                    }
                });
            }
            @Override
            public void onResponse(@NotNull Call call, Response response) throws IOException, SocketException  {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String resStr = "";
                        int limit = 0;
                        try {

                            resStr = response.body().string();
                            Log.d("data", "Response: " + resStr.getBytes().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                            JSONObject jr = null;
                        try {
                            jr = new JSONObject(resStr);
                            limit = jr.length();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            String Deviceid,parkst;
                            Deviceid = jr.getString("id").toString();
                            parkst = jr.getString("Parking_Status").toString();
                            Log.d("data", "Responsed: " + Deviceid);
                            Log.d("data", "Responsep: " + parkst);
                            // Sensor 1
                            if (Deviceid.equals("00-80-00-00-04-01-a4-9f") ) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1")) {
                                    Log.d("data", "True");
                                    Sensor1.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{
                                    Log.d("data", "False");
                                    Sensor1.getBackground().setState(new int[]{android.R.attr.state_enabled});}
                            }

                            // Sensor 2
                            else if (Deviceid.equals("00-80-00-00-04-01-a4-a2")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                String check = "1";
                                if(parkst.equals("1")){
                                    Log.d("data", "True");
                                    Sensor2.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Log.d("data", "False");
                                    Sensor2.getBackground().setState(new int[]{android.R.attr.state_enabled});}
                            }

                            // Sensor 3
                            else if (Deviceid.equals ("00-80-00-00-04-01-9f-d8")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1") ){
                                    Sensor3.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Sensor3.getBackground().setState(new int[]{android.R.attr.state_enabled});}
                            }

                            // Sensor 4
                            else if (Deviceid.equals("00-80-00-00-04-01-a4-8d")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1")){
                                    Log.d("data", "True");
                                    Sensor4.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Sensor4.getBackground().setState(new int[]{android.R.attr.state_enabled}); }
                            }

                            // Sensor 5
                            else if (Deviceid.equals("00-80-00-00-04-01-a0-20")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if (parkst.equals("1")) {
                                    Log.d("data", "True");
                                    Sensor5.getBackground().setState(new int[]{android.R.attr.state_selected});
                                } else {
                                    Sensor5.getBackground().setState(new int[]{android.R.attr.state_enabled});
                                }
                            }

                            // Sensor 6
                            else if (Deviceid.equals("00-80-00-00-04-01-a4-b7")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1") ){
                                    Sensor6.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Sensor6.getBackground().setState(new int[]{android.R.attr.state_enabled});
                                }}


                            else if (Deviceid.equals ("00-80-00-00-04-01-9f-db")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1") ){
                                    Sensor7.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Sensor7.getBackground().setState(new int[]{android.R.attr.state_enabled});}
                            }

                            else if (Deviceid.equals("00-80-00-00-04-01-9f-d7")) {
                                String check = "1";
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1")){
                                    Log.d("data", "True");
                                    Sensor8.getBackground().setState(new int[]{android.R.attr.state_selected});
                                }
                                else{Sensor8.getBackground().setState(new int[]{android.R.attr.state_enabled});
                                }
                            }

                            else if (Deviceid.equals("00-80-00-00-04-01-9f-ea")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1")){
                                    Sensor9.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{Sensor9.getBackground().setState(new int[]{android.R.attr.state_enabled});
                                }
                            }

                            else if (Deviceid.equals("00-80-00-00-04-01-a4-91")) {
                                Log.d("data", "Responsed: In" + Deviceid );
                                if(parkst.equals("1") ){
                                    Log.d("data", "True");
                                    Sensor10.getBackground().setState(new int[]{android.R.attr.state_selected});}
                                else{
                                    Sensor10.getBackground().setState(new int[]{android.R.attr.state_enabled});
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}








