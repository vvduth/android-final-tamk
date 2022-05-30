package org.mycode.finalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.mycode.finalproject.Interface.ItemClickListener;
import org.mycode.finalproject.Model.Products;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {
    @NonNull

    private ArrayList<Products> userCart;

    public cartAdapter(@NonNull ArrayList<Products> userCart) {
        this.userCart = userCart;
    }

    @Override
    public cartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cartView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new cartAdapter.ViewHolder(cartView);
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.ViewHolder holder, int position) {
        String pTemp = userCart.get(position).getPrice();
        float pTempNum = Float.parseFloat(pTemp);
        //Log.i("price", String.valueOf(pTempNum * userCart.get(position).getCountInStock()));
        holder.txtCartProductName.setText(userCart.get(position).getName());
        holder.txtCartProductPrice.setText(String.valueOf(pTempNum * userCart.get(position).getCountInStock()));
        holder.txtCartProductQuantity.setText(String.valueOf(userCart.get(position).getCountInStock()));
    }

    @Override
    public int getItemCount() {
        return userCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCartProductName , txtCartProductPrice, txtCartProductQuantity ;
        public TextView totalPrice ;
        public ItemClickListener itemClickListener ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalPrice = itemView.findViewById(R.id.total_price);
            txtCartProductName = itemView.findViewById(R.id.product_cart_name_txt);
            txtCartProductPrice = itemView.findViewById(R.id.product_cart_price);
            txtCartProductQuantity = itemView.findViewById(R.id.product_cart_quantity);
        }


    }
}
