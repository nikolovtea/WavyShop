package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminDescription extends AppCompatActivity {

    private EditText opisZaAdmin;
    private RatingBar rejtingZaAdmin;
    private Button potvrdi;
    private float sumaRejting,rejting;
    private Integer brojRejting,brPoracki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_description);
        opisZaAdmin = findViewById(R.id.opisZaVolonterText);
        rejtingZaAdmin = findViewById(R.id.rejtingZaVolonterText);
        potvrdi = findViewById(R.id.ocenkaZaVolonter);

        final MediaPlayer mp=MediaPlayer.create(this,R.raw.sample);

        Intent intent = getIntent();
        String item = intent.getStringExtra("Item");
        String emailAdmin = intent.getStringExtra("EmailAdmin");
        HashMap<String, Object> mapUser = new HashMap<>();
        HashMap<String, Object> mapNaracka = new HashMap<>();

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("DescriptionForClothes", opisZaAdmin.getText().toString());
                           mapNaracka.put("ItemRate", rejtingZaAdmin.getRating());
                            FirebaseDatabase.getInstance().getReference("Orders").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailAdmin).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snap: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("Users").child(snap.getKey()).updateChildren(mapUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mp.start();
                startActivity(new Intent(AdminDescription.this,ViewOrders.class));
            }
        });


    }
}