package com.example.entrega2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ElAdaptadorRecycler extends RecyclerView.Adapter<ElViewHolder> {

    private String[] losnombres; //Usuarios
    private int[] lasimagenes; //Imagenes
    private boolean[] seleccionados;
    private String[] lasdescripciones; //descripciones
    private boolean[] botonesSelect;
    private OnItemClickListener listener;

    public ElAdaptadorRecycler (String[] nombres, int[] imagenes, String[] descripciones){
        this.losnombres = nombres;
        this.lasimagenes = imagenes;
        this.lasdescripciones = descripciones;
        seleccionados = new boolean[nombres.length];
        botonesSelect = new boolean[nombres.length];
    }

    @Override
    public ElViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarioslayout, parent, false);
        ElViewHolder holder = new ElViewHolder(elLayoutDeCadaItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ElViewHolder holder, int position) {
        holder.texto.setText(losnombres[position]);
        holder.usuarioFoto.setImageResource(lasimagenes[position]);
        holder.desc.setText(lasdescripciones[position]);
        /*holder.trago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAbsoluteAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, holder.numTragos);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return lasimagenes.length;
        //return 0;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
