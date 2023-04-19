package com.example.timemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editEmail, editPassword, editConfirm;
    Button button;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = findViewById(R.id.editTextRegUsernameLogin);
        editPassword = findViewById(R.id.editTextRegPasswordLogin);
        editPassword = findViewById(R.id.editTextRegEmail);
        editConfirm = findViewById(R.id.editTextRegPasswordLogin2);
        button = findViewById(R.id.registerButton);
        tv = findViewById(R.id.textViewExistUser);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUsername.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String confirm = editConfirm.getText().toString();
                Database db = new Database(getApplicationContext(),"timeManageApp",null,1);

                if(username.length()==0 || email.length()==0 || password.length()==0 || confirm.length()==0){
                    Toast.makeText(getApplicationContext(),"Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.compareTo(confirm)==0){
                        if(isValid(password)){
                            db.register(username,email,password);
                            Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Password must at least 8 characters, having letters, digit and special symbol", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Password and Confirm Password do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean isValid(String passwordHere){
        int f1=0,f2=0,f3=0;
        if (passwordHere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordHere.length(); p++){
                if(Character.isDigit(passwordHere.charAt(p))){
                    f1=1;
                }
            }
            for (int r = 0; r < passwordHere.length(); r++){
                if(Character.isDigit(passwordHere.charAt(r))){
                    f1=1;
                }
            }
            for (int s = 0; s < passwordHere.length(); s++){
                char c = passwordHere.charAt(s);
                if(c>=33&&c<=46||c==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1)
                return true;
            return false;

        }
    }
}