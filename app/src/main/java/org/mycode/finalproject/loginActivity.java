package org.mycode.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rey.material.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;
import org.mycode.finalproject.Model.User;
import org.mycode.finalproject.Prevalent.Prevalent;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
    private EditText InputEmail , InputPassword ;
    private Button loginButton ;
    private ProgressDialog loadingBar ;

    private String parentDbName = "Users";
    private CheckBox checkBoxRRememberMe ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.main_login_btn);
        InputEmail = findViewById(R.id.login_email_input);
        InputPassword = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this );

        checkBoxRRememberMe = findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }


        });


    }
    private void loginUser() {
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        RequestQueue requestQueue;
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }
        String url = "http://192.168.0.13:5000/api/users/login";
        try {
            requestQueue = Volley.newRequestQueue(this);
            String URL = "https://fs9app.herokuapp.com/api/users/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                    Toast.makeText(loginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    User userData = new User();
                    Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                    try {
                      JSONObject resJson = new JSONObject(response);

                      userData.setName(resJson.getString("name"));
                      userData.setEmail(resJson.getString("email"));
                      userData.setAdmin(resJson.getBoolean("isAdmin"));
                      userData.setToken(resJson.getString("token"));
                      userData.setId(resJson.getString("_id"));
                      Log.i("user", "SUCCESS");
                    } catch (JSONException e) {
                        Log.e("error", "cant parse");
                        e.printStackTrace();
                    }
                    Prevalent.currentOnlineUser = userData;
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    loadingBar.dismiss();
                    Toast.makeText(loginActivity.this, "Error: Wrong Info", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = new String(response.data, StandardCharsets.UTF_8);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
            if ( checkBoxRRememberMe.isChecked()) {
                Paper.book().write(Prevalent.UserEmailKey, email);
                Paper.book().write(Prevalent.UserPasswordKey, password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}