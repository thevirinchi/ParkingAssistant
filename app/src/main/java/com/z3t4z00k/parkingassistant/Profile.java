package com.z3t4z00k.parkingassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    public static final String URL_DETS = "https://fileserver2.in/android/parking/dets.php";
    EditText fname, lname, cnum;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fname = findViewById(R.id.fnm);
        lname = findViewById(R.id.lnm);
        cnum = findViewById(R.id.cnm);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Profile.this, data.class);
                startActivity(intent);
            }
        });
    }

    public void register(View view){
        final String fnm = fname.getText().toString();
        final String lnm = lname.getText().toString();
        final String cnm = cnum.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String usn = (preferences.getString("usn", "NULL"));

        if(usn.isEmpty()){
            Toast.makeText(Profile.this, "Username Empty", Toast.LENGTH_LONG).show();
        }
        else if(fnm.isEmpty()){
            Toast.makeText(Profile.this, "Please enter your name", Toast.LENGTH_LONG).show();
        }
        else if(lnm.isEmpty()){
            Toast.makeText(Profile.this, "Please enter your last name", Toast.LENGTH_LONG).show();
        }
        else if(cnm.isEmpty()){
            Toast.makeText(Profile.this, "Please enter your car number", Toast.LENGTH_LONG).show();
        }
        else {
            class Login extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(Profile.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    //this method will be running on UI thread
                    pdLoading.setMessage("\tLoading...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("usn", usn);
                    params.put("fnm", fnm);
                    params.put("lnm", lnm);
                    params.put("cnm", cnm);

                    //returning the response
                    return requestHandler.sendPostRequest(URL_DETS, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            try {
                                editor.putString("fnm", obj.getString("fnm"));
                                editor.putString("lnm", obj.getString("lnm"));
                                editor.putString("cnm", obj.getString("cnm"));
                                editor.putBoolean("dets", true);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                Log.d("OK", "exception: " + e);
                                Toast.makeText(Profile.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                                editor.putBoolean("dets", false);
                            }
                            editor.commit();
                            finish();
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            Intent enter = new Intent(Profile.this, data.class);
                            startActivity(enter);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Profile.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }

}
