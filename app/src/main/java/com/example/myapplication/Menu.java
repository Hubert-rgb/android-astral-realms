//package com.example.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//public class Menu extends AppCompatActivity {
//    GoogleSignInOptions googleSignInOptions;
//    GoogleSignInClient googleSignInClient;
//
//    TextView nameTextView;
//    Button signOutButton;
//
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.menu);
//
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
//
//        signOutButton = findViewById(R.id.signOutButton);
//        nameTextView = findViewById(R.id.nameTextView);
//
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null){
//            String personName = account.getDisplayName();
//        } else {
//            Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT);
//        }
//
//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });
//    }
//    void signOut(){
//        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                finish();
//                startActivity(new Intent(Menu.this, MainActivity.class));
//            }
//        });
//    }
//}
