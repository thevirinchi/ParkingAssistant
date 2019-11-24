package com.z3t4z00k.parkingassistant;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.NavUtils;

public class par1Floor1 extends AppCompatActivity {
    private final String URL_PARK = "https://fileserver2.in/android/parking/par11.php";
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_par1_floor1);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        final RelativeLayout[] slots = new RelativeLayout[24];
        slots[0] = findViewById(R.id.p101);
        slots[1] = findViewById(R.id.p102);
        slots[2] = findViewById(R.id.p103);
        slots[3] = findViewById(R.id.p104);
        slots[4] = findViewById(R.id.p105);
        slots[5] = findViewById(R.id.p106);
        slots[6] = findViewById(R.id.p107);
        slots[7] = findViewById(R.id.p108);
        slots[8] = findViewById(R.id.p109);
        slots[9] = findViewById(R.id.p110);
        slots[10] = findViewById(R.id.p111);
        slots[11] = findViewById(R.id.p112);
        slots[12] = findViewById(R.id.p113);
        slots[13] = findViewById(R.id.p114);
        slots[14] = findViewById(R.id.p115);
        slots[15] = findViewById(R.id.p116);
        slots[16] = findViewById(R.id.p117);
        slots[17] = findViewById(R.id.p118);
        slots[18] = findViewById(R.id.p119);
        slots[19] = findViewById(R.id.p120);
        slots[20] = findViewById(R.id.p121);
        slots[21] = findViewById(R.id.p122);
        slots[22] = findViewById(R.id.p123);
        slots[23] = findViewById(R.id.p124);

        final ProgressDialog pdLoading = new ProgressDialog(par1Floor1.this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        StringRequest request = new StringRequest(Request.Method.GET, URL_PARK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdLoading.dismiss();
                Log.d("CODE", response);
                String s1 = response.substring(response.indexOf(",")+1);
                String id = "p" + s1.trim();
                int resId = getResources().getIdentifier(id, "id", getPackageName());
                RelativeLayout selected = findViewById(resId);
                char[] cur = response.toCharArray();
                for (int i = 0; i < 24; i++){
                    if(cur[i] == '1') slots[i].setBackgroundResource(R.drawable.parkocc);
                    else if(cur[i] == '2') slots[i].setBackgroundResource(R.drawable.parkvac);
                }
                selected.setBackgroundResource((R.drawable.parkdif));
                TextView msg = findViewById(R.id.Message);
                s1 = s1.substring(1);
                msg.setText("1st Floor Slot: " + s1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdLoading.dismiss();
                Toast.makeText(par1Floor1.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(par1Floor1.this, data.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
