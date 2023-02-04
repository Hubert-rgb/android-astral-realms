package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.entity.Galaxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataInput;
import java.io.IOException;

import kotlin.jvm.internal.TypeReference;

public class MenuActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    TextView nameTextView;
    Button signOutButton;

    Button newGalaxyButton;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        /** login and sign out */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signOutButton = findViewById(R.id.signOutButton);
        nameTextView = findViewById(R.id.nameTextView);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            String personName = account.getDisplayName();

            nameTextView.setText(personName);
        } else {
            Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT);
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        /** display existing galaxies */

        getGalaxiesRequest();

        /** creating new Galaxy */
        newGalaxyButton = findViewById(R.id.newGalaxyButton);

        newGalaxyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGalaxy();
            }
        });
    }
    void signOut(){
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
    }
    void createGalaxy(){
        LinearLayout layout = findViewById(R.id.galaxyTable);
        View galaxyTableElement = getLayoutInflater().inflate(R.layout.galaxy_table_element, null);

        TextView galaxyNameView = findViewById(R.id.galaxyNameView);
        TextView playerNumberView = findViewById(R.id.playerNumberView);

        layout.addView(galaxyTableElement);
    }
    public void getGalaxiesRequest(){
        RequestQueue volleyQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.8.105:8080/galaxy-controller/galaxies/";



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                //response OK
                response -> {
                    //response is a json array
                    //then I manage this json
//                    responseJSON = response;

//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = response.getJSONObject(0);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String jsonString;
//                    try {
//                        jsonString = objectMapper.writeValueAsString(jsonObject);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        Galaxy galaxy = objectMapper.readValue(jsonString, Galaxy.class);
//                        Toast.makeText(getApplicationContext(), "jest ale nie wiem co", Toast.LENGTH_LONG).show();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//
//                    try {
//                        jsonObject = (JSONObject) response.get(0);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    int galaxyId;
//                    String galaxyName;
//                    try {
//                        galaxyId = jsonObject.getInt("id");
//                        galaxyName = jsonObject.getString("galaxyName");
//
//                        System.out.println(galaxyName);
//                        Toast.makeText(getApplicationContext(), galaxyName, Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
                },
                //response error
                error -> Toast.makeText(getApplicationContext(), "error =" + error.networkResponse, Toast.LENGTH_LONG).show());


        //View galaxyList = getLayoutInflater().inflate(R.layout.table_layout, null);

        //layoutList.addView(galaxyList);
        volleyQueue.add(jsonArrayRequest);
    }
}