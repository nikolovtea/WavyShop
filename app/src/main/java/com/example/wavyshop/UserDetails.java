package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserDetails extends AppCompatActivity {
    private Button accept, decline, oceniAdmin;
    private TextView itemDesno, colorDesno, datumDesno, velicinaDesno, kolicinaDesno, statusDesno, adminDesno, telefonDesno, emailAdminDesno;
    private TextView adminLevo, telefonLevo,emailAdminLevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Intent intent = getIntent();
        String item = intent.getStringExtra("Item");
        String datum = intent.getStringExtra("Date");
        String boja = intent.getStringExtra("Color");
        String velicina = intent.getStringExtra("Size");
        String kolicina = intent.getStringExtra("Quantity");
        String status = intent.getStringExtra("Status");
        String admin = intent.getStringExtra("ImeAdmin");
        String telefon = intent.getStringExtra("AdminPhone");
        String emailAdmin = intent.getStringExtra("EmailAdmin");
        String rejtingClothes=intent.getStringExtra("ItemRate");


        accept = findViewById(R.id.prifati);
        decline = findViewById(R.id.otkazi);
        oceniAdmin = findViewById(R.id.oceniAdmin);
        itemDesno = findViewById(R.id.itemDesno);
        colorDesno = findViewById(R.id.colorDesno);
        datumDesno = findViewById(R.id.datumDesno);
        velicinaDesno = findViewById(R.id.velicinaDesno);
        kolicinaDesno = findViewById(R.id.kolicinaDesno);
        statusDesno = findViewById(R.id.statusDesno);
        adminDesno = findViewById(R.id.adminDesno);

        telefonDesno = findViewById(R.id.telefonDesno);
        emailAdminDesno = findViewById(R.id.emailAdminDesno);
        adminLevo = findViewById(R.id.adminLevo);

        telefonLevo = findViewById(R.id.telefonLevo);
        emailAdminLevo = findViewById(R.id.emailAdminLevo);

        itemDesno.setText(item);
        colorDesno.setText(boja);
        datumDesno.setText(datum);
        velicinaDesno.setText(velicina);
        kolicinaDesno.setText(kolicina);
        statusDesno.setText(status);
        adminDesno.setText(admin);

        telefonDesno.setText(telefon);
        emailAdminDesno.setText(emailAdmin);

        if (status.equals("Active")) {
            accept.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
            adminDesno.setVisibility(View.GONE);
            telefonDesno.setVisibility(View.GONE);
            emailAdminDesno.setVisibility(View.GONE);
            adminLevo.setVisibility(View.GONE);
            telefonLevo.setVisibility(View.GONE);
            emailAdminLevo.setVisibility(View.GONE);
            oceniAdmin.setVisibility(View.GONE);
        } else if (status.equals("Admin accepted")) {
            oceniAdmin.setVisibility(View.GONE);
        } else if (status.equals("Taken")) {
            accept.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
            oceniAdmin.setVisibility(View.GONE);
        } else if (status.equals("Done")) {
            if(Integer.parseInt(rejtingClothes)!=0){
                oceniAdmin.setVisibility(View.GONE);
            }

            accept.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
        }

        HashMap<String, Object> map = new HashMap<>();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Taken");
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Taken");
                            FirebaseDatabase.getInstance().getReference("Orders").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Active");
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                adminDesno.setVisibility(View.GONE);

                telefonDesno.setVisibility(View.GONE);
                emailAdminDesno.setVisibility(View.GONE);
                adminLevo.setVisibility(View.GONE);
                telefonLevo.setVisibility(View.GONE);
                emailAdminLevo.setVisibility(View.GONE);
                oceniAdmin.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Active");
                            map.put("EmailAdmin","");
                            map.put("ImeAdmin","");
                            map.put("AdminPhone","");
                            //map.put("AdminRatingUser", 0);
                            FirebaseDatabase.getInstance().getReference("Orders").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        oceniAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetails.this, AdminDescription.class);
                intent.putExtra("Item",item);
                intent.putExtra("EmailAdmin", emailAdmin);
                startActivity(intent);
            }
        });
    }
}