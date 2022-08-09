package com.domonz.swipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolder> {

    Context context;
    ArrayList<ProductModel> products;

    public AdapterProducts(Context context, ArrayList<ProductModel> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public AdapterProducts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, parent, false));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull AdapterProducts.ViewHolder holder, int position) {

        ProductModel pm = products.get(position);
        if(Objects.equals(pm.getImage(), "")){
            Glide.with(context).load("https://ph-test-11.slatic.net/p/c6c3712607074232c13774258188997f.png").thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
        }else{
            Glide.with(context).load(pm.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
        }
        holder.name.setText(pm.getProduct_name());
        holder.type.setText(pm.getProduct_type());
        holder.price.setText(Html.fromHtml("<small>₹</small>"+ (int)pm.getPrice()));
        holder.tax.setText( "Tax: "+(int)pm.getTax()+ "%");

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, type, price, tax;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.p_image);
            name = itemView.findViewById(R.id.p_name);
            price = itemView.findViewById(R.id.p_price);
            type = itemView.findViewById(R.id.p_type);
            tax = itemView.findViewById(R.id.p_tax);



        }
    }
}
