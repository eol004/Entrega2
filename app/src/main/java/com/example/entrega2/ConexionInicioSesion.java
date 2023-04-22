package com.example.entrega2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        //Parametros a enviar en inicio sesion
        String usuario = getInputData().getString("usuario");
        String contr = getInputData().getString("contraseña");

        //Conexion con el servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/eonate006/WEB/comprobarIS.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            //Cadena con datos
            String parametros = "usuario=" + usuario + "&contraseña=" + contr;

            //Configurar objeto HttpURLConnection
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Incluir parametros en la llamada
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.flush(); //Como no se ha terminado de enviar los datos utilizar flush

            //Recoger el resultado
            int statusCode = urlConnection.getResponseCode();
            System.out.println("Codigo de estado: " +statusCode);

            //Procesar resultado
            BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();

            bufferedReader.close();
            inputStream.close();
            out.close();
            urlConnection.disconnect();
            return Result.success();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
