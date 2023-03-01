package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    TextView nameTextView;
    Button signOutButton;

    Button newGalaxyButton;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        /** login and sign out */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signOutButton = findViewById(R.id.signOutButtonMenu);
        nameTextView = findViewById(R.id.nameTextViewMenu);

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

        RequestQueue volleyQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.8.105:8080/galaxy-controller/galaxies/";

        List<Galaxy> galaxyList = new ArrayList<>();

        LinearLayout layout = findViewById(R.id.galaxyTable);
        View galaxyTableElement = getLayoutInflater().inflate(R.layout.galaxy_table_element, null);

        layout.addView(galaxyTableElement);

        TextView galaxyNameView = findViewById(R.id.galaxyNameView);
        TextView playerNumberView = findViewById(R.id.playerNumberView);

        /*JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                //response OK
                (JSONArray response) -> {
                    try {
                        galaxyNameView.setText(response.getJSONObject(0).getString("galaxyName"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < response.length(); i++){
                        ObjectMapper objectMapper = new ObjectMapper();

                        String responseString = null;
                        try {
                            responseString = response.getJSONObject(i).toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Galaxy galaxy;
                        try {
                            galaxy = objectMapper.readValue(responseString, Galaxy.class);

                            galaxyNameView.setText(galaxy.getGalaxyName());
                            String userNumberText = galaxy.getUserNumber() + "/" + galaxy.getMaximalUserNumber();
                            playerNumberView.setText(userNumberText);

                            layout.addView(galaxyTableElement);

                            //Toast.makeText(getApplicationContext(), galaxy.getGalaxyName(), Toast.LENGTH_LONG).show();

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        galaxyList.add(galaxy);
                    }
                },
                //response error
                error -> Toast.makeText(getApplicationContext(), "error =" + error.networkResponse, Toast.LENGTH_LONG).show());
        volleyQueue.add(jsonArrayRequest);*/

        //getGalaxiesRequest();

        /** creating new Galaxy */
        newGalaxyButton = findViewById(R.id.newGalaxyButton);

        newGalaxyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogue();



                createGalaxy("1243 gal", 1, 8, 2);
            }
        });

        createGalaxy("2137 galaxy", 2, 5, 2137);
    }
    void showDialogue(){
        /** how to relate to inflated view??? */
        Dialog dialog = new Dialog(this);
        View createGalaxyPopUp = getLayoutInflater().inflate(R.layout.popup, null);
        createGalaxyPopUp.setTag(1);
        createGalaxyPopUp.setId(R.id.popup);

        dialog.setContentView(createGalaxyPopUp);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                View popupView = dialog.findViewById(R.id.popup);
                TextView playerNumberFromSeekBarView = popupView.findViewById(R.id.playerNumberFromSeekBarView);
                playerNumberFromSeekBarView.setText("123");
                SeekBar numberSelectorBar = popupView.findViewById(R.id.seekBar);

                numberSelectorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        playerNumberFromSeekBarView.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
        dialog.create();
        dialog.show();
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
    void createGalaxy(String galaxyName, int playerNumber, int maximalPlayerNumber, int galaxyId){

        LinearLayout layout = findViewById(R.id.galaxyTable);

        ViewGroup galaxyTableElement = (ViewGroup) getLayoutInflater().inflate(R.layout.galaxy_table_element, null);

        galaxyTableElement.setId(galaxyId);

        TextView galaxyNameView = (TextView) galaxyTableElement.getChildAt(0);
        TextView playerNumberView = (TextView) galaxyTableElement.getChildAt(1);

        galaxyNameView.setText(galaxyName);
        String playerNumberString = playerNumber + "/" + maximalPlayerNumber;
        playerNumberView.setText(playerNumberString);

        layout.addView(galaxyTableElement);
    }
    public void displayGalaxies() {
//        //List<Galaxy> galaxyList = getGalaxiesRequest();
//        //Toast.makeText(getApplicationContext(), galaxyList.get(0).getGalaxyName(), Toast.LENGTH_LONG).show();
//        for (Galaxy galaxy: galaxyList){
//            LinearLayout layout = findViewById(R.id.galaxyTable);
//            View galaxyTableElement = getLayoutInflater().inflate(R.layout.galaxy_table_element, null);
//
//            TextView galaxyNameView = findViewById(R.id.galaxyNameView);
//            TextView playerNumberView = findViewById(R.id.playerNumberView);
//
//            galaxyNameView.setText(galaxy.getGalaxyName());
//            String userNumberText = galaxy.getUserNumber() + "/" + galaxy.getMaximalUserNumber();
//            playerNumberView.setText(userNumberText);
//
//            layout.addView(galaxyTableElement);
//        }
    }
    public void getGalaxiesRequest(){
        RequestQueue volleyQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.8.105:8080/galaxy-controller/galaxies/";

        List<Galaxy> galaxyList = new ArrayList<>();

        TextView galaxyNameView = findViewById(R.id.galaxyNameView);
        TextView playerNumberView = findViewById(R.id.playerNumberView);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                //response OK
                (JSONArray response) -> {
                    for (int i = 0; i < response.length(); i++){
                        ObjectMapper objectMapper = new ObjectMapper();

                        String responseString = null;
                        try {
                            responseString = response.getJSONObject(i).toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Galaxy galaxy;
                        try {
                            galaxy = objectMapper.readValue(responseString, Galaxy.class);

                            LinearLayout layout = findViewById(R.id.galaxyTable);
                            View galaxyTableElement = getLayoutInflater().inflate(R.layout.galaxy_table_element, null);



                            galaxyNameView.setText(galaxy.getGalaxyName());
                            String userNumberText = galaxy.getUserNumber() + "/" + galaxy.getMaximalUserNumber();
                            playerNumberView.setText(userNumberText);

                            layout.addView(galaxyTableElement);

                            //Toast.makeText(getApplicationContext(), galaxy.getGalaxyName(), Toast.LENGTH_LONG).show();

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        galaxyList.add(galaxy);
                    }
                },
                //response error
                error -> Toast.makeText(getApplicationContext(), "error =" + error.networkResponse, Toast.LENGTH_LONG).show());
        volleyQueue.add(jsonArrayRequest);

        /*if(galaxyList.size() == 0){
            Toast.makeText(getApplicationContext(), "nie ma nic", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), galaxyList.size(), Toast.LENGTH_LONG).show();
        }

        return galaxyList;*/
    }
}