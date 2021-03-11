package com.example.planeteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.planeteapp.model.Planete;
import com.example.planeteapp.service.PlaneteAdapter;
import com.example.planeteapp.services.PlaneteService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public  static final String TAG ="MainActivity";// cree une entree pour le log
    public static final int PLANETE_CREATE_ACTIVTY=1;
    public static final int PLANETE_EDIT_ACTIVTY=2;
    private PlaneteService service;
    public PlaneteAdapter adapter;
    // ArrayList<Planete>listPlanete=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       RecyclerView rEC= (RecyclerView)findViewById(R.id.list);
        rEC.setVisibility(View.GONE);
       View tvEmpty=findViewById(android.R.id.empty);
       tvEmpty.setVisibility(View.GONE);

       /* Resources res=getResources(); donnes en dur remplacées en bas par un bdd
        String[] nomsTab=res.getStringArray(R.array.nomsPlanetes);
        int[] distancesTab = res.getIntArray(R.array.distances);
        int[] idImagesTab= new int [] {
            R.drawable.mercury,
            R.drawable.venus,
            R.drawable.earth,
            R.drawable.mars,
            R.drawable.jupiter,
            R.drawable.saturn,
            R.drawable.uranus,
            R.drawable.neptune,
            R.drawable.pluto,
        };
        */
        // creer la liste des planetes
        // ArrayList<Planete>listPlanete=new ArrayList<>();

      /*  for (int i = 0; i<nomsTab.length; i++){
           Planete p=new Planete(nomsTab[i], distancesTab[i],idImagesTab[i]);
           listPlanete.add(p);
        }
*/
        ArrayList<Planete>listPlanete=new ArrayList<>();
        //on cree un recycler viex qui va porter la vue
        RecyclerView rv=(RecyclerView)findViewById(R.id.list);
        LinearLayoutManager llm=(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);// ajuste la taille des elements

        //rv.setLayoutManager(new LinearLayoutManager(this));// donne in layout vertical
        //rv.setLayoutManager(new GridLayoutManager(this,3));// affiche une grille
        //rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));// dit au layoutcomment afficher ici donne l instruction linearlayout horizontal avec un scroll il faut verifier l orientation dans le xml vertical à enlver

        // on cree un separateur divider

        DividerItemDecoration did= new DividerItemDecoration(rv.getContext(),llm.getOrientation());// on done l objet qu il faut gerer ici le contxt du recyclervue
        rv.addItemDecoration(did);


        // cree un adptateur  pour afficher la liste
        // ArrayAdapter<Planete> adapterPlanete= new ArrayAdapter<>(this,R.layout.item, R.id.item_nom, listPlanete) ;/*context le layout et l item qui porte l id a afficher*/

        adapter=new PlaneteAdapter(listPlanete);
        adapter.setMenuListener(this);
        rv.setAdapter(adapter);
        // rv.setOnCreateContextMenuListener(this);

        /* Retrofit retrofit = new Retrofit.Builder()     // ce code est remplace par la creation de la class planetapplication
               .baseUrl(" https://0504462e6ef3.ngrok.io/")
               .addConverterFactory(GsonConverterFactory.create())
               .build();

        service = retrofit.create(PlaneteService.class);*/


        PlaneteApplication applicationContext=(PlaneteApplication)getApplicationContext();// recuperetion du context PlaneteApplication
        this.service=applicationContext.getService();


        Call<List<Planete>> planetes =service.getPlanetes();
        planetes.enqueue(new Callback<List<Planete>>() {

            @Override
            public void onResponse(Call<List<Planete>> call, Response<List<Planete>> response) {
              if(response.isSuccessful()){  List<Planete> planetes=response.body();
                // Log.i(TAG, planetes.toString());
                adapter.setPlanetes(planetes);
                adapter.notifyDataSetChanged();
                  findViewById(R.id.progressBar).setVisibility(View.GONE);
                  rEC.animate()
                      .alpha(1f)
                      .setDuration(2000)
                      .setListener(null);
                  rEC.setVisibility(View.VISIBLE);
               /* findViewById(R.id.progressBar).setVisibility(View.GONE);
                rEC.setVisibility(View.VISIBLE);*/


            }
            else if(response.code()==404){
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);

            }
            }


            @Override
            public void onFailure(Call<List<Planete>> call, Throwable errorJettée) {
                Log.i(TAG, errorJettée.toString());
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                ((TextView)tvEmpty).setText("serveur hs");

                tvEmpty.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {// creer le menu burger

       /* MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_planete,menu);*/

        getMenuInflater().inflate(R.menu.menu_planete,menu);// parse le xml du burger menu en java
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId1= item.getItemId();
        switch (itemId1){
            case  R.id.menu_creer_planete:// affiche le formulaire pour creer planete
                Log.i(TAG, "dans menu_creer_planete");
                Intent intent = new Intent(this, PlaneteCreateActivity.class);
                startActivityForResult(intent, PLANETE_CREATE_ACTIVTY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        int position= adapter.getClickedPosition();
        Planete p= adapter.getItem(position);

        switch (itemId) {
            case R.id.menu_edit_planete:// affiche le formulaire pour creer planete
                // Log.i(TAG, "dans menu_edit_planete");
                Intent i=new Intent(this, PlaneteEditActivity.class);
                i.putExtra("id", p.getId());
                i.putExtra("name", p.getNom());
                i.putExtra("distance", p.getDistance());
                //i.putExtra("ImageBase64", p.getImageBase64());

                startActivityForResult(i,PLANETE_EDIT_ACTIVTY );

                return false;

            case R.id.menu_delete_planete:// affiche le formulaire pour creer planete
                Log.i(TAG, "dans menu_delete_planete");
                //on recupere l objet plante en pasant par la position
                // int position= adapter.getClickedPosition();
                //Planete p= adapter.getItem(position);

                if (p==null) return false;
                //on envoie une requete via url
                Call<Planete> planeteCall= service.deletePlanete(p.getId());
                planeteCall.enqueue(new Callback<Planete>() {
                    @Override
                    public void onResponse(Call<Planete> call, Response<Planete> response) {
                        if( response.isSuccessful()){// retour de la methode du dao du back end spring
                            Planete planete = response.body();
                            adapter.deletePlanete(planete.getId());
                        }
                    }

                    @Override
                    public void onFailure(Call<Planete> call, Throwable t) {
                        Log.i(TAG,"on essaie de supprimer mais " +t.toString());
                    }
                });
                return false;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                        if(resultCode==RESULT_OK && requestCode== PLANETE_CREATE_ACTIVTY && null !=data)    {
                                                 /*planetecreate activity nous renvoie une reponse
                                                   final String nomPlanete= data.getStringExtra("nomPlanete");
                                                 final int distancePLanete=  data.getIntExtra("distancePLanete",0);
                                                 Planete planete =new Planete(nomPlanete, distancePLanete, R.drawable.saturn);*/
                            long planeteId=  data.getLongExtra("planeteId", 0L);
                                                      //recuperer la planete par id
                            Call<Planete> call =service.getPlaneteById(planeteId);

                            call.enqueue(new Callback<Planete>() {
                                @Override
                                public void onResponse(Call<Planete> call, Response<Planete> response) {
                                    if(response.isSuccessful()){
                                        Planete planete= response.body();
                                        adapter.addPlanete(planete);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Planete> call, Throwable t) {
                                    Log.i(TAG," call<Planete>  "+t.toString());
                                }
                            }
                            );
                        }

                        if(resultCode==RESULT_OK && requestCode== PLANETE_EDIT_ACTIVTY && null !=data){
                            long planeteId=  data.getLongExtra("planeteId", 0L);
                            Call<Planete> call =service.getPlaneteById(planeteId);
                            call.enqueue(new Callback<Planete>() {
                                @Override
                                public void onResponse(Call<Planete> call, Response<Planete> response) {

                                    if(response.isSuccessful()){
                                        Planete planete= response.body();
                                        adapter.editPlanete(planete);

                                    }
                                }

                                @Override
                                public void onFailure(Call<Planete> call, Throwable t) {
                                    Log.i(TAG,""+t.toString());
                                }
                            });

                        }

    }
    public PlaneteAdapter getAdapter() {
        return adapter;
    }


}