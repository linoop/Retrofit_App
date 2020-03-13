package com.example.retrofit.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.retrofit.R;
import com.example.retrofit.activities.LoginActivity;
import com.example.retrofit.activities.MainActivity;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.models.DefaultResponse;
import com.example.retrofit.models.LoginResponse;
import com.example.retrofit.models.User;
import com.example.retrofit.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail, editTextName, editTextSchool;
    private EditText editTextCurrentPassword, editTextNewPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextName = view.findViewById(R.id.editTextName);
        editTextSchool = view.findViewById(R.id.editTextSchool);
        editTextCurrentPassword = view.findViewById(R.id.editTextCurrentPsw);
        editTextNewPassword = view.findViewById(R.id.editTextNewPsw);

        view.findViewById(R.id.buttonSave).setOnClickListener(this);
        view.findViewById(R.id.buttoChangePassword).setOnClickListener(this);
        view.findViewById(R.id.buttoLogout).setOnClickListener(this);
        view.findViewById(R.id.buttoDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonSave:
                updateProfile();
                break;
            case R.id.buttoChangePassword:
                updatePassword();
                break;
            case R.id.buttoDelete:
                deleteUser();
                break;
            case R.id.buttoLogout:
                logout();
                break;
        }

    }

    private void logout(){
        SharedPrefManager.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void updateProfile(){
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String school = editTextSchool.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        if (school.isEmpty()) {
            editTextSchool.setError("School required");
            editTextSchool.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<LoginResponse> call = RetrofitClient.getInstance()
                .getApi()
                .updateUser(
                        user.getId(),
                        email,
                        name,
                        school
                );
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                if(!response.body().isError()){
                    SharedPrefManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    public void updatePassword(){
        String currentpassword = editTextCurrentPassword.getText().toString().trim();
        String newpassword = editTextNewPassword.getText().toString().trim();

        if (currentpassword.isEmpty()) {
            editTextCurrentPassword.setError("Password required");
            editTextCurrentPassword.requestFocus();
            return;
        }

        if (newpassword.isEmpty()) {
            editTextNewPassword.setError("Enter new password");
            editTextNewPassword.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi()
                .updatePassword(currentpassword, newpassword, user.getEmail());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    public void deleteUser(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("This action is irreversible...");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = SharedPrefManager.getInstance(getActivity()).getUser();
                Call<DefaultResponse> call = RetrofitClient.getInstance()
                        .getApi()
                        .deleteUser(user.getId());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if(!response.body().isErr()){
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });



            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog ad = builder.create();
        ad.show();

    }
}
