package org.mycode.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mycode.finalproject.Model.Products;
import org.mycode.finalproject.Prevalent.Prevalent;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton showCardBtn;
    private Button addToCartBtn;
    private ImageView productImage ;
    private ElegantNumberButton numberButton ;
    private TextView productPrice, productDescription, productName, countInStock ;
    private int countInStockNumber ;
    ArrayList<Products> userCartTemp = new ArrayList<>();
    Products oneProductData = new Products() ;




    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("_id");

        showCardBtn = findViewById(R.id.add_product_to_cart);
        addToCartBtn  = findViewById(R.id.pd_add_to_cart_btn);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_detail);
        productPrice = findViewById(R.id.product_price_detail);
        productDescription = findViewById(R.id.product_description_detail);
        productName = findViewById(R.id.product_name_detail);
        countInStock = findViewById(R.id.count_in_stock);


        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                if (countInStockNumber < Integer.parseInt(numberButton.getNumber())) {
                    Toast.makeText(ProductDetailsActivity.this, "Can not order more than " + countInStockNumber, Toast.LENGTH_SHORT).show();
                } else {
                    Products newProductToAdd = null;
                    try {
                        newProductToAdd = oneProductData.clone();
                        newProductToAdd.setCountInStock(Integer.parseInt(numberButton.getNumber()));
                        Log.i("order", String.valueOf(newProductToAdd.getCountInStock()));
                        Log.i("instock", String.valueOf(oneProductData.getCountInStock()));
                        //Paper.book().write(Prevalent)
                        userCartTemp.add(newProductToAdd);
                        //Paper.book().write(Prevalent.currentUserCart , userCartTemp);
                        ArrayList<Products> userCartTest = new ArrayList<>();
                        userCartTest =  Paper.book().read(Prevalent.currentUserCart);
                        userCartTest.add(newProductToAdd);
                        Paper.book().write(Prevalent.currentUserCart , userCartTest);

                        Log.i("usercartTest: ", String.valueOf(userCartTest.size()));
                        Log.i("usercartTemp: ", String.valueOf(userCartTemp.size()));
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

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

                    try {
                        JSONObject resProductJSON = new JSONObject(response);
                        oneProductData.setName(resProductJSON.getString("name"));
                        oneProductData.setBrand(resProductJSON.getString("brand"));
                        oneProductData.setCategory(resProductJSON.getString("category"));
                        oneProductData.setPrice(resProductJSON.getString("price"));
                        oneProductData.setImage(resProductJSON.getString("image"));
                        oneProductData.setId(resProductJSON.getString("_id"));
                        oneProductData.setDescription(resProductJSON.getString("description"));
                        oneProductData.setCountInStock(resProductJSON.getInt("countInStock"));
                        countInStockNumber = resProductJSON.getInt("countInStock");

                        Log.i("Tag", "Success");

                        if (oneProductData != null) {
                            productName.setText(oneProductData.getName());
                            productDescription.setText(oneProductData.getDescription());
                            productPrice.setText(oneProductData.getPrice());
                            countInStock.setText(("Count in stock: " + oneProductData.getCountInStock()).toString());
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