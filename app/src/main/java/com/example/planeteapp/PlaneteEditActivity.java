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
import android.widget.Button;
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

public class PlaneteEditActivity extends AppCompatActivity {
    public  static final String TAG ="EditActivity";
    private Planete planete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planete_create);

        Intent intent= getIntent();

        final long id= intent.getLongExtra("id", 0);

        PlaneteApplication applicationContext=(PlaneteApplication)getApplicationContext();
        PlaneteService service=  applicationContext.getService();
        Call<Planete> planeteById =service.getPlaneteById(id);
        planeteById.enqueue(new Callback<Planete>() {
            @Override
            public void onResponse(Call<Planete> call, Response<Planete> response) {
                if (response.isSuccessful()){
                    planete=response.body();
                    EditText etNom=(EditText)findViewById(R.id.planete_name);
                    EditText etDistance=(EditText)findViewById(R.id.planete_distance);
                    ImageView ivImage=(ImageView )findViewById(R.id.planete_image);
                    etNom.setText(planete.getNom());
                    etDistance.setText(planete.getDistance()+ "");
                    if(planete.getImageBase64()!=null) {
                        byte[]decode= Base64.decode(planete.getImageBase64(), android.util.Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        ivImage.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onFailure(Call<Planete> call, Throwable t) {

                Log.i(TAG, t.toString());
            }
        });


     /* String nom= intent.getStringExtra("nom");
      int distance=intent.getIntExtra("distance",0);
      String imageBase64= intent.getStringExtra("imageBase64");
      planete =new Planete (nom, distance,imageBase64);
      planete.setId(id);
      EditText etNom=(EditText)findViewById(R.id.planete_name);
      EditText etDistance=(EditText)findViewById(R.id.planete_distance);
      ImageView ivImage=(ImageView )findViewById(R.id.planete_image);

        etNom.setText(nom);
        etDistance.setText(distance+ "");
       if(imageBase64!=null) {
           byte[]decode= Base64.decode(planete.getImageBase64(), android.util.Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(decode, 0, decode.length);
        ivImage.setImageBitmap(bitmap);
       }*/

        Button submitbutton=(Button) findViewById(R.id.planete_create_submit_btn);
        submitbutton.setText("modifier planete");
        //        submit btn
        submitbutton.setOnClickListener((view)->{
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

          /*  PlaneteApplication applicationContext=(PlaneteApplication)getApplicationContext();// recuperetion du context PlaneteApplication
            PlaneteService service=applicationContext.getService();// initialisation de la variable*/

            Call<Planete>call= service.editPlanete(planete.getId(),planete);
            call.enqueue(new Callback<Planete>() {
                @Override
                public void onResponse(Call<Planete> call, Response<Planete> response) {
                    if(response.isSuccessful()){
                        Planete planete= response.body();// remplie les values  (?,?,?,?,?,?) dans la table de la bdd

                        Intent intent=getIntent();
                        intent.putExtra("planeteId", planete.getId());
                        setResult(RESULT_OK, intent);
                    }

                }

                @Override
                public void onFailure(Call<Planete> call, Throwable t) {
                    Log.i("planteCeditActivity",t.toString());

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
        final View upLoadbtn= findViewById(R.id.planete_upload_btn);
        upLoadbtn.setOnClickListener((view)->{
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            Intent fileChooser= Intent.createChooser(i, "selectionner une image");// on cree une fenetre pour pouvoir selectionner une fichier
            startActivityForResult(fileChooser,1);

        });

    }

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
                Log.i("onActivityResult", e.toString());
            }
        }



    }
}