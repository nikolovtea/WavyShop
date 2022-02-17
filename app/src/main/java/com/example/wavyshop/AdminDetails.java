package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminDetails extends AppCompatActivity {
    private TextView imeKorisnikDesno, datumDesno, itemDesno, adresaDesno, rejtingDesno;
    private Button prifatiAdmin;
    private String imeAdmin, rejtingAdmin, telefonAdmin, emailAdmin;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details);


        prifatiAdmin= findViewById(R.id.prifatiVolonter);
        imeKorisnikDesno = findViewById(R.id.imeKorisnikDesno);
        datumDesno = findViewById(R.id.datumVolonterDesno);
        itemDesno = findViewById(R.id.tipUslugaVolonterDesno);
        adresaDesno = findViewById(R.id.rastojanieDesno);


        Intent intent = getIntent();
        String celoIme = intent.getStringExtra("Ime");
        String datum = intent.getStringExtra("Date");
        String item = intent.getStringExtra("Item");
        String adresa = intent.getStringExtra("Address");


        imeKorisnikDesno.setText(celoIme);
        datumDesno.setText(datum);
        itemDesno.setText(item);
        adresaDesno.setText(adresa);

        HashMap<String, Object> map = new HashMap<>();

        prifatiAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prifatiAdmin.setVisibility(View.GONE);
                emailAdmin = auth.getCurrentUser().getEmail().toString();
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailAdmin).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datas: snapshot.getChildren()){
                            imeAdmin = datas.child("FullName").getValue().toString();
                            telefonAdmin = datas.child("Phone").getValue().toString();
                        }
                        map.put("EmailAdmin",emailAdmin);
                        map.put("ImeAdmin",imeAdmin);
                        map.put("AdminPhone",telefonAdmin);
                        map.put("Status","Admin accepted");
                        FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(postSnapshot.getKey()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}