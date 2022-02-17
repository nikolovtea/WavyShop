package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Order extends AppCompatActivity {
    private EditText item;
    private Button logout,back,lokacija,save,listN;
    private EditText boja;
    private Button dateButton;
    private Button timeButton;
    private int hour, minute;
    private Spinner spinner1,spinner2;
    private DatePickerDialog datePickerDialog;
    private Dialog dialog;

    private TextView adresa;
    String pom;

    int PLACE_PICKER_REQUEST=1;

    private FirebaseAuth auth;


    private String fullName,telefon,datumVreme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);



        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay = dialog.findViewById(R.id.btn_okay);


        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Order.this, "Okay", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });




        auth=FirebaseAuth.getInstance();

        item=findViewById(R.id.item1);
        logout=findViewById(R.id.logout);
        back=findViewById(R.id.back);
        lokacija=findViewById(R.id.lokacija);
        save=findViewById(R.id.save);
        listN=findViewById(R.id.listN);
        boja=findViewById(R.id.boja);
        timeButton = findViewById(R.id.time);
        spinner1=findViewById(R.id.velicina);
        spinner2=findViewById(R.id.kolicina);
        adresa=findViewById(R.id.adresa);


        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());


       Intent intent = getIntent();
        pom=intent.getStringExtra("Adresa");
        adresa.setText(pom);



        HashMap<String,Object> map=new HashMap<>();
        String emailKorisnik=auth.getCurrentUser().getEmail().toString();
        map.put("EmailUser",emailKorisnik);


        if (spinner1.getSelectedItem().equals("S")){
            map.put("Size","S");
        }
        else if(spinner1.getSelectedItem().equals("M")){
            map.put("Size","M");
        }
        else{
            map.put("Size","L");
        }

        if(spinner2.getSelectedItem().equals("1")){
            map.put("Quantity","1");
        }
        else if(spinner2.getSelectedItem().equals("2")){
            map.put("Quantity","2");
        }
        else{
            map.put("Quantity","3");
        }

       lokacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order.this,LocationActivity.class));
            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailKorisnik).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            fullName = datas.child("FullName").getValue().toString();
                            telefon = datas.child("Phone").getValue().toString();
                           // rejting = datas.child("Rating").getValue().toString();
                        }
                        map.put("Address",adresa.getText().toString());
                        map.put("Item",item.getText().toString());
                        map.put("Color",boja.getText().toString());

                        datumVreme=dateButton.getText()+" "+timeButton.getText();
                        map.put("Date",datumVreme);
                        map.put("Ime",fullName);
                        map.put("EmailAdmin","");
                        map.put("ImeAdmin","");
                        map.put("Status","Active");
                        map.put("UserPhone",telefon);
                        map.put("AdminPhone","");
                        map.put("DescriptionForClothes","");
                        //map.put("UserRatingAdmin",rejting);
                       // map.put("UserRate",0);
                       // map.put("AdminRatingUser",0);
                       map.put("ItemRate",0);
                        FirebaseDatabase.getInstance().getReference().child("Orders").push().updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.show();

            }
        });
        listN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order.this,ViewOrders.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order.this,MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order.this,AppUser.class));
            }
        });

    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(PovozrasnoLice.this, "This app requsts permission to be granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void updateGPS() {
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(PovozrasnoLice.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            });
        }
        else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
            }
        }
    }*/


}