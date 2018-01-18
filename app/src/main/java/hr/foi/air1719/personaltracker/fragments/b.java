package hr.foi.air1719.personaltracker.fragments;

import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hr.foi.air1719.core.facade.DatabaseFacade;
import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.personaltracker.R;

/**
 * Created by Patricija on 1/17/2018.
 */

public class b {
  /*  fillListView();
    imageView = (ImageView) getView().findViewById(R.id.listview_image) ;

    mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            System.out.println(message.toString());


            List<Activity> ac = (List<Activity>) message.obj;

            //ist<HashMap<String, > aList = new ArrayList<HashMap<String, String>>();
            List<HashMap<String, Object>>aList =   new ArrayList<HashMap<String,Object>>();

          /* for (int i = 0; i < listviewAddress.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("listview_address", listviewAddress[i]);
                hm.put("listview_discription", listviewShortDescription[i]);
                hm.put("listview_image", Integer.toString(listviewImage[i]));
                aList.add(hm);
            }*/

/*
            for (Activity a : ac) {
                //HashMap<String, String> hm = new HashMap<String, String>();
                HashMap<String,Object> hm = new HashMap<String, Object>();
                hm.put("listview_address", a.getDescription());

                hm.put("listview_image", Picasso.with(getView().getContext()).load("https://benkovic.net/air/img/"+a.getImage()).resize(130,160));

                hm.put("listview_description", a.getDescription());
                aList.add(hm);
            }
            String[] from = {"listview_image" ,"listview_address", "listview_description"};
            int[] to = { R.id.listview_image,R.id.listview_address, R.id.listview_short_description};

            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.saved_location, from, to);
            ListView androidListView = (ListView) getView().findViewById(R.id.list_view);
            androidListView.setAdapter(simpleAdapter);

            // androidListView.setAdapter(new ImageListAdapter(getView().getContext(),eatFoodyImages));



        }
    };


}
    private void fillListView() {
        new Thread(new Runnable() {
            public void run() {
                DatabaseFacade dbfacade = new DatabaseFacade(getView().getContext());
                Message message = mHandler.obtainMessage(1, dbfacade.getActivityByModeOrderByStartDESC(ActivityMode.WALKING));
                message.sendToTarget();
            }
        }).start();

    }*/
}
