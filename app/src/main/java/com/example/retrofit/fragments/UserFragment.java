package com.example.retrofit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit.R;
import com.example.retrofit.adapters.UsersAdapter;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.models.User;
import com.example.retrofit.models.UsersResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Call<UsersResponse> call = RetrofitClient.getInstance().getApi().getUsers();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                userList = response.body().getUsers();
                adapter = new UsersAdapter(getActivity(), userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {


            }
        });

    }
}
