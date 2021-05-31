package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.time.temporal.ChronoUnit;

import static android.app.PendingIntent.getActivity;

public class Login extends AppCompatActivity {

    EditText idInput, passwordInput;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button loginBtn,joinBtn;
    int loginSuccess=0;
    int joinSuccess=1;
    static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginBtn=(Button)findViewById(R.id.btn_login);
        joinBtn=(Button)findViewById(R.id.btn_register);




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (id.equals("")||password.equals("")){
                    Toast.makeText(Login.this, "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String query = "select * from User";
                    Cursor cursor = MainActivity.db.rawQuery(query, null);
                    while (cursor.moveToNext()) {
                        String gotId = cursor.getString(1);
                        String gotpw = cursor.getString(2);
                        if (gotId.equals(id) && gotpw.equals(password)) {
                            loginSuccess = 1;
                            Toast.makeText(Login.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            user = id;
                            break;
                        }
                    }
                    if (loginSuccess == 0) {
                        Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        user = null;
                    }
                    if (loginSuccess == 1) {
                        Intent intent=new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(Login.this, "환영합니다, " + user + "님!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String id = idInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(id.equals("")||password.equals("")){
                    Toast.makeText(Login.this,"아이디 또는 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                }else {
                    String query = "select * from User";
                    Cursor cursor = MainActivity.db.rawQuery(query, null);
                    while (cursor.moveToNext()) {
                        String gotId = cursor.getString(1);
                        if (gotId.equals(id)) {
                            Toast.makeText(Login.this, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                            joinSuccess = 0;
                            break;
                        }
                    }
                    if (joinSuccess == 1) {
                        String sql = "insert into User (ID,PW) values (?,?)";
                        String[] params = {id, password};
                        MainActivity.db.execSQL(sql, params);
                        Toast.makeText(Login.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


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