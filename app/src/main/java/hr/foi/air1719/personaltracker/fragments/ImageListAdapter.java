package hr.foi.air1719.personaltracker.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/17/2018.
 */


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private ArrayList<Activity> savedLocations;
    private ArrayList<GpsLocation> gpsLocations;
    List<Address> lokacija;
    String finalAddress;

    private Context context;

    public ImageListAdapter(Context context,ArrayList<Activity> savedLocations, ArrayList<GpsLocation> locationCoordinate) {
        this.context = context;
        this.savedLocations = savedLocations;
        this.gpsLocations= locationCoordinate;
    }

    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_location, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        //lokacija(gpsLocations.get(0).getLongitude(), gpsLocations.get(0).getLatitude());
      //  viewHolder.address.setText(finalAddress);

        viewHolder.description.setText(savedLocations.get(i).getDescription());
        Picasso.with(context).load(savedLocations.get(i).getImage()).resize(160, 180).into(viewHolder.img);
    }
    private void lokacija (double longitude, double latitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            lokacija = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String addressLine= lokacija.get(0).getAddressLine(0);
        String county = lokacija.get(0).getAdminArea();
        finalAddress = addressLine+", "+county;
    }
    @Override
    public int getItemCount() {
        return savedLocations.size();

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        ImageView img;
        TextView address;
        public ViewHolder(View view) {
            super(view);
            description = (TextView)view.findViewById(R.id.description);
            img = (ImageView)view.findViewById(R.id.img);
            address=(TextView)view.findViewById(R.id.address);
        }
    }
}
