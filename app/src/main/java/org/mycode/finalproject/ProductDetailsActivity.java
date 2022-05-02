package org.mycode.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mycode.finalproject.Model.Products;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCartBtn;
    private ImageView productImage ;
    private ElegantNumberButton numberButton ;
    private TextView productPrice, productDescription, productName ;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("_id");

        addToCartBtn = findViewById(R.id.add_product_to_cart);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_detail);
        productPrice = findViewById(R.id.product_price_detail);
        productDescription = findViewById(R.id.product_description_detail);
        productName = findViewById(R.id.product_name_detail);
        
        getProductDetails(productID);

    }

    private void getProductDetails(String productID) {


            RequestQueue requestQueue;


            requestQueue = Volley.newRequestQueue(this);
            String URL = "https://fs9app.herokuapp.com/api/products/" + productID;
            JSONObject jsonBody = new JSONObject();

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    Products oneProductData = new Products() ;
                    try {
                        JSONObject resProductJSON = new JSONObject(response);
                        oneProductData.setName(resProductJSON.getString("name"));
                        oneProductData.setBrand(resProductJSON.getString("brand"));
                        oneProductData.setCategory(resProductJSON.getString("category"));
                        oneProductData.setPrice(resProductJSON.getString("price"));
                        oneProductData.setImage(resProductJSON.getString("image"));
                        oneProductData.setId(resProductJSON.getString("_id"));
                        oneProductData.setDescription(resProductJSON.getString("description"));
                        Log.i("Tag", "Success");

                        if (oneProductData != null) {
                            productName.setText(oneProductData.getName());
                            productDescription.setText(oneProductData.getDescription());
                            productPrice.setText(oneProductData.getPrice());
                            Picasso.get().load( "https://fs9app.herokuapp.com"+ oneProductData.getImage()).into(productImage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("Tag", "Fail");
                    }

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


    }
}