package com.ioana.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    Toast.makeText(MainActivity.this, "Success!" , Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private boolean validate(){
        boolean valid = true;
        EditText email = findViewById(R.id.inputEmail);
        EditText phone =findViewById(R.id.inputPhone);
        if(email.getText().toString().equals("")){
            email.setError("Fill the input with a valid email value!");
            valid=false;
        }
        if(phone.getText().toString().equals("")){
            phone.setError("Fill the phone input with a valid value!");
            valid=false;
        }
        return valid;
    }

}