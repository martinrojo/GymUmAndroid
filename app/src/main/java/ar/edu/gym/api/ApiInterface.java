package ar.edu.gym.api;

import java.util.List;

import ar.edu.gym.model.Movimiento;
import ar.edu.gym.model.Persona;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    String key = "Authorization: Basic YWRtaW46YWRtaW5QYXNz";  //key valida

    //Trae todos los usuarios y los mete en el home activity (listado de usuarios)
    @Headers(key)//mandas la key a traves del header
    @GET("gym/api/personas/")
    Call<List<Persona>> getAllUsers();

    //(Te trae un usuario unico para cuando quieras editarlo te complete automaticamente los campos del usuario) necesitas mandarle el id
    @Headers(key)
    @GET("gym/api/personas/{id}")
    //revisar las url a las q le pegas la httprequest
    Call<Persona> getUserForId(@Path("id") String id);

    //Esta la utilizas para crear un usuario, neecesitas mandarle el modelo completo
    @Headers(key)
    @POST("gym/api/personas/")
    //revisar las url a las q le pegas la httprequest
    Call<Boolean> createUser(@Body Persona usuarios);

    //actualizacion de usuario, mandar modelo con el id
    @Headers(key)
    @PUT("gym/api/personas/{id}")
    //revisar las url a las q le pegas la httprequest
    Call<String> updateUser(@Body Persona usuarios);

    //Esta la utilizas para crear un usuario, neecesitas mandarle el modelo completo
    @Headers(key)
    @POST("gym/api/movimientos/")
    //revisar las url a las q le pegas la httprequest
    Call<Boolean> createMovement(@Body Movimiento movimiento);

    //actualizacion de usuario, mandar modelo con el id
    @Headers(key)
    @PUT("gym/api/movimientos/{id}")
    //revisar las url a las q le pegas la httprequest
    Call<String> updateMovement(@Path("id") String id);

}


//URL BASE
//http://sd.testjava.possumus.cloud


// PATH     "gym/api/usuarios/"   -_> eso va en los verbos GET / POST / PUT