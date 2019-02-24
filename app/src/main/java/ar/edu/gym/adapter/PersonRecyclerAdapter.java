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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

            movimiento = new Movimiento();

            movimiento.setFechaEntrada(null);
            movimiento.setFechaSalida(null);
            nombreYApellido = (TextView) view.findViewById(R.id.nombreyApellido);
            horasYminutos = (TextView) view.findViewById(R.id.horaYminutos);

            view.findViewById(R.id.btn_entrar_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movimiento.setPersona(listPersona.get(getAdapterPosition()));

                    ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
                    Call<Movimiento> startMovement;

                    startMovement = apiInterface.createMovement(movimiento);

                    try {
                        startMovement.enqueue(new Callback<Movimiento>() {

                            @Override
                            public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                                Log.d(TAG, "Call request :" +
                                        call.request() +
                                        "\n Response: " +
                                        response);
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Ingreso de " +
                                                    listPersona.get(getAdapterPosition()).getId()
                                            , Toast.LENGTH_SHORT).show();
                                }
                                if (response.code() == 404) {
                                    Toast.makeText(context, "Debe salir primero",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Movimiento> call, Throwable t) {
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
                        movimiento.setFechaEntrada(null);
                        movimiento.setFechaSalida(null);
                        ApiInterface apiInterface = ApiClient.getApliClient().create(ApiInterface.class);
                        Call<Movimiento> endMovement;

                        endMovement = apiInterface.updateMovement(listPersona.get(getAdapterPosition()).getId());

                        try {
                            endMovement.enqueue(new Callback<Movimiento>() {

                                @Override
                                public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                                    Log.d(TAG, "Call request :" + call.request() + "\n Response: " + response);
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Salida de " +
                                                listPersona.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
                                    }
                                    if (response.code() == 404) {
                                        Toast.makeText(context,
                                                "No hay movimientos creados para el usuario" +
                                                        listPersona.get(getAdapterPosition()).getId(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Movimiento> call, Throwable t) {
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