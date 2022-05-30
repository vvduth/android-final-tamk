package org.mycode.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.mycode.finalproject.Model.Products;
import org.mycode.finalproject.Prevalent.Prevalent;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private RecyclerView.LayoutManager layoutManager ;
    private Button NextProcessBtn ;
    private TextView txtTotalAmount ;
    RecyclerView.Adapter crtAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = findViewById(R.id.next_process);
        txtTotalAmount = findViewById(R.id.total_price);

        ArrayList<Products> userCart = new ArrayList<>();
        userCart =  Paper.book().read(Prevalent.currentUserCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        crtAdapter = new cartAdapter(userCart);
        recyclerView.setAdapter(crtAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}