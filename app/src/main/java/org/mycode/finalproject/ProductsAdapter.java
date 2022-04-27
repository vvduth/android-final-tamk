package org.mycode.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.mycode.finalproject.Interface.ItemClickListener;
import org.mycode.finalproject.Model.Products;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private Context context ;
    private List<Products> products_list ;

    public ProductsAdapter(List<Products> products_list) {
        this.products_list = products_list;
    }

    public ProductsAdapter(Context context, List<Products> products_list) {
        this.context = context;
        this.products_list = products_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
        return new ViewHolder(productsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtProductName.setText(products_list.get(position).getName());
        Glide.with(context).load(products_list.get(position).getImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return products_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtProductName, txtProductDescription, txtProductPrice;
        public ImageView productImage ;
        public ItemClickListener listner ;
        public void setItemClickListenser(ItemClickListener lister) {
            this.listner = lister;
        }

        @Override
        public void onClick(View v) {
            listner.onClick(v, getAdapterPosition(), false);
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductDescription = itemView.findViewById(R.id.product_description);
            txtProductPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
