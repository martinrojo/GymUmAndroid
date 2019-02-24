package ar.edu.gym.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //public static final String BASE_URL = "http://sd.testjava.possumus.cloud/";
    public static final String BASE_URL = "http://192.168.0.103:8080/";
    public static Retrofit retrofit;

    public static Retrofit getApliClient(){
        Gson gson=  new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").create();
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
