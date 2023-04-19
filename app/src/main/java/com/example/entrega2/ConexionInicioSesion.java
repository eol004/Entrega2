package com.example.entrega2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConexionInicioSesion extends Worker {
    public ConexionInicioSesion(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() { //Conectada con InicioSesion
        //Conexion con el servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/eonate006/WEB/main.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            //Parametros a enviar a la bd
            //String usuario =
            //String parametros = "param1="+dato+"&param2="+valor+"&param3="+otrodato;

            return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
