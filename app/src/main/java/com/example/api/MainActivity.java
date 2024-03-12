package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        movieList = new ArrayList<>();

        String dynamicUrl = generateDynamicUrl();

             fetchMovies(dynamicUrl);
    }

    private String generateDynamicUrl() {
        return "https://run.mocky.io/v3/9cf18514-4bed-4247-ae47-ab43e1a52d40";
    }

    private void fetchMovies(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String overview = jsonObject.getString("overview");
                                String poster = jsonObject.getString("poster");
                                Double rating = jsonObject.getDouble("rating");

                                Movie movie = new Movie(title, poster, overview, rating);
                                movieList.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        MovieAdapter adapter = new MovieAdapter(MainActivity.this, movieList);
                        recyclerView.setAdapter(adapter);
                    }
                }, error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }
}
