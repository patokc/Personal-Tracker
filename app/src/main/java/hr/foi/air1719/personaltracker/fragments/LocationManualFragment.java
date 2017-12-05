package hr.foi.air1719.personaltracker.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
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
    EditText description;
    Location location;
    Editable note;

    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_manual_fragment, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        TextView address = (TextView) getView().findViewById(R.id.txt_address);
        description = (EditText) getView().findViewById(R.id.txt_note);
        note = description.getText();

        MyLocation myLocation = new MyLocation();
        location = myLocation.GetLastKnownLocation(this);
        try {
            lokacija = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String addressLine= lokacija.get(0).getAddressLine(0);
        String county = lokacija.get(0).getAdminArea();
        String finalAddress = addressLine+", "+county;
        address.setText(finalAddress);

        AddImage();
        Button buttonSave = (Button) getView().findViewById(R.id.btn_save_location_data);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Save();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void AddImage()
    {
        outputImage = (ImageButton) getView().findViewById(R.id.img_location_photo);
        outputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

    }

    private void Save () {
        if(outputBitmap !=null && note.length() !=0) {
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    Activity ac = new Activity(ActivityMode.WALKING);
                    DatabaseFacade db = new DatabaseFacade(getView().getContext());
                    ac.setImage(encode(outputBitmap, Bitmap.CompressFormat.JPEG, 100));
                    ac.setDescription(note.toString());
                    db.saveActivity(ac);
                    db.saveLocation(new GpsLocation(ac.getActivityId(),
                            location.getLongitude(), location.getLatitude(), location.getAccuracy()));
                }
            });
            thread.start();
            MapFragment mapFragment = new MapFragment();
            mFragmentManager = getFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mapFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }else {
            Toast.makeText(this.getActivity(), "Please fill in all fields!", Toast.LENGTH_LONG).show();
        }

    }

   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
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

    public static String encode(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

  /*  public static Bitmap decode(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
*/
    @Override
    public void locationChanged(Location location) {

    }
}
