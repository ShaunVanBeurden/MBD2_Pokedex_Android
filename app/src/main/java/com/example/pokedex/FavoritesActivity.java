package com.example.pokedex;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FavoritesActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private ListView listView;
    private String favorite;
    private StringBuffer stringBuffer;
    private int lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ReadFavorites();

        // Find the ListView resource.
        listView = findViewById( R.id.lvFavorites);

        mQueue = Volley.newRequestQueue(this);
        jsonParse();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("name", listView.getItemAtPosition(position).toString());
                //based on item add info to intent
                startActivity(intent);
            }
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void jsonParse() {

        String[] pokemonNames = new String[lines];

        for (int i = 0; i < lines; i++) {
            pokemonNames[i] = stringBuffer.toString();
        }

        listView.setAdapter(
                new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        pokemonNames
                ));
    }

    public void ReadFavorites() {
        try {
            FileInputStream fileInputStream = openFileInput("favorite_pokemon");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();
            while ((favorite=bufferedReader.readLine()) != null) {
                stringBuffer.append(favorite + "\n");
                lines++;
            }
            Log.d("Test", stringBuffer.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pokedex:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorites:
                Intent favoriteIntent = new Intent(getApplicationContext(),FavoritesActivity.class);
                startActivity(favoriteIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
