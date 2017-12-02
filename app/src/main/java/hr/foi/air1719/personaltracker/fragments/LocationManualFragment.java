package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.location.MyLocation;
import hr.foi.air1719.personaltracker.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Patricija on 11/22/2017.
 */

public class LocationManualFragment extends Fragment implements IGPSActivity {
    Geocoder geocoder ;
    List<Address> lokacija;
    public static int RESULT_LOAD_IMAGE = 1;
    private ImageButton outputImage;
    Bitmap outputBitmap;

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
        Location location = myLocation.GetLastKnownLocation(this);
        try {
            lokacija = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String addressLine= lokacija.get(0).getAddressLine(0);
        String county = lokacija.get(0).getAdminArea();
        String finalAddress = addressLine+", "+county;

        address.setText(finalAddress);

        outputImage = (ImageButton) getView().findViewById(R.id.img_location_photo);
        outputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddImage();
            }
        });
        Button buttonSave = (Button) getView().findViewById(R.id.btn_save_location_data);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void AddImage()
    {
        Intent intentGalery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGalery.setType("image/*");
        startActivityForResult(intentGalery, RESULT_LOAD_IMAGE);
    }

    private void Save () {

    }

   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                outputImage.setImageURI(imageUri);
                try {
                    outputBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    outputImage.setImageBitmap(outputBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void locationChanged(Location location) {

    }
}
