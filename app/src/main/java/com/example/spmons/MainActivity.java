package com.example.spmons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText mEmail, mPass;



        Button SignInButton = findViewById(R.id.loginb);
        SignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");


                Intent activity2Intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(activity2Intent);
            }
        });
    }
}
