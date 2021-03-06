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
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminPom extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private ListView lista;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button prijaveniZadaci;
    private Button logout;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String celoIme, datum, item,adresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pom);

        lista = findViewById(R.id.listaVolonter);

        prijaveniZadaci = findViewById(R.id.prijaveniZadaci);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPom.this,MainActivity.class));
            }
        });

        prijaveniZadaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPom.this, ActiveOrderAdmin.class));
            }
        });
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Volonter.this);
        /*if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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

        FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Status").equalTo("Active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    /*//  korisnikLat =(Double) postSnapshot.child("Lat").getValue();
                    //  korisnikLon =(Double) postSnapshot.child("Lon").getValue();
                   // Location startPoint = new Location("locationA");
                    //startPoint.setLatitude(volonterLat);
                    //startPoint.setLongitude(volonterLon);

                   Location endPoint = new Location("locationB");
                    endPoint.setLatitude(korisnikLat);
                    endPoint.setLongitude(korisnikLon);
                    DecimalFormat f = new DecimalFormat("##.00");
                    distance=startPoint.distanceTo(endPoint);*/
                    lines.add(postSnapshot.child("Item").getValue().toString());//+ " - " + f.format(distance) + " m away");

                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(AdminPom.this, android.R.layout.simple_list_item_1,lines);
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
                String itemPom = niza[0];
                Intent intent = new Intent(AdminPom.this,AdminDetails.class);
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(itemPom).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            /*korisnikLat =(Double) postSnapshot.child("Lat").getValue();
                            korisnikLon =(Double) postSnapshot.child("Lon").getValue();
                            Location startPoint=new Location("locationA");
                            startPoint.setLatitude(volonterLat);
                            startPoint.setLongitude(volonterLon);

                            Location endPoint=new Location("locationA");
                            endPoint.setLatitude(korisnikLat);
                            endPoint.setLongitude(korisnikLon);

                            rastojanie = String.valueOf(startPoint.distanceTo(endPoint));*/

                            celoIme = postSnapshot.child("Ime").getValue().toString();
                            datum = postSnapshot.child("Date").getValue().toString();
                            item = postSnapshot.child("Item").getValue().toString();
                            adresa=postSnapshot.child("Address").getValue().toString();
                            //rejting = postSnapshot.child("UserRatingAdmin").getValue().toString();
                        }
                        intent.putExtra("Item",item);
                        intent.putExtra("Ime",celoIme);
                        intent.putExtra("Date",datum);
                        intent.putExtra("Address",adresa);
                       // intent.putExtra("Distance",rastojanie);
                        //intent.putExtra("AdminRate",rejting);
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