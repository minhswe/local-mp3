package vlu.android.numberseven.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import vlu.android.numberseven.controllers.UserHelper;
import vlu.android.numberseven.R;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private CheckBox chkRemember;
    private Button btnLogin;
    private TextView txtMessage;
    private UserHelper db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtTenDangNhap);
        edtPassword = findViewById(R.id.edtMatKhau);
        chkRemember = findViewById(R.id.chkLuuThongTin);
        btnLogin = findViewById(R.id.button);
        txtMessage = findViewById(R.id.txtThongBao);

        db = new UserHelper(this);

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Check and populate saved information
        if (sharedPreferences.getBoolean("saveLogin", false)) {
            edtUsername.setText(sharedPreferences.getString("username", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            chkRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                txtMessage.setVisibility(View.GONE);

                if (username.isEmpty() || password.isEmpty()) {
                    txtMessage.setText("Please enter both username and password");
                    txtMessage.setVisibility(View.VISIBLE);
                } else {
                    if (db.checkLogin(username, password)) {
                        txtMessage.setText("Login successful");
                        txtMessage.setVisibility(View.VISIBLE);

                        // Save login information if checkbox is checked
                        if (chkRemember.isChecked()) {
                            editor.putBoolean("saveLogin", true);
                            editor.putString("username", username);
                            editor.putString("password", password);
                        } else {
                            editor.putBoolean("saveLogin", false);
                            editor.putString("username", "");
                            editor.putString("password", "");
                        }
                        editor.apply();

                        // Navigate to another Activity after successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                        finish();


                    } else {
                        txtMessage.setText("Incorrect username or password");
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
