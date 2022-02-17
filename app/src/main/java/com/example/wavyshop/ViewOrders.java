package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {
    private ArrayList<String> lines=new ArrayList<>();
    private ListView lista;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    private String item,data,velicina,kolicina,boja,status,admin,rejting,telefon,emailAdmin,rejtingClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        lista=findViewById(R.id.lista);

        String emailKorisnik=auth.getCurrentUser().getEmail().toString();
        FirebaseDatabase.getInstance().getReference("Orders").orderByChild("EmailUser").equalTo(emailKorisnik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    lines.add(postSnapshot.child("Item").getValue().toString());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(ViewOrders.this, android.R.layout.simple_list_item_1,lines);
                lista.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView < ? > parent, View view, int index, long id){
                String tipUsluga = lines.get(index);
                Intent intent = new Intent(ViewOrders.this,UserDetails.class);
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("Item").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            item = postSnapshot.child("Item").getValue().toString();
                            boja = postSnapshot.child("Color").getValue().toString();
                            data = postSnapshot.child("Date").getValue().toString();
                            velicina = postSnapshot.child("Size").getValue().toString();
                            kolicina = postSnapshot.child("Quantity").getValue().toString();
                            status = postSnapshot.child("Status").getValue().toString();
                            admin = postSnapshot.child("ImeAdmin").getValue().toString();
                           // rejting = postSnapshot.child("AdminRatingUser").getValue().toString();
                            telefon = postSnapshot.child("AdminPhone").getValue().toString();
                            emailAdmin = postSnapshot.child("EmailAdmin").getValue().toString();
                         rejtingClothes = postSnapshot.child("ItemRate").getValue().toString();
                        }
                        intent.putExtra("Item",item);
                        intent.putExtra("Color",boja);
                        intent.putExtra("Date",data);
                        intent.putExtra("Size",velicina);
                        intent.putExtra("Quantity",kolicina);
                        intent.putExtra("Status",status);
                        intent.putExtra("ImeAdmin",admin);
                        intent.putExtra("AdminPhone",telefon);
                        intent.putExtra("EmailAdmin",emailAdmin);
                       intent.putExtra("ItemRate",rejtingClothes);
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