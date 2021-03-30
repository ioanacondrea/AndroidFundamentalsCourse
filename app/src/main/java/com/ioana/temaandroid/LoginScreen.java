package com.ioana.temaandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ioana.temaandroid.database.AppDatabase;

public class LoginScreen extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText name;
    private Button loginBtn;
    private Button registerBtn;
    private TextView title_loginScreen;
    private boolean mode = true; // true ~ login
    private boolean loginStatus = false;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mDb = AppDatabase.getInstance(getApplicationContext());

        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        name= findViewById(R.id.name_input);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn= findViewById(R.id.registerBtn);
        title_loginScreen = findViewById(R.id.title_loginScreen);


        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode) {
                    //LOGIN
                    title_loginScreen.setText(R.string.log_in);
                    String usernamePref = sharedPref.getString("USERNAME", "notFound");
                    String passwordPref = sharedPref.getString("PASSWORD", "notFound");
                    String currencyPref = sharedPref.getString("CURRENCY", "notFound");

                    if (username.getText().toString().equals(usernamePref) && password.getText().toString().equals(passwordPref)) {
                        loginStatus = true;
                        if (!name.getText().toString().equals(currencyPref)) {
                            //Schimbare nume in fisierul de preferinte
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("CURRENCY", name.getText().toString());
                        }
                        Toast.makeText(v.getContext(), "Login successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(),  MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), "Login failed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //REGISTER
                title_loginScreen.setText(R.string.register_title);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("USERNAME", username.getText().toString());
                editor.putString("PASSWORD", password.getText().toString());
                editor.putString("CURRENCY", name.getText().toString());
                editor.apply();
                mode = true;
                Toast.makeText(v.getContext(), "Registered!", Toast.LENGTH_LONG).show();
            }
        });

    }
}