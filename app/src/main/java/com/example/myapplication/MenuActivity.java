package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.entity.Galaxy;
import com.example.myapplication.enumTypes.PeriodType;
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

        getGalaxiesRequest();



        /** creating new Galaxy */
        newGalaxyButton = findViewById(R.id.newGalaxyButton);

        newGalaxyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogue();
            }
        });

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

                Button createGalaxyButton = popupView.findViewById(R.id.create_galaxy_button);


                createGalaxyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText galaxyNameEditText = popupView.findViewById(R.id.galaxy_name_edit_text);
                        String galaxyName = galaxyNameEditText.getText().toString();
                        int maximalPlayerNumber = numberSelectorBar.getProgress();

                        Galaxy galaxy = new Galaxy(maximalPlayerNumber, galaxyName, PeriodType.FAST);
                        createGalaxy(galaxy);

                        dialog.dismiss();
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
    void createGalaxy(Galaxy galaxy){

        LinearLayout layout = findViewById(R.id.galaxyTable);

        ViewGroup galaxyTableElement = (ViewGroup) getLayoutInflater().inflate(R.layout.galaxy_table_element, null);

        galaxyTableElement.setId(galaxy.getId());

        TextView galaxyNameView = (TextView) galaxyTableElement.getChildAt(0);
        TextView playerNumberView = (TextView) galaxyTableElement.getChildAt(1);

        galaxyNameView.setText(galaxy.getGalaxyName());
        String playerNumberString = "0 / " + galaxy.getMaximalUserNumber();
        playerNumberView.setText(playerNumberString);

        layout.addView(galaxyTableElement);

        createGalaxyRequest(galaxy);
    }

    //Post method
    public void createGalaxyRequest(Galaxy galaxy) {
        RequestQueue volleyQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.8.105:8080/galaxy-controller/galaxies/";

        JSONObject createGalaxyRequestBody = new JSONObject();
        try {
            createGalaxyRequestBody.put("maximalUserNumber", galaxy.getMaximalUserNumber());
            createGalaxyRequestBody.put("galaxyName", galaxy.getGalaxyName());
            createGalaxyRequestBody.put("periodType", galaxy.getPeriodType());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest createGalaxyRequest = new JsonObjectRequest(
                Request.Method.POST, url, createGalaxyRequestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Create galaxy", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Create galaxy", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        volleyQueue.add(createGalaxyRequest);
    }

    //Get method
    public void getGalaxiesRequest(){
        RequestQueue volleyQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.8.105:8080/galaxy-controller/galaxies/";

        List<Galaxy> galaxyList = new ArrayList<>();

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
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        LinearLayout layout = findViewById(R.id.galaxyTable);
                        View galaxyTableElement = getLayoutInflater().inflate(R.layout.galaxy_table_element, null);

                        layout.addView(galaxyTableElement);

                        TextView galaxyNameView = galaxyTableElement.findViewById(R.id.galaxyNameView);
                        TextView playerNumberView = galaxyTableElement.findViewById(R.id.playerNumberView);

                        galaxyNameView.setText(galaxy.getGalaxyName());
                        String userNumberText = galaxy.getUserNumber() + "/" + galaxy.getMaximalUserNumber();
                        playerNumberView.setText(userNumberText);

                            //Toast.makeText(getApplicationContext(), galaxy.getGalaxyName(), Toast.LENGTH_LONG).show();


                        galaxyList.add(galaxy);
                    }
                },
                //response error
                error -> Toast.makeText(getApplicationContext(), "error =" + error.networkResponse, Toast.LENGTH_LONG).show());
        volleyQueue.add(jsonArrayRequest);

        if(galaxyList.size() == 0){
            Toast.makeText(getApplicationContext(), "nie ma nic", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), galaxyList.size(), Toast.LENGTH_LONG).show();
        }

        //return galaxyList;
    }
}