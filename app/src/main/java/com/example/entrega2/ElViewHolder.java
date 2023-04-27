package com.example.entrega2;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElViewHolder extends RecyclerView.ViewHolder {
    public TextView texto;
    public ImageView usuarioFoto;
    private AdapterView.OnItemClickListener listener;
    public boolean[] seleccion;
    public TextView desc;
    public boolean[] botonhablar;


    public ElViewHolder(View itemView) {
        super(itemView);
        this.listener = listener;
        texto = itemView.findViewById(R.id.usuario);
        usuarioFoto = itemView.findViewById(R.id.usuarioFoto);
        desc = itemView.findViewById(R.id.descUsuarios);

    }
}

