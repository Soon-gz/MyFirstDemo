package com.abings.baby.data.remote;

import android.content.Context;

import com.abings.baby.BuildConfig;
import com.abings.baby.data.model.CheckIn;
import com.abings.baby.data.model.CheckInRequest;
import com.abings.baby.data.model.Encounter;
import com.abings.baby.data.model.RegisteredBeacon;
import com.abings.baby.data.model.Ribot;
import com.abings.baby.data.model.Venue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface RibotService {

    String ENDPOINT = "https://api.ribot.io/";
    String AUTH_HEADER = "Authorization";

    String ENDAPI = "http://api.avatardata.cn/";

/*    @GET("GuoNeiNews/Query")
    Observable<BaseResponse> getChinaNews(@Query("key") String key,@Query("page") int pageNo);*/

    @POST("auth/sign-in")
    Observable<SignInResponse> signIn(@Body SignInRequest signInRequest);

    @GET("ribots")
    Observable<List<Ribot>> getRibots(@Header(AUTH_HEADER) String authorization,
                                      @Query("embed") String embed);

    @GET("venues")
    Observable<List<Venue>> getVenues(@Header(AUTH_HEADER) String authorization);

    @POST("check-ins")
    Observable<CheckIn> checkIn(@Header(AUTH_HEADER) String authorization,
                                @Body CheckInRequest checkInRequest);

    @PUT("check-ins/{checkInId}")
    Observable<CheckIn> updateCheckIn(@Header(AUTH_HEADER) String authorization,
                                      @Path("checkInId") String checkInId,
                                      @Body UpdateCheckInRequest updateCheckInRequest);

    @POST("/beacons/{beaconId}/encounters")
    Observable<Encounter> performBeaconEncounter(@Header(AUTH_HEADER) String authorization,
                                                 @Path("beaconId") String beaconId);

    @GET("/beacons")
    Observable<List<RegisteredBeacon>> getRegisteredBeacons(
            @Header(AUTH_HEADER) String authorization);


    /******** Factory class that sets up a new ribot services *******/
    class Factory {

        public static RibotService makeRibotService(Context context) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.interceptors().add(new UnauthorisedInterceptor(context));
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            okHttpClient.interceptors().add(logging);
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            // .registerTypeAdapter(ChinaNew.class, new RestDeserializer<>(ChinaNew.class, "result"))
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RibotService.ENDAPI)//ENDPOINT
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(JsonConverterFactory.create()) //把数据转换为JSONOBJECT
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RibotService.class);
        }



    }

    class Util {
        // Build API authorization string from a given access token.
        public static String buildAuthorization(String accessToken) {
            return "Bearer " + accessToken;
        }
    }

    /******** Specific request and response models ********/
    class SignInRequest {
        public String googleAuthorizationCode;

        public SignInRequest(String googleAuthorizationCode) {
            this.googleAuthorizationCode = googleAuthorizationCode;
        }
    }

    class SignInResponse {
        public String accessToken;
        public Ribot ribot;
    }

    class UpdateCheckInRequest {
        public boolean isCheckedOut;

        public UpdateCheckInRequest(boolean isCheckedOut) {
            this.isCheckedOut = isCheckedOut;
        }
    }

}
