package vlu.android.numberseven.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vlu.android.numberseven.controllers.UserHelper;
import vlu.android.numberseven.R;

public class RegisterActivity extends AppCompatActivity {

    EditText fullname, email, username, password, confirmPassword;
    Button register;
    TextView message;
    UserHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);
        message = findViewById(R.id.message);
        db = new UserHelper(this);

        register.setOnClickListener(v -> {
            String fullNameStr = fullname.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confPass = confirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(fullNameStr) || TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confPass)) {
                message.setText("All fields are required");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                message.setText("Invalid email format");
                return;
            }

            if (!pass.equals(confPass)) {
                message.setText("Passwords do not match");
                return;
            }

            if (!isPasswordStrong(pass)) {
                message.setText("Password is too weak");
                return;
            }

            if (db.checkUsername(user)) {
                message.setText("Username already exists");
                return;
            }

            String hashedPassword = hashPassword(pass);
            if (hashedPassword == null) {
                message.setText("Error hashing password");
                return;
            }

            if (db.insertData(fullNameStr, emailStr, user, hashedPassword)) {
                message.setText("Registration successful");
            } else {
                message.setText("Registration failed");
            }
        });
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*].*");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
