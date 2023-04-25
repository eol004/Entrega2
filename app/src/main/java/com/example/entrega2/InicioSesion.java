package com.example.entrega2;

import android.content.Intent;
import android.os.Bundle;
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

public class InicioSesion extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        EditText usuario = findViewById(R.id.nomusuario_is);
        EditText contr = findViewById(R.id.contra_is);
        Button btn_inicses = findViewById(R.id.btn_incioses);
        btn_inicses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usuario.getText().toString().isEmpty() && !contr.getText().toString().isEmpty()){
                    iniciarsesion(usuario.getText().toString(), contr.getText().toString());
                }
                else{
                    Toast.makeText(InicioSesion.this,"Error, debes rellenar todos los campos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void iniciarsesion(String usu, String ctr) {
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionInicioSesion.class).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished()){
                    if(workInfo.getOutputData().getBoolean("existe", true)){
                        // Login correcto
                        Toast.makeText(InicioSesion.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InicioSesion.this, ImagenApp.class);
                        intent.putExtra("usuario", usu);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        // Login incorrecto
                        Toast.makeText(InicioSesion.this, "Inicio de sesión incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Data data = new Data.Builder().putString("usuario", usu).putString("contraseña", ctr).build();
        OneTimeWorkRequest otwr2 = new OneTimeWorkRequest.Builder(ConexionInicioSesion.class).setInputData(data).build();
        WorkManager.getInstance(this).beginWith(otwr).then(otwr2).enqueue();
    }

}
