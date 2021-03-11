package com.example.planeteapp;

import android.app.Application;

import com.example.planeteapp.services.PlaneteService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaneteApplication extends Application {// cette classe créé une sorte de singleto pour la connection avce retrofit2
    private PlaneteService service; // variable globale


    @Override
    public void onCreate() {// on redefini la methode heritée
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://af13824a86a4.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service =retrofit.create(PlaneteService.class);

    }

    public PlaneteService getService() {
        return service;

    }

    public void setService(PlaneteService service) {
        this.service = service;
    }

}
