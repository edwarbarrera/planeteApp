package com.example.planeteapp.service;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planeteapp.R;
import com.example.planeteapp.model.Planete;
import com.example.planeteapp.view.PlaneteView;

import java.util.Iterator;
import java.util.List;

public class PlaneteAdapter  extends RecyclerView.Adapter<PlaneteView> {
    private List<Planete> list;
    private  int clickedPosition=RecyclerView.NO_POSITION;// constante pas encore definie
    private View.OnCreateContextMenuListener menuListener;

      public PlaneteAdapter( @NonNull List<Planete> planetes) {
        super();
        list=planetes;
    }



    @NonNull
    @Override
    public PlaneteView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// cree une vue si elle n existe  pas
         LayoutInflater li=LayoutInflater.from(parent.getContext());// parent correspond a recyclerview et context == this
        View view=  li.inflate(R.layout.item, parent, false); //le false est poyur savoir si on doit attacher l objet à  la racine
        return new PlaneteView(view);
}

    @Override
    public void onBindViewHolder(@NonNull PlaneteView holder, final int position) {// lies les elements de la liste par position dans la liste a un viewholder qui portera la vue
        holder.setItem(list.get(position));
        // on ajoute un ecouteru d evenement de type onclick sur holder

        holder.itemView.setOnClickListener(new View.OnClickListener() {// avec un lambda ((view)->  setClickedPosition(position))
            @Override
            public void onClick(View v) {
                Log.i("planeteAdapter", position+ "");
                setClickedPosition(position);
            }
        }
        );
        holder.itemView.setOnLongClickListener((view)->{
            Log.i("planeteAdapter", position+ "");
            setClickedPosition(position);
            return false;
        });

         /* int color= getClickedPosition()== position ? Color.LTGRAY :Color.TRANSPARENT;
                en if else
                if (getClickedPosition()==position ){
                color=Color.LTGRAY
                }
                else {color=Color.TRANSPARENT}

                 */
        //holder.itemView.setBackgroundColor(color);
        holder.itemView.setBackgroundColor(getClickedPosition() == position? Color.LTGRAY :Color.TRANSPARENT);
        holder.itemView.setOnCreateContextMenuListener(menuListener);
    }


    @Override
    public int getItemCount() {// donne la taille de la liste
        return list.size();
    }

    public void setClickedPosition(int clickedPosition) {
        notifyItemChanged(this.clickedPosition);// pour l instant ne fonctionne pas devrai remettre le backgriund en transparent
        this.clickedPosition = clickedPosition;
        notifyItemChanged(clickedPosition);
    }





    public int getClickedPosition() {
        return clickedPosition;
    }

    public View.OnCreateContextMenuListener getMenuListener() {
        return menuListener;
    }

    public void setMenuListener(View.OnCreateContextMenuListener menuListener) {

        this.menuListener = menuListener;
    }
    public  void addPlanete (Planete planete){
          list.add(planete);
    notifyItemInserted(list.size()-1);
}


    public void setPlanetes(List<Planete> planetes) {
        /*  list.clear();
          list.addAll(planetes);*/
          list=planetes;
    }

    public Planete getItem(int position) {
          if (position>=0 && position<list.size()){
              return  list.get(position);
          }
          return null;


    }


    public void deletePlanete(long id) {
         for(Iterator<Planete> iterator= list.iterator(); iterator.hasNext();) {// iterator pointe vers un objet de la collection verifie s il y a un autre leemnt apres lui
             Planete p =iterator.next(); // deplace le curseur juska trouver l id cherché il
             if (p.getId()==id){ // on teste l element si il corrzespond a celui qu on cherche
                 int position= list.indexOf(p);    //on recupere la position avant la suppresion de l element
                 list.remove(p);
                 notifyItemRemoved(position);
                 break;
             }
         }
         }




    public void editPlanete(Planete planete) {
        for(Iterator<Planete> iterator= list.iterator(); iterator.hasNext();) {// iterator pointe vers un objet de la collection verifie s il y a un autre leemnt apres lui

            Planete p =iterator.next(); // deplace le curseur juska trouver l id cherché il

            if (p.getId()==planete.getId()){    // on teste l element si il corrzespond a celui qu on cherche

                int position= list.indexOf(p);    //on recupere la position avant la suppresion de l element
                list.set(position, planete);
                notifyItemChanged(position);
                break;
            }

        }
    }
}
