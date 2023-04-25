package com.example.entrega2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

public class ImagenApp extends AppCompatActivity {
    ImageView imgSelect;
    Button btnCamara;
    Button btnSig;
    private int codigoPermisoCamara = 101;
    private int codigoSolcCamara = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallafoto);

        Bundle extras = getIntent().getExtras();
        String usuarioIntent = extras.getString("usuario");

        imgSelect = findViewById(R.id.imageView);
        btnCamara = findViewById(R.id.btnCamara);

        btnCamara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                permisosCamara();
            }
        });

        btnSig = findViewById(R.id.button);
        btnSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ImagenApp.this, AnadirDesc.class);
                intent.putExtra("usuario", usuarioIntent);
                startActivity(intent);
                finish();
            }
        });
    }

    //Comprobamos que los permisos de la camara están activos
    private void permisosCamara() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, codigoPermisoCamara);
        }
        else{
            abrirCamara();
        }
    }
    private void abrirCamara() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, codigoSolcCamara);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == codigoPermisoCamara){
            if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Se abrirá la cámara si tiene todos los permisos
            }else {
                Toast.makeText(this, "Se necesitan permisos de cámara para su uso", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==codigoSolcCamara){
            Bitmap elBitmap = (Bitmap) data.getExtras().get("data");
            imgSelect.setImageBitmap(elBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
