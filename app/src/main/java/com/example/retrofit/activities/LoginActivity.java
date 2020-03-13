package com.example.retrofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.retrofit.R;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.models.LoginResponse;
import com.example.retrofit.storage.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.textViewRegister)
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textViewRegister, R.id.buttonLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewRegister:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Invalid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(!loginResponse.isError()){
                    //Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG ).show();
                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveUser(loginResponse.getUser());

                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }else {
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }
}
