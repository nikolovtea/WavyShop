package com.example.wavyshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class AppUser extends AppCompatActivity {

/*private ImageView pic1,pic2,pic3,pic4,pic5,pic6,pic7,pic8;
private Button listaN;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_user);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemData[] myItemData = new ItemData[]{
                new ItemData("Silky Dress","White,Brown,Black",R.drawable.pic1),
                new ItemData("Baggy Pants","Pink,White,Beige",R.drawable.pic2),
                new ItemData("Silky shirt","White",R.drawable.pic3),
                new ItemData("Long Dres","Brown,Purple",R.drawable.pic4),
                new ItemData("Casual set","White,Green",R.drawable.pic5),
                new ItemData("Shirt","Brown,Black",R.drawable.pic6),
                new ItemData("Jeans","Brown",R.drawable.pic7),
                new ItemData("Summer Dress","White,Pink,Yellow",R.drawable.pic8),
        };

       ItemAdapter itemAdapter=new ItemAdapter(myItemData,AppUser.this);
       recyclerView.setAdapter(itemAdapter);

       /* listaN=findViewById(R.id.listaN);

        pic1=findViewById(R.id.pic1);
        pic2=findViewById(R.id.pic2);
        pic3=findViewById(R.id.pic3);
        pic4=findViewById(R.id.pic4);
        pic5=findViewById(R.id.pic5);
        pic6=findViewById(R.id.pic6);
        pic7=findViewById(R.id.pic7);
        pic8=findViewById(R.id.pic8);


        pic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order.class));
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order2.class));
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order3.class));
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order4.class));
            }
        });
        pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order5.class));
            }
        });
        pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order6.class));
            }
        });
        pic7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order7.class));
            }
        });
        pic8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,Order8.class));
            }
        });

        listaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUser.this,ViewOrders.class));
            }
        });*/


    }
}