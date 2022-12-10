package com.example.jagoit.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jagoit.R;
import com.example.jagoit.databinding.ActivityLoginBinding;
import com.example.jagoit.databinding.ActivityVerifyOtpBinding;
import com.example.jagoit.ui.dashboard.DashboardActivity;
import com.example.jagoit.ui.register.RegisterRole;
import com.example.jagoit.util.Constants;
import com.example.jagoit.util.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    FirebaseAuth firebaseAuth;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners(){
        binding.otpButton.setOnClickListener(v -> {
            loading(true);
            String otp = binding.otpEditText.getText().toString();
            Log.d("TEST", "otp: " + otp);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Constants.VERRIFICATION_OTP_ID, otp);
            verifyAuth(credential);
        });

    }



    private void CheckUserInDatabase(String phoneNumber){
        Log.d("TESTING", "CheckUserInDatabase: masuk");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        showToast(database.toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE_NUM, phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        showToast("Task berhasil");
                    }else{
                        showToast("Task tidak berhasil");
                    }

                    if(task.isSuccessful() && task.getResult() != null
                        && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE ));
                        showToast("Phone sudah ada di database");
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    }else{
                        showToast("Phone tidak ada di database");
                        startActivity(new Intent(getApplicationContext(), RegisterRole.class));
                    }

                });


    }

    private void verifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loading(false);
                    showToast("berhasil verify otp");
                    CheckUserInDatabase(Constants.CURR_PHONE);

                }else{
                    showToast("gagal verify otp");
                }
            }
        });
    }

    private void loading(Boolean isloading){
        if(isloading){
            binding.otpButton.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.otpButton.setVisibility(View.VISIBLE);
        }
    }





    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}