package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActiveOrderAdmin extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private ListView lista;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String emailUser, telefonUser, item;
    private double volonterLat, volonterLon, korisnikLat, korisnikLon, distance;
   // FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order_admin);

        lista = findViewById(R.id.listaAktivni);

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ActiveAccVolonter.this);
       /* if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    volonterLat = location.getLatitude();
                    volonterLon = location.getLongitude();
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }*/
        FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Status").equalTo("Taken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                   /* korisnikLat =(Double) postSnapshot.child("Lat").getValue();
                    korisnikLon =(Double) postSnapshot.child("Lon").getValue();
                    Location startPoint = new Location("locationA");
                    startPoint.setLatitude(volonterLat);
                    startPoint.setLongitude(volonterLon);

                    Location endPoint = new Location("locationB");
                    endPoint.setLatitude(korisnikLat);
                    endPoint.setLongitude(korisnikLon);

                    DecimalFormat f = new DecimalFormat("##.00");
                    distance=startPoint.distanceTo(endPoint);*/
                    lines.add(postSnapshot.child("Item").getValue().toString());/*+ " - " + f.format(distance) + " m away");*/
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActiveOrderAdmin.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] niza = lines.get(i).split(" - ");
                String tipUsluga = niza[0];
                Intent intent = new Intent(ActiveOrderAdmin.this,UserDescription.class);
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            emailUser = postSnapshot.child("EmailUser").getValue().toString();
                            telefonUser = postSnapshot.child("UserPhone").getValue().toString();
                            item = postSnapshot.child("Item").getValue().toString();
                        }
                        intent.putExtra("Item",item);
                        intent.putExtra("EmailUser",emailUser);
                        intent.putExtra("UserPhone",telefonUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}
