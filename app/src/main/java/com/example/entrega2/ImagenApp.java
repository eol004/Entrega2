package com.example.entrega2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagenApp extends AppCompatActivity {
    ImageView imgSelect;
    Button btnCamara;
    Button btnSig;
    static final int codigoPermisoCamara = 101;
    static final int codigoSolcCamara = 102;
    static final int REQUEST_TAKE_PHOTO = 1;
    String uriimagen;
    StorageReference storageReference;
    //https://www.youtube.com/watch?v=ofO21jEKeig&ab_channel=DeepSingh
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallafoto);

        //Para pasar el usuario
        Bundle extras = getIntent().getExtras();
        String usuarioIntent = extras.getString("usuario");

        storageReference = FirebaseStorage.getInstance().getReference(); //Referencia a la Firebase

        imgSelect = findViewById(R.id.imageView);
        btnCamara = findViewById(R.id.btnCamara);

        //Boton para abrir la camara
        btnCamara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                permisosCamara();
                Log.d("tag", "abriendo camara");
            }
        });

        //Boton para continuar a la siguiente pantalla
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
        registrarDispositivo();
    }

    //Comprobamos que los permisos de la camara están activos
    private void permisosCamara() {
        Log.d("tag", "entrando a permisosCamara");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, codigoPermisoCamara);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Log.d("tag", "abriendo camara en intent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verificar si se puede utilizar el Intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Crear un archivo para guardar la imagen
            Log.d("tag", "entrando al if donde se crea la imagen");
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }

            // Si se el archivo se ha creado se obtiene la URI del archivo se agrega al Intent
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.Entrega2.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, codigoSolcCamara);
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombrefich = "IMG_" + timeStamp + "_";
        File eldirectorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(nombrefich,".jpg", eldirectorio);

        uriimagen = image.getAbsolutePath();
        Log.d("tag", "uriimagen "+uriimagen);
        return image;
    }

    private void uploadImage(Uri fileUri, String name){
        StorageReference storage = storageReference.child("images/" + name);

        storage.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ImagenApp.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                        mostrarFoto(fileUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImagenApp.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void mostrarFoto(Uri uri){
        Picasso.get().load(uri).into(imgSelect);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == codigoPermisoCamara){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Se abrirá la cámara si tiene todos los permisos
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Se necesitan permisos de cámara para su uso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode==codigoSolcCamara) {
            File fichimg = new File(uriimagen);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(fichimg);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            uploadImage(contentUri, fichimg.getName());
            mostrarFoto(contentUri);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (uriimagen != null) {
            outState.putString("uriimagen", uriimagen.toString());
        }
    }

    //Generar token para utlizar con FCM
    //https://firebase.google.com/docs/reference/android/com/google/firebase/iid/FirebaseInstanceIdService
    //https://www.youtube.com/watch?v=fiUkA2OZQjs&ab_channel=unsimpleDev
    public void registrarDispositivo(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d("tag", "Error al conseguir el token");
                    return;
                }
                String token = task.getResult();
                Log.d("tag", "token: " +token);
            }
        });
    }


}
