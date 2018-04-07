package com.example.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private String pokemonName;
    private String favorite;
    private List<String> pokemonNames = new ArrayList<String>();
    private Button btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btnFavorite = findViewById(R.id.addToFavoriteButton);

        Intent intent = getIntent();
        pokemonName = intent.getStringExtra("name");

        mQueue = Volley.newRequestQueue(this);
        jsonParse();

        ReadFavorites();
    }

    private void jsonParse() {
        String url ="https://pokeapi.co/api/v2/pokemon/" + pokemonName + "/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TextView textName = findViewById(R.id.textViewName);
                            textName.setText(response.getString("name"));
                            TextView textWeight = findViewById(R.id.textViewWeight);
                            textWeight.setText(response.getString("weight"));
                            TextView textHeight = findViewById(R.id.textViewHeight);
                            textHeight.setText(response.getString("height"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


    public void AddToFavorites(View view) {
        String file_name = "favorite_pokemon";
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name,MODE_APPEND);
            fileOutputStream.write(pokemonName.getBytes());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Pokemon " + pokemonName + " added to favorites!", Toast.LENGTH_LONG).show();
            btnFavorite.setVisibility(view.INVISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadFavorites() {
        try {
            FileInputStream fileInputStream = openFileInput("favorite_pokemon");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((favorite=bufferedReader.readLine()) != null) {
                pokemonNames.add(favorite);
            }
            if (pokemonNames.contains(pokemonName)) {
                View view = getLayoutInflater().inflate(R.layout.activity_details, null);
                btnFavorite.setVisibility(view.INVISIBLE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
