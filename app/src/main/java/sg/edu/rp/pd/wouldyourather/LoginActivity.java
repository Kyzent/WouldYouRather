package sg.edu.rp.pd.wouldyourather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    FirebaseAuth fbAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvLogin);
        fbAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                tvRegister.setEnabled(false);
                btnLogin.setEnabled(false);
                etEmail.setEnabled(false);
                etPassword.setEnabled(false);

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is required");
                    etEmail.setEnabled(true);
                    etPassword.setEnabled(true);
                    btnLogin.setEnabled(true);
                    tvRegister.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Password is required");
                    etEmail.setEnabled(true);
                    etPassword.setEnabled(true);
                    btnLogin.setEnabled(true);
                    tvRegister.setEnabled(true);
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Password must contain more than 5 characters");
                    etEmail.setEnabled(true);
                    etPassword.setEnabled(true);
                    btnLogin.setEnabled(true);
                    tvRegister.setEnabled(true);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            etEmail.setEnabled(true);
                            etPassword.setEnabled(true);
                            tvRegister.setEnabled(true);
                            btnLogin.setEnabled(true);
                        }
                    }
                });
            }
        });
    }
}
