package com.example.planeteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.planeteapp.model.Planete;
import com.example.planeteapp.services.PlaneteService;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaneteCreateActivity extends AppCompatActivity {
    private Planete planete;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planete_create);
        //recuperer le bouton upload planete et on y ajoute un listener
        planete=new Planete();

        final View upLoadbtn= findViewById(R.id.planete_upload_btn);
        upLoadbtn.setOnClickListener((view)->{
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            Intent fileChooser= Intent.createChooser(intent, "selectionner une image");// on cree une fenetre pour pouvoir selectionner une fichier
            startActivityForResult(fileChooser,1);


        });







        //recuperer le bouton creer planete

        final View button= findViewById(R.id.planete_create_submit_btn);

        //ajouter le listner
        button.setOnClickListener((view)->{
            final EditText etNom=  (EditText)findViewById(R.id.planete_name);//on recupere le nom du champs de text

            final EditText etDistance= (EditText)findViewById(R.id.planete_distance);// on reucpere la distance
            String nomPlanete=etNom.getText().toString();
            String tempDistance=etDistance.getText().toString();
            int distance= tempDistance.isEmpty() ?0 : Integer.parseInt(etDistance.getText().toString());
// validation des donnees
            boolean error=false;

            nomPlanete=nomPlanete.trim();

            if(nomPlanete.isEmpty()){
                etNom.setBackgroundColor(Color.RED);
                etNom.setTextColor(Color.WHITE);
                error=true;
            }
            //validation à faire en utilisant RegEx
            if(distance < 0){
                etDistance.setBackgroundColor(Color.RED);
                etDistance.setTextColor(Color.WHITE);
                error=true;
            }
            if (error) return;

            planete.setNom(nomPlanete);
            planete.setDistance(distance);

            // envoyer la requete vers le serveur /planetes
            // creer l objet retrofit
           /* Retrofit retrofit = new Retrofit.Builder()// connection a la bdd  remplacé par le singleton dans planeteAppliaction
                    .baseUrl("https://cbc540afb436.ngrok.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PlaneteService service=retrofit.create(PlaneteService.class);*/

            PlaneteApplication applicationContext=(PlaneteApplication)getApplicationContext();// recuperetion du context PlaneteApplication
            PlaneteService service=applicationContext.getService();// initialisation de la variable

            Call<Planete>call= service.createPlanete(this.planete);
            call.enqueue(new Callback<Planete>() {
                @Override
                public void onResponse(Call<Planete> call, Response<Planete> response) {
                    if(response.isSuccessful()){
                        Planete planete= response.body();
                        Intent intent=getIntent();
                        intent.putExtra("planeteId", planete.getId());
                        setResult(RESULT_OK, intent);
                    }

                }

                @Override
                public void onFailure(Call<Planete> call, Throwable t) {
                    Log.i("planteCreateActivity",t.toString());

                }
            });


            // on recupere l objet intent pour envoyer la repo,se vres la main activity


           /* Intent intent=getIntent();
             intent.putExtra("nomPlanete", nomPlanete);
             intent.putExtra("distancePLanete",distance);
             //envoye la reponse via le code result ok ET L OBJET INTENT
             setResult(RESULT_OK,intent);
             finish();*/

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && null!=data){// l user a bien choisi une image

            Uri uri =data.getData();

            try {
                ParcelFileDescriptor r=  getContentResolver().openFileDescriptor(uri,"r");// r POUR READ w POUR WRITTABLE

                FileDescriptor fileDescriptor=r.getFileDescriptor();
                Bitmap bitmap= BitmapFactory.decodeFileDescriptor(fileDescriptor);
                ImageView ivImage =(ImageView)findViewById(R.id.planete_image);
                ivImage.setImageBitmap(bitmap);



                //convertir  bitmap en un byteArray
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);// on convert avec une constate png on garde 100 % de qualite
                byte[] bytes=baos.toByteArray();


                //convertir un tableau de bytes en un string base64

                String imageBase64=Base64.encodeToString(bytes, Base64.DEFAULT);
                planete.setImageBase64(imageBase64);



            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }



    }
}