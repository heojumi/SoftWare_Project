package com.example.myapplication;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText idInput, passwordInput;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);


        // if autoLogin unChecked
        //String id = idInput.getText().toString();
        //String password = passwordInput.getText().toString();
/*        Boolean validation = loginValidation(id, password);

        if(validation) {
            Toast.makeText(Login.this, "Login Success", Toast.LENGTH_LONG).show();

            // if autoLogin Checked, save values
            editor.putString("id", id);
            editor.putString("pw", password);
            editor.putBoolean("autoLogin", true);
            editor.commit();

            // goto mainActivity

        } else {
            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
            // goto LoginActivity
        }




    }

    private boolean loginValidation(String id, String password) {
        if(pref.getString("id","").equals(id) && pref.getString("pw","").equals(password)) {
            // login success
            return true;
        } else if (pref.getString("id","").equals(null)){
            // sign in first
            Toast.makeText(Login.this, "Please Sign in first", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // login failed
            return false;
        }*/
    }
}