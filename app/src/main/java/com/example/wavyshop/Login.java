package com.example.wavyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    public EditText email,password;
    public TextView register;
    public Button login;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.emaillogin);
        password=findViewById(R.id.passwordlogin);
        register=findViewById(R.id.back);
        login=findViewById(R.id.login);

        Intent intent=getIntent();
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user.getDisplayName().equals("AppUser")){
                                startActivity(new Intent(Login.this,AppUser.class));
                            }
                            else if(user.getDisplayName().equals("Admin")){
                                startActivity(new Intent(Login.this,Admin.class));
                            }
                        }
                        else {
                            Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}