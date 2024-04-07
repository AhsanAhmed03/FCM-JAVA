package com.mobile.app.development.fcm.java;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    // Permission Granted
                    //Get Device token from firebase
                    getDeviceToken();
                }else{
                    //Permission denied
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
    }

    public void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED){
                //Permission already Granted
            }else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                //You can explain user that why do you need permission bu showing Dialogue box or Toast Message
            }else {
                //Request For Permission
                resultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }else {
            //GEt Device Token from Firebase
            getDeviceToken();
        }
    }

    public void getDeviceToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.e("FireBaseLogs"," Fetching Toke Failed"+ task.getException());
                    return;
                }

                //Get Device Token
                String token = task.getResult();
                Log.v("FireBaseLogs","Device Token: "+token);
            }
        });
    }
}