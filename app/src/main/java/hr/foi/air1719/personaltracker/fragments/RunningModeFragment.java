package hr.foi.air1719.personaltracker.fragments;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import hr.foi.air1719.location.IGPSActivity;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Nikolina on 13.12.2017..
 */

public class RunningModeFragment extends Fragment implements IGPSActivity
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.running_mode_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void locationChanged(Location location)
    {
    }



}

