package com.example.jagoit.ui.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jagoit.databinding.ActivityRegisterRoleBinding;
import com.example.jagoit.databinding.ActivityVerifyOtpBinding;
import com.example.jagoit.util.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterRole extends AppCompatActivity {

    private ActivityRegisterRoleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterRoleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.imgConsultant.setOnClickListener(v -> {
            Constants.REGISTER_ROLE_CHOOSE = "2"; // dia role consultant
            startActivity(new Intent(getApplicationContext(), RegisterConsultantActivity.class));
        });
        binding.imgConsultee.setOnClickListener(v -> {
            Constants.REGISTER_ROLE_CHOOSE = "1"; // dia role consultee
            startActivity(new Intent(getApplicationContext(), RegisterConsulteeActivity.class));
        });

    }


}
