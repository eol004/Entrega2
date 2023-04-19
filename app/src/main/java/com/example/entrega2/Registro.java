package com.example.entrega2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.ExecutionException;

public class Registro extends AppCompatActivity {
    EditText usuario = findViewById(R.id.usuario_reg);
    EditText contr = findViewById(R.id.contra_reg);
    EditText nombre = findViewById(R.id.nom_reg);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //Datos a añadir
        /*EditText usuario = findViewById(R.id.usuario_reg);
        EditText contr = findViewById(R.id.contra_reg);
        EditText nombre = findViewById(R.id.nom_reg);*/
        if(!usuario.getText().toString().isEmpty() && contr.getText().toString().isEmpty() && nombre.getText().toString().isEmpty()){
            añadir(usuario.getText().toString(), contr.getText().toString());
        }
        else{
            Toast.makeText(Registro.this,"Error, debes rellenar todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private void añadir(String usu, String ctr) {
        //Data data = new Data.Builder();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionServidorDAS.class).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            Data data = new Data.Builder().putString("usuario", usuario.getText().toString()).putString("nombre",nombre.getText().toString()).putString("contraseña", contr.getText().toString()).build();
                            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDWebService.class)
                                    .setInputData(data)
                                    .build();
                            WorkManager.getInstance(Registro.this).enqueue(otwr);
                            finish();
                        }
                    }
                });
    }
}
