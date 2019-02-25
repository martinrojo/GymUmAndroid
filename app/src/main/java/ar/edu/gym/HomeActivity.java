package ar.edu.gym;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ar.edu.gym.api.ApiClient;
import ar.edu.gym.api.ApiInterface;
import ar.edu.gym.adapter.PersonRecyclerAdapter;
import ar.edu.gym.model.Persona;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private List<Persona> personaLista;
    private FloatingActionButton fabAgregarUsuario;
    private FloatingActionButton fabIngresarGym;
    private FloatingActionButton fabSalirGym;
    private RecyclerView recyclerViewPerson;
    private RecyclerView.LayoutManager layoutManager;
    private PersonRecyclerAdapter personRecyclerAdapter;

    private void initVariable(){
        personaLista = new ArrayList<Persona>();
        fabAgregarUsuario = (FloatingActionButton) findViewById(R.id.fab_agregar_usuario);
        fabIngresarGym =  (FloatingActionButton) findViewById(R.id.btn_entrar_gym);
        fabSalirGym  = (FloatingActionButton) findViewById(R.id.btn_salir_gym);
        recyclerViewPerson = (RecyclerView) findViewById(R.id.recyclerViewItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVariable();

        //boton flotante Agregar usuario
        fabAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreateUser = new Intent(HomeActivity.this, CreateUserActivity.class);
                //intentCreateUser.putExtra("key", "valor");  //Funciona como un array
                startActivity(intentCreateUser);
            }
        });


    }


    @Override
    public void onResume(){
        super.onResume();
        consumeApiServices();
    }

    //Load Search Box
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Buscar personas");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String querySearch) {
                //Mientras vos escribas no filtra pero al finalizar y pulsar enter te trae un resultad
                return false;
            }

            @Override
            public boolean onQueryTextChange(String querySearch) {
                //Mientras escribis se filtra
                if(querySearch.length() > 0) {
                   //ACA TENES Q HACER EL FILTRADO CON UN RECYCLER VIEW
                }
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }
    //Consume resource with Retrofit 2
    private void consumeApiServices() {
        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
        Call<List<Persona>> getAllUsers;

        getAllUsers = apiInterface.getAllUsers();

        try {
            getAllUsers.enqueue(new Callback<List<Persona>>() {

                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    personaLista = response.body();
                    notifyChangeAdapter(personaLista);
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {
                    Log.d(TAG, "Error Retrofit: "+ t);
                }
            });

        } catch (Exception ex) {

        }
    }

    //Save resource in ListRecycler
    private void notifyChangeAdapter(final List<Persona> listUser){
        layoutManager = new LinearLayoutManager(this);
        recyclerViewPerson.setLayoutManager (layoutManager);

        personRecyclerAdapter = new PersonRecyclerAdapter(listUser, HomeActivity.this);
        recyclerViewPerson.setAdapter(personRecyclerAdapter);


        personRecyclerAdapter.notifyDataSetChanged();

        editUserCreated();

    }

    private void editUserCreated(){
        personRecyclerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditPersonCreated = new Intent(HomeActivity.this, CreateUserActivity.class);
                intentEditPersonCreated.putExtra("idUser", personaLista.get(recyclerViewPerson.getChildAdapterPosition(view)).getId());
                startActivity(intentEditPersonCreated);
            }
        });    }
}