package org.mycode.finalproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.mycode.finalproject.Interface.ItemClickListener;
import org.mycode.finalproject.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName , txtProductPrice, txtProductQuantity ;
    private ItemClickListener itemClickListener ;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.product_cart_name_txt);
        txtProductPrice = itemView.findViewById(R.id.product_cart_price);
        txtProductQuantity = itemView.findViewById(R.id.product_cart_quantity);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
