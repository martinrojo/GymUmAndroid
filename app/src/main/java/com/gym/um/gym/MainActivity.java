package com.gym.um.gym;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextDni, editTextEmail;
    private Button getUsuarios;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        editTextNombre = findViewById(R.id.nombre);
        editTextApellido = findViewById(R.id.apellido);
        editTextDni = findViewById(R.id.dni);
        editTextEmail = findViewById(R.id.email);
        getUsuarios = findViewById(R.id.getUsuarios);
        getUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();
            }
        });

    }

    private void obtenerDatos(){
        String url = "http://sd.testjava.possumus.cloud/gym/api/usuarios";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("usuarios");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("nombre");
                        String apellido = jsonObject.getString("apellido");
                        String email = jsonObject.getString("email");
                        //String dni = jsonObject.getString("dni");
                    }
                    Log.d("Response", response.toString());
                    Toast.makeText(MainActivity.this, "Nombre:" + response.toString()  , Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader("user1", "user1Pass");
            }
            Map<String, String> createBasicAuthHeader(String username, String password) {
                Map<String, String> headerMap = new HashMap<String, String>();

                String credentials = username + ":" + password;
                String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headerMap.put("Authorization", "Basic " + encodedCredentials);

                return headerMap;
            }
        };
        queue.add(getRequest);
        //
    }
}
