package com.example.spmons;
import android.content.Context;
import android.graphics.Movie;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<DataforParking>{

    private Context mContext;
    private List<DataforParking> parkinglist = new ArrayList<DataforParking>();
    int resource;

    public CustomAdapter(@NonNull Context context, int resource,  ArrayList<DataforParking> list) {
        super(context, resource, list);
        this.mContext = context;
        this.resource = resource;
        this.parkinglist = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(resource, null, false);


        TextView p_spot = view.findViewById(R.id.ps);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);
        TextView p_status = view.findViewById(R.id.status);



        DataforParking dp = parkinglist.get(position);



        p_spot.setText(dp.getParkingspot());
        date.setText(dp.getDate());
        time.setText(dp.getTime());
        p_status.setText(dp.getStatus());





        /*TextView parkingspot = (TextView)listItem.findViewById(R.id.ps);
        parkingspot.setText(parking.get(position));

        TextView name = (TextView) listItem.findViewById(R.id.time);
        name.setText(parking.get(position+1));

        TextView release = (TextView) listItem.findViewById(R.id.duration);
        release.setText(parking.get(position+2));*/

        return view;
    }
}
