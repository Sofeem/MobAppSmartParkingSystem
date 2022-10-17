package com.example.spmons;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Delayed;


public class Dashboard extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseReference;
    Button showdate;
    private TextView datecollect;
    private ArrayList<ParkingSampleData> parksample = new ArrayList<>();


    String mention[] = {"Occupied", "Vacant"};
    Button buttonOne;
    BarChart chart;
    PieChart pieChart;
    String date = "";
    String d;
    OkHttpClient okHttpClient;
    Request request;
    ArrayList<String> Sensor_id = new ArrayList<String>();
    ArrayList<String> Parking_Status = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        datecollect = (TextView) findViewById(R.id.datetexttoshow);
        //SelectDate = (EditText) findViewById(R.id.sdate);
        findViewById(R.id.showdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //d = SelectDate.getText().toString();
                // Log.d("D", "checkdate" + d);
                // readparkingsensordata(String.valueOf(SelectDate.getText()));
                showDatepickerDialog();
                pieChart.setVisibility(View.INVISIBLE);
                chart.setVisibility(View.INVISIBLE);

            }
        });

        chart = findViewById(R.id.barchart);
        pieChart = findViewById(R.id.PieChart);
        buttonOne = findViewById(R.id.Spl);
        buttonOne.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /*System.out.println("Button Clicked");
                Intent intent = new Intent(Dashboard.this, ParkingSpotInfo.class);
                startActivity(intent);*/

                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms

                        setupPieChart();
                        drawBarChart();
                    }
                }, 2000);

            }
        });


        Button buttontwo = findViewById(R.id.button10);

        buttontwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ParkingSpotInfo.class);
                startActivity(intent);
            }


        });



    }

    private void showDatepickerDialog() {
        final Calendar newCalendar = Calendar.getInstance();
        final Calendar[] newDate = new Calendar[1];
        final SimpleDateFormat[] simpleDateFormat = new SimpleDateFormat[1];
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {

        int Dlength = String.valueOf(dayofMonth).length();
        int mon = month + 1;
        int Mlength = String.valueOf(mon).length();
        Log.d("D", "DLength:" + Dlength);
        Log.d("D", "MLength:" + mon);

        if (Dlength == 1 && Mlength == 1) {
            date = year + "-0" + (month + 1) + "-0" + dayofMonth;
        }
        if (Dlength == 1 && Mlength == 2) {
            date = year + "-" + (month + 1) + "-0" + dayofMonth;
        }
        if (Dlength == 2 && Mlength == 1) {
            date = year + "-0" + (month + 1) + "-" + dayofMonth;
        }
        if (Dlength == 2 && Mlength == 2) {
            date = year + "-" + (month + 1) + "-" + dayofMonth;
        }


        datecollect.setText(date);
        buttonOne.setVisibility(View.VISIBLE);
        //getOccupancyRateofSensor(date);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected void onPreExecute() {
            // Getting reference to the TextView tv_counter of the layout activity_main
            //TextView tvCounter = (TextView) findViewById(R.id.tv_counter);
            //tvCounter.setText("*START*");

        }

        @Override
        protected Void doInBackground(Void... voids) {

            //urlBuilder.addQueryParameter("deviceid", "00-80-00-00-04-01-9f-d7");
            okHttpClient = new OkHttpClient();
            Log.d("D", "Date:" + date);
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://sofeem.pythonanywhere.com//api/v1/resources/DSensor").newBuilder();
            urlBuilder.addQueryParameter("date", date);
            String url = urlBuilder.build().toString();
            Log.d("urlcreate", url);
            // building a request
            request = new Request.Builder().url(url)
                    .header("Accept", "application/json")
                    .header("Content-type", "application.json").build();
            //textLoad.setText("Finished");
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Dashboard.this, "server down", Toast.LENGTH_SHORT).show();
                            //textViewResult.setText("error connecting to the server");
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resStr = "";
                            try {

                                resStr = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (resStr.length() == 3) {
                                Log.d("data", "Response: " + resStr.length());
                                //Parking_Status.clear();
                                //Sensor_id.clear();
                                Toast.makeText(getApplicationContext(), "data not available for following date", Toast.LENGTH_SHORT).show();

                            } else {

                                JSONArray jr = null;
                                try {
                                    jr = new JSONArray(resStr);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                                int limit = jr.length();

                                for (int i = 0; i < limit; i++) {
                                    try {
                                        Sensor_id.add(jr.getJSONObject(i).getString("id"));
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                    try {
                                        Parking_Status.add(jr.getJSONObject(i).getString("Parking_Status"));
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }

                                    Log.d("data", String.valueOf(i) + "Sensor: " + Sensor_id.get(i));
                                    Log.d("data", String.valueOf(i) + "Parking_Status: " + Parking_Status.get(i));
                                }

                            }


                            //Toast.makeText(getApplicationContext(), "data recieved", Toast.LENGTH_SHORT).show();
                            //getOccupancyRateofSensor();

                        }
                    });
                }
            });

            //delay()
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // textMessage.setText(result);

            //drawBarChart();
            //setupPieChart();
            //ResetOccupancy();


            //super.onPostExecute(aVoid);
        }
    }





/*
    private void readparkingsensordata(String d) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d("D", "Date:" + d);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://130.231.201.151:5000/api/v1/resources/DSensor").newBuilder();
                urlBuilder.addQueryParameter("deviceid", "00-80-00-00-04-01-9f-d7");
        urlBuilder.addQueryParameter("date", d);
        String url = urlBuilder.build().toString();
        Log.d("urlcreate",  url);
        // building a request
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .header("Content-type","application.json").build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Dashboard.this, "server down", Toast.LENGTH_SHORT).show();
                        //textViewResult.setText("error connecting to the server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String resStr = response.body().string();
                            JSONArray jr = new JSONArray(resStr);
                            int limit = jr.length();
                            for (int i = 0; i < limit; i++) {
                                String time = jr.getJSONObject(i).getString("Parking_Status");
                                Log.d("data", String.valueOf(i) + ": "+ time);
                                //textViewResult.setMovementMethod(new ScrollingMovementMethod());
                                //textViewResult.setText(time);

                            }


                            //JSONArray Jarray = json.getJSONArray("id");
                            //String id = null;
                            //int limit = Jarray.length();
                            //for (int i = 0; i < limit; i++) {
                            //JSONObject object     = Jarray.getJSONObject(i);

                            //id = object.getString("id");
                            //lname = object.getString("lastName");

                            //Log.d("JSON DATA", Jarray.get(0) + " ## ");

                            //store the data into the array
                            //dataStore[i] = fname + " ## " + lname;
                            //}


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }


                        //textViewResult.setText();
                        //Log.d("Data", "successResponse: " );

                        //String data = new Gson().toJson(response.body());
                        //JSONObject JsonObject = new JSONObject(response.body().string());

                        //String Date = JsonObject.getString("Date");
                        //String time = JsonObject.getString("time");
                        //String ParkingStatus = JsonObject.getString("Parking_Status");
                        //Log.d("Data", id + " "+ Date + " " + time + " " + ParkingStatus);

                        //data = ;
                        //textViewResult.setText(data);
                        //Log.d("Data", data);
                        Toast.makeText(getApplicationContext(), "data recieved", Toast.LENGTH_SHORT).show();


                    }
                });



            }
        });

    } */


    private void setupPieChart() {
        List<PieEntry> Pieentries = new ArrayList<>();
        float[] OverallOccupancyData = new float[]{0, 0};

        if (Parking_Status.size() == 0) {
            Toast.makeText(getApplicationContext(), "data not available for following date", Toast.LENGTH_SHORT).show();
        } else {

            OverallOccupancyData[0] = (float) Collections.frequency(Parking_Status, "1") / Parking_Status.size() * 100;
            OverallOccupancyData[1] = (float) Collections.frequency(Parking_Status, "0") / Parking_Status.size() * 100;

            for (int i = 0; i < OverallOccupancyData.length; i++) {
                Pieentries.add(new PieEntry(OverallOccupancyData[i], mention[i]));
            }
            PieDataSet DataSet = new PieDataSet(Pieentries, "");
            DataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            DataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            DataSet.setValueTextSize(15f);
            PieData data = new PieData(DataSet);


            pieChart.setVisibility(View.VISIBLE);
            pieChart.setData(data);

            pieChart.animateY(1000);
            pieChart.invalidate();


            //Parking_Status.clear();
           // Sensor_id.clear();

        }


    }


    private void ResetOccupancy() {

    }


    private void drawBarChart() {

        float[] OccupancyData = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        ArrayList<String> pslist = new ArrayList<String>();
        ArrayList<String> s1Orate = new ArrayList<String>();
        ArrayList<String> s2Orate = new ArrayList<String>();
        ArrayList<String> s3Orate = new ArrayList<String>();
        ArrayList<String> s4Orate = new ArrayList<String>();
        ArrayList<String> s5Orate = new ArrayList<String>();
        ArrayList<String> s6Orate = new ArrayList<String>();
        ArrayList<String> s7Orate = new ArrayList<String>();
        ArrayList<String> s8Orate = new ArrayList<String>();
        ArrayList<String> s9Orate = new ArrayList<String>();
        ArrayList<String> stOrate = new ArrayList<String>();

        for (int i = 0; i < Parking_Status.size(); i++) {

            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a4-a2")) {
                s1Orate.add(Parking_Status.get(i));
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a4-9f")) {
                s2Orate.add(Parking_Status.get(i));
                //OccupancyData[1] = (Collections.frequency(s2Orate, "1")/Collections.frequency(s2Orate, "0"))*100;
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-9f-d8")) {
                s3Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a4-8d")) {
                s4Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a0-20")) {
                s5Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a4-b7")) {
                s6Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-9f-db")) {
                s7Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-9f-d7")) {
                s8Orate.add(Parking_Status.get(i));

                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-9f-ea")) {
                s9Orate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (Sensor_id.get(i).equals("00-80-00-00-04-01-a4-91")) {
                stOrate.add(Parking_Status.get(i));
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }

        }

        OccupancyData[0] = (float) Collections.frequency(s1Orate, "1") / s1Orate.size() * 100;
        Log.d("D","t:" + OccupancyData[0]);
        OccupancyData[1] = (float) Collections.frequency(s2Orate, "1") / s2Orate.size() * 100;
        OccupancyData[2] = (float) Collections.frequency(s3Orate, "1") / s3Orate.size() * 100;
        OccupancyData[3] = (float) Collections.frequency(s4Orate, "1") / s4Orate.size() * 100;
        OccupancyData[4] = (float) Collections.frequency(s5Orate, "1") / s5Orate.size() * 100;
        OccupancyData[5] = (float) Collections.frequency(s6Orate, "1") / s6Orate.size() * 100;
        OccupancyData[6] = (float) Collections.frequency(s7Orate, "1") / s7Orate.size() * 100;
        OccupancyData[7] = (float) Collections.frequency(s8Orate, "1") / s8Orate.size() * 100;
        OccupancyData[8] = (float) Collections.frequency(s6Orate, "1") / s9Orate.size() * 100;
        OccupancyData[9] = (float) Collections.frequency(s7Orate, "1") / stOrate.size() * 100;


        ArrayList OccupancyRate = new ArrayList();
        //String[] Dbreference = new String[]{"00-80-00-00-04-01-a4-9f","00-80-00-00-04-01-a4-a2","00-80-00-00-04-01-9f-d8","00-80-00-00-04-01-a4-8d","00-80-00-00-04-01-a0-20",
        //"00-80-00-00-04-01-a4-b7","00-80-00-00-04-01-9f-db","00-80-00-00-04-01-9f-d7","00-80-00-00-04-01-9f-ea","00-80-00-00-04-01-a4-91"};


        pslist.add("PS1");
        pslist.add("PS2");
        pslist.add("PS3");
        pslist.add("PS4");
        pslist.add("PS5");
        pslist.add("PS6");
        pslist.add("PS7");
        pslist.add("PS8");
        pslist.add("PS9");
        pslist.add("PS10");

        for (int i = 0; i < 10; i++) {
            //float or = getOccupancyRate(Dbreference[i]);
            OccupancyRate.add(new BarEntry(i, OccupancyData[i]));
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(pslist));
        xAxis.setGranularity(0.5f);
        xAxis.setGranularityEnabled(true);
        BarDataSet bardataset = new BarDataSet(OccupancyRate, "Rate");
        bardataset.setValueTextSize(12f);
        chart.animateY(500);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setVisibility(View.VISIBLE);
        chart.setData(data);

        Parking_Status.clear();
        Sensor_id.clear();

    }



}






    /*private void getOccupancyRateofSensor(String Caldate) {
        ArrayList<String> OverallRate = new ArrayList<String>();
        ArrayList<DataList> dList = new ArrayList<>();
        ArrayList<String> s1Orate = new ArrayList<String>();
        ArrayList<String> s2Orate = new ArrayList<String>();
        ArrayList<String> s3Orate = new ArrayList<String>();
        ArrayList<String> s4Orate = new ArrayList<String>();
        ArrayList<String> s5Orate = new ArrayList<String>();
        ArrayList<String> s6Orate = new ArrayList<String>();
        ArrayList<String> s7Orate = new ArrayList<String>();
        ArrayList<String> s8Orate = new ArrayList<String>();
        ArrayList<String> s9Orate = new ArrayList<String>();
        ArrayList<String> stOrate = new ArrayList<String>();

        Log.d("D", "t:" + Caldate);
        for (int i = 0; i < parksample.size(); i++) {
            //Log.d("D","Data :" + parksample.get(i).getParkingstatus());


            if (parksample.get(i).getDate().equals(Caldate)) {

                OverallRate.add( parksample.get(i).getParkingstatus());


                DataList d = new DataList(parksample.get(i).getDeviceId(), parksample.get(i).getParkingstatus());
                dList.add(d);
                Log.d("D", "t:" + dList.get(0).getStatus());

            }

        }
        Log.d("D","PieData :" + (float) Collections.frequency(OverallRate, "0"));
        OverallOccupancyData[0]= (float) Collections.frequency(OverallRate, "1") / OverallRate.size() * 100;
        OverallOccupancyData[1]=  (float) Collections.frequency(OverallRate, "0") / OverallRate.size() * 100;


               for (int j = 0; j < dList.size(); j++) {
            //Log.d("D","Data :" + parksample.get(i).getParkingstatus());
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a4-a2")) {
                s1Orate.add(dList.get(j).getStatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a4-9f")) {
                s2Orate.add(dList.get(j).getStatus());
                //OccupancyData[1] = (Collections.frequency(s2Orate, "1")/Collections.frequency(s2Orate, "0"))*100;

            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-9f-d8")) {
                s3Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a4-8d")) {
                s4Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a0-20")) {
                s5Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a4-b7")) {
                s6Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-9f-db")) {
                s7Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-9f-d7")) {
                s8Orate.add(dList.get(j).getStatus());

                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-9f-ea")) {
                s9Orate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
            if (dList.get(j).getid().equals("00-80-00-00-04-01-a4-91")) {
                stOrate.add(dList.get(j).getStatus());
                //Log.d("D","t:" + parksample.get(i).getParkingstatus());
            }
        }

        Log.d("D", "newlist:" + s1Orate.size());
        Log.d("D", "newlist2:" + s2Orate.size());
        Log.d("D", "VOccurrance:" + (float) Collections.frequency(s1Orate, "1") / s1Orate.size() * 100);
        Log.d("D", "VOccurrance2:" + Collections.frequency(s2Orate, "1"));

        OccupancyData[0] = (float) Collections.frequency(s1Orate, "1") / s1Orate.size() * 100;
        OccupancyData[1] = (float) Collections.frequency(s2Orate, "1") / s2Orate.size() * 100;
        OccupancyData[2] = (float) Collections.frequency(s3Orate, "1") / s3Orate.size() * 100;
        OccupancyData[3] = (float) Collections.frequency(s4Orate, "1") / s4Orate.size() * 100;
        OccupancyData[4] = (float) Collections.frequency(s5Orate, "1") / s5Orate.size() * 100;
        OccupancyData[5] = (float) Collections.frequency(s6Orate, "1") / s6Orate.size() * 100;
        OccupancyData[6] = (float) Collections.frequency(s7Orate, "1") / s7Orate.size() * 100;
        OccupancyData[7] = (float) Collections.frequency(s8Orate, "1") / s8Orate.size() * 100;
        OccupancyData[8] = (float) Collections.frequency(s6Orate, "1") / s9Orate.size() * 100;
        OccupancyData[9] = (float) Collections.frequency(s7Orate, "1") / stOrate.size() * 100;

    }




}*/

    /*private  float getOccupancyRate(String path){


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(path);


        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    Date ddate = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    strDate = dateFormat.format(ddate);
                    String Time = snapshot.child(ds.getKey()).child("Time").getValue().toString();
                    String date  = Time.substring(0, 10);
                    String time  = Time.substring(11, 16);
                    //Log.d("D", "Data:" + strDate);
                    int i = 0;
                    String Status = "";
                    String P1key = ds.getKey();
                    String ParkingSpot = "PS1";



                    if (date.equals("2021-06-10")){
                        String Statusvalue = snapshot.child(ds.getKey()).child("Parking_status").getValue().toString();
                        St.add(Statusvalue);}


                    //pinfo.add(new DataforParking(ParkingSpot,date,time,Status,Statusvalue));
                    //Collections.sort(pinfo,Collections.reverseOrder());



                }

                occuV = Collections.frequency(St, "0");
                occuO = Collections.frequency(St, "1");
                OccRate = (occuO/occuV)*100;
                Log.d("D", "Data22:" + occuV);
                Log.d("D", "Data32:" + occuO);
                Log.d("D", "OR:" + OccRate );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });














        return OccRate;
    }*/




