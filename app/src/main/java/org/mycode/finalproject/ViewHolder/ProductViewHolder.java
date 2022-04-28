package org.mycode.finalproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.mycode.finalproject.Interface.ItemClickListener;
import org.mycode.finalproject.R;

import java.io.File;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductCategory, txtProductPrice;
    public ImageView productImage ;
    public ItemClickListener listner ;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductCategory = itemView.findViewById(R.id.product_category);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }
    public void setItemClickListenser(ItemClickListener lister) {
        this.listner = lister;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
