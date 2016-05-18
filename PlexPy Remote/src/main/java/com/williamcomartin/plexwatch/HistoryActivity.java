package com.williamcomartin.plexwatch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.williamcomartin.plexwatch.Adapters.HistoryAdapter;
import com.williamcomartin.plexwatch.Helpers.GsonRequest;
import com.williamcomartin.plexwatch.Models.HistoryModels;

public class HistoryActivity extends NavBaseActivity {

    private SharedPreferences SP;
    private RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setupActionBar();

        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);

        SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String url = SP.getString("server_settings_address", "") + "/api/v2?apikey=" + SP.getString("server_settings_apikey", "") + "&cmd=get_history";

        GsonRequest<HistoryModels> request = new GsonRequest<>(
                url,
                HistoryModels.class,
                null,
                requestListener(),
                errorListener()
        );

        ApplicationController.getInstance().addToRequestQueue(request);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HistoryActivity", error.toString());
            }
        };
    }

    private Response.Listener<HistoryModels> requestListener() {
        return new Response.Listener<HistoryModels>() {
            @Override
            public void onResponse(HistoryModels response) {
                Log.d("HistoryActivity", response.toString());

                HistoryAdapter adapter = new HistoryAdapter(response.response.data.data);
                rvHistory.setAdapter(adapter);
            }
        };
    }

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.history);
    }
}
