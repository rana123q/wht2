package com.example.rana_jabareen.wearablehealthtracker;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView NewUser; //for sign Up
    private EditText Email;    //user email
    private EditText Password; //user password
    private Button LogIn;     //login button
    private TextView LoginMessage;
    public static  Patient patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        NewUser=(TextView)findViewById(R.id.newuser); // sign up new user when click on newUser
        LogIn=(Button)findViewById(R.id.logIn);      // set action listener for logIn Button
        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.password);
        LoginMessage = (TextView) findViewById(R.id.loginstatus);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMessage.setText("");
                UserDetails UserInfo = new UserDetails();
                String UserEmail = Email.getText().toString();
                String UserPassword = Password.getText().toString();
                if (UserEmail.length() == 0)
                    Email.setError("Enter Email");
                else if (UserPassword.length() == 0)
                    Password.setError("Enter Password");
                else if (!isValidEmail(UserEmail) && UserEmail.length() > 0) {
                    Email.setError("Invalid Email");
                } else if (UserEmail.length() > 0 && UserPassword.length() > 0) {
                    if (!isNetworkAvailable())
                        LoginMessage.setText("No,Internet Connection");
                    else {
                        UserInfo.setEmail(Email.getText().toString());
                        UserInfo.setPassword(Password.getText().toString());
                        String URL = "http://ranasaleh-001-site1.atempurl.com/WHT.svc/UserLogin";
                        ReadUserLoginTask task = new ReadUserLoginTask(UserInfo);
                        task.delegate = new AsyncResponse() {

                            @Override
                            public void processFinish(String output) {
                                String s = "Invalid User";


                                if (output.equals(s))
                                    LoginMessage.setText("Invalid User");
                                else if (output.equals("Error"))
                                    LoginMessage.setText("an Error ocurred");
                                else {

                                    try {

                                         patient = new Gson().fromJson(output, Patient.class);
                                        SaveSharedPreference.setPatientID(getApplication(), patient.getPatientID());
                                        Intent HomePage = new Intent(MainActivity.this, HomePage.class);

                                        startActivity(HomePage);

                                    } catch (Exception e) {

                                       Log.e("login",e.getMessage());
                                    }
                                }

                            }
                        };
                        task.execute(URL);
                    }


                }
            }
        });

       /* NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // used to go to sign up activity

            }
        });*/
    }

    //////////////////////////////////////////////////////////////////////////////////
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    ///////////////////////////////////////////////////////////////////////////////////
// validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public String UserLogin(String URL, UserDetails UserInfo) // to check login
    {
        StringBuilder stringbuilder = new StringBuilder(); // to return the result
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        try {
            Gson gson = new GsonBuilder().create();  // parse object to json
            String postMessage = gson.toJson(UserInfo);
            StringEntity params = new StringEntity(postMessage); // add parameter to post request
            httpPost.setEntity(params);
        } catch (UnsupportedEncodingException e) {
            Log.d("Json", e.getLocalizedMessage());

        }

        try {
            HttpResponse response = httpclient.execute(httpPost); // get the response
            StatusLine statusline = response.getStatusLine();
            int StatusCode = statusline.getStatusCode();
            if (StatusCode == 200) // there is result
            {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringbuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("Json", "Failed to download file");

            }


        } catch (Exception E) {
            Log.d("Json", E.getLocalizedMessage());


        }

        return stringbuilder.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private class ReadUserLoginTask extends AsyncTask<String, Void, String> // to read login
    {
        public AsyncResponse delegate = null;
        private UserDetails UserInfo;

        public ReadUserLoginTask(UserDetails user) {
            UserInfo = user;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... URL) {
            return UserLogin(URL[0], UserInfo);
        }

        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }


    }
////////////////////////////////////////////////////////////////////////////////////////////////////////
}