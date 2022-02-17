package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UserDescription extends AppCompatActivity {
    private EditText opisZaUser;
    private RatingBar userRate;
    private Button oceniUser;
    private TextView emailDesno, telefonDesno;
    private String emailUser, telefonUser, item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_description);
        emailDesno = findViewById(R.id.emailUserDesno);
        telefonDesno = findViewById(R.id.telefonUserDesno);
        //opisZaUser = findViewById(R.id.opisZaUserText);
        //userRate = findViewById(R.id.rejtingZaUserText);
        oceniUser = findViewById(R.id.ocenkaZaUser);

        HashMap<String, Object> mapNaracka = new HashMap<>();
        HashMap<String, Object> mapUser = new HashMap<>();
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("EmailUser");
        telefonUser = intent.getStringExtra("UserPhone");
        item = intent.getStringExtra("Item");

        emailDesno.setText(emailUser);
        telefonDesno.setText(telefonUser);

        oceniUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("Status", "Done");
                            //mapNaracka.put("DescriptionForUser", opisZaUser.getText().toString());
                           // mapNaracka.put("UserRate", userRate.getRating());
                            FirebaseDatabase.getInstance().getReference("Orders").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                           // sumaRejting = Integer.parseInt(snap.child("SumaRejting").getValue().toString());
                            //brojRejting = Integer.parseInt(snap.child("BrojNaRejting").getValue().toString());
                            //brojPoracki=Integer.parseInt(snap.child("NumberOfOrders").getValue().toString());
                        }
                       /* sumaRejting += userRate.getRating();
                        brojRejting += 1;
                        brojPoracki+=1;
                        rejting = sumaRejting/brojRejting;
                        mapUser.put("BrojNaRejting", brojRejting);
                        mapUser.put("SumaRejting", sumaRejting);
                        mapUser.put("Rating", rejting);
                        mapUser.put("NumberOfOrders",brojPoracki);*/
                        for(DataSnapshot snap: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("Users").child(snap.getKey()).updateChildren(mapUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(UserDescription.this,AdminPom.class));
            }
        });
    }
}