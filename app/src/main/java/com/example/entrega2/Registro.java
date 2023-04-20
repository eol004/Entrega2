package com.example.entrega2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class Registro extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //Datos a añadir
        EditText usuario = findViewById(R.id.usuario_reg);
        EditText contr = findViewById(R.id.contra_reg);
        EditText nombre = findViewById(R.id.nom_reg);
        //Añadir a la BD al pulsar el boton de Registro
        Button btn_registrarse = findViewById(R.id.btn_registrarse);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usuario.getText().toString().isEmpty() && !contr.getText().toString().isEmpty() && !nombre.getText().toString().isEmpty()){
                    System.out.println("nombre Registro: "+nombre.getText().toString()+"usu registro: "+usuario.getText().toString()+"contr: "+contr.getText().toString());
                    añadir(usuario.getText().toString(), contr.getText().toString(), nombre.getText().toString());
                    Toast.makeText(Registro.this,"Usuario registrado",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Registro.this,"Error, debes rellenar todos los campos",Toast.LENGTH_LONG).show();
                }
            }
        });
        //Cuando se de al boton de iniciar sesion lleva a la pantalla de inicio sesión
        Button btn_inicioses = findViewById(R.id.btn_inicioses_reg);
        btn_inicioses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InicioSesion.class);
                startActivity(intent);
            }
        });
    }

    private void añadir(String usu, String ctr, String nom) {
        Data data = new Data.Builder()
                .putString("usuario", usu)
                .putString("contraseña", ctr)
                .putString("nombre", nom)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(otwr);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                //System.out.println("nombre Registro: " + nom + "usu registro: " + usu + "contr: " + ctr);
                            } else {
                                System.out.println("Error al conectar con la base de datos");
                            }
                        }
                    }
                });
    }

}
