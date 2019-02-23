package ar.edu.gym.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ar.edu.gym.R;
import ar.edu.gym.api.ApiClient;
import ar.edu.gym.api.ApiInterface;
import ar.edu.gym.model.Movimiento;
import ar.edu.gym.model.Persona;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "PersonRecyclerAdapter";

    private List<Persona> listPersona;
    private Movimiento movimiento;
    private Context context;
    private View.OnClickListener onClickItemListener;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading = false, isLastPage = false;


    public PersonRecyclerAdapter(List<Persona> listUser, Context context) {
        this.listPersona = listUser;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_usuario, parent, false);
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holders, int position) {
        final ViewHolder holder = holders;
        Persona Persona = listPersona.get(position);

        holder.nombreYApellido.setText(Persona.getNombre() + " " + Persona.getApellido());
        holder.horasYminutos.setText(Persona.getHoras() + "hs y " + Persona.getMinutos() + "min");
    }


    @Override
    public int getItemCount() {
        return listPersona.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreYApellido, horasYminutos;

        public ViewHolder(final View view) {
            super(view);

            Calendar c = Calendar.getInstance();


            movimiento = new Movimiento();
            movimiento.setFechaEntrada(c.getTime());

            nombreYApellido = (TextView) view.findViewById(R.id.nombreyApellido);
            horasYminutos = (TextView) view.findViewById(R.id.horaYminutos);

            view.findViewById(R.id.btn_entrar_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movimiento.setPersona(listPersona.get(getAdapterPosition()));

                    ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
                    Call<Boolean> startMovement;

                    startMovement = apiInterface.createMovement(movimiento);

                    try {
                        startMovement.enqueue(new Callback<Boolean>() {

                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                Log.d(TAG, "Call request :" + call.request() + "\n Response: " + response);
                                Toast.makeText(context, "Ingreso de " +
                                        listPersona.get(getAdapterPosition()).getId() +
                                        " Status code: " +
                                        response.code(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.d(TAG, "Error Retrofit: " + t);
                            }
                        });

                    } catch (Exception ex) {
                        Log.d(TAG, "Error Retrofit: " + ex);
                    }
                }
            });

            view.findViewById(R.id.btn_salir_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        movimiento.setPersona(listPersona.get(getAdapterPosition()));

                        Calendar c = Calendar.getInstance();

                        movimiento.setFechaSalida(c.getTime());

                        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
                        Call<String> endMovement;

                        endMovement = apiInterface.updateMovement(movimiento);

                        try {
                            endMovement.enqueue(new Callback<String>() {

                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d(TAG, "Call request :" + call.request() + "\n Response: " + response);
                                    Toast.makeText(context, "Salida de " +
                                            listPersona.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(TAG, "Error Retrofit: " + t);
                                }
                            });

                        } catch (Exception ex) {
                            Log.d(TAG, "Error Retrofit: " + ex);
                        }
                    }
                }
            });

        }
    }

    //Implement View.OnClickListener
    @Override
    public void onClick(View view) {
        if (onClickItemListener != null) {
            onClickItemListener.onClick((view));
        }
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickItemListener = listener;
    }
}