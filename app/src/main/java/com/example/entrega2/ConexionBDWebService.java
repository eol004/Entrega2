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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConexionBDWebService extends Worker {

    public ConexionBDWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() { //Conectada con Registro
        //Registro de usuarios
        String nombre = getInputData().getString("nombre");
        String usuario = getInputData().getString("usuario");
        String contr = getInputData().getString("contraseña");

        //Generar objeto HttpURLConnection
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/eonate006/WEB/main.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            //Parametros a enviar
            String parametros = "usuario=" + usuario + "&nombre=" + nombre + "&contraseña=" + contr;

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

            //Procesar resultado
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                return Result.success();
            }
        } catch (ProtocolException e) {
            System.out.println("Error de protocolo");
            return Result.failure();
        } catch (MalformedURLException e) {
            System.out.println("Error de URL");
            return Result.failure();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }
}
