package hr.foi.air1719.restservice;


import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import hr.foi.air1719.database.entities.Activity;
import hr.foi.air1719.database.entities.GpsLocation;
import hr.foi.air1719.database.entities.User;
import hr.foi.air1719.restservice.responses.ImageResponse;
import hr.foi.air1719.restservice.responses.UserResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abenkovic
 */

public class RestServiceCaller {
    RestServiceHandler trsHandler;

    // retrofit object
    Retrofit retrofit;
    // base URL of the web service
    private final String baseUrl = "https://tracker-21f6d.firebaseio.com/";


    public RestServiceCaller(RestServiceHandler trsHandler) {
        this.trsHandler = trsHandler;

        //To verify what's sending over the network, use Interceptors
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public void getUser(String user) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<UserResponse> call = serviceCaller.getUser(user);

        if (call != null) {
            call.enqueue(new Callback<UserResponse>() {

                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }
                }
            });
        }

    }

    public void createUser(User user) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<User> call = serviceCaller.createUser(user, user.getUsername());

        if (call != null) {
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }

                }


            });
        }

    }

    public void saveLocation(GpsLocation location, String user, String activityId) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<GpsLocation> call = serviceCaller.saveLocation(location, user, location.getLocationId());

        if (call != null) {
            call.enqueue(new Callback<GpsLocation>() {
                @Override
                public void onResponse(Call<GpsLocation> call, Response<GpsLocation> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(null, true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GpsLocation> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }
                }

            });
        }

    }

    public void getLocations(String user, String activityId) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Map<String, GpsLocation>> call = serviceCaller.getLocations(user, activityId);

        if (call != null) {
            call.enqueue(new Callback<Map<String,GpsLocation>>() {
                @Override
                public void onResponse(Call<Map<String, GpsLocation>> call, Response<Map<String, GpsLocation>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, GpsLocation>> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }

                }

            });
        }

    }

    public void getAllLocations(String user) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Map<String, GpsLocation>> call = serviceCaller.getAllLocations(user);

        if (call != null) {
            call.enqueue(new Callback<Map<String,GpsLocation>>() {
                @Override
                public void onResponse(Call<Map<String, GpsLocation>> call, Response<Map<String, GpsLocation>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, GpsLocation>> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }
                }

            });
        }

    }

    public void saveActivity(final Activity activity) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Activity> call = serviceCaller.saveActivity(activity, activity.getUser(), activity.getActivityId());

        if (call != null) {
            call.enqueue(new Callback<Activity>() {
                @Override
                public void onResponse(Call<Activity> call, Response<Activity> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(null, true); // ne zanima me response.body()
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Activity> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(activity, false);
                    }
                }

            });
        }

    }

    public void getActivity(String user, String activityId) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Activity> call = serviceCaller.getActivity(user, activityId);

        if (call != null) {
            call.enqueue(new Callback<Activity>() {
                @Override
                public void onResponse(Call<Activity> call, Response<Activity> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Activity> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }
                }

            });
        }

    }

    public void getAllActivities(String user) {
        RestService serviceCaller = retrofit.create(RestService.class);
        Call<Map<String, Activity>> call = serviceCaller.getAllActivities(user);

        if (call != null) {
            call.enqueue(new Callback<Map<String,Activity>>() {
                @Override
                public void onResponse(Call<Map<String, Activity>> call, Response<Map<String, Activity>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (trsHandler != null) {
                                trsHandler.onDataArrived(response.body(), true);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Activity>> call, Throwable t) {
                    if (trsHandler != null) {
                        trsHandler.onDataArrived(null, false);
                    }
                }

            });
        }

    }

    public String uploadImage(Bitmap image) {
        String name = UUID.randomUUID().toString();
        RestService serviceCaller = retrofit.create(RestService.class);
        System.out.println("tu sam");
        try {


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            final byte[] img = stream.toByteArray();

                // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), img);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("userfile", name, requestFile);


            Call<ImageResponse> call = serviceCaller.uploadImage(body);

            if (call != null) {
                System.out.println("poziv");
                call.enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (trsHandler != null) {
                                    System.out.println("Sve ok");
                                    System.out.println(response.body().getName());
                                    System.out.println(response.body().getPath());

                                    System.out.println(response.body().isError());
                                    trsHandler.onDataArrived(null, true); // ne zanima me response.body()
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        if (trsHandler != null) {
                            System.out.println("greska " + t.toString());

                            trsHandler.onDataArrived(null, false);
                        }
                    }

                });
            }

        } catch (Exception e) {
            System.out.println("Greska kod ucitavanja slike: " + e.toString());
        }

    return name;
    }

}