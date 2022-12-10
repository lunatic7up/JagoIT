package com.example.jagoit.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.jagoit.databinding.ActivityRegisterConsultantBinding;

public class RegisterConsultantActivity extends AppCompatActivity {

    private ActivityRegisterConsultantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterConsultantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){

    }
}