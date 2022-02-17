package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    private EditText name,lastname,phone,email,password;
    private Button signup;
    private TextView login;
    private RadioGroup radioGrupa;
    private RadioButton radioButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=findViewById(R.id.ime);
        lastname=findViewById(R.id.prezime);
        phone=findViewById(R.id.tel);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        radioGrupa=findViewById(R.id.radioGrupa);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.back);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });

        Intent intent=getIntent();
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getText().toString();
                String txt_lastName = lastname.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Name", txt_name);
                            map.put("LastName", txt_lastName);
                            map.put("Phone", txt_phone);
                            map.put("Email", txt_email);
                            map.put("Password", txt_password);
                           // map.put("NumberOfOrders", 0);
                            //map.put("BrojNaRejting",0);
                           // map.put("Rating",0);
                            //map.put("SumaRejting",0);
                            String fullName = txt_name + " " + txt_lastName;
                            map.put("FullName", fullName);
                            int selectedId = radioGrupa.getCheckedRadioButtonId();
                            radioButton = (RadioButton) findViewById(selectedId);

                            if (radioButton.getId() == R.id.newUser) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("AppUser").build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(Signup.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, AppUser.class));

                            } else if (radioButton.getId() == R.id.admin) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("Admin").build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(Signup.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, Admin.class));
                            } else {
                                Toast.makeText(Signup.this, "Failed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });

            }
        });


    }
}