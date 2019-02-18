package ar.edu.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ar.edu.gym.api.ApiClient;
import ar.edu.gym.api.ApiInterface;
import ar.edu.gym.model.Persona;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = "CreateUserActivity";
    private FloatingActionButton fabCargarusuario;
    private EditText edNombre;
    private EditText edApellido;
    private EditText edEmail;
    private EditText edDNI;
    private Intent intentCreatePerson;
    private Bundle bundleDatosUser;
    private String stringUserId;
    private Persona persona;

    private void initVariable(){
        fabCargarusuario = (FloatingActionButton) findViewById(R.id.fab_cargar_usuario);
        edNombre = (EditText) findViewById(R.id.ed_nombre);
        edApellido = (EditText) findViewById(R.id.ed_apellido);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edDNI = (EditText) findViewById(R.id.ed_dni);
        stringUserId = null;
        persona = new Persona();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVariable();


    }


    @Override
    protected void onResume() {
        super.onResume();
        intentCreatePerson  = getIntent();
        bundleDatosUser = intentCreatePerson .getExtras();

        if(bundleDatosUser != null) {
            stringUserId = bundleDatosUser.getString("idUser");
            getUserForId(stringUserId);
        }



        fabCargarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarModelo(); //metodo para agregar datos de usuario
            }
        });
    }

    private void cargarModelo() {


        if(!Objects.isNull(persona)){
            if(stringUserId != null && !stringUserId.equals("")){
                //si el formulario tiene datos actualiza un usuario
                //consumeUpdateApiServices();
                Toast.makeText(this, "Tenes que crear el update en la api... pancho!!", Toast.LENGTH_SHORT).show();
            }else{
                //si el formulario estaba vacio crea un usuario
                persona =  new Persona(null,
                        edNombre.getText().toString(),
                        edApellido.getText().toString(),
                        edEmail.getText().toString(),
                        edDNI.getText().toString(),
                        null, null);

                consumeCreateApiServices();

            }
        }

    }

    private void llenarFormulario() {
        edNombre.setText(persona.getNombre());
        edApellido.setText(persona.getApellido());
        edEmail.setText(persona.getEmail());
        edDNI.setText(persona.getDni());
    }

    private void vaciarFormulario(){
        edNombre.setText("");
        edApellido.setText("");
        edEmail.setText("");
        edDNI.setText("");
    }


    //Consume resource with Retrofit 2 creando
    private void consumeCreateApiServices() {

        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
        Call<Boolean> createUser;
        createUser = apiInterface.createUser(persona);

        try {

            createUser.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.code() == 200){
                        Toast.makeText(CreateUserActivity.this, "Usuario creado correctamente...", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    if (response.code() == 404){
                        Toast.makeText(CreateUserActivity.this, "No se pudo cargar el usuario. Email en uso. Intente nuevamente...", Toast.LENGTH_SHORT).show();
                        vaciarFormulario();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                    Toast.makeText(CreateUserActivity.this, "Hubo un error jaja salu2", Toast.LENGTH_SHORT).show();
                    vaciarFormulario();
                }
            });

        } catch (Exception ex) {
            Log.d(TAG, "consumeCreateApiServices: Error: " + ex);
        }
    }


    //Consume resource with Retrofit 2 creando
    private void consumeUpdateApiServices() {
        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
        Call<String> updateUser;
        updateUser = apiInterface.updateUser(persona);

        try {

            updateUser.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(CreateUserActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        } catch (Exception ex) {
            Log.d(TAG, "consumeUpdateApiServices: Error: " + ex);
        }
    }



    //Consume resource with Retrofit 2 creando
    private void getUserForId(String id) {

        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
        Call<Persona> getUserForId;
        getUserForId = apiInterface.getUserForId(id);

        try {
            getUserForId.enqueue(new Callback<Persona>() {
                @Override
                public void onResponse(Call<Persona> call, Response<Persona> response) {
                    persona.setId(response.body().getId().toString());
                    persona.setNombre(response.body().getNombre().toString());
                    persona.setApellido(response.body().getApellido().toString());
                    persona.setEmail(response.body().getEmail().toString());
                    persona.setDni(response.body().getDni().toString());
                    persona.setHoras(response.body().getHoras().toString());
                    persona.setMinutos(response.body().getMinutos().toString());

                    llenarFormulario();
                }

                @Override
                public void onFailure(Call<Persona> call, Throwable t) {
                    Log.d(TAG, "onFailure: Error select for update " + t);
                }
            });


        } catch (Exception ex) {
            Log.d(TAG, "getUserForId: Error: " + ex);
        }
    }
}
