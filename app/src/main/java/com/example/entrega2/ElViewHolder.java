package com.example.entrega2;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElViewHolder extends RecyclerView.ViewHolder {
    public TextView texto;
    public ImageView trago;
    private AdapterView.OnItemClickListener listener;
    public boolean[] seleccion;
    public int numTragos;

    public ElViewHolder(View itemView) {
        super(itemView);
        this.listener = listener;
        texto = itemView.findViewById(R.id.texto);
        trago = itemView.findViewById(R.id.trago);

    }
}

