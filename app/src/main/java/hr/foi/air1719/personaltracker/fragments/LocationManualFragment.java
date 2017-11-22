package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 11/22/2017.
 */

public class LocationManualFragment extends Fragment {
    Geocoder geocoder ;
    List<Address> lokacija;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_manual_fragment, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        TextView address = (TextView) getView().findViewById(R.id.txt_address);
        EditText note = (EditText) getView().findViewById(R.id.txt_note);
        MyLocation myLocation = new MyLocation();
        Location location = myLocation.GetLastKnownLocation((IGPSActivity) getActivity());
        try {
            lokacija = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String addressLine= lokacija.get(0).getAddressLine(0);

        String city = lokacija.get(0).getAdminArea();

        String finalAddress = addressLine+","+city;

        address.setText(finalAddress);

    }

}
