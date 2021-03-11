package com.example.planeteapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planeteapp.R;
import com.example.planeteapp.model.Planete;

import java.util.jar.Attributes;

public class PlaneteView extends RecyclerView.ViewHolder {// ici on lie les lements de l avue avec la donnees
    private TextView tvName;
    private TextView tvDistance;
    private ImageView ivImage;


    public PlaneteView(View view){
        super(view);
        findViews(view);

    };
    /*
    public static PlaneteView create(ViewGroup parent){
    LayoutInflater inflater=LayoutInflater.from((parent.getContext()));
    PlaneteView planeteView=(PlaneteView) inflater.inflate(R.layout.item, parent,false);
    planeteView.findViews();
    return planeteView;
    };*/

    public void findViews(View view){
        this.tvName=view.findViewById(R.id.nomPlanete);
        this.tvDistance=view.findViewById(R.id.distancePlanete);
        this.ivImage=view.findViewById(R.id.imagePlanete);

    }

    public void setItem(final Planete planete) {
        this.tvName.setText(planete.getNom());
        this.tvDistance.setText(planete.getDistance()+" Gm");


        if(planete.getImageBase64()!=null){
            byte[]decode= Base64.decode(planete.getImageBase64(), android.util.Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(decode, 0, decode.length);
            this.ivImage.setImageBitmap(bitmap);

        }
    }
}
