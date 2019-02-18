package ar.edu.gym.adapter;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.edu.gym.R;
import ar.edu.gym.model.Persona;


public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.ViewHolder> implements View.OnClickListener{
    private List<Persona> listUsuarios;
    private Context context;
    private View.OnClickListener onClickItemListener;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading = false, isLastPage = false;



    public PersonRecyclerAdapter(List<Persona> listUser, Context context) {
        this.listUsuarios = listUser;
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
        Persona usuario = listUsuarios.get(position);

        holder.nombreYApellido.setText(usuario.getNombre() + " " + usuario.getApellido());
        holder.horasYminutos.setText(usuario.getHoras()+"hs y "+ usuario.getMinutos()+"min");
    }


    @Override
    public int getItemCount() {
        return listUsuarios.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreYApellido, horasYminutos;

        public ViewHolder(final View view){
            super(view);

            nombreYApellido = (TextView) view.findViewById(R.id.nombreyApellido);
            horasYminutos = (TextView) view.findViewById(R.id.horaYminutos);

            view.findViewById(R.id.btn_entrar_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listUsuarios.get(getAdapterPosition()).getId()
                    //llamar al metodo con retrofit
                    Toast.makeText(context, "Ingreso de " +listUsuarios.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
                }
            });

            view.findViewById(R.id.btn_salir_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listUsuarios.get(getAdapterPosition()).getId()
                    //lamar al metodo con retrofit que haga el consumeservice para hacer egreso
                    Toast.makeText(context, "Salida de " +listUsuarios.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }



    //Implement View.OnClickListener
    @Override
    public void onClick(View view) {
        if(onClickItemListener!=null){
            onClickItemListener.onClick((view));
        }
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.onClickItemListener = listener;
    }

}
