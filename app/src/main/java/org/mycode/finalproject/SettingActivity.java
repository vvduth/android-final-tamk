package org.mycode.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.mycode.finalproject.Model.User;
import org.mycode.finalproject.Prevalent.Prevalent;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SettingActivity extends AppCompatActivity {
    private CircleImageView profileImageView ;
    private EditText nameEditText, userEmailEditText, passwordEditText ;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextBtn;
    private String cheker="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_setting);
        profileImageView = findViewById(R.id.settings_profile_image);
        nameEditText = findViewById(R.id.settings_full_name);
        userEmailEditText =findViewById(R.id.setting_email);
        passwordEditText = findViewById(R.id.settings_changepassword);
        profileChangeTextBtn = findViewById(R.id.profile_image_change_btn);
        closeTextBtn = findViewById(R.id.close_settings_btn);
        saveTextBtn = findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(nameEditText, userEmailEditText, passwordEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSave();
            }
        });

    }

    private void userInfoDisplay(EditText nameEditText, EditText userEmailEditText, EditText passwordEditText) {
        nameEditText.setText(Prevalent.currentOnlineUser.getName());
        userEmailEditText.setText(Prevalent.currentOnlineUser.getEmail());

    }
    private void putUserRequest() {

    }
    private void checkAndSave()
    {
        RequestQueue requestQueue ;
        User userData2 = new User();
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userEmailEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                requestQueue = Volley.newRequestQueue(this);
                String URL = "https://fs9app.herokuapp.com/api/users/profile";
                JSONObject jsonHeaders = new JSONObject();
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", nameEditText.getText().toString());
                jsonBody.put("email", userEmailEditText.getText().toString());
                jsonHeaders.put("authorization", "Bearer " + Prevalent.currentOnlineUser.getToken());
                if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    jsonBody.put("password", passwordEditText.getText().toString());
                }
                final String requestBody = jsonBody.toString();
                final String requestHeaders = jsonHeaders.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                        try {
                            JSONObject resJson = new JSONObject(response);
                            Log.i("res", resJson.toString());
                            Log.i("type", resJson.getString("name").getClass().getSimpleName());
                            userData2.setName(resJson.getString("name"));
                            userData2.setEmail(resJson.getString("email"));
                            userData2.setAdmin(resJson.getBoolean("isAdmin"));
                            userData2.setToken(resJson.getString("token"));
                            userData2.setId(resJson.getString("_id"));
                            Log.i("user", "SUCCESS");
                            Toast.makeText(SettingActivity.this, "Update success.", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error" , "cant parse");
                        }

                        Prevalent.currentOnlineUser = userData2;
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());


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
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("authorization", "Bearer " +  Prevalent.currentOnlineUser.getToken());
                        return params;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}