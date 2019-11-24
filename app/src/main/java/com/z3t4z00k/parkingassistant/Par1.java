package com.z3t4z00k.parkingassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Par1 extends AppCompatActivity {

    final private String URL_PARK = "https://fileserver2.in/android/parking/par1stat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog pdLoading = new ProgressDialog(Par1.this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        StringRequest request = new StringRequest(Request.Method.GET, URL_PARK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Par1.this, "Welcome", Toast.LENGTH_LONG).show();
                pdLoading.dismiss();
                Log.d("CODE", response);
                char[] cur = response.toCharArray();
                if(cur[0] == '1')
                    startActivity( new Intent(Par1.this, par1Floor1.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdLoading.dismiss();
                Toast.makeText(Par1.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
