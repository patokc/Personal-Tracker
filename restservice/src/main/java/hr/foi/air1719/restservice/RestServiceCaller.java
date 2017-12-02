package hr.foi.air1719.restservice;

import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.ActivityMode;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.responses.UserResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by abenkovic
 */

public class RestServiceCaller {
    RestServiceHandler trsHandler;

    // retrofit object
    Retrofit retrofit;
    // base URL of the web service
    private final String baseUrl = "https://tracker-21f6d.firebaseio.com/";

    // constructor
    public RestServiceCaller(RestServiceHandler trsHandler){
        this.trsHandler = trsHandler;

        //To verify what's sending over the network, use Interceptors
        OkHttpClient client = new OkHttpClient();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public void getUser(String user){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<UserResponse> call = serviceCaller.getUser(user);

        if(call != null){
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Response<UserResponse> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void createUser(User user){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<User> call = serviceCaller.createUser(user, user.getUsername());

        if(call != null){
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Response<User> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void saveLocation(GpsLocation location, String user, String activityId){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<GpsLocation> call = serviceCaller.saveLocation(location, user, activityId, location.getLocationId());

        if(call != null){
            call.enqueue(new Callback<GpsLocation>() {
                @Override
                public void onResponse(Response<GpsLocation> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(null, true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void getLocations(String user, String activityId){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Map<String, GpsLocation>> call = serviceCaller.getLocations(user, activityId);

        if(call != null){
            call.enqueue(new Callback<Map<String, GpsLocation>>() {
                @Override
                public void onResponse(Response<Map<String, GpsLocation>> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void saveActivity(Activity activity){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Activity> call = serviceCaller.saveActivity(activity, activity.getUser(), activity.getActivityId(), activity.getMode());

        if(call != null){
            call.enqueue(new Callback<Activity>() {
                @Override
                public void onResponse(Response<Activity> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(null, true); // ne zanima me response.body()
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void getActivity(String user,ActivityMode mode, String activityId){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Activity> call = serviceCaller.getActivity(user, mode, activityId);

        if(call != null){
            call.enqueue(new Callback<Activity>() {
                @Override
                public void onResponse(Response<Activity> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void getAllActivities(String user, ActivityMode mode){
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Map<String, Activity>> call = serviceCaller.getAllActivities(user, mode);

        if(call != null){
            call.enqueue(new Callback<Map<String, Activity>>() {
                @Override
                public void onResponse(Response<Map<String, Activity>> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(trsHandler != null){
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(trsHandler != null){
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

}
