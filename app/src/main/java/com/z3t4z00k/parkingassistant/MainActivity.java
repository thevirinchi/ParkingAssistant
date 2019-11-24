package com.z3t4z00k.parkingassistant;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String URL_LOGIN = "https://fileserver2.in/android/parking/login.php";
    EditText ed_username, ed_password;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String STATUS = "status";
    public static final String USERNAME = "username";
    private boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(MainActivity.this, SignUp.class);
                startActivity(registerActivity);
            }
        });

        ed_username = findViewById(R.id.usn);
        ed_password = findViewById(R.id.psw);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        status = prefs.getBoolean(STATUS, false);

        if(status){
            try {
                Toast.makeText(MainActivity.this, "Hello " + prefs.getString("fnm", "NULL") + "!", Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
                Toast.makeText(MainActivity.this, "Hello " + prefs.getString("usn", "NULL") + "!", Toast.LENGTH_LONG).show();
            }

            finish();
            Intent intent = new Intent(MainActivity.this, data.class);
            startActivity(intent);
        }
    }

    public void login(View view){
        final String username = ed_username.getText().toString();
        final String password = ed_password.getText().toString();

        if(username.isEmpty()|| password.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }

        else {
            class Login extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

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
                    params.put("usn", username);
                    params.put("psw", password);

                    //returing the response
                    return requestHandler.sendPostRequest(URL_LOGIN, params);
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
                            //String username = obj.getString("usn");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("usn", obj.getString("usn"));
                            editor.putBoolean(STATUS, true);
                            editor.apply();

                            finish();
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, data.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_about) {
            Intent about = new Intent(MainActivity.this, About.class);
            startActivity(about);
        }*/

        return super.onOptionsItemSelected(item);
    }
}
