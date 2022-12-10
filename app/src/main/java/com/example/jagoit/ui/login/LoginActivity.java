package com.example.jagoit.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jagoit.R;
import com.example.jagoit.databinding.ActivityLoginBinding;
import com.example.jagoit.util.Constants;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    PhoneAuthProvider.ForceResendingToken token;

    private FirebaseAuth firebaseAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        setListeners();

    }

    public boolean validatePhoneNumber(){
        String phoneNum = binding.inputPhoneNumber.getText().toString();
        int size = phoneNum.length();
        for(int i = 0; i < size; i++){
            if(!(phoneNum.charAt(i) >= '0' && phoneNum.charAt(i) <= '9')){
                return false;
            }
        }
        Constants.CURR_PHONE = "+62" + phoneNum;
        return true;
    }

    public void setListeners(){
        binding.buttonLanjut.setOnClickListener(v -> {
            if(validatePhoneNumber()){
                binding.buttonLanjut.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                requestOTP(Constants.CURR_PHONE);
            }else{
                showToast("Inputan Nomor Handphone masih belum sesuai");
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void requestOTP(String phoneNum){
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("TEST", "Error: " + e);
                showToast("Failed Verify OTP");
                binding.buttonLanjut.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                binding.progressBar.setVisibility(View.INVISIBLE);
                Constants.VERRIFICATION_OTP_ID = s;
                token = forceResendingToken;

                startActivity(new Intent(getApplicationContext(), VerifyOTP.class));
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }




}
