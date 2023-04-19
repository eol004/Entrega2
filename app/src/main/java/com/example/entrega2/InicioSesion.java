package com.example.entrega2;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class InicioSesion extends AppCompatActivity {
    EditText usuario = findViewById(R.id.nomusuario_is);
    EditText contr = findViewById(R.id.contra_is);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        if(!usuario.getText().toString().isEmpty() && contr.getText().toString().isEmpty()){
            iniciarsesion(usuario.getText().toString(), contr.getText().toString());
        }
        else{
            Toast.makeText(InicioSesion.this,"Error, debes rellenar todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarsesion(String usu, String ctr) {
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionInicioSesion.class).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished()){
                    Data data = new Data.Builder().putString("usuario", usu).putString("contraseña", ctr).build();
                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBDWebService.class).setInputData(data).build();
                    WorkManager.getInstance(InicioSesion.this).enqueue(otwr);
                    finish();
                }
            }
        });


    }

}
